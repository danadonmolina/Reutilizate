<?php
// Respuesta formato JSON
header('Content-Type: application/json');
require_once "conexion.php";

// Obtenemos id de usuarios de la conversacion 
// y se convierten en enteros para evitar errores
$yo = intval($_GET["yo"]);
$otro = intval($_GET["otro"]);

// La consulta sql tiene una condicion mensajes enviados por yo a otro o otro a yo 
// y se ordenan por fecha
$sql = "
SELECT contenido, fecha, id_emisor
FROM mensajes
WHERE (id_emisor = $yo AND id_receptor = $otro)
   OR (id_emisor = $otro AND id_receptor = $yo)
ORDER BY fecha ASC
";

$stmt = $conexion->query($sql);
// Array donde lo almacenaremos y añadiremos los resultados
$mensajes = [];

while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
    $mensajes[] = $row;
}

echo json_encode($mensajes);
