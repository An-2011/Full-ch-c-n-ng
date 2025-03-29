<?php
header("Content-Type: application/json; charset=UTF-8");

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

// Nhận dữ liệu từ JSON
$data = json_decode(file_get_contents('php://input'), true);

if (!isset($data['id_nguoi_gui']) || !isset($data['id_nguoi_nhan'])) {
    echo json_encode(["status" => "error", "message" => "Thiếu thông tin người gửi hoặc người nhận"]);
    exit();
}

$id_nguoi_gui = (int) $data['id_nguoi_gui'];
$id_nguoi_nhan = (int) $data['id_nguoi_nhan'];

// Truy vấn tin nhắn giữa 2 người dùng
$sql = "SELECT id, id_nguoi_gui, id_nguoi_nhan, tin_nhan, hinh_anh, thoi_gian_gui FROM messages 
        WHERE ((id_nguoi_gui = ? AND id_nguoi_nhan = ?) OR (id_nguoi_gui = ? AND id_nguoi_nhan = ?)) 
        AND (tin_nhan IS NOT NULL AND TRIM(tin_nhan) != '') 
        ORDER BY thoi_gian_gui ASC";

$stmt = $conn->prepare($sql);
$stmt->bind_param('iiii', $id_nguoi_gui, $id_nguoi_nhan, $id_nguoi_nhan, $id_nguoi_gui);
$stmt->execute();
$result = $stmt->get_result();

$messages = [];
while ($row = $result->fetch_assoc()) {
    $messages[] = [
        "id" => $row['id'],
        "id_nguoi_gui" => $row['id_nguoi_gui'],
        "id_nguoi_nhan" => $row['id_nguoi_nhan'],
        "tin_nhan" => $row['tin_nhan'],
        "hinh_anh" => $row['hinh_anh'],
        "thoi_gian_gui" => $row['thoi_gian_gui']
    ];
}

$response = ["status" => "success", "message" => "Lấy tin nhắn thành công", "data" => $messages];
echo json_encode($response, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);

$stmt->close();
$conn->close();
