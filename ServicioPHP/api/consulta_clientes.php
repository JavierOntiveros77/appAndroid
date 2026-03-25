<?php

header("Content-Type: application/json");

require_once "db.php";

$conn = getConnection();

$sql = "SELECT mae.id_maeclientes as Numcliente, 
	mae.nom_cliente as Nombre, 
	mae.num_cantidad as Préstamo,
	mae.num_cantidadtotal as PréstamoTotal, 
	mae.num_debe as Debe, mae.num_abonar as Abona, 
	(SELECT COUNT(1) FROM movclientes mv2 WHERE mv2.id_maeclientes = mae.id_maeclientes AND mv2.num_renovacion = mae.num_renovacion) as TotalPagos,
	CASE WHEN mae.num_debe = 0 THEN 0 ELSE num_pagosrestantes END as PagosRestantes, 
	mae.des_domicilio as Domicilio, 
	mae.tel_cliente as Teléfono, 
	mae.des_trabajo as Trabajo, 
	mae.num_ingresos as Ingresos, 
	mae.nom_referencia as Referencia, 
	CASE WHEN id_catplazos = 1 THEN 'Semanal' 
	WHEN id_catplazos = 2 THEN 'Catorcenal' 
	WHEN id_catplazos = 3 THEN '15 - 30'
	WHEN id_catplazos = 4 THEN 'Mensual' 
	WHEN id_catplazos = 5 THEN '1 - 16'
	WHEN id_catplazos = 6 THEN '5 - 20'
	WHEN id_catplazos = 7 THEN '13 - 28' END as Plazo,
	DATE_FORMAT(mae.fecha_cobro, '%d/%m/%Y') as FechaCobro, 
	TIME_FORMAT(mae.hr_cobro, '%H:%i') as HoraCobro, 
	DATE_FORMAT(mae.fec_ultmov, '%d/%m/%Y') as ÚltimoMovimiento, 
	mae.id_estatus as Confianza, 
	mae.num_renovacion as Renovación,
	(SELECT IFNULL(SUM(mov.num_atrasos), 0) 
FROM movclientes mov 
WHERE mov.id_maeclientes = mae.id_maeclientes AND mov.num_renovacion = mae.num_renovacion) as Atrasos
FROM maeclientes mae
GROUP BY mae.id_maeclientes
ORDER BY FechaCobro, mae.num_debe DESC";

$result = $conn->query($sql);

$data = [];

while ($row = $result->fetch_assoc()) {
    $data[] = $row;
}

echo json_encode($data);