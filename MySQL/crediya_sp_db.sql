CREATE DATABASE IF NOT EXISTS CrediYa_SP_db;
USE CrediYa_SP_db;

CREATE TABLE empleados (
    id_empleado INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    documento VARCHAR(30) NOT NULL UNIQUE,
    correo VARCHAR(120) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    salario DECIMAL(12,2) NOT NULL
);

CREATE TABLE clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    documento VARCHAR(30) NOT NULL UNIQUE,
    correo VARCHAR(120) NOT NULL,
    telefono VARCHAR(20) NOT NULL
);

CREATE TABLE prestamos (
    id_prestamo INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_empleado INT NOT NULL,
    monto DECIMAL(12,2) NOT NULL,
    tasa_interes DECIMAL(7,4) NOT NULL,
    numero_cuotas INT NOT NULL,
    fecha_inicio DATE NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'pendiente',

    FOREIGN KEY (id_cliente)
        REFERENCES clientes(id_cliente),

    FOREIGN KEY (id_empleado)
        REFERENCES empleados(id_empleado)
);

CREATE TABLE cuotas (
    id_prestamo INT NOT NULL,
    numero_cuota INT NOT NULL,
    valor DECIMAL(12,2) NOT NULL,
    interes DECIMAL(12,2) NOT NULL,
    capital DECIMAL(12,2) NOT NULL,
    monto_pagado DECIMAL(12,2) NOT NULL DEFAULT 0,
    fecha_vencimiento DATE NOT NULL,

    PRIMARY KEY (id_prestamo, numero_cuota),

    FOREIGN KEY (id_prestamo)
        REFERENCES prestamos(id_prestamo)
);

CREATE TABLE pagos (
    id_pago INT AUTO_INCREMENT PRIMARY KEY,
    id_prestamo INT NOT NULL,
    monto DECIMAL(12,2) NOT NULL,
    fecha DATE NOT NULL,

    FOREIGN KEY (id_prestamo)
        REFERENCES prestamos(id_prestamo)
);

SHOW TABLES;