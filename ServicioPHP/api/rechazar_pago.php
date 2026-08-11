<?php
header("Content-Type: application/json; charset=UTF-8");
require_once "db_pruebas.php";
$conn = getConnection();

$id_movclientes_temp = $_POST['id_movclientes_temp'] ?? null;
$id_empleado_valida  = $_POST['id_empleado_valida']  ?? null;
$observaciones       = $_POST['observaciones']       ?? 'Sin motivo especificado';

if (!$id_movclientes_temp || !$id_empleado_valida) {
    echo json_encode(["status" => "ERROR", "mensaje" => "Faltan datos requeridos"]);
    exit;
}

$stmt = $conn->prepare("
    UPDATE movclientes_temp SET
        estatus            = 'rechazado',
        fec_validacion     = CONVERT_TZ(NOW(), 'UTC', 'America/Mazatlan'),
        id_empleado_valida = ?,
        observaciones      = ?
    WHERE id_movclientes_temp = ?
      AND estatus = 'pendiente'
");
$stmt->bind_param("isi", $id_empleado_valida, $observaciones, $id_movclientes_temp);
$stmt->execute();

if ($stmt->affected_rows === 0) {
    echo json_encode(["status" => "ERROR", "mensaje" => "Pago no encontrado o ya procesado"]);
    exit;
}

$stmt->close();
$conn->close();

echo json_encode(["status" => "OK", "mensaje" => "Pago rechazado"]);
?>