<?php
// Archivo de conexion a la bd
include "conexion.php";

// Obtenemos el id del usuario lo convertimos a entero
$id = intval($_POST["id_usuario"]);

// Se actualiza la suscripcion del usuario 
$sql = "UPDATE suscripciones SET tipo='PREMIUM', activa=1 WHERE id_usuario=?";
$stmt = $conexion->prepare($sql);
$stmt->execute([$id]);

echo "OK";
