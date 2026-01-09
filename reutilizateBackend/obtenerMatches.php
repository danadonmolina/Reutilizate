<?php
header("Content-Type: application/json");
require_once __DIR__ . "/conexion.php";

$idUsuario = intval($_GET["id_usuario"]);

$sql = "
SELECT 
    o2.nombre AS nombreObjeto,
    o2.imagen AS imagenObjeto,
    u2.nombre AS nombreUsuario,
    u2.id AS idUsuarioOtro
FROM interacciones i1
JOIN objetos o1 ON o1.id = i1.id_objeto
JOIN interacciones i2 
    ON i2.id_usuario = o1.id_usuario
    AND i2.id_objeto IN (
        SELECT id FROM objetos WHERE id_usuario = i1.id_usuario
    )
JOIN objetos o2 ON o2.id = i2.id_objeto
JOIN usuarios u2 ON u2.id = i2.id_usuario
WHERE i1.tipo = 'LIKE'
  AND i2.tipo = 'LIKE'
  AND i1.id_usuario = $idUsuario
  AND u2.id != $idUsuario
";

$stmt = $conexion->query($sql);   // ← AQUÍ EL CAMBIO IMPORTANTE

$matches = [];

while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
    $matches[] = $row;
}

echo json_encode($matches);
?>
