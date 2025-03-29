package com.example.ungdungcoxuongkhop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SelfCheckActivity extends AppCompatActivity {
    private RadioGroup q1, q2, q3, q4, q5;
    private Button btnSubmit;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_check);

        // Ánh xạ các RadioGroup từ layout
        q1 = findViewById(R.id.q1);
        q2 = findViewById(R.id.q2);
        q3 = findViewById(R.id.q3);
        q4 = findViewById(R.id.q4);
        q5 = findViewById(R.id.q5);

        btnSubmit = findViewById(R.id.btn_submit);
        resultText = findViewById(R.id.result_text);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluateRisk();
            }
        });
        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Quay lại màn hình trước đó
            }
        });

    }

    private void evaluateRisk() {
        int riskScore = 0;

        // Tính điểm dựa trên câu trả lời của người dùng
        riskScore += getSelectedScore(q1, new int[]{2, 1, 0}); // Câu hỏi 1
        riskScore += getSelectedScore(q2, new int[]{2, 1, 0}); // Câu hỏi 2
        riskScore += getSelectedScore(q3, new int[]{0, 1}); // Câu hỏi 3
        riskScore += getSelectedScore(q4, new int[]{1, 0}); // Câu hỏi 4
        riskScore += getSelectedScore(q5, new int[]{2, 0}); // Câu hỏi 5

        // Xác định mức độ nguy cơ mắc bệnh Gout
        String result;
        if (riskScore >= 8) {
            result = "Nguy cơ cao mắc bệnh Gout. Hãy đi khám bác sĩ ngay!";
        } else if (riskScore >= 4) {
            result = "Nguy cơ trung bình. Nên thay đổi thói quen ăn uống và sinh hoạt.";
        } else {
            result = "Nguy cơ thấp. Tiếp tục duy trì lối sống lành mạnh!";
        }

        resultText.setText(result);
    }

    private int getSelectedScore(RadioGroup group, int[] scores) {
        int selectedId = group.getCheckedRadioButtonId();
        if (selectedId == -1) return 0; // Nếu không chọn, điểm là 0

        int index = group.indexOfChild(findViewById(selectedId));
        return scores[index];
    }
}