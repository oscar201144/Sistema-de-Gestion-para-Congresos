package dao;

import java.util.ArrayList;
import java.sql.*;
import vista.VentanaError;
import vista.VentanaExito;
import modelo.*;

public class EspaciosDAO {
    public void guardarEspacio(Espacio espacio) {
        String sql = "INSERT INTO espacio (id_congreso, nombre, capacidad) VALUES (?, ?, ?)";

        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, espacio.getCongreso().getId());
            preparedStatement.setString(2, espacio.getNombre());
            preparedStatement.setInt(3, espacio.getCapacidad());
            preparedStatement.executeUpdate();
            new VentanaExito("Espacio guardado exitosamente: " + espacio.getNombre());
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al guardar el espacio: " + e.getMessage());
        }
    }

    public ArrayList<String> listarEspacios(int congresoId) {
        ArrayList<String> espacios = new ArrayList<>();
        String sql = "SELECT nombre FROM espacio WHERE id_congreso = ?";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, congresoId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                espacios.add(resultSet.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return espacios;
    }

    public boolean eliminarEspacio(int idEspacio) {
        String sql = "DELETE FROM espacio WHERE id_espacio = ?";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idEspacio);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Retorna true si se eliminó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al eliminar el espacio: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarEspacio(Espacio espacioActualizado) {
        String sql = "UPDATE espacio SET nombre = ?, capacidad = ? WHERE id_espacio = ?";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, espacioActualizado.getNombre());
            preparedStatement.setInt(2, espacioActualizado.getCapacidad());
            preparedStatement.setInt(3, espacioActualizado.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Retorna true si se actualizó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al actualizar el espacio: " + e.getMessage());
            return false;
        }
    }
}
