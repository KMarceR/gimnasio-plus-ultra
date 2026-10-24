-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Oct 23, 2026 at 04:52 AM
-- Server version: 8.0.30
-- PHP Version: 8.2.24

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bd-plus-ultra-gimnasio`
--

-- --------------------------------------------------------

--
-- Table structure for table `asistencia`
--

CREATE TABLE `asistencia` (
  `id_asistencia` int NOT NULL,
  `id_cliente` int NOT NULL,
  `id_estado` int NOT NULL,
  `fecha_asistencia` date NOT NULL,
  `hora_sesion` time NOT NULL,
  `hora_salida` time DEFAULT NULL,
  `creado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `asistencia`
--

INSERT INTO `asistencia` (`id_asistencia`, `id_cliente`, `id_estado`, `fecha_asistencia`, `hora_sesion`, `hora_salida`, `creado_en`) VALUES
(1, 1, 5, '2026-09-24', '06:15:00', NULL, '2026-09-26 04:30:11'),
(2, 2, 5, '2026-09-24', '18:30:00', NULL, '2026-09-26 04:30:11'),
(3, 1, 1, '2026-09-25', '06:00:00', NULL, '2026-09-26 04:30:11'),
(4, 1, 5, '2026-10-22', '21:22:36', '21:36:49', '2026-10-23 03:34:31'),
(5, 1, 1, '2026-10-22', '21:36:51', NULL, '2026-10-23 03:36:51'),
(6, 5, 6, '2026-10-22', '22:12:50', NULL, '2026-10-23 04:12:50'),
(7, 6, 5, '2026-10-22', '22:22:32', '22:49:27', '2026-10-23 04:22:32');

-- --------------------------------------------------------

--
-- Table structure for table `clientes`
--

CREATE TABLE `clientes` (
  `id_cliente` int NOT NULL,
  `id_persona` int NOT NULL,
  `id_plansuscripcion` int DEFAULT NULL,
  `id_estado` int NOT NULL,
  `codigo_qr` varchar(36) NOT NULL,
  `notas` text,
  `creado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `clientes`
--

INSERT INTO `clientes` (`id_cliente`, `id_persona`, `id_plansuscripcion`, `id_estado`, `codigo_qr`, `notas`, `creado_en`) VALUES
(1, 4, 3, 1, '8d55c192-ce7e-11f1-95ea-005056c00001', 'Lesión crónica de meniscos en rodilla izquierda. Evitar prensa inclinada pesada.', '2026-09-26 04:30:11'),
(2, 5, 2, 1, '8d55cad8-ce7e-11f1-95ea-005056c00001', 'Meta: Resistencia aeróbica y pérdida de grasa. Preparación para media maratón.', '2026-09-26 04:30:11'),
(3, 6, 2, 1, '8d55cc11-ce7e-11f1-95ea-005056c00001', 'Nueva atleta inscrita para el área de cardio.', '2026-09-27 03:56:02'),
(4, 7, 2, 1, '8d55cdb4-ce7e-11f1-95ea-005056c00001', 'Nueva atleta inscrita para el área de cardio.', '2026-09-27 16:50:30'),
(5, 14, 2, 2, '8d55ced0-ce7e-11f1-95ea-005056c00001', 'Nueva atleta inscrita para el área de cardio.', '2026-09-27 17:46:18'),
(6, 21, 1, 1, 'd70e91dc-1b2b-4652-a599-779f7ceab5d4', 'Objetivo: ganar masa muscular.', '2026-10-23 04:16:18');

-- --------------------------------------------------------

--
-- Table structure for table `entrenadores`
--

CREATE TABLE `entrenadores` (
  `id_entrenador` int NOT NULL,
  `id_persona` int NOT NULL,
  `id_usuario` int NOT NULL,
  `id_estado` int NOT NULL,
  `especialidad` varchar(255) DEFAULT NULL,
  `tarifa_por_sesion` double NOT NULL DEFAULT '0',
  `creado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `entrenadores`
--

INSERT INTO `entrenadores` (`id_entrenador`, `id_persona`, `id_usuario`, `id_estado`, `especialidad`, `tarifa_por_sesion`, `creado_en`) VALUES
(1, 3, 3, 1, 'Culturismo, HIIT y Nutrición Funcional', 15, '2026-09-26 04:30:11'),
(3, 10, 4, 1, 'Spinning, Crossfit y Acondicionamiento Físico', 12.5, '2026-09-27 17:10:34'),
(5, 20, 5, 1, 'Yoga y movilidad', 18, '2026-10-22 06:21:31');

-- --------------------------------------------------------

--
-- Table structure for table `estado`
--

CREATE TABLE `estado` (
  `id_estado` int NOT NULL,
  `nombre_estado` varchar(255) NOT NULL,
  `creado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `estado`
--

INSERT INTO `estado` (`id_estado`, `nombre_estado`, `creado_en`) VALUES
(1, 'Activo', '2026-09-26 04:28:44'),
(2, 'Inactivo', '2026-09-26 04:28:44'),
(3, 'Programada', '2026-09-26 04:28:44'),
(4, 'Confirmada', '2026-09-26 04:28:44'),
(5, 'Completada', '2026-09-26 04:28:44'),
(6, 'Cancelada', '2026-09-26 04:28:44'),
(8, 'Vencida', '2026-10-23 01:10:35'),
(9, 'Salida no marcada', '2026-10-23 01:10:35');

-- --------------------------------------------------------

--
-- Table structure for table `persona`
--

CREATE TABLE `persona` (
  `id_persona` int NOT NULL,
  `nombres` varchar(255) NOT NULL,
  `apellidos` varchar(255) NOT NULL,
  `genero` varchar(255) NOT NULL,
  `fecha_nacimiento` date NOT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  `dui` varchar(255) NOT NULL,
  `creado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `persona`
--

INSERT INTO `persona` (`id_persona`, `nombres`, `apellidos`, `genero`, `fecha_nacimiento`, `direccion`, `email`, `telefono`, `dui`, `creado_en`) VALUES
(1, 'Ing. Daniel Enrique', 'Guevara Gómez', 'Masculino', '1985-06-15', 'Colonia San Benito, San Salvador', 'daniel.guevara@plusultra.com', '2257-8888', '01234567-9', '2026-09-26 04:30:11'),
(2, 'Karla Marcela', 'Bonilla Rosales', 'Femenino', '2002-11-30', 'Residencial San Francisco, San Salvador', 'karla.bonilla@plusultra.com', '7512-3456', '06010191-2', '2026-09-26 04:30:11'),
(3, 'Nelson Eduardo', 'Palacios Henríquez', 'Masculino', '1991-02-03', 'Urbanización Prados de Venecia, Soyapango', 'nelson.trainer@gmail.com', '7745-9988', '04891234-5', '2026-09-26 04:30:11'),
(4, 'Roberto Antonio', 'Zelaya Flores', 'Masculino', '1998-07-19', 'Colonia Utila, Santa Tecla, La Libertad', 'roberto.zelaya@outlook.com', '6102-3344', '02564718-9', '2026-09-26 04:30:11'),
(5, 'Sofía Alejandra', 'Quintanilla Cruz', 'Femenino', '2001-04-11', 'Residencial Villa Lourdes, Colón, La Libertad', 'sofia.quinta@yahoo.com', '7988-1122', '05987412-3', '2026-09-26 04:30:11'),
(6, 'Marcela Alejandra', 'Rosales Cruz', 'Femenino', '2003-05-20', 'Antiguo Cuscatlán, El Salvador', 'marcela.rosales@ues.edu.sv', '7777-1234', '06123456-7', '2026-09-27 03:56:02'),
(7, 'Kenia Alejandra', 'Rosales Cruz', 'Femenino', '2005-05-20', 'Antiguo Cuscatlán, El Salvador', 'kenia@gmail.com', '7777-1235', '06123456-8', '2026-09-27 16:50:30'),
(10, 'Carlos Mauricio', 'Torres Alfaro', 'Masculino', '1994-08-12', 'Colonia Escalón, San Salvador', 'carlos@plusultra.com', '7122-3344', '05112233-5', '2026-09-27 17:06:00'),
(14, 'Kenia Maria', 'Rosales Cruz', 'Femenino', '1999-05-20', 'Antiguo Cuscatlán, El Salvador', 'mariya@gmail.com', '7777-1235', '06123456-2', '2026-09-27 17:46:18'),
(20, 'Andrea Sofía', 'Martínez López', 'Femenino', '1995-03-14', 'Santa Tecla, La Libertad', 'andrea.martinez@plusultra.com', '7123-4567', '05432109-8', '2026-10-22 06:21:31'),
(21, 'Diego Alejandro', 'Hernández Pineda', 'Masculino', '1999-08-21', 'Mejicanos, San Salvador', 'diego.hernandez@gmail.com', '7456-1230', '07894561-2', '2026-10-23 04:16:18');

-- --------------------------------------------------------

--
-- Table structure for table `plan_suscripcion`
--

CREATE TABLE `plan_suscripcion` (
  `id_plansuscripcion` int NOT NULL,
  `id_estado` int NOT NULL,
  `nombre_suscripcion` varchar(100) NOT NULL,
  `detalles` text,
  `precio` double NOT NULL DEFAULT '0',
  `tipo_plan` varchar(20) NOT NULL DEFAULT 'MENSUAL',
  `creado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `plan_suscripcion`
--

INSERT INTO `plan_suscripcion` (`id_plansuscripcion`, `id_estado`, `nombre_suscripcion`, `detalles`, `precio`, `tipo_plan`, `creado_en`) VALUES
(1, 1, 'Membresía Plus Ultra Black', 'Acceso completo ilimitado a zona de pesas, cardio, spinning y evaluaciones mensuales.', 45, 'MENSUAL', '2026-09-26 04:30:11'),
(2, 1, 'Membresía Smart Fit Estudiante', 'Acceso al área de musculación de lunes a viernes en horario matutino.', 25, 'MENSUAL', '2026-09-26 04:30:11'),
(3, 1, 'Membresía Anual Plus Ultra', 'Acceso ilimitado por 12 meses.', 450, 'ANUAL', '2026-10-23 00:00:40');

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `id_rol` int NOT NULL,
  `nombre_rol` varchar(255) NOT NULL,
  `creado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`id_rol`, `nombre_rol`, `creado_en`) VALUES
(1, 'Administrador', '2026-09-26 04:28:44'),
(2, 'Recepcionista', '2026-09-26 04:28:44'),
(3, 'Entrenador', '2026-09-26 04:28:44'),
(4, 'Gerente', '2026-09-26 04:28:44');

-- --------------------------------------------------------

--
-- Table structure for table `sesion`
--

CREATE TABLE `sesion` (
  `id_sesion` int NOT NULL,
  `id_cliente` int NOT NULL,
  `id_entrenador` int NOT NULL,
  `id_estado` int NOT NULL,
  `fecha_sesion` date NOT NULL,
  `hora_inicio` time NOT NULL,
  `hora_fin` time NOT NULL,
  `creado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `sesion`
--

INSERT INTO `sesion` (`id_sesion`, `id_cliente`, `id_entrenador`, `id_estado`, `fecha_sesion`, `hora_inicio`, `hora_fin`, `creado_en`) VALUES
(1, 1, 1, 5, '2026-09-23', '08:00:00', '09:00:00', '2026-09-26 04:30:11'),
(2, 2, 1, 3, '2026-09-26', '16:00:00', '17:00:00', '2026-09-26 04:30:11');

-- --------------------------------------------------------

--
-- Table structure for table `suscripcion`
--

CREATE TABLE `suscripcion` (
  `id_suscripcion` int NOT NULL,
  `id_cliente` int NOT NULL,
  `id_plansuscripcion` int NOT NULL,
  `id_estado` int NOT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_vencimiento` date NOT NULL,
  `precio_pagado` double NOT NULL,
  `creado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `suscripcion`
--

INSERT INTO `suscripcion` (`id_suscripcion`, `id_cliente`, `id_plansuscripcion`, `id_estado`, `fecha_inicio`, `fecha_vencimiento`, `precio_pagado`, `creado_en`) VALUES
(1, 1, 1, 1, '2026-10-22', '2026-11-21', 45, '2026-10-23 01:10:35'),
(2, 2, 2, 1, '2026-10-22', '2026-11-21', 25, '2026-10-23 01:10:35'),
(3, 3, 2, 1, '2026-10-22', '2026-11-21', 25, '2026-10-23 01:10:35'),
(4, 4, 2, 1, '2026-10-22', '2026-11-21', 25, '2026-10-23 01:10:35'),
(8, 1, 3, 1, '2026-11-22', '2027-11-21', 450, '2026-10-23 04:15:13'),
(9, 6, 1, 1, '2026-10-22', '2026-11-21', 45, '2026-10-23 04:16:18'),
(10, 1, 3, 1, '2027-11-22', '2028-11-21', 450, '2026-10-23 04:49:30');

-- --------------------------------------------------------

--
-- Table structure for table `usuarios`
--

CREATE TABLE `usuarios` (
  `id_usuario` int NOT NULL,
  `id_persona` int NOT NULL,
  `id_rol` int NOT NULL,
  `id_estado` int NOT NULL,
  `nombre_usuario` varchar(255) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `creado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `usuarios`
--

INSERT INTO `usuarios` (`id_usuario`, `id_persona`, `id_rol`, `id_estado`, `nombre_usuario`, `contrasena`, `creado_en`) VALUES
(1, 1, 1, 1, '01234567-9', '$2a$12$7kO1gW5X7Y28kX2b6z8uO.XvE1yQ2E2UeH5zI9M8xJ2w9F2R3K2mG', '2026-09-26 04:30:11'),
(2, 2, 4, 1, '06010191-2', '$2a$12$7kO1gW5X7Y28kX2b6z8uO.XvE1yQ2E2UeH5zI9M8xJ2w9F2R3K2mG', '2026-09-26 04:30:11'),
(3, 3, 3, 1, '04891234-5', '$2a$12$7kO1gW5X7Y28kX2b6z8uO.XvE1yQ2E2UeH5zI9M8xJ2w9F2R3K2mG', '2026-09-26 04:30:11'),
(4, 10, 3, 1, '05112233-5', '$2a$10$kzjrLRZNW5fvhAxfWDEFcOCT6wqZkEUJM3uY4FZ1.i9b.7RQcdDae', '2026-09-27 17:06:00'),
(5, 20, 3, 1, '05432109-8', '$2a$10$uDbY2HT.evTSOBGPNKP4o.RZEGbODPnYgnoJT6vpIdgEIcoxHSjyO', '2026-10-22 06:21:31');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `asistencia`
--
ALTER TABLE `asistencia`
  ADD PRIMARY KEY (`id_asistencia`),
  ADD KEY `fk_asistencia_cliente` (`id_cliente`),
  ADD KEY `fk_asistencia_estado` (`id_estado`);

--
-- Indexes for table `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id_cliente`),
  ADD UNIQUE KEY `id_persona` (`id_persona`),
  ADD UNIQUE KEY `uk_cliente_codigo_qr` (`codigo_qr`),
  ADD KEY `fk_cliente_plan` (`id_plansuscripcion`),
  ADD KEY `fk_cliente_estado` (`id_estado`);

--
-- Indexes for table `entrenadores`
--
ALTER TABLE `entrenadores`
  ADD PRIMARY KEY (`id_entrenador`),
  ADD UNIQUE KEY `id_persona` (`id_persona`),
  ADD UNIQUE KEY `id_usuario` (`id_usuario`),
  ADD KEY `fk_entrenador_estado` (`id_estado`);

--
-- Indexes for table `estado`
--
ALTER TABLE `estado`
  ADD PRIMARY KEY (`id_estado`),
  ADD UNIQUE KEY `nombre_estado` (`nombre_estado`);

--
-- Indexes for table `persona`
--
ALTER TABLE `persona`
  ADD PRIMARY KEY (`id_persona`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `dui` (`dui`);

--
-- Indexes for table `plan_suscripcion`
--
ALTER TABLE `plan_suscripcion`
  ADD PRIMARY KEY (`id_plansuscripcion`),
  ADD UNIQUE KEY `nombre_suscripcion` (`nombre_suscripcion`),
  ADD KEY `fk_plan_estado` (`id_estado`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id_rol`),
  ADD UNIQUE KEY `nombre_rol` (`nombre_rol`);

--
-- Indexes for table `sesion`
--
ALTER TABLE `sesion`
  ADD PRIMARY KEY (`id_sesion`),
  ADD KEY `fk_sesion_cliente` (`id_cliente`),
  ADD KEY `fk_sesion_entrenador` (`id_entrenador`),
  ADD KEY `fk_sesion_estado` (`id_estado`);

--
-- Indexes for table `suscripcion`
--
ALTER TABLE `suscripcion`
  ADD PRIMARY KEY (`id_suscripcion`),
  ADD KEY `fk_suscripcion_cliente` (`id_cliente`),
  ADD KEY `fk_suscripcion_plan` (`id_plansuscripcion`),
  ADD KEY `fk_suscripcion_estado` (`id_estado`);

--
-- Indexes for table `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `id_persona` (`id_persona`),
  ADD UNIQUE KEY `nombre_usuario` (`nombre_usuario`),
  ADD KEY `fk_usuario_rol` (`id_rol`),
  ADD KEY `fk_usuario_estado` (`id_estado`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `asistencia`
--
ALTER TABLE `asistencia`
  MODIFY `id_asistencia` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id_cliente` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `entrenadores`
--
ALTER TABLE `entrenadores`
  MODIFY `id_entrenador` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `estado`
--
ALTER TABLE `estado`
  MODIFY `id_estado` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `persona`
--
ALTER TABLE `persona`
  MODIFY `id_persona` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `plan_suscripcion`
--
ALTER TABLE `plan_suscripcion`
  MODIFY `id_plansuscripcion` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `id_rol` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `sesion`
--
ALTER TABLE `sesion`
  MODIFY `id_sesion` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `suscripcion`
--
ALTER TABLE `suscripcion`
  MODIFY `id_suscripcion` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id_usuario` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `asistencia`
--
ALTER TABLE `asistencia`
  ADD CONSTRAINT `fk_asistencia_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_asistencia_estado` FOREIGN KEY (`id_estado`) REFERENCES `estado` (`id_estado`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `clientes`
--
ALTER TABLE `clientes`
  ADD CONSTRAINT `fk_cliente_estado` FOREIGN KEY (`id_estado`) REFERENCES `estado` (`id_estado`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_cliente_persona` FOREIGN KEY (`id_persona`) REFERENCES `persona` (`id_persona`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_cliente_plan` FOREIGN KEY (`id_plansuscripcion`) REFERENCES `plan_suscripcion` (`id_plansuscripcion`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `entrenadores`
--
ALTER TABLE `entrenadores`
  ADD CONSTRAINT `fk_entrenador_estado` FOREIGN KEY (`id_estado`) REFERENCES `estado` (`id_estado`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_entrenador_persona` FOREIGN KEY (`id_persona`) REFERENCES `persona` (`id_persona`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_entrenador_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `plan_suscripcion`
--
ALTER TABLE `plan_suscripcion`
  ADD CONSTRAINT `fk_plan_estado` FOREIGN KEY (`id_estado`) REFERENCES `estado` (`id_estado`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `sesion`
--
ALTER TABLE `sesion`
  ADD CONSTRAINT `fk_sesion_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sesion_entrenador` FOREIGN KEY (`id_entrenador`) REFERENCES `entrenadores` (`id_entrenador`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sesion_estado` FOREIGN KEY (`id_estado`) REFERENCES `estado` (`id_estado`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `suscripcion`
--
ALTER TABLE `suscripcion`
  ADD CONSTRAINT `fk_suscripcion_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_suscripcion_estado` FOREIGN KEY (`id_estado`) REFERENCES `estado` (`id_estado`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_suscripcion_plan` FOREIGN KEY (`id_plansuscripcion`) REFERENCES `plan_suscripcion` (`id_plansuscripcion`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `usuarios`
--
ALTER TABLE `usuarios`
  ADD CONSTRAINT `fk_usuario_estado` FOREIGN KEY (`id_estado`) REFERENCES `estado` (`id_estado`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_usuario_persona` FOREIGN KEY (`id_persona`) REFERENCES `persona` (`id_persona`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_usuario_rol` FOREIGN KEY (`id_rol`) REFERENCES `roles` (`id_rol`) ON DELETE RESTRICT ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
