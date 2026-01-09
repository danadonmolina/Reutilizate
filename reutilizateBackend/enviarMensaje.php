<?php
header("Content-Type: application/json");
require_once "conexion.php";

$emisor = intval($_POST["emisor"]);
$receptor = intval($_POST["receptor"]);
$contenido = $_POST["contenido"] ?? "";

$sql = "INSERT INTO mensajes (id_emisor, id_receptor, contenido)
        VALUES (?, ?, ?)";

$stmt = $conexion->prepare($sql);
$stmt->execute([$emisor, $receptor, $contenido]);

echo json_encode(["status" => "OK"]);
