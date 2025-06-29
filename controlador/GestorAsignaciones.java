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

    public String procesarNuevaAsignacionEspacios(AsignacionEspacio nuevaAsignacion) {
        ArrayList<AsignacionEspacio> asignacionesExistentes = obtenerAsignacionesEspacios(
                nuevaAsignacion.getCongreso().getId());
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        GestorConflictos gestorConflictos = new GestorConflictos();
        
        // Procesar la nueva asignación y obtener los conflictos detectados
        ArrayList<Conflicto> conflictosDetectados = gestorConflictos.procesarNuevaAsignacionEspacios(nuevaAsignacion,
                asignacionesExistentes);
        
        // AGREGAR ESTE LOG TEMPORAL
        System.out.println("Conflictos detectados: " + conflictosDetectados.size());
        for (Conflicto c : conflictosDetectados) {
            if (c.getIdAsignacion2() != -1) {
                System.out.println("Conflicto: Asignación " + c.getIdAsignacion1() + " vs " + c.getIdAsignacion2());
            } else {
                System.out.println("Conflicto: " + c.getTipo() + " - " + c.getDescripcion());
            }
        }

        // Guardar la asignación
        asignacionDAO.guardarAsignacionEspacio(nuevaAsignacion);
        
        if (conflictosDetectados.isEmpty()) {
            return "Asignación exitosa";
        } else {
            // Registrar los nuevos conflictos detectados
            gestorConflictos.registrarConflictos(conflictosDetectados);
            return "Conflictos detectados: " + conflictosDetectados.size()
                    + ". Por favor, revise los conflictos antes de continuar.";
        }
    }

    public String actualizarAsignacionEspacio(AsignacionEspacio asignacion) {
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        GestorConflictos gestorConflictos = new GestorConflictos();

        // Obtener todas las asignaciones existentes del congreso
        ArrayList<AsignacionEspacio> asignacionesExistentes = obtenerAsignacionesEspacios(
                asignacion.getCongreso().getId());

        // Filtrar las asignaciones para excluir la que estamos actualizando
        ArrayList<AsignacionEspacio> asignacionesFiltradas = new ArrayList<>();
        for (AsignacionEspacio a : asignacionesExistentes) {
            if (a.getId() != asignacion.getId()) {
                asignacionesFiltradas.add(a);
            }
        }

        // Verificar conflictos con la asignación actualizada
        ArrayList<Conflicto> conflictosDetectados = gestorConflictos.procesarNuevaAsignacionEspacios(
                asignacion, asignacionesFiltradas);

        // Actualizar la asignación
        asignacionDAO.actualizarAsignacionEspacio(asignacion);

        if (conflictosDetectados.isEmpty()) {
            // Si no hay conflictos, eliminar cualquier conflicto previo de esta asignación
            gestorConflictos.eliminarConflictosPorAsignacion(asignacion.getId());
            return "Actualización exitosa";
        } else {
            // Si hay conflictos, eliminar los antiguos y registrar los nuevos
            gestorConflictos.eliminarConflictosPorAsignacion(asignacion.getId());
            gestorConflictos.registrarConflictos(conflictosDetectados);
            return "Conflictos detectados: " + conflictosDetectados.size()
                    + ". Por favor, revise los conflictos antes de continuar.";
        }
    }

    public String forzarActualizacionAsignacion(AsignacionEspacio asignacion) {
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        GestorConflictos gestorConflictos = new GestorConflictos();
        
        // Actualizar la asignación
        asignacionDAO.actualizarAsignacionEspacio(asignacion);
        
        // Eliminar todos los conflictos relacionados con esta asignación
        gestorConflictos.eliminarConflictosPorAsignacion(asignacion.getId());
        
        return "Actualización forzada exitosa";
    }

    public boolean eliminarAsignacionEspacio(int idAsignacion) {
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        asignacionDAO.eliminarAsignacionEspacio(idAsignacion);
        return true;
    }
}