package com.example.ungdungcoxuongkhop;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DoctorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);

        recyclerView = findViewById(R.id.recyclerViewDoctors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchDoctors();
    }

    private void fetchDoctors() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Doctor>> call = apiService.getDoctors();

        call.enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(@NonNull Call<List<Doctor>> call, @NonNull Response<List<Doctor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Doctor> doctorList = response.body();

                    if (!doctorList.isEmpty()) {
                        adapter = new DoctorAdapter(doctorList);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(DoctorActivity.this, "Không có bác sĩ nào", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("DoctorActivity", "Response không thành công: " + response.message());
                    Toast.makeText(DoctorActivity.this, "Lỗi khi tải danh sách bác sĩ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Doctor>> call, @NonNull Throwable t) {
                Log.e("DoctorActivity", "Lỗi kết nối API: ", t);
                Toast.makeText(DoctorActivity.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
