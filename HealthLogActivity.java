package com.example.ungdungcoxuongkhop;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthLogActivity extends AppCompatActivity {

    private EditText edtDate, edtPainLevel, edtUricAcid, edtNotes;
    private Button btnSubmit;
    private TextView txtResult;
    private static final String TAG = "HealthLogActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_log2);

        // Ánh xạ view từ layout
        edtDate = findViewById(R.id.edtDate);
        edtPainLevel = findViewById(R.id.edtPainLevel);
        edtUricAcid = findViewById(R.id.edtUricAcid);
        edtNotes = findViewById(R.id.edtNotes);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtResult = findViewById(R.id.txtResult);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng Activity và quay lại màn hình trước
            }
        });

    }

    private void submitHealthLog() {
        String userId = "1"; // Nếu lấy từ SharedPreferences, thay đổi chỗ này
        String date = edtDate.getText().toString().trim();
        String painLevel = edtPainLevel.getText().toString().trim();
        String uricAcid = edtUricAcid.getText().toString().trim();
        String notes = edtNotes.getText().toString().trim();

        if (date.isEmpty() || painLevel.isEmpty() || uricAcid.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", userId);
            jsonObject.put("date", date);

            try {
                int pain = Integer.parseInt(painLevel);
                double uric = Double.parseDouble(uricAcid);
                jsonObject.put("pain_level", pain);
                jsonObject.put("uric_acid", uric);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Lỗi: Hãy nhập số hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            jsonObject.put("notes", notes);

            Log.d(TAG, "Dữ liệu gửi: " + jsonObject.toString());

            RequestBody requestBody = RequestBody.create(jsonObject.toString(), MediaType.get("application/json; charset=utf-8"));

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<ResponseBody> call = apiService.updateHealthLog(requestBody);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            String responseData = response.body().string().trim();
                            Log.d(TAG, "Phản hồi từ server: " + responseData);

                            // Loại bỏ ký tự không mong muốn trước JSON
                            responseData = cleanJson(responseData);

                            // Kiểm tra phản hồi có phải JSON hợp lệ không
                            if (isValidJson(responseData)) {
                                JSONObject jsonResponse = new JSONObject(responseData);
                                String status = jsonResponse.optString("status", "error");
                                String message = jsonResponse.optString("message", "Không có phản hồi từ server.");

                                if (status.equals("success")) {
                                    txtResult.setText("✅ Nhật ký sức khỏe đã được cập nhật thành công!");
                                } else {
                                    txtResult.setText("❗ " + message);
                                }
                            } else {
                                txtResult.setText("Lỗi: Server không trả về JSON hợp lệ.");
                                Log.e(TAG, "Phản hồi server không phải JSON: " + responseData);
                            }
                        } else {
                            txtResult.setText("Lỗi từ server. Mã lỗi: " + response.code());
                            Log.e(TAG, "Lỗi từ server: " + response.errorBody().string());
                        }
                    } catch (Exception e) {
                        txtResult.setText("Lỗi khi đọc phản hồi từ server.");
                        Log.e(TAG, "Lỗi phân tích phản hồi: ", e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    txtResult.setText("Lỗi kết nối: " + t.getMessage());
                    Log.e(TAG, "Lỗi kết nối: ", t);
                }
            });
        } catch (JSONException e) {
            txtResult.setText("Lỗi xử lý dữ liệu. Vui lòng kiểm tra lại.");
            Log.e(TAG, "Lỗi JSON: ", e);
        }
    }

    // Hàm loại bỏ các ký tự không hợp lệ trước JSON (fix lỗi server trả về không đúng JSON)
    private String cleanJson(String response) {
        int startIndex = response.indexOf("{");
        if (startIndex != -1) {
            return response.substring(startIndex);
        }
        return response;
    }

    // Hàm kiểm tra xem chuỗi có phải JSON hợp lệ không
    private boolean isValidJson(String response) {
        try {
            new JSONObject(response);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }
}
