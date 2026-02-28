DROP DATABASE IF EXISTS eco_activistas_JDBC;
CREATE DATABASE eco_activistas_JDBC;
USE eco_activistas_JDBC;


CREATE TABLE cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido_paterno VARCHAR(100) NOT NULL,
    apellido_materno VARCHAR(100),
    direccion VARCHAR(255) NOT NULL
);

CREATE TABLE telefonos_cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    num_telefono VARCHAR(20) NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE activista (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido_paterno VARCHAR(100) NOT NULL,
    apellido_materno VARCHAR(100),
    telefono VARCHAR(20) NOT NULL,
    fecha_inicio_colaboracion DATE NOT NULL
);

CREATE TABLE problema (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    estado ENUM('pendiente', 'concluido', 'cancelado') NOT NULL,
    id_cliente INT NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE problema_activistas (
    id_problema INT NOT NULL,
    id_activista INT NOT NULL,
    PRIMARY KEY (id_problema, id_activista),
    FOREIGN KEY (id_problema) REFERENCES problema(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (id_activista) REFERENCES activista(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


select * from activista;
select * from cliente;