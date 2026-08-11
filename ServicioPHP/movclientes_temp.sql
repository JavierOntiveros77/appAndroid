CREATE TABLE movclientes_temp (
    id_movclientes_temp  INT AUTO_INCREMENT PRIMARY KEY,

    -- Referencia al registro en movclientes
    id_maeclientes       INT NOT NULL,
    num_abono            INT NOT NULL,
    num_renovacion       INT NOT NULL,
    id_cattipomovimiento INT NOT NULL DEFAULT 4,

    -- Datos del pago
    num_cantidad         DOUBLE,
    num_cantidadtotal    DOUBLE,
    fec_pago             TIMESTAMP NULL,
    num_cantidadPagada   DOUBLE NOT NULL,
    num_pagoAtraso       INT DEFAULT 0,
    num_atrasos          INT DEFAULT 0,

    -- Control
    id_empleado          INT NOT NULL,
    fec_registro         TIMESTAMP DEFAULT (CONVERT_TZ(NOW(), 'UTC', 'America/Mazatlan')),
    estatus              ENUM('pendiente', 'aprobado', 'rechazado') DEFAULT 'pendiente',
    fec_validacion       TIMESTAMP NULL,
    id_empleado_valida   INT NULL,
    observaciones        VARCHAR(255) NULL,

    FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado)
);