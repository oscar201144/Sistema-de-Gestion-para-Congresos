package dao;

import java.sql.*;
import java.util.ArrayList;
import modelo.Persona;

public class ParticipanteDAO {
    
    public void guardarParticipante(Persona participante) throws SQLException {
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
            
        } catch (SQLException e) {
            throw new SQLException("Error al guardar el participante: " + e.getMessage(), e);
        }
    }
    
    public ArrayList<Persona> obtenerTodosLosParticipantes() throws SQLException {
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
            throw new SQLException("Error al obtener los participantes: " + e.getMessage(), e);
        }
        
        return participantes;
    }
    
    public Persona obtenerParticipantePorId(int id) throws SQLException {
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
            throw new SQLException("Error al obtener el participante: " + e.getMessage(), e);
        }
        
        return participante;
    }
    
    public boolean actualizarParticipante(Persona participante) throws SQLException {
        String sql = "UPDATE participante SET nombre = ? WHERE id_participante = ?";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, participante.getNombre());
            preparedStatement.setInt(2, participante.getId());
            
            int filasAfectadas = preparedStatement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el participante: " + e.getMessage(), e);
        }
    }
    
    public boolean eliminarParticipante(int id) throws SQLException {
        // Primero verificar si tiene asignaciones
        String verificarSql = "SELECT COUNT(*) as total FROM asignacion_participante WHERE id_participante = ?";
        
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement verificarStmt = connection.prepareStatement(verificarSql)) {
            
            verificarStmt.setInt(1, id);
            ResultSet rs = verificarStmt.executeQuery();
            
            if (rs.next() && rs.getInt("total") > 0) {
                throw new SQLException("No se puede eliminar el participante. Tiene asignaciones activas.");
            }
            
            // Si no tiene asignaciones, proceder a eliminar
            String eliminarSql = "DELETE FROM participante WHERE id_participante = ?";
            PreparedStatement eliminarStmt = connection.prepareStatement(eliminarSql);
            eliminarStmt.setInt(1, id);
            
            int filasAfectadas = eliminarStmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar el participante: " + e.getMessage(), e);
        }
    }
    
    public boolean existeParticipante(String nombre) throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM participante WHERE LOWER(nombre) = LOWER(?)";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, nombre.trim());
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt("total") > 0;
            }
        } catch (SQLException e) {
            throw new SQLException("Error al verificar la existencia del participante: " + e.getMessage(), e);
        }
        
        return false;
    }
}
