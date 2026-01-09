<?php
require "conexion.php";

$correo = $_POST["correo"] ?? "";
$password = $_POST["password"] ?? "";

$sql = "SELECT id FROM usuarios WHERE email = ? AND password = ?";
$stmt = $conexion->prepare($sql);
$stmt->execute([$correo, $password]);

if ($stmt->rowCount() > 0) {
    $fila = $stmt->fetch(PDO::FETCH_ASSOC);
    echo $fila["id"]; // devolvemos el ID
} else {
    echo "ERROR";
}
