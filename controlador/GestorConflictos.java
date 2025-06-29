package controlador;

import modelo.*;

import java.util.ArrayList;
import java.sql.SQLException;

import dao.ConflictoDAO;

public class GestorConflictos {
    private ConflictoDAO conflictoDAO;

    public GestorConflictos() {
        this.conflictoDAO = new ConflictoDAO();
    }

    public String registrarConflicto(Conflicto conflicto) {
        try {
            boolean exito = conflictoDAO.guardarConflicto(conflicto);
            if (exito) {
                return "Conflicto registrado exitosamente";
            } else {
                return "Error: No se pudo registrar el conflicto";
            }
        } catch (SQLException e) {
            return "Error en base de datos: " + e.getMessage();
        }
    }
    
    public String registrarConflictos(ArrayList<Conflicto> conflictos) {
        int exitosos = 0;
        int fallidos = 0;
        StringBuilder errores = new StringBuilder();
        
        for (Conflicto conflicto : conflictos) {
            try {
                boolean exito = conflictoDAO.guardarConflicto(conflicto);
                if (exito) {
                    exitosos++;
                } else {
                    fallidos++;
                }
            } catch (SQLException e) {
                fallidos++;
                errores.append("Error: ").append(e.getMessage()).append("\n");
            }
        }
        
        if (fallidos == 0) {
            return String.format("Todos los conflictos registrados exitosamente (%d)", exitosos);
        } else {
            return String.format("Registrados: %d, Fallidos: %d. Errores: %s", 
                               exitosos, fallidos, errores.toString());
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
        try {
            return conflictoDAO.listarConflictosPendientes(congreso);
        } catch (SQLException e) {
            System.err.println("Error al obtener conflictos: " + e.getMessage());
            return new ArrayList<>(); // Retorna lista vacía en caso de error
        }
    }

    public String eliminarConflictosPorAsignacion(int idAsignacion) {
        try {
            boolean exito = conflictoDAO.eliminarConflictosPorAsignacion(idAsignacion);
            if (exito) {
                return "Conflictos eliminados exitosamente";
            } else {
                return "No se encontraron conflictos para eliminar";
            }
        } catch (SQLException e) {
            return "Error al eliminar conflictos: " + e.getMessage();
        }
    }


}
