package controlador;

import modelo.*;

import java.util.ArrayList;

import dao.ConflictoDAO;

public class GestorConflictos {
    private ConflictoDAO conflictoDAO;

    public GestorConflictos() {
        this.conflictoDAO = new ConflictoDAO();
    }

    public void registrarConflicto(Conflicto conflicto) {
        conflictoDAO.guardarConflicto(conflicto);
    }

    public ArrayList<Conflicto> procesarNuevaAsignacionEspacios(AsignacionEspacio nuevaAsignacion,
            ArrayList<AsignacionEspacio> asignacionesExistentes) {
        ArrayList<Conflicto> conflictosDetectados = new ArrayList<>();

        if (actividadYaAsignada(nuevaAsignacion, asignacionesExistentes)) {
            Conflicto conflictoActividad = new Conflicto(-1, nuevaAsignacion.getCongreso(),
                    "Conflicto: Actividad ya asignada - Una actividad solo puede estar una vez por congreso",
                    nuevaAsignacion.getActividad(), null, // No hay actividad conflictiva específica
                    nuevaAsignacion.getEspacio());

            conflictosDetectados.add(conflictoActividad);
            registrarConflicto(conflictoActividad);
        }

        // Verificar conflictos con cada asignación existente
        for (AsignacionEspacio asignacionExistente : asignacionesExistentes) {
            if (nuevaAsignacion.getEspacio().equals(asignacionExistente.getEspacio()) &&
                    nuevaAsignacion.getFecha().equals(asignacionExistente.getFecha()) &&
                    nuevaAsignacion.getHoraInicio().equals(asignacionExistente.getHoraInicio())) {

                // Crear y registrar el conflicto
                Conflicto conflicto = new Conflicto(0, nuevaAsignacion.getCongreso(),
                        "Conflicto de espacio entre actividades",
                        nuevaAsignacion.getActividad(), asignacionExistente.getActividad(),
                        nuevaAsignacion.getEspacio());

                conflictosDetectados.add(conflicto);
                registrarConflicto(conflicto);
            }
        }

        return conflictosDetectados;
    }

    private boolean actividadYaAsignada(AsignacionEspacio nuevaAsignacion,
            ArrayList<AsignacionEspacio> asignacionesExistentes) {
        for (AsignacionEspacio asignacionExistente : asignacionesExistentes) {
            if (nuevaAsignacion.getActividad().equals(asignacionExistente.getActividad()) &&
                    nuevaAsignacion.getCongreso().equals(asignacionExistente.getCongreso())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Conflicto> obtenerConflictosPendientes(Congreso congreso) {
        return conflictoDAO.listarConflictosPendientes(congreso);
    }

    public void eliminarConflicto(int idConflicto) {
        conflictoDAO.eliminarConflicto(idConflicto);
    }

}
