<?php
// Respuesta en formato JSON
header("Content-Type: application/json");

// Archivo de conexion a la bd
require_once "conexion.php";

// Obtenemos los datos enviados por post 
// Convertimos emisor y receptor a enteros por simple seguridad
$emisor = intval($_POST["emisor"]);
$receptor = intval($_POST["receptor"]);
// El contenido del mensaje puede venir vacio 
$contenido = $_POST["contenido"] ?? "";

// Preparamos la consulta SQL para intentar un nuevo mensaje 
$sql = "INSERT INTO mensajes (id_emisor, id_receptor, contenido)
        VALUES (?, ?, ?)";

$stmt = $conexion->prepare($sql);
// Se ejecuta la consulta pasando los valores como parametros
$stmt->execute([$emisor, $receptor, $contenido]);

echo json_encode(["status" => "OK"]);
