<?php
// Archivo de conexion
require "conexion.php";

//Se obtienen los datos enviados por post
$correo = $_POST["correo"] ?? "";
$password = $_POST["password"] ?? "";

// Preparamos la consulta sql para verificar si existe el usuario
$sql = "SELECT id FROM usuarios WHERE email = ? AND password = ?";
$stmt = $conexion->prepare($sql);
$stmt->execute([$correo, $password]);

// Si existe al menos un usuario el registro es valido
if ($stmt->rowCount() > 0) {
    $fila = $stmt->fetch(PDO::FETCH_ASSOC);// Obtenemos la fila con el el id usuario y devolvemos solamente ese id
    echo $fila["id"]; // devolvemos el ID
} else {
    echo "ERROR";
}
