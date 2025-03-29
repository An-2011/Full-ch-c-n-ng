<?php
include 'db_connect.php';

// Thiết lập header để đảm bảo chỉ trả về JSON
header('Content-Type: application/json; charset=UTF-8');

// Đọc dữ liệu JSON từ Android
$json = file_get_contents('php://input');
$data = json_decode($json, true);

// Kiểm tra lỗi JSON
if (json_last_error() !== JSON_ERROR_NONE) {
    echo json_encode(["status" => "error", "message" => "Dữ liệu JSON không hợp lệ: " . json_last_error_msg()]);
    exit;
}

if (!$data) {
    echo json_encode(["status" => "error", "message" => "Không nhận được dữ liệu JSON"]);
    exit;
}

// Lấy dữ liệu từ JSON
$user_id = $data['user_id'] ?? null;
$date = $data['date'] ?? null;
$pain_level = $data['pain_level'] ?? null;
$uric_acid = $data['uric_acid'] ?? null;
$notes = $data['notes'] ?? "";

// Kiểm tra dữ liệu đầu vào
if (empty($user_id) || empty($date) || !isset($pain_level) || !isset($uric_acid)) {
    echo json_encode(["status" => "error", "message" => "Thiếu thông tin bắt buộc."]);
    exit;
}

// Kiểm tra dữ liệu có hợp lệ không
if (!is_numeric($pain_level) || !is_numeric($uric_acid)) {
    echo json_encode(["status" => "error", "message" => "Dữ liệu không hợp lệ. Vui lòng nhập số hợp lệ."]);
    exit;
}

// Chuẩn bị query an toàn với ON DUPLICATE KEY UPDATE
$stmt = $conn->prepare(
    "INSERT INTO nhat_ky_suc_khoe (id_nguoi_dung, ngay, muc_do_dau, chi_so_axit_uric, ghi_chu) 
    VALUES (?, ?, ?, ?, ?) 
    ON DUPLICATE KEY UPDATE muc_do_dau = VALUES(muc_do_dau), chi_so_axit_uric = VALUES(chi_so_axit_uric), ghi_chu = VALUES(ghi_chu)"
);

if (!$stmt) {
    echo json_encode(["status" => "error", "message" => "Lỗi truy vấn: " . $conn->error]);
    exit;
}

// Gán giá trị vào câu lệnh prepare
$stmt->bind_param("ssdds", $user_id, $date, $pain_level, $uric_acid, $notes);

// Thực thi query và kiểm tra lỗi
if ($stmt->execute()) {
    // Gửi thông báo ưu đãi bằng cURL
    $notificationData = [
        'user_id' => $user_id,
        'title' => 'Ưu đãi sức khỏe!',
        'message' => 'Cảm ơn bạn đã cập nhật sức khỏe. Hãy xem ngay ưu đãi đặc biệt dành cho bạn!'
    ];

    $ch = curl_init('http://localhost/ungdung_api/guithongbaovsuudai.php');
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($notificationData));
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

    $response = curl_exec($ch);
    $http_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);

    if ($http_code === 200) {
        $notificationResult = json_decode($response, true);
        echo json_encode(["status" => "success", "message" => "Cập nhật nhật ký sức khỏe thành công", "notification" => $notificationResult]);
    } else {
        echo json_encode(["status" => "success", "message" => "Cập nhật nhật ký sức khỏe thành công nhưng không thể gửi thông báo"]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Lỗi khi cập nhật: " . $stmt->error]);
}

// Đóng kết nối
$stmt->close();
$conn->close();
?>
