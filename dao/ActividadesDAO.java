package dao;
import modelo.Actividad;
import java.util.ArrayList;
import java.sql.*;
import vista.VentanaError;
import vista.VentanaExito;

public class ActividadesDAO {
    public void guardarActividad(Actividad actividad) {
        String sql = "INSERT INTO actividades (id_congreso, nombre, tipo, fecha_hora_inicio, fecha_hora_fin, duracion) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, actividad.getCongreso().getId());
            preparedStatement.setString(2, actividad.getNombre());
            preparedStatement.setString(3, actividad.getTipo());
            preparedStatement.setString(4, actividad.getFechaHoraInicio());
            preparedStatement.setString(5, actividad.getFechaHoraFin());
            preparedStatement.setInt(6, actividad.getDuracion());
            preparedStatement.executeUpdate();
            new VentanaExito("Actividad guardada exitosamente: " + actividad.getNombre());
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al guardar la actividad: " + e.getMessage());
        }
    }
    public ArrayList<Actividad> listarActividades(int idCongreso) {
        ArrayList<Actividad> actividades = new ArrayList<>();
        String sql = "SELECT * FROM actividades WHERE id_congreso = ?";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idCongreso);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Actividad actividad = new Actividad(
                        resultSet.getInt("id_actividad"),
                        null, // El congreso se asignará después
                        resultSet.getString("nombre"),
                        resultSet.getString("tipo"),
                        resultSet.getString("fecha_hora_inicio"),
                        resultSet.getString("fecha_hora_fin"),
                        resultSet.getInt("duracion")
                );
                actividades.add(actividad);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al obtener la lista de actividades: " + e.getMessage());
        }
        return actividades;
    }
    public boolean eliminarActividad(int idActividad) {
        String sql = "DELETE FROM actividades WHERE id_actividad = ?";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idActividad);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Retorna true si se eliminó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al eliminar la actividad: " + e.getMessage());
            return false;
        }
    }
    public boolean actualizarActividad(Actividad actividad) {
        String sql = "UPDATE actividades SET nombre = ?, tipo = ?, fecha_hora_inicio = ?, fecha_hora_fin = ?, duracion = ? WHERE id_actividad = ?";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, actividad.getNombre());
            preparedStatement.setString(2, actividad.getTipo());
            preparedStatement.setString(3, actividad.getFechaHoraInicio());
            preparedStatement.setString(4, actividad.getFechaHoraFin());
            preparedStatement.setInt(5, actividad.getDuracion());
            preparedStatement.setInt(6, actividad.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Retorna true si se actualizó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al actualizar la actividad: " + e.getMessage());
            return false;
        }
    }

}
