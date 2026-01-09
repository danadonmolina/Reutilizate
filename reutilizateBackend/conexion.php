<?php
$host = "localhost";
$db   = "reutilizate";
$user = "root";
$pass = "";
$charset = "utf8mb4";

try {
    $conexion = new PDO(
        "mysql:host=$host;dbname=$db;charset=$charset",
        $user,
        $pass
    );
    $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    die("ERROR");
}
