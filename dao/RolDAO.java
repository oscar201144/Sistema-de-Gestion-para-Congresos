package dao;

import java.sql.*;
import java.util.ArrayList;
import modelo.Rol;
import vista.VentanaError;
import vista.VentanaExito;

public class RolDAO {
    
    public void guardarRol(String nombreRol) {
        String sql = "INSERT INTO rol (nombre_rol) VALUES (?)";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, nombreRol);
            preparedStatement.executeUpdate();
            new VentanaExito("Rol guardado exitosamente: " + nombreRol);
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al guardar el rol: " + e.getMessage());
        }
    }
    
    public ArrayList<Rol> obtenerTodosLosRoles() {
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
            e.printStackTrace();
            new VentanaError("Error al obtener los roles: " + e.getMessage());
        }
        
        return roles;
    }
    
    public Rol obtenerRolPorId(int id) {
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
            e.printStackTrace();
            new VentanaError("Error al obtener el rol: " + e.getMessage());
        }
        
        return rol;
    }
    
    public void eliminarRol(int id) {
        // Primero verificar si tiene asignaciones
        String verificarSql = "SELECT COUNT(*) as total FROM asignacion_participante WHERE id_rol = ?";
        
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement verificarStmt = connection.prepareStatement(verificarSql)) {
            
            verificarStmt.setInt(1, id);
            ResultSet rs = verificarStmt.executeQuery();
            
            if (rs.next() && rs.getInt("total") > 0) {
                new VentanaError("No se puede eliminar el rol. Tiene asignaciones activas.");
                return;
            }
            
            // Si no tiene asignaciones, proceder a eliminar
            String eliminarSql = "DELETE FROM rol WHERE id_rol = ?";
            PreparedStatement eliminarStmt = connection.prepareStatement(eliminarSql);
            eliminarStmt.setInt(1, id);
            
            int filasAfectadas = eliminarStmt.executeUpdate();
            if (filasAfectadas > 0) {
                new VentanaExito("Rol eliminado exitosamente.");
            } else {
                new VentanaError("No se encontró el rol con ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al eliminar el rol: " + e.getMessage());
        }
    }
    
    public boolean existeRol(String nombreRol) {
        String sql = "SELECT COUNT(*) as total FROM rol WHERE LOWER(nombre_rol) = LOWER(?)";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, nombreRol.trim());
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt("total") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al verificar la existencia del rol: " + e.getMessage());
        }
        
        return false;
    }
}
