<?php
include "conexion.php";

$id = intval($_POST["id_usuario"]);

$sql = "UPDATE suscripciones SET tipo='FREE', activa=0 WHERE id_usuario=?";
$stmt = $conexion->prepare($sql);
$stmt->execute([$id]);

echo "OK";
