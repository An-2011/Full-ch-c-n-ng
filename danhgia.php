<?php
include 'db_connect.php';
header('Content-Type: application/json; charset=UTF-8');

// Nhận dữ liệu JSON từ request
$json = file_get_contents('php://input');
$data = json_decode($json, true);

// Kiểm tra JSON hợp lệ
if (json_last_error() !== JSON_ERROR_NONE) {
    echo json_encode(["status" => "error", "message" => "❌ Dữ liệu JSON không hợp lệ"]);
    exit;
}

// Ghi log dữ liệu nhận từ ứng dụng
file_put_contents("log_danhgia.txt", "[" . date("Y-m-d H:i:s") . "] Request: " . print_r($data, true) . "\n", FILE_APPEND);

// Kiểm tra tham số action
$action = $data['action'] ?? null;
if (!$action) {
    echo json_encode(["status" => "error", "message" => "❌ Thiếu tham số action"]);
    exit;
}

// ✅ Thêm đánh giá mới
if ($action === "add") {
    $user_id = isset($data['user_id']) ? intval($data['user_id']) : null;
    $id_bac_si = isset($data['id_bac_si']) ? intval($data['id_bac_si']) : null;
    $diem_so = isset($data['diem_so']) ? intval($data['diem_so']) : null;
    $nhan_xet = $data['nhan_xet'] ?? '';

    if (!$user_id || !$id_bac_si || !$diem_so) {
        echo json_encode(["status" => "error", "message" => "❌ Thiếu dữ liệu bắt buộc", "received_id_bac_si" => $id_bac_si]);
        exit;
    }

    $sql = "INSERT INTO danh_gia (id_nguoi_dung, id_bac_si, diem_so, nhan_xet, ngay_tao) 
            VALUES (?, ?, ?, ?, NOW())";
    $stmt = $conn->prepare($sql);
    if ($stmt) {
        $stmt->bind_param("iiis", $user_id, $id_bac_si, $diem_so, $nhan_xet);
        if ($stmt->execute()) {
            echo json_encode(["status" => "success", "message" => "✅ Đánh giá đã được thêm"]);
        } else {
            echo json_encode(["status" => "error", "message" => "❌ Lỗi khi thêm đánh giá: " . $stmt->error]);
        }
        $stmt->close();
    } else {
        echo json_encode(["status" => "error", "message" => "❌ Lỗi truy vấn khi thêm đánh giá"]);
    }
    $conn->close();
    exit;
}

// ✅ Lấy danh sách đánh giá theo bác sĩ
if ($action === "fetch_by_doctor") {
    $id_bac_si = isset($data['id_bac_si']) ? intval($data['id_bac_si']) : null;

    if ($id_bac_si) {
        $sql = "SELECT id, id_nguoi_dung, id_bac_si, diem_so, nhan_xet, ngay_tao 
                FROM danh_gia 
                WHERE id_bac_si = ? 
                ORDER BY ngay_tao DESC";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("i", $id_bac_si);
    } else {
        echo json_encode(["status" => "error", "message" => "❌ ID bác sĩ không hợp lệ", "received_id_bac_si" => $id_bac_si]);
        exit;
    }

    if ($stmt) {
        $stmt->execute();
        $result = $stmt->get_result();
        $reviews = [];

        while ($row = $result->fetch_assoc()) {
            $reviews[] = $row;
        }

        echo json_encode(["status" => "success", "data" => $reviews]);
        $stmt->close();
    } else {
        error_log("❌ Lỗi truy vấn khi lấy đánh giá theo bác sĩ: " . $conn->error);
        echo json_encode(["status" => "error", "message" => "❌ Lỗi truy vấn khi lấy đánh giá theo bác sĩ"]);
    }
    $conn->close();
    exit;
}
?>