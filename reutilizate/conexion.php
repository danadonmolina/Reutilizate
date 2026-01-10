<?php
// Parametros de conexion a la base de datos
$host = "localhost";
$db   = "reutilizate";
$user = "root";
$pass = "";
$charset = "utf8mb4";

// Creamos el PDO para establecer la conexion con MySql especificando el host, la bd y el charset
try {
    $conexion = new PDO(
        "mysql:host=$host;dbname=$db;charset=$charset",
        $user,
        $pass
    );
// Configuracion del modo errores para que lance la conexion
// si hay un erro se detiene la ejecucion y se manda un mensaje generico
    $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    die("ERROR");
}
