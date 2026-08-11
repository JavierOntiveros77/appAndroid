<?php
header("Content-Type: application/json; charset=UTF-8");
require_once "db_pruebas.php";
$conn = getConnection();

$id_maeclientes       = $_POST['id_maeclientes']       ?? null;
$num_abono            = $_POST['num_abono']            ?? null;
$num_renovacion       = $_POST['num_renovacion']       ?? null;
$num_cantidad         = $_POST['num_cantidad']         ?? null;
$num_cantidadtotal    = $_POST['num_cantidadtotal']    ?? null;
$fec_pago             = $_POST['fec_pago']             ?? null;
$num_cantidadPagada   = $_POST['num_cantidadPagada']   ?? null;
$id_empleado          = $_POST['id_empleado']          ?? null;

if (!$id_maeclientes || !$num_abono || !$num_renovacion || !$num_cantidadPagada || !$id_empleado) {
    echo json_encode(["status" => "ERROR", "mensaje" => "Faltan datos requeridos"]);
    exit;
}

// Verificar que no exista ya un pago pendiente para este abono
$stmt = $conn->prepare("
    SELECT id_movclientes_temp FROM movclientes_temp
    WHERE id_maeclientes = ?
      AND num_abono      = ?
      AND num_renovacion = ?
      AND estatus        = 'pendiente'
    LIMIT 1
");
$stmt->bind_param("iii", $id_maeclientes, $num_abono, $num_renovacion);
$stmt->execute();
$result = $stmt->get_result();
$existe  = $result->fetch_assoc();
$stmt->close();

if ($existe) {

    // Ya existe — actualizamos la cantidad del pago
    $id_existente = $existe["id_movclientes_temp"];

    $stmt = $conn->prepare("
        UPDATE movclientes_temp SET
            num_cantidadPagada = ?,
            id_empleado        = ?,
            fec_registro       = CONVERT_TZ(NOW(), 'UTC', 'America/Mazatlan')
        WHERE id_movclientes_temp = ?
    ");
    $stmt->bind_param("dii", $num_cantidadPagada, $id_empleado, $id_existente);
    $stmt->execute();
    $stmt->close();
    $conn->close();

    echo json_encode([
        "status"  => "UPDATED",
        "mensaje" => "Cantidad del pago actualizada correctamente"
    ]);
    exit;
} else {
	$stmt = $conn->prepare("
		INSERT INTO movclientes_temp
			(id_maeclientes, num_abono, num_renovacion, id_cattipomovimiento,
			 num_cantidad, num_cantidadtotal, fec_pago, num_cantidadPagada, id_empleado)
		VALUES (?, ?, ?, 4, ?, ?, ?, ?, ?)
	");
	$stmt->bind_param(
		"iiiiddsi",
		$id_maeclientes,
		$num_abono,
		$num_renovacion,
		$num_cantidad,
		$num_cantidadtotal,
		$fec_pago,
		$num_cantidadPagada,
		$id_empleado
	);
	$stmt->execute();
	$stmt->close();
	$conn->close();
}

echo json_encode(["status" => "OK", "mensaje" => "Pago registrado, pendiente de aprobación"]);
?>