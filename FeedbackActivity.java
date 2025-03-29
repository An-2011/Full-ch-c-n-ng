package com.example.ungdungcoxuongkhop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeedbackActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FeedbackAdapter adapter;
    private List<Feedback> feedbackList;
    private EditText edtComment;
    private RatingBar ratingBar;
    private Button btnSubmit;
    private TextView emptyText;
    // Thêm khai báo nút quay lại
    private Button btnBack;

    private int doctorId = -1;
    private static final String SERVER_URL = "http://172.22.144.1/ungdung_api/danhgia.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        recyclerView = findViewById(R.id.recyclerView);
        edtComment = findViewById(R.id.edtComment);
        ratingBar = findViewById(R.id.ratingBar);
        btnSubmit = findViewById(R.id.btnSubmit);
        emptyText = findViewById(R.id.emptyText);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Log.d("DEBUG", "Nút quay lại được bấm!");
            finish();
        });





        SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        // ✅ Kiểm tra Intent trước
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id_bac_si")) {
            doctorId = intent.getIntExtra("id_bac_si", -1);
            if (doctorId != -1) {
                preferences.edit().putInt("id_bac_si", doctorId).apply(); // 🔥 Lưu ngay vào SharedPreferences
            }
        }

        // ✅ Nếu doctorId vẫn là -1, thử lấy từ SharedPreferences
        if (doctorId == -1) {
            doctorId = preferences.getInt("id_bac_si", -1);
        }

        // ✅ Ghi log kiểm tra doctorId
        Log.d("DEBUG", "🩺 doctorId hiện tại: " + doctorId);

        // ❌ Nếu vẫn không có doctorId, hiển thị lỗi
        if (doctorId == -1) {
            Toast.makeText(this, "❌ Bạn chưa chọn bác sĩ nào!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        feedbackList = new ArrayList<>();
        adapter = new FeedbackAdapter(feedbackList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadFeedback();
        btnSubmit.setOnClickListener(v -> submitFeedback());
    }


    private void submitFeedback() {
        String comment = edtComment.getText().toString().trim();
        int score = (int) ratingBar.getRating();

        if (score == 0) {
            Toast.makeText(this, "❌ Vui lòng chọn số sao!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(this, "❌ Lỗi: Không tìm thấy user_id!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", "add");
            jsonObject.put("user_id", userId);
            jsonObject.put("id_bac_si", doctorId);
            jsonObject.put("diem_so", score);
            jsonObject.put("nhan_xet", comment);

            Log.d("DEBUG", "📡 Gửi JSON lên API: " + jsonObject.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SERVER_URL, jsonObject,
                    response -> {
                        try {
                            Log.d("DEBUG", "✅ Phản hồi từ API: " + response.toString());
                            String status = response.getString("status");
                            Toast.makeText(this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            if (status.equals("success")) {
                                loadFeedback();
                                edtComment.setText("");
                                ratingBar.setRating(0);
                            }
                        } catch (JSONException e) {
                            Log.e("JSONError", "Lỗi xử lý JSON", e);
                        }
                    },
                    error -> {
                        Log.e("VolleyError", "🚨 Lỗi kết nối đến server!", error);
                        Toast.makeText(this, "Lỗi kết nối đến máy chủ!", Toast.LENGTH_SHORT).show();
                    });

            MySingleton.getInstance(this).addToRequestQueue(request);
        } catch (JSONException e) {
            Log.e("JSONError", "Lỗi tạo JSON!", e);
        }
    }

    private void loadFeedback() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", "fetch_by_doctor");
            jsonObject.put("id_bac_si", doctorId);

            Log.d("DEBUG", "📡 Gửi yêu cầu lấy đánh giá: " + jsonObject.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SERVER_URL, jsonObject,
                    response -> {
                        try {
                            Log.d("DEBUG", "✅ Phản hồi từ API: " + response.toString());
                            feedbackList.clear();
                            if (response.getString("status").equals("success")) {
                                JSONArray data = response.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject obj = data.getJSONObject(i);
                                    feedbackList.add(new Feedback(
                                            obj.getInt("id"),
                                            obj.getInt("id_nguoi_dung"),
                                            obj.getInt("id_bac_si"),
                                            obj.getInt("diem_so"),
                                            obj.getString("nhan_xet"),
                                            obj.getString("ngay_tao")
                                    ));
                                }
                                adapter.notifyDataSetChanged();
                                emptyText.setVisibility(feedbackList.isEmpty() ? View.VISIBLE : View.GONE);
                                recyclerView.setVisibility(feedbackList.isEmpty() ? View.GONE : View.VISIBLE);
                            } else {
                                emptyText.setText(response.getString("message"));
                                emptyText.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            Log.e("JSONError", "Lỗi xử lý JSON!", e);
                        }
                    },
                    error -> {
                        Log.e("VolleyError", "🚨 Lỗi kết nối server!", error);
                        Toast.makeText(this, "Lỗi tải đánh giá! Kiểm tra kết nối mạng.", Toast.LENGTH_SHORT).show();
                    });

            MySingleton.getInstance(this).addToRequestQueue(request);
        } catch (JSONException e) {
            Log.e("JSONError", "Lỗi tạo JSON!", e);
        }
    }
}