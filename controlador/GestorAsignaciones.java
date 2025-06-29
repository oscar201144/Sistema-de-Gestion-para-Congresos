package controlador;
import java.util.ArrayList;
import dao.AsignacionDAO;
import modelo.AsignacionEspacio;
import modelo.Congreso;

import modelo.Conflicto;

public class GestorAsignaciones {
    public ArrayList<AsignacionEspacio> obtenerAsignacionesEspacios(int idCongreso) {
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        if (asignacionDAO.obtenerAsignacionesEspacios(idCongreso) == null) {
            return new ArrayList<>();
        } else {
            return asignacionDAO.obtenerAsignacionesEspacios(idCongreso);
        }
    }
    public String obtenerHoraTempranaDisponible(Congreso congreso, String fechaSeleccionada) {
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        String horaTemprana = asignacionDAO.obtenerHoraTempranaDisponible(congreso.getId(), fechaSeleccionada);
        if (horaTemprana == null) {
            return String.valueOf(congreso.getHoraInicio());
        } else {
            return horaTemprana;
        }
    }
    public Boolean procesarNuevaAsignacionEspacios(AsignacionEspacio nuevaAsignacion) {
        ArrayList<AsignacionEspacio> asignacionesExistentes = obtenerAsignacionesEspacios(nuevaAsignacion.getCongreso().getId());
        // Crear un gestor de conflictos
        GestorConflictos gestorConflictos = new GestorConflictos();
        // Procesar la nueva asignación y obtener los conflictos detectados
        ArrayList<Conflicto> conflictosDetectados = gestorConflictos.procesarNuevaAsignacionEspacios(nuevaAsignacion, asignacionesExistentes);
        // Si no hay conflictos, se procede a guardar la asignación
        if (conflictosDetectados.isEmpty()) {
            AsignacionDAO asignacionDAO = new AsignacionDAO();
            asignacionDAO.guardarAsignacionEspacio(nuevaAsignacion);
            return true;
        } else {
            // Si hay conflictos, se retorna false
            return false;
        }
    }

    public int actualizarAsignacionEspacio(AsignacionEspacio asignacion) {
        // Buscar si la actividad ya está asignada en algun espacio
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        if (asignacionDAO.obtenerAsignacionesEspacios(asignacion.getCongreso().getId()).stream()
                .anyMatch(a -> a.getActividad().equals(asignacion.getActividad()))) {
                return 1; // Retorna 1 si la actividad ya está asignada a otro espacio

        }
        // Si no está asignada, se procede a actualizar la asignación
        asignacionDAO.actualizarAsignacionEspacio(asignacion);
        return 0; // Retorna 0 si la actualización fue exitosa
    }

    public int forzarActualizacionAsignacion(AsignacionEspacio asignacion) {
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        asignacionDAO.actualizarAsignacionEspacio(asignacion);
        return 0; // Retorna 0 si la actualización fue exitosa
    }

    public boolean eliminarAsignacionEspacio(int idAsignacion) {
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        asignacionDAO.eliminarAsignacionEspacio(idAsignacion);
        return true;
    }
}