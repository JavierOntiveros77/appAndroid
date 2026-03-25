<?php

header("Content-Type: application/json");

require_once "db.php";

$conn = getConnection();

$sql = "SELECT mae.id_maerepresentantes as Grupo, mae.nom_grupo as NombreGrupo, 
	mae.num_ciclo as NumCiclo, mae.nom_representante NombreRepresentante,
	mae.num_integrantes as NumIntegrantes,
	DATE_FORMAT(mov.fec_pago, '%d/%m/%Y') as FechaPago,
	mov.num_pago as NumPago, 
	mae.num_pagostotales as De,
	mae.num_prestamototal PrestamoTotal, mae.num_pagopendiente as Liquida,
	CASE WHEN mae.id_catplazos = 1 THEN 'Semanal' 
	WHEN mae.id_catplazos = 2 THEN 'Catorcenal' 
	WHEN mae.id_catplazos = 3 THEN '15 - 30' 
	WHEN mae.id_catplazos = 4 THEN 'Mensual'
	WHEN mae.id_catplazos = 5 THEN '1 - 16'
	WHEN mae.id_catplazos = 6 THEN '5 - 20'
	WHEN mae.id_catplazos = 7 THEN '13 - 28' END as Plazo
	FROM maerepresentantes mae
	LEFT JOIN movgrupos mov ON mov.id_grupo = mae.id_maerepresentantes
	LEFT JOIN maegrupos mgr ON mgr.id_integrante = mae.id_maerepresentantes
	WHERE mae.num_pagopendiente > 0 and DATE(mov.fec_pago) <= DATE(CONVERT_TZ(NOW(),'UTC','America/Mexico_City')) 
	AND mov.num_cantidad <= 0
	GROUP BY mae.id_maerepresentantes ORDER BY mov.fec_pago";

$result = $conn->query($sql);

$data = [];

while ($row = $result->fetch_assoc()) {
    $data[] = $row;
}

echo json_encode($data);