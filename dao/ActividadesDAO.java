package dao;
import modelo.Actividad;
import modelo.Congreso;

import java.util.ArrayList;
import java.sql.*;

public class ActividadesDAO {
    public boolean guardarActividad(Congreso congreso, Actividad actividad) {
        String sql = "INSERT INTO actividad (id_congreso, nombre, tipo, duracion) VALUES (?, ?, ?, ?)";

        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, congreso.getId());
            preparedStatement.setString(2, actividad.getNombre());
            preparedStatement.setString(3, actividad.getTipo());
            preparedStatement.setInt(4, actividad.getDuracion());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ArrayList<Actividad> listarActividades(int idCongreso) {
        ArrayList<Actividad> actividades = new ArrayList<>();
        String sql = "SELECT actividad.id_actividad, actividad.id_congreso,actividad.nombre, actividad.tipo, actividad.duracion, congreso.nombre, congreso.fecha_inicio, congreso.hora_inicio, congreso.fecha_fin, congreso.hora_fin FROM actividad INNER join congreso on actividad.id_congreso = congreso.id_congreso WHERE actividad.id_congreso = ?";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idCongreso);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Actividad actividad = new Actividad(
                        resultSet.getInt("id_actividad"),
                        new Congreso(idCongreso, resultSet.getString("congreso.nombre"),
                                resultSet.getDate("congreso.fecha_inicio").toLocalDate(),
                                resultSet.getTime("congreso.hora_inicio").toLocalTime(),
                                resultSet.getDate("congreso.fecha_fin").toLocalDate(),
                                resultSet.getTime("congreso.hora_fin").toLocalTime()),
                        resultSet.getString("nombre"),
                        resultSet.getString("tipo"),
                        resultSet.getInt("duracion")
                );
                actividades.add(actividad);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actividades;
    }
    public boolean eliminarActividad(int idActividad) {
        try {
            Connection connection = new ConexionDB().conectarDB();
            
            // 1. Obtener todas las asignaciones de espacio de esta actividad
            String sqlAsignacionesEspacio = "SELECT id_asignacion FROM asignacion_espacio WHERE id_actividad = ?";
            PreparedStatement pstmtEspacio = connection.prepareStatement(sqlAsignacionesEspacio);
            pstmtEspacio.setInt(1, idActividad);
            ResultSet rsEspacio = pstmtEspacio.executeQuery();
            
            // Eliminar conflictos de asignaciones de espacio
            ConflictoDAO conflictoDAO = new ConflictoDAO();
            while (rsEspacio.next()) {
                int idAsignacion = rsEspacio.getInt("id_asignacion");
                conflictoDAO.eliminarConflictosPorAsignacion(idAsignacion);
            }
            rsEspacio.close();
            pstmtEspacio.close();
            
            // 2. Obtener todas las asignaciones de participante de esta actividad
            String sqlAsignacionesParticipante = "SELECT id_asignacion FROM asignacion_participante WHERE id_actividad = ?";
            PreparedStatement pstmtParticipante = connection.prepareStatement(sqlAsignacionesParticipante);
            pstmtParticipante.setInt(1, idActividad);
            ResultSet rsParticipante = pstmtParticipante.executeQuery();
            
            // Eliminar conflictos de asignaciones de participante
            while (rsParticipante.next()) {
                int idAsignacion = rsParticipante.getInt("id_asignacion");
                conflictoDAO.eliminarConflictosPorAsignacion(idAsignacion);
            }
            rsParticipante.close();
            pstmtParticipante.close();
            
            // 3. Eliminar asignaciones de espacio
            String sqlDeleteEspacio = "DELETE FROM asignacion_espacio WHERE id_actividad = ?";
            PreparedStatement pstmtDeleteEspacio = connection.prepareStatement(sqlDeleteEspacio);
            pstmtDeleteEspacio.setInt(1, idActividad);
            pstmtDeleteEspacio.executeUpdate();
            pstmtDeleteEspacio.close();
            
            // 4. Eliminar asignaciones de participante
            String sqlDeleteParticipante = "DELETE FROM asignacion_participante WHERE id_actividad = ?";
            PreparedStatement pstmtDeleteParticipante = connection.prepareStatement(sqlDeleteParticipante);
            pstmtDeleteParticipante.setInt(1, idActividad);
            pstmtDeleteParticipante.executeUpdate();
            pstmtDeleteParticipante.close();
            
            // 5. Finalmente eliminar la actividad
            String sql = "DELETE FROM actividad WHERE id_actividad = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idActividad);
            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
            
            if (rowsAffected > 0) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean actualizarActividad(Actividad actividad) {
        String sql = "UPDATE actividades SET nombre = ?, tipo = ?, fecha_hora_inicio = ?, fecha_hora_fin = ?, duracion = ? WHERE id_actividad = ?";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, actividad.getNombre());
            preparedStatement.setString(2, actividad.getTipo());
            preparedStatement.setInt(3, actividad.getDuracion());
            preparedStatement.setInt(4, actividad.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Retorna true si se actualizó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

        public ArrayList<Actividad> listarActividadesTodas(){
        String sql = "Select actividad.id_actividad, actividad.nombre, actividad.tipo, actividad.duracion, congreso.id_congreso, congreso.nombre, congreso.fecha_inicio, congreso.hora_inicio, congreso.fecha_fin, congreso.hora_fin from actividad inner join congreso on actividad.id_congreso = congreso.id_congreso";
        ArrayList<Actividad> actividades = new ArrayList<>();
        try (Connection connection = new ConexionDB().conectarDB();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id_actividad");
                int congresoID = resultSet.getInt("id_congreso");
                String nombre = resultSet.getString("nombre");
                String tipo = resultSet.getString("tipo");
                int duracion = resultSet.getInt("duracion");
                Congreso congreso = new Congreso(congresoID, resultSet.getString("congreso.nombre"),
                        resultSet.getDate("congreso.fecha_inicio").toLocalDate(),
                        resultSet.getTime("congreso.hora_inicio").toLocalTime(),
                        resultSet.getDate("congreso.fecha_fin").toLocalDate(),
                        resultSet.getTime("congreso.hora_fin").toLocalTime());
                Actividad actividad = new Actividad(id, congreso, nombre, tipo, duracion);
                actividades.add(actividad);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actividades;
    }

}
