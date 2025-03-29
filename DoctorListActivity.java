package com.example.ungdungcoxuongkhop;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.ungdungcoxuongkhop.DoctorAdapter;


public class DoctorListActivity extends AppCompatActivity {
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
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Doctor> doctorList = response.body();

                    Log.d("DoctorListActivity", "Số lượng bác sĩ nhận được: " + doctorList.size());
                    Toast.makeText(DoctorListActivity.this, "Nhận được " + doctorList.size() + " bác sĩ", Toast.LENGTH_SHORT).show();

                    if (doctorList.isEmpty()) {
                        Toast.makeText(DoctorListActivity.this, "Không có bác sĩ nào", Toast.LENGTH_LONG).show();
                    } else {
                        adapter = new DoctorAdapter(doctorList);
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(DoctorListActivity.this, "Lỗi lấy danh sách bác sĩ!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                Log.e("DoctorListActivity", "Lỗi kết nối API: " + t.getMessage());
                Toast.makeText(DoctorListActivity.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
