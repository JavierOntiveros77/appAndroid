<?php

header("Content-Type: application/json");

require_once "db.php";

$conn = getConnection();

$sql = "SELECT id_maerepresentantes as Grupo, 
	nom_grupo as NombreGrupo, 
	num_ciclo as NumCiclo,
	nom_representante NombreRepresentante, 
	des_domiciliorepresentante as Domicilio, 
	num_telefono as NumTelefono,
	num_integrantes as NumIntegrantes, 
	num_pagostotales as NumPagos, 
	DATE_FORMAT(fecha_pago, '%d/%m/%Y') as FechaPróximoPago,
	num_prestamo as Préstamo, 
	num_prestamototal as PréstamoTotal, 
	num_pagopendiente as Pendiente,
	CASE WHEN id_catplazos = 1 THEN 'Semanal' 
	WHEN id_catplazos = 2 THEN 'Catorcenal' 
	WHEN id_catplazos = 3 THEN '15 - 30'
	WHEN id_catplazos = 4 THEN 'Mensual' 
	WHEN id_catplazos = 6 THEN '5 - 20'
	WHEN id_catplazos = 7 THEN '13 - 28' END as Plazo
FROM maerepresentantes";

$result = $conn->query($sql);

$data = [];

while ($row = $result->fetch_assoc()) {
    $data[] = $row;
}

echo json_encode($data);