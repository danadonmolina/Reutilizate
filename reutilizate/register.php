<?php
require "conexion.php";

// Obtenemos los datos que si no estan se pone como cadena vacia 
$nombre = $_POST["nombre"] ?? "";
$correo = $_POST["correo"] ?? "";
$password = $_POST["password"] ?? "";

// Encriptar contraseña pero en este caso se deja tal cual
$passwordHash = $password;

// Insertar nuevo usuario
$sql = "INSERT INTO usuarios (nombre, email, password)
        VALUES (?, ?, ?)";

// Preparamos la consulta para evitar inyecciones sql
$stmt = $conexion->prepare($sql);

// Pasamos los parametros
if ($stmt->execute([$nombre, $correo, $passwordHash])) {
    echo "OK";
} else {
    echo "ERROR: " . implode(" | ", $stmt->errorInfo());
}
