package dao;

import java.sql.*;
import java.util.ArrayList;
import modelo.Rol;

public class RolDAO {
    
    public void guardarRol(String nombreRol) throws SQLException {
        String sql = "INSERT INTO rol (nombre_rol) VALUES (?)";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, nombreRol);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al guardar el rol: " + e.getMessage(), e);
        }
    }
    
    public ArrayList<Rol> obtenerTodosLosRoles() throws SQLException {
        String sql = "SELECT id_rol, nombre_rol FROM rol ORDER BY nombre_rol";
        ArrayList<Rol> roles = new ArrayList<>();
        
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Rol rol = new Rol(
                    resultSet.getInt("id_rol"),
                    resultSet.getString("nombre_rol")
                );
                roles.add(rol);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener los roles: " + e.getMessage(), e);
        }
        
        return roles;
    }
    
    public Rol obtenerRolPorId(int id) throws SQLException {
        String sql = "SELECT id_rol, nombre_rol FROM rol WHERE id_rol = ?";
        Rol rol = null;
        
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                rol = new Rol(
                    resultSet.getInt("id_rol"),
                    resultSet.getString("nombre_rol")
                );
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener el rol: " + e.getMessage(), e);
        }
        
        return rol;
    }
    
    public boolean eliminarRol(int id) throws SQLException {
        // Primero verificar si tiene asignaciones
        String verificarSql = "SELECT COUNT(*) as total FROM asignacion_participante WHERE id_rol = ?";
        
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement verificarStmt = connection.prepareStatement(verificarSql)) {
            
            verificarStmt.setInt(1, id);
            ResultSet rs = verificarStmt.executeQuery();
            
            if (rs.next() && rs.getInt("total") > 0) {
                throw new SQLException("No se puede eliminar el rol. Tiene asignaciones activas.");
            }
            
            // Si no tiene asignaciones, proceder a eliminar
            String eliminarSql = "DELETE FROM rol WHERE id_rol = ?";
            PreparedStatement eliminarStmt = connection.prepareStatement(eliminarSql);
            eliminarStmt.setInt(1, id);
            
            int filasAfectadas = eliminarStmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar el rol: " + e.getMessage(), e);
        }
    }
    
    public boolean existeRol(String nombreRol) throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM rol WHERE LOWER(nombre_rol) = LOWER(?)";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, nombreRol.trim());
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt("total") > 0;
            }
        } catch (SQLException e) {
            throw new SQLException("Error al verificar la existencia del rol: " + e.getMessage(), e);
        }
        
        return false;
    }
}
