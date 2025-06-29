package dao;

import java.util.ArrayList;
import java.sql.*;
import vista.VentanaError;
import vista.VentanaExito;
import modelo.*;


public class AsignacionDAO {
    public void guardarAsignacionEspacio(AsignacionEspacio asignacion) {
        String sql = "INSERT INTO `asignacion_espacio`(`id_asignacion`, `id_actividad`, `id_espacio`, `hora_inicio`, `hora_fin`,`fecha`) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = new ConexionDB().conectarDB();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, asignacion.getId());
            preparedStatement.setInt(2, asignacion.getActividad().getId());
            preparedStatement.setInt(3, asignacion.getEspacio().getId());
            preparedStatement.setObject(4, asignacion.getHoraInicio());
            preparedStatement.setObject(5, asignacion.getHoraFin());
            preparedStatement.setObject(6, asignacion.getFecha());
            preparedStatement.executeUpdate();
            new VentanaExito("Asignación de espacio guardada exitosamente: " + asignacion.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al guardar la asignación de espacio: " + e.getMessage());
        }
    }

    public void guardarAsignacionParticipante(AsignacionParticipante asignacion) {
        String sql = "INSERT INTO `asignacion_participante`(`id_asignacion`, `id_actividad`, `id_participante`, `id_rol`, `hora_inicio`, `hora_fin`) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = new ConexionDB().conectarDB();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, asignacion.getId());
            preparedStatement.setInt(2, asignacion.getActividad().getId());
            preparedStatement.setInt(3, asignacion.getPersona().getId());
            preparedStatement.setInt(4, asignacion.getRol().getId());
            preparedStatement.setString(5, asignacion.getFechaHoraInicio());
            preparedStatement.setString(6, asignacion.getFechaHoraFin());
            preparedStatement.executeUpdate();
            new VentanaExito("Asignación de participante guardada exitosamente: " + asignacion.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al guardar la asignación de participante: " + e.getMessage());
        }
    }

    public ArrayList<AsignacionEspacio> obtenerAsignacionesEspacios(int idCongreso) {
        String sql = "SELECT asignacion_espacio.id_asignacion, asignacion_espacio.fecha, asignacion_espacio.hora_inicio, asignacion_espacio.hora_fin, espacio.id_espacio, espacio.nombre, espacio.capacidad, actividad.id_actividad, actividad.nombre, actividad.tipo, actividad.duracion, congreso.id_congreso, congreso.nombre, congreso.fecha_inicio, congreso.hora_inicio, congreso.fecha_fin, congreso.hora_fin FROM asignacion_espacio INNER JOIN actividad ON asignacion_espacio.id_actividad = actividad.id_actividad INNER JOIN congreso ON actividad.id_congreso = congreso.id_congreso INNER JOIN espacio ON asignacion_espacio.id_espacio = espacio.id_espacio WHERE actividad.id_congreso = ?";
        ArrayList<AsignacionEspacio> asignaciones = new ArrayList<>();
        try (Connection connection = new ConexionDB().conectarDB();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idCongreso);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Congreso congreso = new Congreso(resultSet.getInt("id_congreso"),
                        resultSet.getString("congreso.nombre"),
                        resultSet.getDate("congreso.fecha_inicio").toLocalDate(),
                        resultSet.getTime("congreso.hora_inicio").toLocalTime(),
                        resultSet.getDate("congreso.fecha_fin").toLocalDate(),
                        resultSet.getTime("congreso.hora_fin").toLocalTime());
                Espacio espacio = new Espacio(resultSet.getInt("id_espacio"), congreso,
                        resultSet.getString("espacio.nombre"), resultSet.getInt("capacidad"));
                Actividad actividad = new Actividad(resultSet.getInt("id_actividad"), congreso,
                        resultSet.getString("actividad.nombre"), resultSet.getString("actividad.tipo"),
                        resultSet.getInt("actividad.duracion"));
                // Create the AsignacionEspacio object
                AsignacionEspacio asignacion = new AsignacionEspacio(
                        resultSet.getInt("id_asignacion"),
                        congreso,
                        espacio,
                        actividad,
                        resultSet.getDate("fecha").toLocalDate(),
                        resultSet.getTime("hora_inicio").toLocalTime(),
                        resultSet.getTime("hora_fin").toLocalTime());
                asignaciones.add(asignacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al obtener las asignaciones de espacio: " + e.getMessage());
        }
        return asignaciones;
    }

    public String obtenerHoraTempranaDisponible(int idCongreso, String fechaSeleccionada) {
        String sql = "SELECT MAX(asignacion_espacio.hora_fin) AS hora_temprana FROM asignacion_espacio INNER JOIN actividad ON asignacion_espacio.id_actividad = actividad.id_actividad WHERE actividad.id_congreso = ? AND DATE(asignacion_espacio.fecha) = ?";
        String horaTemprana = null;
        try (Connection connection = new ConexionDB().conectarDB();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idCongreso);
            preparedStatement.setString(2, fechaSeleccionada);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                horaTemprana = resultSet.getString("hora_temprana");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al obtener la hora temprana disponible: " + e.getMessage());
        }
        return horaTemprana;
    }

    public ArrayList<AsignacionParticipante> obtenerAsignacionesParticipantes(int idCongreso) {
        String sql = "Select actividad.id_actividad, actividad.nombre, actividad.tipo, actividad.hora_inicio, actividad.hora_fin, actividad.duracion, congreso.id_congreso, congreso.nombre_congreso, congreso.fecha_inicio, congreso.hora_inicio, congreso.fecha_fin, congreso.hora_fin, participante.id_participante, participante.nombre_participante, rol.id_rol, rol.nombre_rol  FROM asignacion_participante INNER JOIN actividad ON asignacion_participante.id_actividad = actividad.id_actividad INNER JOIN congreso ON actividad.id_congreso = congreso.id_congreso INNER JOIN participante ON asignacion_participante.id_participante = participante.id_participante INNER JOIN rol ON asignacion_participante.id_rol = rol.id_rol WHERE actividad.id_congreso = ?";
        ArrayList<AsignacionParticipante> asignaciones = new ArrayList<>();
        try (Connection connection = new ConexionDB().conectarDB();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idCongreso);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Congreso congreso = new Congreso(resultSet.getInt("congreso.id_congreso"),
                        resultSet.getString("congreso.nombre_congreso"),
                        resultSet.getDate("congreso.fecha_inicio").toLocalDate(),
                        resultSet.getTime("congreso.hora_inicio").toLocalTime(),
                        resultSet.getDate("congreso.fecha_fin").toLocalDate(),
                        resultSet.getTime("congreso.hora_fin").toLocalTime());
                Actividad actividad = new Actividad(resultSet.getInt("actividad.id_actividad"),
                        congreso,
                        resultSet.getString("actividad.nombre"),
                        resultSet.getString("actividad.tipo"),
                        resultSet.getInt("actividad.duracion"));
                Persona persona = new Persona(resultSet.getInt("participante.id_participante"),
                        resultSet.getString("participante.nombre_participante"));
                Rol rol = new Rol(resultSet.getInt("rol.id_rol"),
                        resultSet.getString("rol.nombre_rol"));
                AsignacionParticipante asignacion = new AsignacionParticipante(
                        resultSet.getInt("asignacion_participante.id_asignacion"),
                        congreso,
                        persona,
                        actividad,
                        resultSet.getString("actividad.hora_inicio"),
                        resultSet.getString("actividad.hora_fin"),  
                        rol);
                asignaciones.add(asignacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al obtener las asignaciones de participante: " + e.getMessage());
        }
        return asignaciones;
    }

    public ArrayList<AsignacionEspacio> obtenerAsignacionesEspaciosPorActividad(int idActividad) {
        String sql = "SELECT asignacion_espacio.id_asignacion, asignacion_espacio.fecha, asignacion_espacio.hora_inicio, asignacion_espacio.hora_fin, espacio.id_espacio, espacio.nombre, espacio.capacidad FROM asignacion_espacio INNER JOIN espacio ON asignacion_espacio.id_espacio = espacio.id_espacio WHERE asignacion_espacio.id_actividad = ?";
        ArrayList<AsignacionEspacio> asignaciones = new ArrayList<>();
        try (Connection connection = new ConexionDB().conectarDB();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idActividad);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Espacio espacio = new Espacio(resultSet.getInt("id_espacio"),
                        null,
                        resultSet.getString("nombre"),
                        resultSet.getInt("capacidad"));
                AsignacionEspacio asignacion = new AsignacionEspacio(
                        resultSet.getInt("id_asignacion"),
                        null,
                        espacio,
                        null,
                        resultSet.getDate("fecha").toLocalDate(),
                        resultSet.getTime("hora_inicio").toLocalTime(),
                        resultSet.getTime("hora_fin").toLocalTime());
                asignaciones.add(asignacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al obtener las asignaciones de espacio por actividad: " + e.getMessage());
        }
        return asignaciones;
    }

    public void eliminarAsignacionEspacio(int idAsignacion) {
        String sql = "DELETE FROM `asignacion_espacio` WHERE `id_asignacion` = ?";
        try (Connection connection = new ConexionDB().conectarDB();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idAsignacion);
            preparedStatement.executeUpdate();
            new VentanaExito("Asignación de espacio eliminada exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al eliminar la asignación de espacio: " + e.getMessage());
        }
    }

    public void eliminarAsignacionParticipante(int idAsignacion) {
        String sql = "DELETE FROM `asignacion_participante` WHERE `id_asignacion` = ?";
        try (Connection connection = new ConexionDB().conectarDB();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idAsignacion);
            preparedStatement.executeUpdate();
            new VentanaExito("Asignación de participante eliminada exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al eliminar la asignación de participante: " + e.getMessage());
        }
    }

    public void actualizarAsignacionEspacio(AsignacionEspacio asignacion) {
        String sql = "UPDATE `asignacion_espacio` SET `id_espacio` = ?, `id_actividad` = ?, `hora_inicio` = ?, `hora_fin` = ? WHERE `id_asignacion` = ?";
        try (Connection connection = new ConexionDB().conectarDB();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, asignacion.getEspacio().getId());
            preparedStatement.setInt(2, asignacion.getActividad().getId());
            preparedStatement.setObject(3, asignacion.getHoraInicio());
            preparedStatement.setObject(4, asignacion.getHoraFin());
            preparedStatement.setInt(5, asignacion.getId());
            preparedStatement.executeUpdate();
            new VentanaExito("Asignación de espacio actualizada exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al actualizar la asignación de espacio: " + e.getMessage());
        }
    }

    public void actualizarAsignacionParticipante(AsignacionParticipante asignacion) {
        String sql = "UPDATE `asignacion_participante` SET `id_participante` = ?, `id_actividad` = ?, `id_rol` = ?, `hora_inicio` = ?, `hora_fin` = ? WHERE `id_asignacion` = ?";
        try (Connection connection = new ConexionDB().conectarDB();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(2, asignacion.getPersona().getId());
            preparedStatement.setInt(3, asignacion.getActividad().getId());
            preparedStatement.setInt(4, asignacion.getRol().getId());
            preparedStatement.setString(5, asignacion.getFechaHoraInicio());
            preparedStatement.setString(6, asignacion.getFechaHoraFin());
            preparedStatement.setInt(7, asignacion.getId());
            preparedStatement.executeUpdate();
            new VentanaExito("Asignación de participante actualizada exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al actualizar la asignación de participante: " + e.getMessage());
        }
    }

}
