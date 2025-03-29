<?php
header("Access-Control-Allow-Origin: *");  
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// Thông tin kết nối CSDL
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ung_dung_co_xuong_khop";  // Đổi lại đúng tên CSDL của bạn

// Kết nối MySQL
$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die(json_encode(["status" => "error", "message" => "Kết nối thất bại: " . $conn->connect_error]));
}

// Truy vấn lấy danh sách bác sĩ
$sql = "SELECT id, ho_ten, chuyen_khoa, kinh_nghiem, dia_chi_phong_kham, so_dien_thoai, ngay_tao FROM bac_si";
$result = $conn->query($sql);

$doctors = [];
if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $doctors[] = $row;
    }
    echo json_encode(["status" => "success", "data" => $doctors], JSON_UNESCAPED_UNICODE);
} else {
    echo json_encode(["status" => "error", "message" => "Không có dữ liệu bác sĩ"]);
}

$conn->close();
?>
