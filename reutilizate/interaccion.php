<?php
require "conexion.php";

$id_usuario = $_POST['id_usuario'] ?? null;
$id_objeto = $_POST['id_objeto'] ?? null;
$tipo = $_POST['tipo'] ?? null;

if (!$id_usuario || !$id_objeto || !$tipo) {
    echo "ERROR_DATOS";
    exit;
}

try {

    // Obtener el dueño del objeto
    $stmt = $conexion->prepare("
        SELECT id_usuario 
        FROM objetos 
        WHERE id = ?
    ");
    $stmt->execute([$id_objeto]);
    $objeto = $stmt->fetch(PDO::FETCH_ASSOC);

    if (!$objeto) {
        echo "OBJETO_NO_EXISTE";
        exit;
    }

    $id_dueno_objeto = $objeto['id_usuario'];

    // Guardar interacción
    $stmt = $conexion->prepare("
        INSERT INTO interacciones (id_usuario, id_objeto, tipo)
        VALUES (?, ?, ?)
        ON DUPLICATE KEY UPDATE tipo = VALUES(tipo)
    ");
    $stmt->execute([$id_usuario, $id_objeto, $tipo]);

    if ($tipo === "DISLIKE") {
        echo "DISLIKE_OK";
        exit;
    }

    // Comprobar LIKE inverso
    $stmt = $conexion->prepare("
        SELECT i.id_objeto
        FROM interacciones i
        JOIN objetos o ON i.id_objeto = o.id
        WHERE i.id_usuario = ?
          AND o.id_usuario = ?
          AND i.tipo = 'LIKE'
    ");
    $stmt->execute([$id_dueno_objeto, $id_usuario]);

    if ($stmt->rowCount() > 0) {

        // Guardar el ID del objeto del otro usuario
        $objeto_inverso = $stmt->fetchColumn();

        // Ordenar para evitar duplicados
        $id1 = min($id_objeto, $objeto_inverso);
        $id2 = max($id_objeto, $objeto_inverso);

        // Crear MATCH entre objetos
        $stmt = $conexion->prepare("
            INSERT IGNORE INTO matches (id_objeto_1, id_objeto_2)
            VALUES (?, ?)
        ");
        $stmt->execute([$id1, $id2]);

        echo "MATCH";
        exit;
    }

    echo "LIKE_OK";

} catch (PDOException $e) {
    echo "ERROR_BD";
}
