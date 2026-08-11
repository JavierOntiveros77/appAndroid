<?php
header("Content-Type: application/json; charset=UTF-8");

// =====================================
// VERIFICACIÓN DEL WEBHOOK
// =====================================
$verify_token = "CrediEficazWebhook2026";

if ($_SERVER["REQUEST_METHOD"] === "GET") {
    $mode      = $_GET["hub_mode"]          ?? "";
    $token     = $_GET["hub_verify_token"]  ?? "";
    $challenge = $_GET["hub_challenge"]     ?? "";

    if ($mode === "subscribe" && $token === $verify_token) {
        echo $challenge;
    } else {
        http_response_code(403);
        echo "Forbidden";
    }
    exit;
}

// =====================================
// RECIBIR NOTIFICACIÓN DE META (POST)
// =====================================
$body    = file_get_contents("php://input");
$payload = json_decode($body, true);

if (!$payload) {
    http_response_code(400);
    echo json_encode(["status" => "ERROR", "msg" => "Payload inválido"]);
    exit;
}

require_once "db.php";
$conn = getConnection();

// =====================================
// RECORRER ENTRADAS DEL PAYLOAD
// =====================================
foreach ($payload["entry"] ?? [] as $entry) {
    foreach ($entry["changes"] ?? [] as $change) {

        $value = $change["value"] ?? [];

        // ── Actualización de estado de mensaje ──
        foreach ($value["statuses"] ?? [] as $status) {

            $wamid     = $status["id"]        ?? null;
            $estado    = $status["status"]     ?? null; // sent|delivered|read|failed
            $timestamp = $status["timestamp"]  ?? null;

            if (!$wamid || !$estado) continue;

            // Estado adicional si falló
            $error_detalle = null;
            if ($estado === "failed" && isset($status["errors"][0])) {
                $error_detalle = $status["errors"][0]["title"]
                               . " (código: " . $status["errors"][0]["code"] . ")";
            }

            // Actualiza el registro en la tabla usando el wamid como llave
            $stmt = $conn->prepare("
                UPDATE whatsapp_mensajes
                SET
                    estado         = ?,
                    error_detalle  = COALESCE(?, error_detalle),
                    fecha_status   = FROM_UNIXTIME(?)
                WHERE wamid = ?
            ");
            $stmt->bind_param("ssis", $estado, $error_detalle, $timestamp, $wamid);
            $stmt->execute();
            $stmt->close();
        }

        // ── Mensaje entrante (respuesta del cliente) — opcional ──
        foreach ($value["messages"] ?? [] as $msg) {
            // Aquí podrías guardar respuestas si las necesitas
        }
    }
}

// Meta espera siempre HTTP 200, de lo contrario reintenta
http_response_code(200);
echo json_encode(["status" => "OK"]);
?>