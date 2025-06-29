package controlador;
import modelo.Congreso;
import dao.ActividadesDAO;
import java.util.ArrayList;
import modelo.Actividad;

public class GestorActividades {
    ActividadesDAO actividadesDAO;
    public GestorActividades() {
        actividadesDAO = new ActividadesDAO();
    }
    public boolean agregarActividad(Congreso congreso, Actividad actividad) {
        if (actividad != null) {
            return actividadesDAO.guardarActividad(congreso, actividad);
        }
        return false; // No se pudo agregar la actividad
    }

    public boolean registrarActividad(Congreso congreso, Actividad actividad) {
        if (actividad != null) {
            return actividadesDAO.guardarActividad(congreso, actividad);
        }
        return false; // No se pudo registrar la actividad
    }
    public boolean eliminarActividad(Congreso congreso, int idActividad) {
        return actividadesDAO.eliminarActividad(idActividad);
    }
    public boolean actualizarActividad(Congreso congreso, Actividad actividadActualizada) {
        if (actividadActualizada != null) {
            return actividadesDAO.actualizarActividad(actividadActualizada);
        }
        return false; // No se pudo actualizar la actividad
    }
    public ArrayList<Actividad> listarActividades(Congreso congreso) {
        return actividadesDAO.listarActividades(congreso.getId());
    }

    public ArrayList<Actividad> getActividades() {
        return actividadesDAO.listarActividadesTodas();// 0 para obtener todas las actividades sin filtrar por congreso
    }
}
