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
?>