<?php
header('Content-Type: application/json');
require_once "conexion.php";

$yo = intval($_GET["yo"]);
$otro = intval($_GET["otro"]);

$sql = "
SELECT contenido, fecha, id_emisor
FROM mensajes
WHERE (id_emisor = $yo AND id_receptor = $otro)
   OR (id_emisor = $otro AND id_receptor = $yo)
ORDER BY fecha ASC
";

$stmt = $conexion->query($sql);

$mensajes = [];

while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
    $mensajes[] = $row;
}

echo json_encode($mensajes);
