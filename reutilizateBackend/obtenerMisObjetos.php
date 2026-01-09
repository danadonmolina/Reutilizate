<?php
require "conexion.php"; 

$id_usuario = $_POST["id_usuario"] ?? 0;

try {
    $stmt = $conexion->prepare("
        SELECT id, nombre, descripcion, imagen 
        FROM objetos
        WHERE id_usuario = ?
    ");
    $stmt->execute([$id_usuario]);

    $resultado = $stmt->fetchAll(PDO::FETCH_ASSOC);

    header('Content-Type: application/json; charset=utf-8');
    echo json_encode($resultado, JSON_UNESCAPED_UNICODE);

} catch (PDOException $e) {
    header('Content-Type: application/json; charset=utf-8');
    echo json_encode([]);
}
