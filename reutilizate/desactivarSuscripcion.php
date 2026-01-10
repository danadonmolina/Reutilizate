<?php
// Archivo de conexion a la bd
include "conexion.php";
// Obtenemos el id por la peticion y se pasa a un entero
$id = intval($_POST["id_usuario"]);

// Se prepara la consulta para poner el tipo FREE y desactivar la suscripcion
$sql = "UPDATE suscripciones SET tipo='FREE', activa=0 WHERE id_usuario=?";
$stmt = $conexion->prepare($sql);
$stmt->execute([$id]);

echo "OK";
