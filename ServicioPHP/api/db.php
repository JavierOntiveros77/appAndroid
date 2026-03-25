<?php
// Asegúrate de que no haya ni un solo espacio o línea arriba de esta etiqueta
function getConnection() {
    $host = "mysql-credieficaz2.alwaysdata.net";
    $user = "440190_pagos";
    $password = "Credieficaz0802";
    $database = "credieficaz2_dbclientes";

    $conn = new mysqli($host, $user, $password, $database);

    if ($conn->connect_error) {
        header('Content-Type: application/json');
        echo json_encode([
            "authorized" => false,
            "message" => "Error de conexión a la base de datos"
        ]);
        exit;
    }

    $conn->set_charset("utf8mb4");
    return $conn;
}
?>