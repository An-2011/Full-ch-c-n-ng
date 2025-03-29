package com.example.ungdungcoxuongkhop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Xử lý nút quay lại
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish()); // Đóng Activity khi nhấn nút

        // Hiển thị nội dung
        TextView textView = findViewById(R.id.tvInfo);
        textView.setText("🦵 Bệnh Gout – Nguyên nhân, Triệu chứng và Cách Phòng Ngừa\n\n" +
                "🔹 1. Bệnh Gout là gì?\n" +
                "Bệnh Gout là một dạng viêm khớp do sự tích tụ axit uric trong máu, tạo thành tinh thể urat trong khớp, gây viêm và đau đớn.\n\n" +
                "🔹 2. Nguyên nhân gây bệnh Gout\n" +
                "• Ăn quá nhiều thịt đỏ, hải sản, nội tạng động vật.\n" +
                "• Uống nhiều rượu bia, cản trở đào thải axit uric.\n" +
                "• Béo phì, thừa cân, di truyền và các bệnh lý khác như cao huyết áp, tiểu đường.\n\n" +
                "🔹 3. Triệu chứng của bệnh Gout\n" +
                "• Đau khớp dữ dội, đặc biệt ở ngón chân cái, đầu gối, khuỷu tay.\n" +
                "• Sưng, đỏ, nóng khớp, cứng khớp vào buổi sáng.\n" +
                "• Xuất hiện hạt tophi nếu bệnh kéo dài.\n\n" +
                "🔹 4. Cách phòng ngừa bệnh Gout\n" +
                "✅ Hạn chế thịt đỏ, nội tạng động vật, hải sản.\n" +
                "✅ Ăn nhiều rau xanh, uống đủ nước (2-3 lít/ngày).\n" +
                "✅ Tập thể dục đều đặn như đi bộ, bơi lội, yoga.\n" +
                "✅ Hạn chế rượu bia, nước ngọt có ga.\n" +
                "✅ Kiểm soát cân nặng, điều trị các bệnh liên quan.\n" +
                "✅ Sử dụng thuốc theo chỉ định bác sĩ.\n\n" +
                "🔹 5. Khi nào cần gặp bác sĩ?\n" +
                "👉 Nếu bạn bị đau khớp dữ dội, sưng đỏ kéo dài hơn 48 giờ, hãy đến cơ sở y tế để kiểm tra và điều trị kịp thời.\n\n" +
                "⚠ Bệnh Gout có thể kiểm soát tốt nếu bạn duy trì lối sống lành mạnh! 💪");
    }
}
