package com.example.ungdungcoxuongkhop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import com.example.ungdungcoxuongkhop.model.FutureFeature;


public class FutureDevelopmentActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FutureFeatureAdapter adapter;
    private List<FutureFeature> featureList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future_development);

        recyclerView = findViewById(R.id.recyclerViewFuture);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        featureList = new ArrayList<>();
        featureList.add(new FutureFeature("📌 Tích hợp đồng hồ thông minh", "Giúp bạn theo dõi chỉ số sức khỏe ngay trên cổ tay."));
        featureList.add(new FutureFeature("📌 Hỗ trợ đa ngôn ngữ", "Dễ dàng sử dụng ứng dụng với nhiều ngôn ngữ khác nhau."));

        adapter = new FutureFeatureAdapter(featureList);
        recyclerView.setAdapter(adapter);

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}