-- Crear base de datos
CREATE DATABASE IF NOT EXISTS planificacion_congreso;
USE planificacion_congreso;

-- Tabla: Congreso
CREATE TABLE congreso (
    id_congreso INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

-- Tabla: Actividad
CREATE TABLE actividad (
    id_actividad INT AUTO_INCREMENT PRIMARY KEY,
    id_congreso INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(50),
    duracion TIME,
    hora_inicio DATETIME,
    hora_fin DATETIME,
    FOREIGN KEY (id_congreso) REFERENCES congreso(id_congreso)
);

-- Tabla: Espacio
CREATE TABLE espacio (
    id_espacio INT AUTO_INCREMENT PRIMARY KEY,
    id_congreso INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    capacidad INT,
    FOREIGN KEY (id_congreso) REFERENCES congreso(id_congreso)
);

-- Tabla: Participante
CREATE TABLE participante (
    id_participante INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

-- Tabla: Rol
CREATE TABLE rol (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL
);

-- Tabla: Asignación de Participantes a Actividades
CREATE TABLE asignacion_participante (
    id_asignacion INT AUTO_INCREMENT PRIMARY KEY,
    id_actividad INT NOT NULL,
    id_participante INT NOT NULL,
    id_rol INT NOT NULL,
    hora_inicio DATETIME,
    hora_fin DATETIME,
    FOREIGN KEY (id_actividad) REFERENCES actividad(id_actividad),
    FOREIGN KEY (id_participante) REFERENCES participante(id_participante),
    FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
);

-- Tabla: Asignación de Espacios a Actividades
CREATE TABLE asignacion_espacio (
    id_asignacion INT AUTO_INCREMENT PRIMARY KEY,
    id_actividad INT NOT NULL,
    id_espacio INT NOT NULL,
    hora_inicio DATETIME,
    hora_fin DATETIME,
    FOREIGN KEY (id_actividad) REFERENCES actividad(id_actividad),
    FOREIGN KEY (id_espacio) REFERENCES espacio(id_espacio)
);

-- Tabla: Conflictos
CREATE TABLE conflicto (
    id_conflicto INT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(50),
    descripcion TEXT,
    id_actividad_afectada INT,
    FOREIGN KEY (id_actividad_afectada) REFERENCES actividad(id_actividad)
);

-- Consultas de inserción, busqueda y eliminación

-- Insertar congreso
INSERT INTO congreso (nombre) VALUES ('Congreso Médico 2025');

-- Insertar espacio
INSERT INTO espacio (id_congreso, nombre, capacidad)
VALUES (1, 'Sala A', 100);

-- Insertar actividad
INSERT INTO actividad (id_congreso, nombre, tipo, duracion, hora_inicio, hora_fin)
VALUES (1, 'Charla sobre cardiología', 'Conferencia', '01:30:00', '2025-06-01 10:00:00', '2025-06-01 11:30:00');

-- Insertar participante
INSERT INTO participante (nombre)
VALUES ('Dra. María Gómez');

-- Insertar rol
INSERT INTO rol (nombre_rol)
VALUES ('Expositora');

-- Asignar espacio a actividad
INSERT INTO asignacion_espacio (id_actividad, id_espacio, hora_inicio, hora_fin)
VALUES (1, 1, '2025-06-01 10:00:00', '2025-06-01 11:30:00');

-- Asignar participante a actividad
INSERT INTO asignacion_participante (id_actividad, id_participante, id_rol, hora_inicio, hora_fin)
VALUES (1, 1, 1, '2025-06-01 10:00:00', '2025-06-01 11:30:00');

-- Ver todos los congresos
SELECT * FROM congreso;

-- Ver actividades de un congreso
SELECT * FROM actividad WHERE id_congreso = 1;

-- Ver espacios disponibles en un congreso
SELECT * FROM espacio WHERE id_congreso = 1;

-- Ver participantes registrados
SELECT * FROM participante;

-- Eliminar una asignacion
DELETE FROM asignacion_participante WHERE id_asignacion = 1;

-- Eliminar un participante
DELETE FROM participante WHERE id_participante = 1;
