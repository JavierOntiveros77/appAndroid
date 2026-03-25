<?php
error_reporting(0); 
header('Content-Type: application/json; charset=utf-8');

require_once "db.php";

$db = getConnection(); 

$pass_recibida = $_REQUEST['pass'] ?? '';

$query = $db->prepare("SELECT id_cat_accesos FROM cat_accesos WHERE password = ? LIMIT 1");
$query->bind_param("s", $pass_recibida);
$query->execute();
$resultado = $query->get_result();

$response = ["authorized" => false];

if ($resultado->num_rows > 0) {
    $response["authorized"] = true;
}

echo json_encode($response);
exit;
?>