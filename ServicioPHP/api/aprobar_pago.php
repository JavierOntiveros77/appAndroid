<?php
header("Content-Type: application/json; charset=UTF-8");
require_once "db_pruebas.php";
$conn = getConnection();

$id_movclientes_temp = $_POST['id_movclientes_temp'] ?? null;
$id_empleado_valida  = $_POST['id_empleado_valida']  ?? null;

if (!$id_movclientes_temp || !$id_empleado_valida) {
    echo json_encode(["status" => "ERROR", "mensaje" => "Faltan datos requeridos"]);
    exit;
}

// Obtener datos del pago temporal
$stmt = $conn->prepare("
    SELECT t.*, mae.num_abonar, mae.num_debe, mae.num_pagosrestantes
    FROM movclientes_temp t
    INNER JOIN maeclientes mae ON mae.id_maeclientes = t.id_maeclientes
    WHERE t.id_movclientes_temp = ? AND t.estatus = 'pendiente'
    LIMIT 1
");
$stmt->bind_param("i", $id_movclientes_temp);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 0) {
    echo json_encode(["status" => "ERROR", "mensaje" => "Pago no encontrado o ya procesado"]);
    exit;
}

$pago = $result->fetch_assoc();
$stmt->close();

$id_maeclientes    = $pago["id_maeclientes"];
$num_abono         = $pago["num_abono"];
$num_renovacion    = $pago["num_renovacion"];
$cantidadPagada    = $pago["num_cantidadPagada"];
$num_abonar        = $pago["num_abonar"];       // monto esperado del abono
$num_debe          = $pago["num_debe"];
$num_pagosrestantes = $pago["num_pagosrestantes"];

// Determinar si el pago fue completo o no
$pagoAtraso = ($cantidadPagada >= $num_abonar) ? 0 : 1;

$conn->begin_transaction();

try {

    // 1. UPDATE movclientes
    $stmt = $conn->prepare("
        UPDATE movclientes SET
            num_cantidadPagada = ?,
            num_pagoAtraso     = ?,
            fec_movto          = CONVERT_TZ(NOW(), 'UTC', 'America/Mazatlan')
        WHERE id_maeclientes        = ?
          AND num_renovacion        = ?
          AND id_cattipomovimiento  = 4
          AND num_abono             = ?
    ");
    $stmt->bind_param("diiii", $cantidadPagada, $pagoAtraso, $id_maeclientes, $num_renovacion, $num_abono);
    $stmt->execute();
    $stmt->close();

    // 2. UPDATE maeclientes
    $nuevo_debe            = $num_debe - $cantidadPagada;
    $nuevo_pagosrestantes  = $num_pagosrestantes - 1;

    $stmt = $conn->prepare("
        UPDATE maeclientes SET
            num_debe            = ?,
            num_pagosrestantes  = ?,
            fec_ultmov          = CONVERT_TZ(NOW(), 'UTC', 'America/Mazatlan')
        WHERE id_maeclientes = ?
    ");
    $stmt->bind_param("dii", $nuevo_debe, $nuevo_pagosrestantes, $id_maeclientes);
    $stmt->execute();
    $stmt->close();

    // 3. UPDATE movclientes_temp → aprobado
    $stmt = $conn->prepare("
        UPDATE movclientes_temp SET
            estatus            = 'aprobado',
            fec_validacion     = CONVERT_TZ(NOW(), 'UTC', 'America/Mazatlan'),
            id_empleado_valida = ?
        WHERE id_movclientes_temp = ?
    ");
    $stmt->bind_param("ii", $id_empleado_valida, $id_movclientes_temp);
    $stmt->execute();
    $stmt->close();

    $conn->commit();

    echo json_encode([
        "status"         => "OK",
        "mensaje"        => "Pago aprobado correctamente",
        "nuevo_debe"     => $nuevo_debe,
        "pago_atraso"    => $pagoAtraso
    ]);

} catch (Exception $e) {
    $conn->rollback();
    echo json_encode(["status" => "ERROR", "mensaje" => $e->getMessage()]);
}

$conn->close();
?>