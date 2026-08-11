<?php
set_time_limit(300);
ignore_user_abort(true);

require_once "db.php";
$conn = getConnection();

$token           = "EAAUzrOtL8jkBR5WJPJIDZA6vvHI9ZChZAVD2JIR5LiofVRfsfVA5SZACaqZAhjT1dPkxFzRtXFlXI70rZCVzNXoKkUKG0OAwKVQ0IBvd8RWa1RvNdMpFyQXyHCxkYYVkpEP2KyXVaaigK7zlMibqOFFQ8ZAXgawsZBnjDD1JgqZCLr6nliMQHVFSWKREg1LviKQZDZD";
$phone_number_id = "1194265853776961";
$url             = "https://graph.facebook.com/v25.0/{$phone_number_id}/messages";
$plantilla       = "recordatorio_de_pago";
$id_cliente      = 0;

// =====================================
// ENVIO DEFAULT
// =====================================
$telefono = 6672365384;

// Armar body para Meta
$data = [
	"messaging_product" => "whatsapp",
	"to"                => $telefono,
	"type"              => "template",
	"template"          => [
		"name"     => $plantilla,
		"language" => ["code" => "es_MX"]
	]
];

// Enviar a Meta
$curl = curl_init();
curl_setopt_array($curl, [
	CURLOPT_URL            => $url,
	CURLOPT_RETURNTRANSFER => true,
	CURLOPT_POST           => true,
	CURLOPT_POSTFIELDS     => json_encode($data),
	CURLOPT_HTTPHEADER     => [
		"Authorization: Bearer " . $token,
		"Content-Type: application/json"
	]
]);

$response  = curl_exec($curl);
$http_code = curl_getinfo($curl, CURLINFO_HTTP_CODE);
curl_close($curl);
	
	
// =====================================
// ENVIO MASIVO
// =====================================
$queryClientes = $conn->query("
    SELECT DISTINCT mae.tel_cliente
    FROM maeclientes mae
    LEFT JOIN movclientes mov
        ON mov.id_maeclientes = mae.id_maeclientes
    WHERE DATE(mov.fec_pago) = DATE(CONVERT_TZ(NOW(), 'UTC', 'America/Mazatlan')) + INTERVAL 1 DAY
");

if (!$queryClientes || $queryClientes->num_rows === 0) {
    exit;
}

while ($row = $queryClientes->fetch_assoc()) {

    $telefono = $row["tel_cliente"];

    $data = [
        "messaging_product" => "whatsapp",
        "to"                => $telefono,
        "type"              => "template",
        "template"          => [
            "name"     => $plantilla,
            "language" => ["code" => "es_MX"]
        ]
    ];

    $curl = curl_init();
    curl_setopt_array($curl, [
        CURLOPT_URL            => $url,
        CURLOPT_RETURNTRANSFER => true,
        CURLOPT_POST           => true,
        CURLOPT_POSTFIELDS     => json_encode($data),
        CURLOPT_HTTPHEADER     => [
            "Authorization: Bearer " . $token,
            "Content-Type: application/json"
        ]
    ]);

    $response  = curl_exec($curl);
    $http_code = curl_getinfo($curl, CURLINFO_HTTP_CODE);

    if ($response === false) {
        $error = curl_error($curl);
        curl_close($curl);

        $stmt = $conn->prepare("
            INSERT INTO whatsapp_mensajes
                (id_cliente, telefono, wamid, plantilla, estado, error_detalle, fecha_envio)
            VALUES (?, ?, NULL, ?, 'failed', ?, CONVERT_TZ(NOW(), 'UTC', 'America/Mazatlan'))
        ");
        $stmt->bind_param("isss", $id_cliente, $telefono, $plantilla, $error);
        $stmt->execute();
        $stmt->close();
        continue;
    }

    curl_close($curl);
    $resultado = json_decode($response, true);

    if (isset($resultado["messages"][0]["id"])) {

        $wamid = $resultado["messages"][0]["id"];

        $stmt = $conn->prepare("
            INSERT INTO whatsapp_mensajes
                (id_cliente, telefono, wamid, plantilla, estado, fecha_envio)
            VALUES (?, ?, ?, ?, 'accepted', CONVERT_TZ(NOW(), 'UTC', 'America/Mazatlan'))
        ");
        $stmt->bind_param("isss", $id_cliente, $telefono, $wamid, $plantilla);
        $stmt->execute();
        $stmt->close();

    } else {

        $error_msg = $resultado["error"]["message"] ?? "Error desconocido";

        $stmt = $conn->prepare("
            INSERT INTO whatsapp_mensajes
                (id_cliente, telefono, wamid, plantilla, estado, error_detalle, fecha_envio)
            VALUES (?, ?, NULL, ?, 'failed', ?, CONVERT_TZ(NOW(), 'UTC', 'America/Mazatlan'))
        ");
        $stmt->bind_param("isss", $id_cliente, $telefono, $plantilla, $error_msg);
        $stmt->execute();
        $stmt->close();
    }
}

$conn->close();
?>