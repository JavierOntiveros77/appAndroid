USE dbclientes;
DROP TABLE IF EXISTS `cat_accesos`;
CREATE TABLE IF NOT EXISTS `cat_accesos` (
  `id_cat_accesos` INT NOT NULL AUTO_INCREMENT,
  `password` VARCHAR(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_cat_accesos`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Tabla de accesos para aplicacion android';