-- Crear base de datos
CREATE DATABASE IF NOT EXISTS planificacion_congreso;
USE planificacion_congreso;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `actividad`
--

CREATE TABLE `actividad` (
  `id_actividad` int(11) NOT NULL,
  `id_congreso` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `tipo` varchar(50) DEFAULT NULL,
  `duracion` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `asignacion_espacio`
--

CREATE TABLE `asignacion_espacio` (
  `id_asignacion` int(11) NOT NULL,
  `id_actividad` int(11) NOT NULL,
  `id_espacio` int(11) NOT NULL,
  `hora_inicio` time DEFAULT NULL,
  `hora_fin` time DEFAULT NULL,
  `fecha` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `asignacion_participante`
--

CREATE TABLE `asignacion_participante` (
  `id_asignacion` int(11) NOT NULL,
  `id_actividad` int(11) NOT NULL,
  `id_participante` int(11) NOT NULL,
  `id_rol` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `conflicto`
--

CREATE TABLE `conflicto` (
  `id_conflicto` int(11) NOT NULL,
  `id_congreso` int(11) NOT NULL,
  `tipo` varchar(50) DEFAULT NULL,
  `descripcion` text DEFAULT NULL,
  `id_actividad_1` int(11) DEFAULT NULL,
  `id_actividad_2` int(11) DEFAULT NULL,
  `id_persona` int(11) DEFAULT NULL,
  `id_espacio` int(11) DEFAULT NULL,
  `id_asignacion` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `congreso`
--

CREATE TABLE `congreso` (
  `id_congreso` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `fecha_inicio` date NOT NULL,
  `hora_inicio` time NOT NULL,
  `fecha_fin` date NOT NULL,
  `hora_fin` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `espacio`
--

CREATE TABLE `espacio` (
  `id_espacio` int(11) NOT NULL,
  `id_congreso` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `capacidad` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `participante`
--

CREATE TABLE `participante` (
  `id_participante` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol`
--

CREATE TABLE `rol` (
  `id_rol` int(11) NOT NULL,
  `nombre_rol` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `actividad`
--
ALTER TABLE `actividad`
  ADD PRIMARY KEY (`id_actividad`),
  ADD KEY `id_congreso` (`id_congreso`);

--
-- Indices de la tabla `asignacion_espacio`
--
ALTER TABLE `asignacion_espacio`
  ADD PRIMARY KEY (`id_asignacion`),
  ADD KEY `id_actividad` (`id_actividad`),
  ADD KEY `id_espacio` (`id_espacio`);

--
-- Indices de la tabla `asignacion_participante`
--
ALTER TABLE `asignacion_participante`
  ADD PRIMARY KEY (`id_asignacion`),
  ADD KEY `id_actividad` (`id_actividad`),
  ADD KEY `id_participante` (`id_participante`),
  ADD KEY `id_rol` (`id_rol`);

--
-- Indices de la tabla `conflicto`
--
ALTER TABLE `conflicto`
  ADD PRIMARY KEY (`id_conflicto`),
  ADD KEY `FK_id_conflicto_Id_congreso` (`id_congreso`);

--
-- Indices de la tabla `congreso`
--
ALTER TABLE `congreso`
  ADD PRIMARY KEY (`id_congreso`);

--
-- Indices de la tabla `espacio`
--
ALTER TABLE `espacio`
  ADD PRIMARY KEY (`id_espacio`),
  ADD KEY `id_congreso` (`id_congreso`);

--
-- Indices de la tabla `participante`
--
ALTER TABLE `participante`
  ADD PRIMARY KEY (`id_participante`);

--
-- Indices de la tabla `rol`
--
ALTER TABLE `rol`
  ADD PRIMARY KEY (`id_rol`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `actividad`
--
ALTER TABLE `actividad`
  MODIFY `id_actividad` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT de la tabla `asignacion_espacio`
--
ALTER TABLE `asignacion_espacio`
  MODIFY `id_asignacion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `asignacion_participante`
--
ALTER TABLE `asignacion_participante`
  MODIFY `id_asignacion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `conflicto`
--
ALTER TABLE `conflicto`
  MODIFY `id_conflicto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `congreso`
--
ALTER TABLE `congreso`
  MODIFY `id_congreso` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `espacio`
--
ALTER TABLE `espacio`
  MODIFY `id_espacio` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT de la tabla `participante`
--
ALTER TABLE `participante`
  MODIFY `id_participante` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `rol`
--
ALTER TABLE `rol`
  MODIFY `id_rol` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `actividad`
--
ALTER TABLE `actividad`
  ADD CONSTRAINT `actividad_ibfk_1` FOREIGN KEY (`id_congreso`) REFERENCES `congreso` (`id_congreso`);

--
-- Filtros para la tabla `asignacion_espacio`
--
ALTER TABLE `asignacion_espacio`
  ADD CONSTRAINT `asignacion_espacio_ibfk_1` FOREIGN KEY (`id_actividad`) REFERENCES `actividad` (`id_actividad`),
  ADD CONSTRAINT `asignacion_espacio_ibfk_2` FOREIGN KEY (`id_espacio`) REFERENCES `espacio` (`id_espacio`);

--
-- Filtros para la tabla `asignacion_participante`
--
ALTER TABLE `asignacion_participante`
  ADD CONSTRAINT `asignacion_participante_ibfk_1` FOREIGN KEY (`id_actividad`) REFERENCES `actividad` (`id_actividad`),
  ADD CONSTRAINT `asignacion_participante_ibfk_2` FOREIGN KEY (`id_participante`) REFERENCES `participante` (`id_participante`),
  ADD CONSTRAINT `asignacion_participante_ibfk_3` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id_rol`);

--
-- Filtros para la tabla `conflicto`
--
ALTER TABLE `conflicto`
  ADD CONSTRAINT `FK_id_conflicto_Id_congreso` FOREIGN KEY (`id_congreso`) REFERENCES `congreso` (`id_congreso`);

--
-- Filtros para la tabla `espacio`
--
ALTER TABLE `espacio`
  ADD CONSTRAINT `espacio_ibfk_1` FOREIGN KEY (`id_congreso`) REFERENCES `congreso` (`id_congreso`);
COMMIT;

