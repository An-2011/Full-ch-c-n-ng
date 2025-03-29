<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

$host = "localhost";  // Máy chủ MySQL
$username = "root";   // Tài khoản MySQL (thường là root trong XAMPP)
$password = "";       // Mật khẩu MySQL (thường trống nếu dùng XAMPP)
$database = "ung_dung_co_xuong_khop_cua_ban"; // Đổi lại đúng tên database

// Kết nối MySQL
$conn = new mysqli($host, $username, $password, $database);

// Kiểm tra kết nối
if ($conn->connect_error) {
    die(json_encode(["status" => "error", "message" => "Kết nối thất bại: " . $conn->connect_error]));
}

// Không hiển thị "Kết nối thành công!" vì API không cần thông báo này
?>
