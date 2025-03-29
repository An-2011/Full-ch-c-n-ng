package com.example.ungdungcoxuongkhop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewFunctions;
    private FunctionAdapter adapter;
    private List<FunctionItem> functionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupRecyclerView();
        setupFunctionList();
        registerFirebaseNotifications();
    }

    private void setupRecyclerView() {
        recyclerViewFunctions = findViewById(R.id.recyclerViewFunctions);
        if (recyclerViewFunctions == null) {
            Log.e("ERROR", "❌ recyclerViewFunctions không tìm thấy! Kiểm tra activity_main.xml");
            return;
        }

        int columnCount = 2;
        recyclerViewFunctions.setLayoutManager(new GridLayoutManager(this, columnCount));
    }

    private void setupFunctionList() {
        functionList = new ArrayList<>();
        functionList.add(new FunctionItem("Tìm hiểu bệnh Gout", R.drawable.timhieubenhgout, InfoActivity.class));
        functionList.add(new FunctionItem("Tự kiểm tra nguy cơ", R.drawable.ic_medical_logo, SelfCheckActivity.class));
        functionList.add(new FunctionItem("Đặt lịch khám", R.drawable.ic_health_journal, BookAppointmentActivity.class));
        functionList.add(new FunctionItem("Nhật ký sức khỏe", R.drawable.nhatkysuckhoe, HealthLogActivity.class));
        functionList.add(new FunctionItem("Tư vấn trực tuyến", R.drawable.tuvantructiep, ChatActivity.class));
        functionList.add(new FunctionItem("Thông báo & Ưu đãi", R.drawable.thongbaouudai, NotificationActivity.class));
        functionList.add(new FunctionItem("Phản hồi & Đánh giá", R.drawable.phanhoidanhgia, FeedbackActivity.class));
        functionList.add(new FunctionItem("Phát triển tương lai", R.drawable.phattrientuonglai, FutureDevelopmentActivity.class));

        adapter = new FunctionAdapter(this, functionList);
        recyclerViewFunctions.setAdapter(adapter);

        adapter.setOnItemClickListener(this::handleItemClick);
    }

    private void handleItemClick(int position) {
        if (position < 0 || position >= functionList.size()) {
            Log.e("ERROR", "❌ Click vào vị trí không hợp lệ: " + position);
            return;
        }

        FunctionItem item = functionList.get(position);
        if (item == null || item.getActivityClass() == null) {
            Log.e("ERROR", "❌ FunctionItem bị null tại vị trí: " + position);
            return;
        }

        final Class<?> activityClass = item.getActivityClass();
        Log.d("DEBUG", "📢 Đang mở activity: " + activityClass.getSimpleName());

        Intent intent = new Intent(MainActivity.this, activityClass);

        if (activityClass == FeedbackActivity.class) {
            SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            final int doctorId = preferences.getInt("id_bac_si", -1);
            if (doctorId == -1) {
                Log.e("ERROR", "❌ ID bác sĩ không hợp lệ!");
            } else {
                Log.d("DEBUG", "📢 Truyền doctorId: " + doctorId);
                intent.putExtra("id_bac_si", doctorId);
            }
        }

        startActivity(intent);
    }

    private void registerFirebaseNotifications() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("FCM", "❌ Lấy token Firebase thất bại", task.getException());
                        return;
                    }
                    final String token = task.getResult();
                    if (token == null) {
                        Log.e("FCM", "❌ Token Firebase null!");
                    } else {
                        Log.d("FCM", "📢 Token Firebase: " + token);
                    }
                });
    }
}