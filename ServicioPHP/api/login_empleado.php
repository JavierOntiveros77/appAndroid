<?php
header("Content-Type: application/json; charset=UTF-8");
require_once "db_pruebas.php";
$conn = getConnection();

$usuario = $_POST['usuario'] ?? '';
$password = $_POST['password'] ?? '';

if (!$usuario || !$password) {
    echo json_encode(["status" => "ERROR", "mensaje" => "Usuario y contraseña requeridos"]);
    exit;
}

$stmt = $conn->prepare("
    SELECT id_empleado, nombre, rol
    FROM empleados
    WHERE usuario = ?
      AND password = MD5(?)
      AND activo = 1
    LIMIT 1
");
$stmt->bind_param("ss", $usuario, $password);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 0) {
    echo json_encode(["status" => "ERROR", "mensaje" => "Credenciales incorrectas"]);
    exit;
}

$empleado = $result->fetch_assoc();
$stmt->close();
$conn->close();

echo json_encode([
    "status"      => "OK",
    "id_empleado" => $empleado["id_empleado"],
    "nombre"      => $empleado["nombre"],
    "rol"         => $empleado["rol"]
]);
?>