package controlador;
import java.util.ArrayList;

import dao.AsignacionDAO;
import modelo.AsignacionEspacio;
import modelo.Congreso;

public class GestorAsignaciones {
    public ArrayList<AsignacionEspacio> GestorAsignacionesEspacios(int idCongreso) {
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        if (asignacionDAO.obtenerAsignacionesEspacios(idCongreso) == null) {
            return new ArrayList<>();
        } else {
            return asignacionDAO.obtenerAsignacionesEspacios(idCongreso);
        }
    }
    public String obtenerHoraTempranaDisponible (Congreso congreso, String fechaSeleccionada) {
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        String horaTemprana = asignacionDAO.obtenerHoraTempranaDisponible(congreso.getId(), fechaSeleccionada);
        if (horaTemprana == null) {
            return String.valueOf(congreso.getHoraInicio());
        } else {
            return horaTemprana;
        }
        
    }
}
