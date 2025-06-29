package controlador;
import java.sql.SQLException;
import java.util.ArrayList;
import modelo.Congreso;
import modelo.Espacio;
import dao.*;

public class GestorEspacio {
    EspaciosDAO espaciosDAO;

    public GestorEspacio() {
        espaciosDAO = new EspaciosDAO();
    }

    public String agregarEspacio(Congreso congreso, Espacio espacio) {
        if (espacio != null) {
            try {
                espaciosDAO.guardarEspacio(espacio);
                return "Espacio guardado exitosamente: " + espacio.getNombre();
            } catch (SQLException e) {
                return "Error al guardar el espacio: " + e.getMessage();
            }
        }
        return "Error: No se puede agregar un espacio nulo";
    }

    public ArrayList<Espacio> listarEspacios(Congreso congreso) {
        try {
            return espaciosDAO.listarEspacios(congreso.getId());
        } catch (SQLException e) {
            System.err.println("Error al listar espacios: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Boolean eliminarEspacio(int idEspacio) {
        try {
            boolean eliminado = espaciosDAO.eliminarEspacio(idEspacio);
            if (eliminado) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar el espacio: " + e.getMessage());
            return false;
        }
    }

    public String actualizarEspacio(Espacio espacioActualizado) {
        if (espacioActualizado != null) {
            try {
                boolean actualizado = espaciosDAO.actualizarEspacio(espacioActualizado);
                if (actualizado) {
                    return "Espacio actualizado exitosamente.";
                } else {
                    return "No se encontró el espacio o no se pudo actualizar.";
                }
            } catch (SQLException e) {
                return "Error al actualizar el espacio: " + e.getMessage();
            }
        }
        return "Error: No se puede actualizar un espacio nulo";
    }
}
