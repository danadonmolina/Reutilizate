DROP DATABASE IF EXISTS REUTILIZATE;

CREATE DATABASE IF NOT EXISTS REUTILIZATE;
USE REUTILIZATE;


-- 1. TABLA USUARIOS
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    tipo ENUM('USER', 'ADMIN') DEFAULT 'USER'
);

-- 2. TABLA OBJETOS
CREATE TABLE objetos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    imagen VARCHAR(255),
    id_usuario INT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
        ON DELETE CASCADE
);


-- 3. TABLA INTERACCIONES (LIKE / DISLIKE)
CREATE TABLE interacciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_objeto INT NOT NULL,
    tipo ENUM('LIKE', 'DISLIKE') NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
        ON DELETE CASCADE,
    FOREIGN KEY (id_objeto) REFERENCES objetos(id)
        ON DELETE CASCADE,
    UNIQUE (id_usuario, id_objeto)
);


-- 4. TABLA MATCHES 
CREATE TABLE matches (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_objeto_1 INT NOT NULL,
    id_objeto_2 INT NOT NULL,
    aceptado BOOLEAN DEFAULT FALSE,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_objeto_1) REFERENCES objetos(id),
    FOREIGN KEY (id_objeto_2) REFERENCES objetos(id)
);


-- 5. TABLA MENSAJES
CREATE TABLE mensajes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_emisor INT NOT NULL,
    id_receptor INT NOT NULL,
    contenido TEXT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_emisor) REFERENCES usuarios(id),
    FOREIGN KEY (id_receptor) REFERENCES usuarios(id)
);


-- 6. TABLA SUSCRIPCIONES
CREATE TABLE suscripciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    tipo ENUM('FREE', 'PREMIUM') DEFAULT 'FREE',
    activa BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

-- 7. INSEERCIONES
INSERT INTO usuarios (nombre, email, password)
VALUES ('Diego', 'diego@test.com', '1234'),
       ('Alex', 'alex@test.com', '1234');

INSERT INTO suscripciones (id_usuario, tipo)
VALUES (1, 'FREE'), (2, 'FREE');

-- INSERT INTO objetos (nombre, descripcion, imagen, id_usuario)
-- VALUES 
-- ('Bicicleta', 'Bici en buen estado', 'bici.jpg', 1),
-- ('Libro', 'Libro de aventuras', 'libro.jpg', 2),
-- ('Consola', 'Consola retro', 'consola.jpg', 1),
-- ('Patinete', 'Patinete casi nuevo', 'patinete.jpg', 2);

-- Likes de ejemplo
-- INSERT INTO interacciones (id_usuario, id_objeto, tipo)
-- VALUES 
-- (1, 2, 'LIKE'),
-- (2, 1, 'LIKE');

-- Mensajes de ejemplo
-- INSERT INTO mensajes (id_emisor, id_receptor, contenido)
-- VALUES 
-- (1, 2, 'Hola, tenemos match!'),
-- (2, 1, 'Genial, hablemos!');
