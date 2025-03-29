<?php
include 'db_connect.php';
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT); // Hiển thị lỗi rõ ràng

try {
    // Nhận dữ liệu từ Android
    $json = file_get_contents("php://input");
    $data = json_decode($json, true);

    // Kiểm tra JSON hợp lệ
    if (!$data) {
        throw new Exception("Dữ liệu JSON không hợp lệ");
    }

    // Kiểm tra dữ liệu đầu vào
    if (empty($data['id_nguoi_dung']) || empty($data['id_bac_si']) || empty($data['ngay_gio_kham'])) {
        throw new Exception("Thiếu thông tin bắt buộc");
    }

    // Gán biến từ dữ liệu nhận được
    $user_id = intval($data['id_nguoi_dung']);
    $doctor_id = intval($data['id_bac_si']);
    $appointment_time = $data['ngay_gio_kham'];
    $status = "Đã đặt"; // Trạng thái mặc định
    $created_at = date("Y-m-d H:i:s"); // Thời gian hiện tại

    // Kiểm tra định dạng ngày giờ hợp lệ (YYYY-MM-DD HH:MM:SS)
    if (!preg_match('/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/', $appointment_time)) {
        throw new Exception("Định dạng ngày giờ không hợp lệ");
    }

    // Kiểm tra xem bác sĩ có tồn tại không
    $stmt = $conn->prepare("SELECT id FROM bac_si WHERE id = ?");
    $stmt->bind_param("i", $doctor_id);
    $stmt->execute();
    $stmt->store_result();
    if ($stmt->num_rows == 0) {
        throw new Exception("Bác sĩ không tồn tại");
    }
    $stmt->close();

    // Kiểm tra xem người dùng có tồn tại không
    $stmt = $conn->prepare("SELECT id FROM nguoi_dung WHERE id = ?");
    $stmt->bind_param("i", $user_id);
    $stmt->execute();
    $stmt->store_result();
    if ($stmt->num_rows == 0) {
        throw new Exception("Người dùng không tồn tại");
    }
    $stmt->close();

    // Kiểm tra xem bác sĩ đã có lịch vào **cùng ngày** chưa (bỏ kiểm tra giờ)
    $date_only = explode(" ", $appointment_time)[0]; // Lấy phần ngày (YYYY-MM-DD)
    $stmt = $conn->prepare("SELECT id FROM lich_kham WHERE id_bac_si = ? AND DATE(ngay_gio_kham) = ?");
    $stmt->bind_param("is", $doctor_id, $date_only);
    $stmt->execute();
    $stmt->store_result();
    if ($stmt->num_rows > 0) {
        throw new Exception("Bác sĩ đã có lịch hẹn vào ngày này");
    }
    $stmt->close();

    // Thêm lịch khám vào CSDL
    $stmt = $conn->prepare("INSERT INTO lich_kham (id_nguoi_dung, id_bac_si, ngay_gio_kham, trang_thai, ngay_tao) VALUES (?, ?, ?, ?, ?)");
    $stmt->bind_param("iisss", $user_id, $doctor_id, $appointment_time, $status, $created_at);
    
    if ($stmt->execute()) {
        echo json_encode(["status" => "success", "message" => "Đặt lịch thành công"]);
    } else {
        throw new Exception("Lỗi đặt lịch: " . $stmt->error);
    }

    $stmt->close();
    $conn->close();

} catch (Exception $e) {
    error_log("Lỗi API đặt lịch: " . $e->getMessage()); // Ghi log lỗi
    echo json_encode(["status" => "error", "message" => $e->getMessage()]);
}
?>
