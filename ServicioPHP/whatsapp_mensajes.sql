CREATE TABLE whatsapp_mensajes (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente     INT          NOT NULL,
    telefono       VARCHAR(20)  NOT NULL,
    wamid          VARCHAR(100) NULL,        -- ID de Meta (NULL si falló al enviar)
    plantilla      VARCHAR(100) NOT NULL,
    estado         ENUM(
                       'accepted',           -- Meta aceptó el envío
                       'sent',               -- Enviado al teléfono
                       'delivered',          -- Entregado al dispositivo
                       'read',               -- Leído
                       'failed'              -- Falló
                   ) DEFAULT 'accepted',
    error_detalle  VARCHAR(255) NULL,        -- Solo si estado = 'failed'
    fecha_envio    DATETIME     NOT NULL,
    fecha_status   DATETIME     NULL         -- Última actualización de estado
);