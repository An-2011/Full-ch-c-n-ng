package com.example.ungdungcoxuongkhop;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import java.util.List;

public interface ApiService {

    // ✅ Đăng nhập
    @POST("login.php")
    Call<ResponseBody> loginUser(@Body RequestBody body);

    // ✅ Lấy danh sách bác sĩ
    @GET("getData.php")
    Call<List<Doctor>> getDoctors();

    // ✅ Cập nhật nhật ký sức khỏe
    @POST("capnhatsuckhoe.php")
    Call<ResponseBody> updateHealthLog(@Body RequestBody body);

    // ✅ Đặt lịch khám
    @POST("book_appointment.php")
    Call<ResponseBody> bookAppointment(@Body RequestBody body);

    // ✅ Gửi tin nhắn
    @POST("send_message.php")
    Call<ResponseBody> sendMessage(@Body RequestBody body);

    // ✅ Lấy tin nhắn
    @GET("get_messages.php")
    Call<ResponseBody> getMessages(
            @Query("id_nguoi_gui") int senderId,
            @Query("id_nguoi_nhan") int receiverId
    );

    // ✅ Lấy danh sách thông báo (Sửa lỗi đường dẫn)
    @GET("guithongbaovsuudai.php")
    Call<ResponseBody> getNotifications(@Query("user_id") int userId);

    // ✅ Gửi thông báo hoặc ưu đãi
    @POST("guithongbaovsuudai.php")
    Call<ResponseBody> sendNotification(@Body RequestBody body);

    // ✅ Đánh dấu thông báo đã đọc
    @POST("guithongbaovsuudai.php")
    Call<ResponseBody> markNotificationAsRead(@Body RequestBody body);

    // ✅ Thêm đánh giá
    @POST("danhgia.php")
    Call<ResponseBody> addFeedback(@Body RequestBody body);

    // ✅ Lấy danh sách đánh giá theo bác sĩ
    @POST("danhgia.php")
    Call<ResponseBody> getFeedbackByDoctor(@Body RequestBody body);

    // ✅ Lấy danh sách đánh giá của người dùng
    @POST("danhgia.php")
    Call<ResponseBody> getFeedbackByUser(@Body RequestBody body);
}
