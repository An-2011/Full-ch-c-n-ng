<?php
// send_message.php
header('Content-Type: application/json');

// Kết nối cơ sở dữ liệu
$conn = new mysqli('localhost', 'root', '', 'ung_dung_co_xuong_khop_cua_ban');
if ($conn->connect_error) {
    echo json_encode(["status" => "error", "message" => "Kết nối thất bại: " . $conn->connect_error]);
    exit();
}

// Kiểm tra phương thức HTTP
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    echo json_encode(["status" => "error", "message" => "Phương thức không hợp lệ. Chỉ chấp nhận POST."]);
    exit();
}

// Nhận và giải mã dữ liệu JSON
$data = json_decode(file_get_contents('php://input'), true);

// Kiểm tra lỗi JSON
if (json_last_error() !== JSON_ERROR_NONE) {
    echo json_encode(["status" => "error", "message" => "Lỗi JSON: " . json_last_error_msg()]);
    exit();
}

// Kiểm tra dữ liệu đầu vào
if (!$data || empty($data['id_nguoi_gui']) || empty($data['id_nguoi_nhan']) || !isset($data['tin_nhan'])) {
    echo json_encode(["status" => "error", "message" => "Thiếu dữ liệu đầu vào. Vui lòng kiểm tra id_nguoi_gui, id_nguoi_nhan và tin_nhan."]);
    exit();
}

// Gán và làm sạch dữ liệu
$id_nguoi_gui = (int) $data['id_nguoi_gui'];
$id_nguoi_nhan = (int) $data['id_nguoi_nhan'];
$tin_nhan = trim($conn->real_escape_string($data['tin_nhan']));
$hinh_anh = isset($data['hinh_anh']) ? trim($conn->real_escape_string($data['hinh_anh'])) : NULL;

// Kiểm tra nội dung tin nhắn
if (empty($tin_nhan)) {
    echo json_encode(["status" => "error", "message" => "Nội dung tin nhắn không được để trống."]);
    exit();
}

// Debug log để kiểm tra dữ liệu
error_log("Dữ liệu nhận được: " . print_r($data, true));

// Thực hiện câu lệnh SQL
$sql = "INSERT INTO tin_nhan_tu_van (id_nguoi_gui, id_nguoi_nhan, tin_nhan, hinh_anh, thoi_gian_gui) VALUES (?, ?, ?, ?, NOW())";
$stmt = $conn->prepare($sql);

if (!$stmt) {
    echo json_encode(["status" => "error", "message" => "Lỗi prepare statement: " . $conn->error]);
    exit();
}

$stmt->bind_param('iiss', $id_nguoi_gui, $id_nguoi_nhan, $tin_nhan, $hinh_anh);

// Thực thi và kiểm tra kết quả
if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Tin nhắn đã được gửi thành công"]);
} else {
    echo json_encode(["status" => "error", "message" => "Lỗi khi gửi tin nhắn: " . $stmt->error]);
}

// Đóng statement và kết nối
$stmt->close();
$conn->close();

