<?php
set_time_limit(300);
ignore_user_abort(true);

// Responde inmediatamente a cron-job.org (antes de los 30s)
header("Content-Type: application/json; charset=UTF-8");
header("Connection: close");
ob_start();
echo json_encode(["status" => "OK", "mensaje" => "Procesando en background..."]);
$size = ob_get_length();
header("Content-Length: $size");
ob_end_flush();
flush();

require_once "db.php";
$conn = getConnection();

$sql_tarea = "
    SELECT COUNT(*) AS count
    FROM (
        SELECT mae.id_maeclientes
        FROM maeclientesporcentaje mae
        INNER JOIN movclientesporcentaje mov 
            ON mae.id_maeclientes = mov.id_maeclientes
        WHERE mae.num_debe > 0
        GROUP BY mae.id_maeclientes
        HAVING MAX(DATE(mov.fec_pago)) < DATE(CONVERT_TZ(NOW(), 'UTC', 'America/Mazatlan')) + INTERVAL 1 DAY
    ) AS sub
";

$iteraciones = 0;

try {

    do {

        // Obtener el conteo actual
        $resultado = $conn->query($sql_tarea);
        $countClientes = 0;

        if ($resultado && $row = $resultado->fetch_assoc()) {
            $countClientes = (int)($row["count"] ?? 0);
        }

        if ($countClientes > 0) {

            // Ejecutar el Stored Procedure
            $conn->query("CALL spGeneraFechasPagoPor()");
            $iteraciones++;

            // Esperar 1 segundo antes de volver a consultar
            sleep(1);
        }

    } while ($countClientes > 0);

    echo json_encode([
        "status"      => "OK",
        "mensaje"     => "SP ejecutado correctamente",
        "iteraciones" => $iteraciones
    ]);

} catch (Exception $e) {

    echo json_encode([
        "status"  => "ERROR",
        "mensaje" => $e->getMessage()
    ]);

}

$conn->close();
?>