package controlador;

import java.sql.SQLException;
import java.util.ArrayList;
import dao.ParticipanteDAO;
import modelo.Persona;

public class GestorPersona {
    private ParticipanteDAO participanteDAO;
    
    public GestorPersona() {
        this.participanteDAO = new ParticipanteDAO();
    }
    
    public String registrarParticipante(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre del participante no puede estar vacío";
        }
        
        nombre = nombre.trim();
        
        try {
            // Verificar si ya existe
            if (participanteDAO.existeParticipante(nombre)) {
                return "Ya existe un participante con ese nombre";
            }
            
            Persona nuevoParticipante = new Persona(0, nombre);
            participanteDAO.guardarParticipante(nuevoParticipante);
            return "Participante registrado exitosamente: " + nombre;
        } catch (SQLException e) {
            return "Error al registrar el participante: " + e.getMessage();
        }
    }
    
    public String modificarParticipante(int id, String nuevoNombre) {
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            return "El nombre del participante no puede estar vacío";
        }
        
        nuevoNombre = nuevoNombre.trim();
        
        try {
            // Verificar que el participante existe
            Persona participanteExistente = participanteDAO.obtenerParticipantePorId(id);
            if (participanteExistente == null) {
                return "No se encontró el participante con ID: " + id;
            }
            
            // Verificar si el nuevo nombre ya existe (y no es el mismo participante)
            if (participanteDAO.existeParticipante(nuevoNombre) && 
                !participanteExistente.getNombre().equalsIgnoreCase(nuevoNombre)) {
                return "Ya existe otro participante con ese nombre";
            }
            
            participanteExistente.setNombre(nuevoNombre);
            boolean actualizado = participanteDAO.actualizarParticipante(participanteExistente);
            if (actualizado) {
                return "Participante modificado exitosamente";
            } else {
                return "No se pudo modificar el participante";
            }
        } catch (SQLException e) {
            return "Error al modificar el participante: " + e.getMessage();
        }
    }
    
    public String eliminarParticipante(int id) {
        try {
            // Verificar que el participante existe
            Persona participante = participanteDAO.obtenerParticipantePorId(id);
            if (participante == null) {
                return "No se encontró el participante con ID: " + id;
            }
            
            boolean eliminado = participanteDAO.eliminarParticipante(id);
            if (eliminado) {
                return "Participante eliminado exitosamente";
            } else {
                return "No se pudo eliminar el participante";
            }
        } catch (SQLException e) {
            return "Error al eliminar el participante: " + e.getMessage();
        }
    }

    public ArrayList<Persona> obtenerTodosLosParticipantes() {
        try {
            return participanteDAO.obtenerTodosLosParticipantes();
        } catch (SQLException e) {
            System.err.println("Error al obtener participantes: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public Persona obtenerParticipantePorId(int id) {
        try {
            return participanteDAO.obtenerParticipantePorId(id);
        } catch (SQLException e) {
            System.err.println("Error al obtener participante por ID: " + e.getMessage());
            return null;
        }
    }
    
    public ArrayList<Persona> buscarParticipantesPorNombre(String nombre) {
        ArrayList<Persona> todosLosParticipantes = obtenerTodosLosParticipantes();
        ArrayList<Persona> resultados = new ArrayList<>();
        
        if (nombre == null || nombre.trim().isEmpty()) {
            return todosLosParticipantes;
        }
        
        String nombreBusqueda = nombre.trim().toLowerCase();
        for (Persona participante : todosLosParticipantes) {
            if (participante.getNombre().toLowerCase().contains(nombreBusqueda)) {
                resultados.add(participante);
            }
        }
        
        return resultados;
    }
}
