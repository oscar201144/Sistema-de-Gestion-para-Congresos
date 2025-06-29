package controlador;

import java.sql.SQLException;
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
        try {
            ArrayList<AsignacionEspacio> asignaciones = asignacionDAO.obtenerAsignacionesEspacios(idCongreso);
            return asignaciones != null ? asignaciones : new ArrayList<>();
        } catch (SQLException e) {
            System.err.println("Error al obtener asignaciones de espacios: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public String obtenerHoraTempranaDisponible(Congreso congreso, String fechaSeleccionada) {
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        try {
            String horaTemprana = asignacionDAO.obtenerHoraTempranaDisponible(congreso.getId(), fechaSeleccionada);
            if (horaTemprana == null) {
                return String.valueOf(congreso.getHoraInicio());
            } else {
                return horaTemprana;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener hora temprana disponible: " + e.getMessage());
            return String.valueOf(congreso.getHoraInicio());
        }
    }

    public String procesarNuevaAsignacionEspacios(AsignacionEspacio nuevaAsignacion) {
        ArrayList<AsignacionEspacio> asignacionesExistentes = obtenerAsignacionesEspacios(
                nuevaAsignacion.getCongreso().getId());
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        GestorConflictos gestorConflictos = new GestorConflictos();
        
        try {
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
                String resultadoConflictos = gestorConflictos.registrarConflictos(conflictosDetectados);
                return "Conflictos detectados: " + conflictosDetectados.size()
                        + ". " + resultadoConflictos;
            }
        } catch (SQLException e) {
            return "Error al procesar asignación de espacios: " + e.getMessage();
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

        try {
            // Actualizar la asignación
            boolean actualizada = asignacionDAO.actualizarAsignacionEspacio(asignacion);
            
            if (!actualizada) {
                return "Error al actualizar la asignación";
            }

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
        } catch (SQLException e) {
            return "Error al actualizar asignación de espacio: " + e.getMessage();
        }
    }

    public String forzarActualizacionAsignacion(AsignacionEspacio asignacion) {
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        GestorConflictos gestorConflictos = new GestorConflictos();
        
        try {
            // Actualizar la asignación
            boolean actualizada = asignacionDAO.actualizarAsignacionEspacio(asignacion);
            
            if (!actualizada) {
                return "Error al actualizar la asignación";
            }
            
            // Eliminar todos los conflictos relacionados con esta asignación
            gestorConflictos.eliminarConflictosPorAsignacion(asignacion.getId());
            
            return "Actualización forzada exitosa";
        } catch (SQLException e) {
            return "Error al forzar actualización: " + e.getMessage();
        }
    }

    public boolean eliminarAsignacionEspacio(int idAsignacion) {
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        try {
            return asignacionDAO.eliminarAsignacionEspacio(idAsignacion);
        } catch (SQLException e) {
            System.err.println("Error al eliminar asignación de espacio: " + e.getMessage());
            return false;
        }
    }
    
    //Obtiene todas las asignaciones de participantes para un congreso
    public ArrayList<AsignacionParticipante> obtenerAsignacionesParticipantes(int idCongreso) {
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        try {
            ArrayList<AsignacionParticipante> asignaciones = asignacionDAO.obtenerAsignacionesParticipantes(idCongreso);
            return asignaciones != null ? asignaciones : new ArrayList<>();
        } catch (SQLException e) {
            System.err.println("Error al obtener asignaciones de participantes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Procesa una nueva asignación de participante con control de conflictos
    public String procesarNuevaAsignacionParticipante(AsignacionParticipante nuevaAsignacion) {
        ArrayList<AsignacionParticipante> asignacionesExistentes = obtenerAsignacionesParticipantes(
                nuevaAsignacion.getCongreso().getId());
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        GestorConflictos gestorConflictos = new GestorConflictos();
        
        try {
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
        } catch (SQLException e) {
            return "Error al procesar asignación de participante: " + e.getMessage();
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
        
        try {
            // Actualizar la asignación
            boolean actualizada = asignacionDAO.actualizarAsignacionParticipante(asignacion);
            
            if (!actualizada) {
                return "Error al actualizar la asignación de participante";
            }
            
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
        } catch (SQLException e) {
            return "Error al actualizar asignación de participante: " + e.getMessage();
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
        try {
            return rolDAO.obtenerTodosLosRoles();
        } catch (SQLException e) {
            System.err.println("Error al obtener roles: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public Rol obtenerRolPorId(int id) {
        try {
            return rolDAO.obtenerRolPorId(id);
        } catch (SQLException e) {
            System.err.println("Error al obtener rol por ID: " + e.getMessage());
            return null;
        }
    }
    public String registrarRol(String nombreRol) {
        if (nombreRol == null || nombreRol.trim().isEmpty()) {
            return "El nombre del rol no puede estar vacío";
        }
        
        nombreRol = nombreRol.trim();
        
        try {
            // Verificar si ya existe
            if (rolDAO.existeRol(nombreRol)) {
                return "Ya existe un rol con ese nombre";
            }
            
            rolDAO.guardarRol(nombreRol);
            return "Rol registrado exitosamente: " + nombreRol;
        } catch (SQLException e) {
            return "Error al registrar el rol: " + e.getMessage();
        }
    }
    public String eliminarRol(int id) {
        try {
            // Verificar que el rol existe
            Rol rol = rolDAO.obtenerRolPorId(id);
            if (rol == null) {
                return "No se encontró el rol con ID: " + id;
            }
            
            boolean eliminado = rolDAO.eliminarRol(id);
            if (eliminado) {
                return "Rol eliminado exitosamente";
            } else {
                return "No se pudo eliminar el rol";
            }
        } catch (SQLException e) {
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