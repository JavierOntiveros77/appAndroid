<?php

header("Content-Type: application/json");

require_once "db.php";

$conn = getConnection();

$sql = "SELECT 
	mov.id_maeclientes AS NumeroCliente,
	mae.nom_cliente AS Nombre,
	mae.nom_referencia AS Referencia,
	DATE_FORMAT(mov.fec_pago, '%d/%m/%Y') AS FechaPago,
	TIME_FORMAT(hr_cobro, '%H:%i') AS HoraCobro,
	mov.num_abono AS NumPago,
	mae.num_pagosrestantes - 1 AS PagosRestantes,
	mae.num_abonar AS Abono,
	mae.num_debe AS Liquida,
	CASE 
		WHEN mae.id_catplazos = 1 THEN 'Semanal'
		WHEN mae.id_catplazos = 2 THEN 'Catorcenal'
		WHEN mae.id_catplazos = 3 THEN '15 - 30'
		WHEN mae.id_catplazos = 4 THEN 'Mensual'
		WHEN mae.id_catplazos = 5 THEN '1 - 16'
		WHEN mae.id_catplazos = 6 THEN '5 - 20'
		WHEN mae.id_catplazos = 7 THEN '13 - 28'
	END AS Plazo,
	mae.num_renovacion AS Renovación
	FROM movclientes mov
	INNER JOIN maeclientes mae 
		ON mae.id_maeclientes = mov.id_maeclientes 
		AND mae.num_renovacion = mov.num_renovacion
	WHERE mae.num_debe > 0
	  AND mov.id_cattipomovimiento = 4
	  AND mov.num_cantidadPagada <= 0
	  AND DATE(mov.fec_pago) <= DATE(CONVERT_TZ(NOW(),'UTC','America/Mexico_City'))
	GROUP BY mae.id_maeclientes, mov.num_abono
	ORDER BY mov.fec_pago, hr_cobro";

$result = $conn->query($sql);

$data = [];

while ($row = $result->fetch_assoc()) {
    $data[] = $row;
}

echo json_encode($data);