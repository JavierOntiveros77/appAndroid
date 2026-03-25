<?php

header("Content-Type: application/json");

require_once "db.php";

$conn = getConnection();

$sql = "SELECT 
mae.id_maeclientes AS Numcliente,
mae.nom_cliente AS Nombre,
mae.num_cantidad AS Préstamo,
mae.num_cantidadtotal AS PréstamoTotal,
mae.num_debe AS Debe,
mae.num_abonar AS Abona,
COALESCE(movdata.TotalPagos, 0) AS TotalPagos,
mae.des_domicilio AS Domicilio,
mae.tel_cliente AS Teléfono,
mae.des_garantia AS Garantía,
mae.porcentaje AS Porcentaje,
mae.cantidad_porcentaje AS CantidadPorcentaje,
mae.des_trabajo AS Trabajo,
mae.num_ingresos AS Ingresos,
mae.nom_referencia AS Referencia,
CASE 
	WHEN mae.id_catplazos = 1 THEN 'Semanal'
	WHEN mae.id_catplazos = 2 THEN 'Catorcenal'
	WHEN mae.id_catplazos = 3 THEN '15 - 30'
	WHEN mae.id_catplazos = 4 THEN 'Mensual'
	WHEN mae.id_catplazos = 5 THEN '1 - 16'
	WHEN mae.id_catplazos = 6 THEN '5 - 20'
	WHEN mae.id_catplazos = 7 THEN '13 - 28'
END AS Plazo,
(
	SELECT MIN(fec_pago)
	FROM movclientesporcentaje mov
	WHERE mov.id_maeclientes = mae.id_maeclientes
	  AND mov.num_renovacion = mae.num_renovacion
	  AND mov.num_cantidadPagada < mae.num_abonar
) AS FechaCobro,
mae.hr_cobro AS HoraCobro,
DATE_FORMAT(mae.fec_ultmov, '%d/%m/%Y') AS ÚltimoMovimiento,
mae.id_estatus AS Confianza,
mae.num_renovacion AS Renovación
FROM maeclientesporcentaje mae
LEFT JOIN (
	SELECT 
		id_maeclientes,
		num_renovacion,
		COUNT(*) AS TotalPagos,
		SUM(IFNULL(num_atrasos, 0)) AS Atrasos
	FROM movclientesporcentaje
	GROUP BY id_maeclientes, num_renovacion
) AS movdata 
  ON movdata.id_maeclientes = mae.id_maeclientes AND movdata.num_renovacion = mae.num_renovacion
ORDER BY FechaCobro, mae.num_debe DESC";

$result = $conn->query($sql);

$data = [];

while ($row = $result->fetch_assoc()) {
    $data[] = $row;
}

echo json_encode($data);