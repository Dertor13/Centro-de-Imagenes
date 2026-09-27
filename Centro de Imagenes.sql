-- Creacion de la base de datos y tablas --

CREATE DATABASE IF NOT EXISTS centro_imagenes;
USE centro_imagenes;

CREATE TABLE Especialidad (
    idEspecialidad INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE Obrasocial (
    idobraSocial INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE MedicoSolicitante (
    idMedicoSolicitante INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    matricula VARCHAR(30) NOT NULL UNIQUE,
    especialidad VARCHAR(100)
);


CREATE TABLE Paciente (
    idPaciente INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    fechaNacimiento DATE,
    telefono VARCHAR(30),
    email VARCHAR(100),
    idObraSocial INT,
    FOREIGN KEY (idObraSocial) REFERENCES Obrasocial(idobraSocial)
);

CREATE TABLE Practica (
    idPractica INT AUTO_INCREMENT PRIMARY KEY,
    codigoNomenclador VARCHAR(30) NOT NULL UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    descripcion VARCHAR(255),
    idEspecialidad INT NOT NULL,
    FOREIGN KEY (idEspecialidad) REFERENCES Especialidad(idEspecialidad)
);

CREATE TABLE Agenda (
    idAgenda INT AUTO_INCREMENT PRIMARY KEY,
    diaSemana VARCHAR(15) NOT NULL,
    horaInicio TIME NOT NULL,
    horaFin TIME NOT NULL,
    intervalo INT NOT NULL,
    idEspecialidad INT NOT NULL,
    FOREIGN KEY (idEspecialidad) REFERENCES Especialidad(idEspecialidad)
);


CREATE TABLE Turno (
    idTurno INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    estado ENUM('PENDIENTE', 'CONFIRMADO', 'CANCELADO') NOT NULL DEFAULT 'PENDIENTE',
    asistencia ENUM('PENDIENTE', 'ASISTIO', 'NO_ASISTIO') NOT NULL DEFAULT 'PENDIENTE',
    idPaciente INT NOT NULL,
    idPractica INT NOT NULL,
    idMedicoSolicitante INT NOT NULL,
    FOREIGN KEY (idPaciente) REFERENCES Paciente(idPaciente),
    FOREIGN KEY (idPractica) REFERENCES Practica(idPractica),
    FOREIGN KEY (idMedicoSolicitante) REFERENCES MedicoSolicitante(idMedicoSolicitante)
);

-- inserts de datos de prueba --

INSERT INTO Especialidad (nombre, descripcion)
VALUES ('Resonancia Nuclear Magnetica', 'resonancias');

INSERT INTO Practica (codigoNomenclador, nombre, descripcion, idEspecialidad)
VALUES ('302002', 'Resonancia magnética de abdomen', 'Resonancia Nuclear Magnetica', 1);

INSERT INTO Obrasocial (nombre)
VALUES ('PAMI');

INSERT INTO Paciente (dni, nombre, apellido, fechaNacimiento, telefono, email, idObraSocial)
VALUES ('12345678', 'Facundo', 'Maienza', '2005-10-13', '2241551197', 'facu_mai@hotmail.com', 1);

INSERT INTO MedicoSolicitante (nombre, apellido, matricula, especialidad)
VALUES ('Mario', 'Dagum', '117852', 'Traumatologo');

INSERT INTO Agenda (diaSemana, horaInicio, horaFin, intervalo, idEspecialidad)
VALUES 
('lunes', '07:00:00', '19:00:00', 60, 1),
('martes', '07:00:00', '19:00:00', 60, 1),
('miercoles', '07:00:00', '19:00:00', 60, 1),
('jueves', '07:00:00', '19:00:00', 60, 1),
('viernes', '07:00:00', '19:00:00', 60, 1);

INSERT INTO Turno (fecha, hora, estado, asistencia, idPaciente, idPractica, idMedicoSolicitante)
VALUES ('2026-09-24', '09:00:00', 'CONFIRMADO', 'PENDIENTE', 1, 1, 1);

-- Ejemplo de eliminacion de datos --

DELETE FROM Turno 
WHERE idTurno = 1;

DELETE FROM MedicoSolicitante 
WHERE idMedicoSolicitante = 1;

DELETE FROM Obrasocial 
WHERE idobraSocial = 2;

-- Consultas y verificaciones --

SHOW TABLES;

SELECT * FROM Especialidad;
SELECT * FROM Practica;
SELECT * FROM Obrasocial;
SELECT * FROM Paciente;
SELECT * FROM MedicoSolicitante;
SELECT * FROM Agenda;
SELECT * FROM Turno;

SELECT
    e.nombre AS especialidad,
    p.nombre AS practica,
    p.codigoNomenclador
FROM Especialidad e
INNER JOIN Practica p ON e.idEspecialidad = p.idEspecialidad;

SELECT
    t.idTurno,
    t.fecha,
    t.hora,
    t.estado,
    t.asistencia,
    p.nombre AS paciente,
    p.apellido AS apellido_paciente,
    pr.nombre AS practica,
    prof.nombre AS medico_solicitante
FROM Turno t
INNER JOIN Paciente p ON t.idPaciente = p.idPaciente
INNER JOIN Practica pr ON t.idPractica = pr.idPractica
INNER JOIN MedicoSolicitante prof ON t.idMedicoSolicitante = prof.idMedicoSolicitante;