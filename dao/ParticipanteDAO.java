package dao;

import java.sql.*;
import java.util.ArrayList;
import modelo.Persona;
import vista.VentanaError;
import vista.VentanaExito;

public class ParticipanteDAO {
    
    public void guardarParticipante(Persona participante) {
        String sql = "INSERT INTO participante (nombre) VALUES (?)";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setString(1, participante.getNombre());
            preparedStatement.executeUpdate();
            
            // Obtener el ID generado
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                participante.setId(generatedKeys.getInt(1));
            }
            
            new VentanaExito("Participante guardado exitosamente: " + participante.getNombre());
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al guardar el participante: " + e.getMessage());
        }
    }
    
    public ArrayList<Persona> obtenerTodosLosParticipantes() {
        String sql = "SELECT id_participante, nombre FROM participante ORDER BY nombre";
        ArrayList<Persona> participantes = new ArrayList<>();
        
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Persona participante = new Persona(
                    resultSet.getInt("id_participante"),
                    resultSet.getString("nombre")
                );
                participantes.add(participante);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al obtener los participantes: " + e.getMessage());
        }
        
        return participantes;
    }
    
    public Persona obtenerParticipantePorId(int id) {
        String sql = "SELECT id_participante, nombre FROM participante WHERE id_participante = ?";
        Persona participante = null;
        
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                participante = new Persona(
                    resultSet.getInt("id_participante"),
                    resultSet.getString("nombre")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al obtener el participante: " + e.getMessage());
        }
        
        return participante;
    }
    
    public void actualizarParticipante(Persona participante) {
        String sql = "UPDATE participante SET nombre = ? WHERE id_participante = ?";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, participante.getNombre());
            preparedStatement.setInt(2, participante.getId());
            
            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas > 0) {
                new VentanaExito("Participante actualizado exitosamente: " + participante.getNombre());
            } else {
                new VentanaError("No se encontró el participante con ID: " + participante.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al actualizar el participante: " + e.getMessage());
        }
    }
    
    public void eliminarParticipante(int id) {
        // Primero verificar si tiene asignaciones
        String verificarSql = "SELECT COUNT(*) as total FROM asignacion_participante WHERE id_participante = ?";
        
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement verificarStmt = connection.prepareStatement(verificarSql)) {
            
            verificarStmt.setInt(1, id);
            ResultSet rs = verificarStmt.executeQuery();
            
            if (rs.next() && rs.getInt("total") > 0) {
                new VentanaError("No se puede eliminar el participante. Tiene asignaciones activas.");
                return;
            }
            
            // Si no tiene asignaciones, proceder a eliminar
            String eliminarSql = "DELETE FROM participante WHERE id_participante = ?";
            PreparedStatement eliminarStmt = connection.prepareStatement(eliminarSql);
            eliminarStmt.setInt(1, id);
            
            int filasAfectadas = eliminarStmt.executeUpdate();
            if (filasAfectadas > 0) {
                new VentanaExito("Participante eliminado exitosamente.");
            } else {
                new VentanaError("No se encontró el participante con ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al eliminar el participante: " + e.getMessage());
        }
    }
    
    public boolean existeParticipante(String nombre) {
        String sql = "SELECT COUNT(*) as total FROM participante WHERE LOWER(nombre) = LOWER(?)";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, nombre.trim());
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt("total") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al verificar la existencia del participante: " + e.getMessage());
        }
        
        return false;
    }
}
