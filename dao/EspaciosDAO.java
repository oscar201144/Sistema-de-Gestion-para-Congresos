package dao;

import java.util.ArrayList;
import java.sql.*;
import modelo.*;

public class EspaciosDAO {
    public void guardarEspacio(Espacio espacio) throws SQLException {
        String sql = "INSERT INTO espacio (id_congreso, nombre, capacidad) VALUES (?, ?, ?)";

        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, espacio.getCongreso().getId());
            preparedStatement.setString(2, espacio.getNombre());
            preparedStatement.setInt(3, espacio.getCapacidad());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al guardar el espacio: " + e.getMessage(), e);
        }
    }

    public ArrayList<Espacio> listarEspacios(int congresoId) throws SQLException {
        ArrayList<Espacio> espacios = new ArrayList<>();
        String sql = "SELECT espacio.id_espacio, espacio.id_congreso, espacio.nombre, espacio.capacidad, congreso.nombre, congreso.fecha_inicio, congreso.hora_inicio, congreso.fecha_fin, congreso.hora_fin FROM espacio INNER JOIN congreso ON espacio.id_congreso = congreso.id_congreso WHERE espacio.id_congreso = ?";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, congresoId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int idEspacio = resultSet.getInt("id_espacio");
                int idCongreso = resultSet.getInt("id_congreso");
                String nombre = resultSet.getString("nombre");
                int capacidad = resultSet.getInt("capacidad");
                String nombreCongreso = resultSet.getString("congreso.nombre");
                Date fechaInicio = resultSet.getDate("congreso.fecha_inicio");
                Time horaInicio = resultSet.getTime("congreso.hora_inicio");
                Date fechaFin = resultSet.getDate("congreso.fecha_fin");
                Time horaFin = resultSet.getTime("congreso.hora_fin");

                Congreso congreso = new Congreso(idCongreso, nombreCongreso, fechaInicio.toLocalDate(), horaInicio.toLocalTime(), fechaFin.toLocalDate(), horaFin.toLocalTime());
                Espacio espacio = new Espacio(idEspacio, congreso, nombre, capacidad);
                espacios.add(espacio);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar espacios: " + e.getMessage(), e);
        }
        return espacios;
    }

    public boolean eliminarEspacio(int idEspacio) throws SQLException {
        try {
            Connection connection = new ConexionDB().conectarDB();

            // Obtener todas las asignaciones de espacio de este espacio
            String sqlAsignaciones = "SELECT id_asignacion FROM asignacion_espacio WHERE id_espacio = ?";
            PreparedStatement pstmtAsignaciones = connection.prepareStatement(sqlAsignaciones);
            pstmtAsignaciones.setInt(1, idEspacio);
            ResultSet rsAsignaciones = pstmtAsignaciones.executeQuery();
            
            // Eliminar conflictos de asignaciones de espacio
            ConflictoDAO conflictoDAO = new ConflictoDAO();
            while (rsAsignaciones.next()) {
                int idAsignacion = rsAsignaciones.getInt("id_asignacion");
                conflictoDAO.eliminarConflictosPorAsignacion(idAsignacion);
            }
            rsAsignaciones.close();
            pstmtAsignaciones.close();
            
            // Eliminar asignaciones de espacio
            String sqlDeleteAsignaciones = "DELETE FROM asignacion_espacio WHERE id_espacio = ?";
            PreparedStatement pstmtDeleteAsignaciones = connection.prepareStatement(sqlDeleteAsignaciones);
            pstmtDeleteAsignaciones.setInt(1, idEspacio);
            pstmtDeleteAsignaciones.executeUpdate();
            pstmtDeleteAsignaciones.close();
            
            // Finalmente eliminar el espacio
            String sql = "DELETE FROM espacio WHERE id_espacio = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idEspacio);
            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
            
            if (rowsAffected > 0) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar el espacio: " + e.getMessage(), e);
        }
    }

    public boolean actualizarEspacio(Espacio espacioActualizado) throws SQLException {
        String sql = "UPDATE espacio SET nombre = ?, capacidad = ? WHERE id_espacio = ?";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, espacioActualizado.getNombre());
            preparedStatement.setInt(2, espacioActualizado.getCapacidad());
            preparedStatement.setInt(3, espacioActualizado.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Retorna true si se actualizó al menos una fila
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el espacio: " + e.getMessage(), e);
        }
    }
}
