<?php
header("Content-Type: application/json; charset=UTF-8");
require_once "db_pruebas.php";
$conn = getConnection();

$result = $conn->query("
    SELECT
        t.id_movclientes_temp,
        t.id_maeclientes,
        mae.nom_cliente         AS nombre_cliente,
        t.num_abono,
        t.num_renovacion,
        t.num_cantidadPagada,
        t.num_cantidad,
        t.num_cantidadtotal,
        DATE_FORMAT(t.fec_pago, '%d/%m/%Y')      AS fec_pago,
        DATE_FORMAT(t.fec_registro, '%d/%m/%Y %H:%i') AS fec_registro,
        e.nombre                AS nombre_empleado
    FROM movclientes_temp t
    INNER JOIN maeclientes mae ON mae.id_maeclientes = t.id_maeclientes
    INNER JOIN empleados    e  ON e.id_empleado      = t.id_empleado
    WHERE t.estatus = 'pendiente'
    ORDER BY t.fec_registro ASC
");

$data = [];
while ($row = $result->fetch_assoc()) {
    $data[] = $row;
}

$conn->close();
echo json_encode($data);
?>