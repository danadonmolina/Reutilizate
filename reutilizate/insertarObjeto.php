<?php
require "conexion.php";

$nombre      = $_POST["nombre"] ?? "";
$descripcion = $_POST["descripcion"] ?? "";
$id_usuario  = intval($_POST["id_usuario"] ?? 0);
$imagenBase64 = $_POST["imagen"] ?? "";  // ahora recibimos la imagen en Base64

// Si no hay imagen, usamos una por defecto
$nombreArchivo = "uploads/default.png";

if (!empty($imagenBase64)) {

    // Decodificar la imagen Base64
    $imagenDecodificada = base64_decode($imagenBase64);

    // Crear nombre único
    $nombreArchivo = "uploads/img_" . uniqid() . ".jpg";

    // Guardar archivo físico
    file_put_contents($nombreArchivo, $imagenDecodificada);
}

try {
    $sql = "INSERT INTO objetos (nombre, descripcion, imagen, id_usuario)
            VALUES (:nombre, :descripcion, :imagen, :id_usuario)";

    $stmt = $conexion->prepare($sql);

    $stmt->bindParam(":nombre", $nombre);
    $stmt->bindParam(":descripcion", $descripcion);
    $stmt->bindParam(":imagen", $nombreArchivo); // guardamos el nombre del archivo
    $stmt->bindParam(":id_usuario", $id_usuario);

    if ($stmt->execute()) {
        echo "OK";
    } else {
        echo "ERROR";
    }

} catch (PDOException $e) {
    echo "ERROR_SQL";
}
