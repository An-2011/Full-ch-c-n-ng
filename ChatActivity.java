package com.example.ungdungcoxuongkhop;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private EditText edtMessage;
    private Button btnSend, btnBack; // Thêm nút Quay lại
    private RecyclerView recyclerViewMessages;
    private MessageAdapter messageAdapter;
    private ArrayList<Message> messageList;
    private RequestQueue requestQueue;

    private final int userId = 1; // ID người dùng
    private final int doctorId = 2; // ID bác sĩ

    private static final String API_SEND_MESSAGE = "http://172.22.144.1/ungdung_api/send_message.php";
    private static final String API_GET_MESSAGES = "http://172.22.144.1/ungdung_api/get_messages.php";

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final int REFRESH_INTERVAL = 5000; // 5 giây

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack); // Khai báo nút Quay lại
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);

        requestQueue = Volley.newRequestQueue(this);
        messageList = new ArrayList<>();

        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(this, messageList, userId);
        recyclerViewMessages.setAdapter(messageAdapter);

        btnSend.setOnClickListener(v -> sendMessage());

        // Xử lý sự kiện khi nhấn nút Quay lại
        btnBack.setOnClickListener(v -> {
            finish(); // Kết thúc Activity và quay lại màn hình trước đó
        });

        // Tải tin nhắn khi mở màn hình chat
        loadMessages();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(refreshMessages, REFRESH_INTERVAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(refreshMessages);
    }

    private final Runnable refreshMessages = new Runnable() {
        @Override
        public void run() {
            loadMessages();
            handler.postDelayed(this, REFRESH_INTERVAL);
        }
    };

    private void sendMessage() {
        String message = edtMessage.getText().toString().trim();
        if (message.isEmpty()) {
            showToast("Vui lòng nhập tin nhắn");
            return;
        }

        Message tempMessage = new Message(0, userId, doctorId, message, null, "Vừa xong");
        messageList.add(tempMessage);
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerViewMessages.scrollToPosition(messageList.size() - 1);

        Map<String, String> params = new HashMap<>();
        params.put("id_nguoi_gui", String.valueOf(userId));
        params.put("id_nguoi_nhan", String.valueOf(doctorId));
        params.put("tin_nhan", message);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, API_SEND_MESSAGE, new JSONObject(params),
                response -> {
                    try {
                        if (response.getString("status").equals("success")) {
                            showToast("Tin nhắn đã gửi!");
                            loadMessages();
                        } else {
                            showToast(response.getString("message"));
                        }
                    } catch (JSONException e) {
                        Log.e("API_ERROR", "JSON Exception: " + e.getMessage());
                    }
                },
                error -> Log.e("API_ERROR", "Lỗi gửi tin nhắn: " + error.toString()));

        requestQueue.add(request);
        edtMessage.setText(""); // Xóa input sau khi gửi
    }

    private void loadMessages() {
        Log.d("DEBUG", "Bắt đầu load tin nhắn...");

        Map<String, String> params = new HashMap<>();
        params.put("id_nguoi_gui", String.valueOf(userId));
        params.put("id_nguoi_nhan", String.valueOf(doctorId));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, API_GET_MESSAGES, new JSONObject(params),
                response -> {
                    try {
                        Log.d("DEBUG", "Phản hồi API: " + response.toString());

                        if (response.getString("status").equals("success")) {
                            JSONArray messagesArray = response.getJSONArray("data");

                            ArrayList<Message> newMessages = new ArrayList<>();
                            for (int i = 0; i < messagesArray.length(); i++) {
                                JSONObject msg = messagesArray.getJSONObject(i);
                                String tinNhan = msg.optString("tin_nhan", "").trim();
                                String hinhAnh = msg.isNull("hinh_anh") ? null : msg.optString("hinh_anh", null);
                                String thoiGianGui = msg.optString("thoi_gian_gui", "Không xác định");

                                if (!tinNhan.isEmpty()) {
                                    newMessages.add(new Message(
                                            msg.optInt("id", 0),
                                            msg.optInt("id_nguoi_gui", 0),
                                            msg.optInt("id_nguoi_nhan", 0),
                                            tinNhan,
                                            hinhAnh,
                                            thoiGianGui
                                    ));
                                }
                            }

                            Log.d("DEBUG", "Số tin nhắn API trả về: " + newMessages.size());
                            Log.d("DEBUG", "Số tin nhắn trước khi cập nhật: " + messageList.size());

                            if (messageAdapter != null) {
                                messageList.clear();
                                messageList.addAll(newMessages);
                                messageAdapter.notifyDataSetChanged();
                                recyclerViewMessages.scrollToPosition(messageList.size() - 1);
                            } else {
                                Log.e("DEBUG", "messageAdapter chưa được khởi tạo!");
                            }
                        } else {
                            showToast(response.getString("message"));
                        }
                    } catch (JSONException e) {
                        Log.e("API_ERROR", "JSON Exception: " + e.getMessage());
                    }
                },
                error -> Log.e("API_ERROR", "Lỗi tải tin nhắn: " + error.toString()));

        requestQueue.add(request);
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(ChatActivity.this, message, Toast.LENGTH_SHORT).show());
    }
}
