package controlador;
import java.util.ArrayList;
import modelo.Congreso;
import modelo.Espacio;
import dao.*;

public class GestorEspacio {
    EspaciosDAO espaciosDAO;

    public GestorEspacio() {
        espaciosDAO = new EspaciosDAO();
    }

    public boolean agregarEspacio(Congreso congreso, Espacio espacio) {
        if (espacio != null) {
            espaciosDAO.guardarEspacio(espacio);
            return true; // Espacio agregado exitosamente
        }
        return false; // No se pudo agregar el espacio
    }

    public ArrayList<String> listarEspacios(Congreso congreso) {
        return espaciosDAO.listarEspacios(congreso.getId());
    }

    public boolean eliminarEspacio(int idEspacio) {
        return espaciosDAO.eliminarEspacio(idEspacio);
    }

    public boolean actualizarEspacio(Espacio espacioActualizado) {
        if (espacioActualizado != null) {
            espaciosDAO.actualizarEspacio(espacioActualizado);
            return true; // Espacio actualizado exitosamente
        }
        return false; // No se pudo actualizar el espacio
    }
}
