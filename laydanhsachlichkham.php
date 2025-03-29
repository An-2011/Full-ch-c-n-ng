<?php
// API: Lấy danh sách lịch khám của người dùng
include 'db_connect.php';

$user_id = $_POST['user_id'];

$sql = "SELECT * FROM appointments WHERE user_id='$user_id' ORDER BY appointment_date DESC";
$result = $conn->query($sql);

$appointments = array();
if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $appointments[] = $row;
    }
    echo json_encode(["status" => "success", "appointments" => $appointments]);
} else {
    echo json_encode(["status" => "error", "message" => "Không có lịch khám nào"]);
}
$conn->close();

// API: Cập nhật nhật ký sức khỏe
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['update_health_log'])) {
    $user_id = $_POST['user_id'];
    $date = $_POST['date'];
    $pain_level = $_POST['pain_level'];
    $uric_acid = $_POST['uric_acid'];
    $notes = $_POST['notes'];

    $sql = "INSERT INTO nhat_ky_suc_khoe (id_nguoi_dung, ngay, muc_do_dau, chi_so_axit_uric, ghi_chu) 
            VALUES ('$user_id', '$date', '$pain_level', '$uric_acid', '$notes') 
            ON DUPLICATE KEY UPDATE muc_do_dau='$pain_level', chi_so_axit_uric='$uric_acid', ghi_chu='$notes'";

    if ($conn->query($sql) === TRUE) {
        echo json_encode(["status" => "success", "message" => "Cập nhật nhật ký sức khỏe thành công"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Lỗi: " . $conn->error]);
    }
}

// API: Gửi thông báo & ưu đãi
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['send_notification'])) {
    $user_id = $_POST['user_id'];
    $title = $_POST['title'];
    $message = $_POST['message'];
    $type = $_POST['type']; // 'Nhắc nhở' hoặc 'Ưu đãi'

    $sql = "INSERT INTO thong_bao (id_nguoi_dung, tieu_de, noi_dung, loai_thong_bao) 
            VALUES ('$user_id', '$title', '$message', '$type')";

    if ($conn->query($sql) === TRUE) {
        echo json_encode(["status" => "success", "message" => "Thông báo đã được gửi"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Lỗi: " . $conn->error]);
    }
}

// API: Đánh giá & phản hồi từ người dùng
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['submit_review'])) {
    $user_id = $_POST['user_id'];
    $doctor_id = $_POST['doctor_id'];
    $rating = $_POST['rating'];
    $review = $_POST['review'];

    $sql = "INSERT INTO danh_gia (id_nguoi_dung, id_bac_si, diem_so, nhan_xet) 
            VALUES ('$user_id', '$doctor_id', '$rating', '$review')";

    if ($conn->query($sql) === TRUE) {
        echo json_encode(["status" => "success", "message" => "Đánh giá đã được gửi"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Lỗi: " . $conn->error]);
    }
}
?>