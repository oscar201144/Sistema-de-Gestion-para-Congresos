package dao;
import modelo.Actividad;
import modelo.Congreso;

import java.util.ArrayList;
import java.sql.*;
import vista.VentanaError;
import vista.VentanaExito;

public class ActividadesDAO {
    public void guardarActividad(Actividad actividad) {
        String sql = "INSERT INTO actividad (id_congreso, nombre, tipo, duracion) VALUES (?, ?, ?, ?)";

        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, actividad.getCongreso().getId());
            preparedStatement.setString(2, actividad.getNombre());
            preparedStatement.setString(3, actividad.getTipo());
            preparedStatement.setString(4, actividad.getDuracion());
            preparedStatement.executeUpdate();
            new VentanaExito("Actividad guardada exitosamente: " + actividad.getNombre());
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al guardar la actividad: " + e.getMessage());
        }
    }
    public ArrayList<Actividad> listarActividades(int idCongreso) {
        ArrayList<Actividad> actividades = new ArrayList<>();
        String sql = "SELECT actividad.id_actividad, actividad.id_congreso,actividad.nombre, actividad.tipo, actividad.duracion, congreso.nombre FROM actividad INNER join congreso on actividad.id_congreso = congreso.id_congreso WHERE actividad.id_congreso = ?";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idCongreso);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Actividad actividad = new Actividad(
                        resultSet.getInt("id_actividad"),
                        new Congreso(idCongreso, resultSet.getString("congreso.nombre")),
                        resultSet.getString("nombre"),
                        resultSet.getString("tipo"),
                        resultSet.getString("duracion")
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
        String sql = "DELETE FROM actividad WHERE id_actividad = ?";
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
            preparedStatement.setString(3, actividad.getDuracion());
            preparedStatement.setInt(4, actividad.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Retorna true si se actualizó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al actualizar la actividad: " + e.getMessage());
            return false;
        }
    }

        public ArrayList<Actividad> listarActividadesTodas(){
        String sql = "Select * from actividad inner join congreso on actividad.id_congreso = congreso.id_congreso";
        ArrayList<Actividad> actividades = new ArrayList<>();
        try (Connection connection = new ConexionDB().conectarDB();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id_actividad");
                int congresoID = resultSet.getInt("id_congreso");
                String nombre = resultSet.getString("nombre");
                String tipo = resultSet.getString("tipo");
                String duracion = resultSet.getString("duracion");
                Congreso congreso = new Congreso(congresoID, resultSet.getString("congreso.nombre"));
                Actividad actividad = new Actividad(id, congreso, nombre, tipo, duracion);
                actividades.add(actividad);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al listar actividades: " + e.getMessage());
        }
        return actividades;
    }

}
