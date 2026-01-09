<?php
require "conexion.php";

$nombre = $_POST["nombre"] ?? "";
$correo = $_POST["correo"] ?? "";
$password = $_POST["password"] ?? "";

// Encriptar contraseña (RECOMENDADO)
$passwordHash = $password;

$sql = "INSERT INTO usuarios (nombre, email, password)
        VALUES (?, ?, ?)";

$stmt = $conexion->prepare($sql);

if ($stmt->execute([$nombre, $correo, $passwordHash])) {
    echo "OK";
} else {
    echo "ERROR: " . implode(" | ", $stmt->errorInfo());
}
