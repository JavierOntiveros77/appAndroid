<?php
header("Content-Type: application/json; charset=UTF-8");

// Dispara el procesamiento de forma asíncrona
$ch = curl_init("https://credieficaz2.alwaysdata.net/api/procesar_whatsapp.php");
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_TIMEOUT, 1);      // Solo espera 1 segundo
curl_setopt($ch, CURLOPT_NOSIGNAL, 1);
curl_exec($ch);
curl_close($ch);

// Responde inmediatamente a cron-job.org
echo json_encode([
    "status"  => "OK",
    "mensaje" => "Procesando en background..."
]);
?>