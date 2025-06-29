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
    public void registrarConflictos(ArrayList<Conflicto> conflictos) {
        for (Conflicto conflicto : conflictos) {
            conflictoDAO.guardarConflicto(conflicto);
        }
    }

    public ArrayList<Conflicto> procesarNuevaAsignacionEspacios(AsignacionEspacio nuevaAsignacion,
            ArrayList<AsignacionEspacio> asignacionesExistentes) {
        ArrayList<Conflicto> conflictosDetectados = new ArrayList<>();

        if (actividadYaAsignada(nuevaAsignacion, asignacionesExistentes)) {
            Conflicto conflictoActividad = new Conflicto(0, nuevaAsignacion.getCongreso(),
                    "Conflicto: Actividad ya asignada - Una actividad solo puede estar una vez por congreso",
                    nuevaAsignacion.getActividad(), nuevaAsignacion.getId());

            conflictosDetectados.add(conflictoActividad);
        }

        // Verificar conflictos con cada asignación existente
        for (AsignacionEspacio asignacionExistente : asignacionesExistentes) {
            if (nuevaAsignacion.getEspacio().getId() == asignacionExistente.getEspacio().getId() &&
                    nuevaAsignacion.getFecha().equals(asignacionExistente.getFecha()) &&
                    nuevaAsignacion.getHoraInicio().equals(asignacionExistente.getHoraInicio())) {

                // Crear y registrar el conflicto
                Conflicto conflicto = new Conflicto(0, nuevaAsignacion.getCongreso(),
                        "Conflicto de espacio entre actividades",
                        nuevaAsignacion.getActividad(), asignacionExistente.getActividad(),
                        nuevaAsignacion.getEspacio(), nuevaAsignacion.getId());

                conflictosDetectados.add(conflicto);
            }
        }

        return conflictosDetectados;
    }

    private boolean actividadYaAsignada(AsignacionEspacio nuevaAsignacion,
            ArrayList<AsignacionEspacio> asignacionesExistentes) {
        for (AsignacionEspacio asignacionExistente : asignacionesExistentes) {
            if (nuevaAsignacion.getActividad().getId() == asignacionExistente.getActividad().getId() &&
                    nuevaAsignacion.getCongreso().getId() == asignacionExistente.getCongreso().getId()) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Conflicto> obtenerConflictosPendientes(Congreso congreso) {
        return conflictoDAO.listarConflictosPendientes(congreso);
    }

    public void eliminarConflictosPorAsignacion(int idAsignacion) {
        conflictoDAO.eliminarConflictosPorAsignacion(idAsignacion);
    }


}
