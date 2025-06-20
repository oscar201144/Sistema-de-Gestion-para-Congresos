package controlador;
import java.util.ArrayList;

import dao.AsignacionDAO;
import modelo.AsignacionParticipante;
import modelo.AsignacionEspacio;

public class GestorAsignaciones {
    public ArrayList<AsignacionEspacio> GestorAsignacionesEspacios(int idCongreso) {
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        if (asignacionDAO.obtenerAsignacionesEspacios(idCongreso) == null) {
            return new ArrayList<>();
        } else {
            return asignacionDAO.obtenerAsignacionesEspacios(idCongreso);
        }
    }
}
