package com.example.ungdungcoxuongkhop;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;

public class BookAppointmentActivity extends AppCompatActivity {

    private EditText edtUserId, edtDate;
    private Spinner spnDoctor;
    private ProgressBar progressBar;
    private Button btnBook;
    private TextView tvBack;
    private RequestQueue requestQueue;
    private String selectedDoctorId = "-1"; // Mặc định không có bác sĩ nào

    private static final String API_GET_DOCTORS = "http://172.22.144.1/ungdung_api/getData.php";
    private static final String API_BOOK_APPOINTMENT = "http://172.22.144.1/ungdung_api/book_appointment.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        edtUserId = findViewById(R.id.edtUserId);
        edtDate = findViewById(R.id.edtDate);
        spnDoctor = findViewById(R.id.spnDoctor);
        progressBar = findViewById(R.id.progressBar);
        btnBook = findViewById(R.id.btnBook);
        tvBack = findViewById(R.id.btnBack);
        requestQueue = Volley.newRequestQueue(this);

        // ✅ Xử lý sự kiện nhấn vào nút Quay lại
        tvBack.setOnClickListener(v -> onBackPressed());
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());

        // ✅ Load doctorId từ SharedPreferences nếu có
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        selectedDoctorId = String.valueOf(prefs.getInt("id_bac_si", -1));
        Log.d("DEBUG", "📢 doctorId đã lưu trong SharedPreferences: " + selectedDoctorId);

        loadDoctors();
        edtDate.setOnClickListener(v -> showDatePicker());
        btnBook.setOnClickListener(v -> bookAppointment());
    }

    private void loadDoctors() {
        progressBar.setVisibility(View.VISIBLE);
        Log.d("API", "Đang tải danh sách bác sĩ từ API: " + API_GET_DOCTORS);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_GET_DOCTORS, null,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    Log.d("API_RESPONSE", "Phản hồi từ API: " + response.toString());

                    try {
                        if (!response.getString("status").equals("success")) {
                            Toast.makeText(this, "Lỗi tải danh sách bác sĩ!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONArray jsonArray = response.getJSONArray("data");
                        String[] doctorNames = new String[jsonArray.length()];
                        final String[] doctorIds = new String[jsonArray.length()];

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            doctorNames[i] = jsonObject.getString("ho_ten");
                            doctorIds[i] = jsonObject.getString("id");
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_spinner_item, doctorNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnDoctor.setAdapter(adapter);

                        // ✅ Chọn lại bác sĩ đã lưu nếu có
                        for (int i = 0; i < doctorIds.length; i++) {
                            if (doctorIds[i].equals(selectedDoctorId)) {
                                spnDoctor.setSelection(i);
                                break;
                            }
                        }

                        // ✅ Lưu doctorId khi người dùng chọn bác sĩ
                        spnDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                selectedDoctorId = doctorIds[position];

                                // ✅ Lưu doctorId vào SharedPreferences
                                SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("id_bac_si", Integer.parseInt(selectedDoctorId));
                                editor.apply();

                                Log.d("DEBUG", "✅ doctorId đã lưu vào SharedPreferences: " + selectedDoctorId);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });

                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Lỗi xử lý JSON: " + e.getMessage());
                        Toast.makeText(this, "Lỗi xử lý danh sách bác sĩ!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Log.e("API_ERROR", "Lỗi kết nối API: " + error.getMessage());
                    Toast.makeText(this, "Lỗi tải danh sách bác sĩ!", Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(request);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String date = year1 + "-" + String.format("%02d", (month1 + 1)) + "-" + String.format("%02d", dayOfMonth);
            edtDate.setText(date);
        }, year, month, day);

        datePickerDialog.show();
    }
    private void bookAppointment() {
        String userId = edtUserId.getText().toString().trim();
        String date = edtDate.getText().toString().trim();

        if (userId.isEmpty() || date.isEmpty() || selectedDoctorId.equals("-1")) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("user_id", userId);
            requestData.put("doctor_id", selectedDoctorId);
            requestData.put("date", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, API_BOOK_APPOINTMENT, requestData,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        if (response.getString("status").equals("success")) {
                            Toast.makeText(this, "Đặt lịch thành công!", Toast.LENGTH_SHORT).show();
                            finish(); // Đóng activity sau khi đặt lịch thành công
                        } else {
                            Toast.makeText(this, "Đặt lịch thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi xử lý dữ liệu!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(request);
    }

}
