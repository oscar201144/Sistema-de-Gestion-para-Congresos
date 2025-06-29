package controlador;

import java.sql.SQLException;
import java.util.ArrayList;
import dao.RolDAO;
import modelo.Rol;

public class GestorRol {
    private RolDAO rolDAO;
    
    public GestorRol() {
        this.rolDAO = new RolDAO();
    }
    
    public String registrarRol(String nombreRol) {
        if (nombreRol == null || nombreRol.trim().isEmpty()) {
            return "El nombre del rol no puede estar vacío";
        }
        
        nombreRol = nombreRol.trim();
        
        try {
            // Verificar si ya existe
            if (rolDAO.existeRol(nombreRol)) {
                return "Ya existe un rol con ese nombre";
            }
            
            rolDAO.guardarRol(nombreRol);
            return "Rol guardado exitosamente: " + nombreRol;
        } catch (SQLException e) {
            return "Error al registrar el rol: " + e.getMessage();
        }
    }
    
    public String eliminarRol(int id) {
        try {
            // Verificar que el rol existe
            Rol rol = rolDAO.obtenerRolPorId(id);
            if (rol == null) {
                return "No se encontró el rol con ID: " + id;
            }
            
            boolean eliminado = rolDAO.eliminarRol(id);
            if (eliminado) {
                return "Rol eliminado exitosamente";
            } else {
                return "No se pudo eliminar el rol";
            }
        } catch (SQLException e) {
            return "Error al eliminar el rol: " + e.getMessage();
        }
    }

    public ArrayList<Rol> obtenerTodosLosRoles() {
        try {
            return rolDAO.obtenerTodosLosRoles();
        } catch (SQLException e) {
            System.err.println("Error al obtener roles: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public Rol obtenerRolPorId(int id) {
        try {
            return rolDAO.obtenerRolPorId(id);
        } catch (SQLException e) {
            System.err.println("Error al obtener rol por ID: " + e.getMessage());
            return null;
        }
    }
    
    public boolean existeRol(String nombreRol) {
        try {
            return rolDAO.existeRol(nombreRol);
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia del rol: " + e.getMessage());
            return false;
        }
    }
}
