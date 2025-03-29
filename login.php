<?php
include 'db_connect.php'; // Kết nối database

header("Content-Type: application/json; charset=UTF-8");

// 🔹 Xóa dữ liệu dư thừa trước khi trả JSON
ob_clean();
$jsonData = file_get_contents("php://input");
$data = json_decode($jsonData, true);

// Lấy dữ liệu từ JSON request
$email = isset($data['email']) ? trim($data['email']) : null;
$password = isset($data['mat_khau']) ? trim($data['mat_khau']) : null;

// Kiểm tra input
if (empty($email) || empty($password)) {
    echo json_encode(["status" => "error", "message" => "Vui lòng nhập email và mật khẩu!"]);
    exit;
}

// Kiểm tra tài khoản trong database
$stmt = $conn->prepare("SELECT id, email, mat_khau FROM nguoi_dung WHERE email = ?");
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $user = $result->fetch_assoc();

    // Kiểm tra mật khẩu (hỗ trợ mã hóa)
    if (password_verify($password, $user['mat_khau']) || $password === $user['mat_khau']) {
        echo json_encode([
            "status" => "success",
            "message" => "Đăng nhập thành công!",
            "user_id" => $user['id'], // Trả về user_id
            "email" => $user['email']
        ]);
    } else {
        echo json_encode(["status" => "error", "message" => "Sai mật khẩu!"]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Không tìm thấy email!"]);
}

$stmt->close();
$conn->close();
?>
