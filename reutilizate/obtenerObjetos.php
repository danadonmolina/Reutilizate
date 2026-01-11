<?php
require "conexion.php"; 

$id_usuario = $_POST["id_usuario"] ?? 0;

// Consulta para obtener los ovjetos que no pertenecen al usuario
// Se muestran los objetos de otros usuarios
try {
    $stmt = $conexion->prepare("
        SELECT id, nombre, descripcion, imagen 
        FROM objetos
        WHERE id_usuario != ?
    ");
    $stmt->execute([$id_usuario]);

// Se obtienen los resultados como array asociativo
    $resultado = $stmt->fetchAll(PDO::FETCH_ASSOC);
//Respuesta en JSON con UTF-8
    header('Content-Type: application/json; charset=utf-8');
    echo json_encode($resultado, JSON_UNESCAPED_UNICODE);

} catch (PDOException $e) {
    header('Content-Type: application/json; charset=utf-8');
    echo json_encode([]);
}
