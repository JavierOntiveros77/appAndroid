CREATE TABLE empleados (
    id_empleado  INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    usuario      VARCHAR(50)  NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    rol          ENUM('admin', 'cobrador') DEFAULT 'cobrador',
    activo       TINYINT(1) DEFAULT 1
);