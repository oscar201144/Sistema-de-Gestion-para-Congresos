package controlador;

import java.util.ArrayList;
import dao.AsignacionDAO;
import dao.RolDAO;
import modelo.AsignacionEspacio;
import modelo.AsignacionParticipante;
import modelo.Congreso;
import modelo.Conflicto;
import modelo.Rol;

public class GestorAsignaciones {
    private RolDAO rolDAO;
    
    public GestorAsignaciones() {
        this.rolDAO = new RolDAO();
    }
    
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
        
        // Guardar la asignación primero para obtener el ID generado
        int idAsignacionGenerado = asignacionDAO.guardarAsignacionEspacio(nuevaAsignacion);
        
        if (idAsignacionGenerado == -1) {
            return "Error al guardar la asignación";
        }
        
        // Ahora procesar los conflictos con el ID correcto
        ArrayList<Conflicto> conflictosDetectados = gestorConflictos.procesarNuevaAsignacionEspacios(nuevaAsignacion,
                asignacionesExistentes);
        
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
    
    //Obtiene todas las asignaciones de participantes para un congreso
    public ArrayList<AsignacionParticipante> obtenerAsignacionesParticipantes(int idCongreso) {
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        ArrayList<AsignacionParticipante> asignaciones = asignacionDAO.obtenerAsignacionesParticipantes(idCongreso);
        return asignaciones != null ? asignaciones : new ArrayList<>();
    }

    // Procesa una nueva asignación de participante con control de conflictos
    public String procesarNuevaAsignacionParticipante(AsignacionParticipante nuevaAsignacion) {
        ArrayList<AsignacionParticipante> asignacionesExistentes = obtenerAsignacionesParticipantes(
                nuevaAsignacion.getCongreso().getId());
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        GestorConflictos gestorConflictos = new GestorConflictos();
        
        // Guardar la asignación primero para obtener el ID generado
        int idAsignacionGenerado = asignacionDAO.guardarAsignacionParticipante(nuevaAsignacion);
        
        if (idAsignacionGenerado == -1) {
            return "Error al guardar la asignación de participante";
        }
        
        // Ahora detectar conflictos de participantes con el ID correcto
        ArrayList<Conflicto> conflictosDetectados = detectarConflictosParticipante(nuevaAsignacion, asignacionesExistentes);
        
        if (conflictosDetectados.isEmpty()) {
            return "Asignación de participante exitosa";
        } else {
            // Registrar los conflictos detectados
            gestorConflictos.registrarConflictos(conflictosDetectados);
            return "Conflictos detectados: " + conflictosDetectados.size()
                    + ". Por favor, revise los conflictos antes de continuar.";
        }
    }

    // Actualiza una asignación de participante existente
    public String actualizarAsignacionParticipante(AsignacionParticipante asignacion) {
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        GestorConflictos gestorConflictos = new GestorConflictos();
        
        // Obtener todas las asignaciones existentes del congreso
        ArrayList<AsignacionParticipante> asignacionesExistentes = obtenerAsignacionesParticipantes(
                asignacion.getCongreso().getId());
        
        // Filtrar las asignaciones para excluir la que estamos actualizando
        ArrayList<AsignacionParticipante> asignacionesFiltradas = new ArrayList<>();
        for (AsignacionParticipante a : asignacionesExistentes) {
            if (a.getId() != asignacion.getId()) {
                asignacionesFiltradas.add(a);
            }
        }
        
        // Verificar conflictos con la asignación actualizada
        ArrayList<Conflicto> conflictosDetectados = detectarConflictosParticipante(asignacion, asignacionesFiltradas);
        
        // Actualizar la asignación
        asignacionDAO.actualizarAsignacionParticipante(asignacion);
        
        if (conflictosDetectados.isEmpty()) {
            // Si no hay conflictos, eliminar cualquier conflicto previo de esta asignación
            gestorConflictos.eliminarConflictosPorAsignacion(asignacion.getId());
            return "Actualización de participante exitosa";
        } else {
            // Si hay conflictos, eliminar los antiguos y registrar los nuevos
            gestorConflictos.eliminarConflictosPorAsignacion(asignacion.getId());
            gestorConflictos.registrarConflictos(conflictosDetectados);
            return "Conflictos detectados: " + conflictosDetectados.size()
                    + ". Por favor, revise los conflictos antes de continuar.";
        }
    }

    // Elimina una asignación de participante
    public String eliminarAsignacionParticipante(int idAsignacion) {
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        GestorConflictos gestorConflictos = new GestorConflictos();
        
        try {
            // Eliminar conflictos relacionados con esta asignación
            gestorConflictos.eliminarConflictosPorAsignacion(idAsignacion);
            
            // Eliminar la asignación
            asignacionDAO.eliminarAsignacionParticipante(idAsignacion);
            
            return "Asignación de participante eliminada exitosamente";
        } catch (Exception e) {
            return "Error al eliminar la asignación: " + e.getMessage();
        }
    }
    
    // Detecta conflictos para una asignación de participante
    // Verifica si el participante ya está asignado a la misma actividad
    // Y también verifica conflictos de horario (participante en actividades simultáneas)
    private ArrayList<Conflicto> detectarConflictosParticipante(AsignacionParticipante nuevaAsignacion,
                                                              ArrayList<AsignacionParticipante> asignacionesExistentes) {
        ArrayList<Conflicto> conflictos = new ArrayList<>();
        
        // Obtener las asignaciones de espacio para verificar horarios
        ArrayList<AsignacionEspacio> asignacionesEspacio = obtenerAsignacionesEspacios(
                nuevaAsignacion.getCongreso().getId());
        
        // Buscar la asignación de espacio de la nueva actividad
        AsignacionEspacio asignacionEspacioNueva = null;
        for (AsignacionEspacio asigEsp : asignacionesEspacio) {
            if (asigEsp.getActividad().getId() == nuevaAsignacion.getActividad().getId()) {
                asignacionEspacioNueva = asigEsp;
                break;
            }
        }
        
        for (AsignacionParticipante asignacionExistente : asignacionesExistentes) {
            // 1. Verificar si el mismo participante ya está asignado a la misma actividad
            if (asignacionExistente.getPersona().getId() == nuevaAsignacion.getPersona().getId() &&
                asignacionExistente.getActividad().getId() == nuevaAsignacion.getActividad().getId()) {
                
                String descripcionConflicto = "El participante " + nuevaAsignacion.getPersona().getNombre() + 
                                             " ya está asignado a la actividad '" + 
                                             nuevaAsignacion.getActividad().getNombre() + "'";
                
                Conflicto conflicto = new Conflicto(0, nuevaAsignacion.getCongreso(), descripcionConflicto,
                                                  nuevaAsignacion.getPersona(),
                                                  nuevaAsignacion.getActividad(),
                                                  asignacionExistente.getActividad(),
                                                  nuevaAsignacion.getId());
                conflictos.add(conflicto);
            }
            
            // 2. Verificar conflictos de horario (mismo participante en actividades simultáneas)
            else if (asignacionExistente.getPersona().getId() == nuevaAsignacion.getPersona().getId() && 
                     asignacionEspacioNueva != null) {
                
                // Buscar la asignación de espacio de la actividad existente
                AsignacionEspacio asignacionEspacioExistente = null;
                for (AsignacionEspacio asigEsp : asignacionesEspacio) {
                    if (asigEsp.getActividad().getId() == asignacionExistente.getActividad().getId()) {
                        asignacionEspacioExistente = asigEsp;
                        break;
                    }
                }
                
                // Si ambas actividades tienen asignación de espacio, verificar superposición de horarios
                if (asignacionEspacioExistente != null && 
                    hayConflictoDeHorario(asignacionEspacioNueva, asignacionEspacioExistente)) {
                    
                    String descripcionConflicto = "Conflicto de horario: El participante " + 
                                                 nuevaAsignacion.getPersona().getNombre() + 
                                                 " está asignado a actividades simultáneas: '" + 
                                                 nuevaAsignacion.getActividad().getNombre() + "' y '" + 
                                                 asignacionExistente.getActividad().getNombre() + "'";
                    
                    Conflicto conflicto = new Conflicto(0, nuevaAsignacion.getCongreso(), descripcionConflicto,
                                                      nuevaAsignacion.getPersona(),
                                                      nuevaAsignacion.getActividad(),
                                                      asignacionExistente.getActividad(),
                                                      nuevaAsignacion.getId());
                    conflictos.add(conflicto);
                }
            }
        }
        
        return conflictos;
    }

    // Verifica si dos asignaciones de espacio tienen conflicto de horario
    private boolean hayConflictoDeHorario(AsignacionEspacio asignacion1, AsignacionEspacio asignacion2) {
        // Verificar si es la misma fecha
        if (!asignacion1.getFecha().equals(asignacion2.getFecha())) {
            return false;
        }
        
        // Verificar superposición de horarios
        // Conflicto si: inicio1 < fin2 && inicio2 < fin1
        return asignacion1.getHoraInicio().isBefore(asignacion2.getHoraFin()) && 
               asignacion2.getHoraInicio().isBefore(asignacion1.getHoraFin());
    }
    public ArrayList<Rol> obtenerTodosLosRoles() {
        return rolDAO.obtenerTodosLosRoles();
    }
    
    public Rol obtenerRolPorId(int id) {
        return rolDAO.obtenerRolPorId(id);
    }
    public String registrarRol(String nombreRol) {
        if (nombreRol == null || nombreRol.trim().isEmpty()) {
            return "El nombre del rol no puede estar vacío";
        }
        
        nombreRol = nombreRol.trim();
        
        // Verificar si ya existe
        if (rolDAO.existeRol(nombreRol)) {
            return "Ya existe un rol con ese nombre";
        }
        
        try {
            rolDAO.guardarRol(nombreRol);
            return "Rol registrado exitosamente";
        } catch (Exception e) {
            return "Error al registrar el rol: " + e.getMessage();
        }
    }
    public String eliminarRol(int id) {
        // Verificar que el rol existe
        Rol rol = rolDAO.obtenerRolPorId(id);
        if (rol == null) {
            return "No se encontró el rol con ID: " + id;
        }
        
        try {
            rolDAO.eliminarRol(id);
            return "Rol eliminado exitosamente";
        } catch (Exception e) {
            return "Error al eliminar el rol: " + e.getMessage();
        }
    }
    public ArrayList<AsignacionParticipante> buscarAsignacionesPorParticipante(int idCongreso, int idParticipante) {
        ArrayList<AsignacionParticipante> todasLasAsignaciones = obtenerAsignacionesParticipantes(idCongreso);
        ArrayList<AsignacionParticipante> asignacionesParticipante = new ArrayList<>();
        
        for (AsignacionParticipante asignacion : todasLasAsignaciones) {
            if (asignacion.getPersona().getId() == idParticipante) {
                asignacionesParticipante.add(asignacion);
            }
        }
        
        return asignacionesParticipante;
    }

    public ArrayList<AsignacionParticipante> buscarAsignacionesPorActividad(int idCongreso, int idActividad) {
        ArrayList<AsignacionParticipante> todasLasAsignaciones = obtenerAsignacionesParticipantes(idCongreso);
        ArrayList<AsignacionParticipante> asignacionesActividad = new ArrayList<>();
        
        for (AsignacionParticipante asignacion : todasLasAsignaciones) {
            if (asignacion.getActividad().getId() == idActividad) {
                asignacionesActividad.add(asignacion);
            }
        }
        
        return asignacionesActividad;
    }
}