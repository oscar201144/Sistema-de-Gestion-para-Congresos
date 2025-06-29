package modelo;

public class Conflicto {
    private int id;
    private Congreso congreso;
    private String tipo; // "Horario", "Persona", "Espacio"
    private String descripcion;
    private Actividad actividad1;
    private Actividad actividad2;
    private Persona persona;    // solo si aplica
    private Espacio espacio;    // solo si aplica

    // Constructor para conflictos generales (con todos los campos)
    public Conflicto(int id, Congreso congreso, String tipo, 
                     String descripcion, Actividad actividad1, Actividad actividad2, 
                     Persona persona, Espacio espacio) {
        this.id = id;
        this.congreso = congreso;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.actividad1 = actividad1;
        this.actividad2 = actividad2;
        this.persona = persona;
        this.espacio = espacio;
    }

    // Constructor para conflictos de HORARIO (dos actividades superpuestas en mismo lugar)
    public Conflicto(int id, Congreso congreso, String descripcion, 
                     Actividad actividad1, Actividad actividad2, Espacio espacio) {
        this.id = id;
        this.congreso = congreso;
        this.tipo = "Horario";
        this.descripcion = descripcion;
        this.actividad1 = actividad1;
        this.actividad2 = actividad2;
        this.espacio = espacio;
        this.persona = null;
    }
    
    // Constructor para conflictos de PERSONA (persona asignada a dos actividades simultáneas)
    public Conflicto(int id, Congreso congreso, String descripcion, 
                     Persona persona, Actividad actividad1, Actividad actividad2) {
        this.id = id;
        this.congreso = congreso;
        this.tipo = "Persona";
        this.descripcion = descripcion;
        this.persona = persona;
        this.actividad1 = actividad1;
        this.actividad2 = actividad2;
        this.espacio = null;
    }
    
    // Constructor para conflictos de ESPACIO (actividad asignada múltiples veces al mismo espacio)
    public Conflicto(int id, Congreso congreso, String descripcion, 
                     Espacio espacio, Actividad actividad) {
        this.id = id;
        this.congreso = congreso;
        this.tipo = "Espacio";
        this.descripcion = descripcion;
        this.espacio = espacio;
        this.actividad1 = actividad;
        this.actividad2 = null;
        this.persona = null;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Congreso getCongreso() {
        return congreso;
    }
    public void setCongreso(Congreso congreso) {
        this.congreso = congreso;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Actividad getActividad1() {
        return actividad1;
    }
    public void setActividad1(Actividad actividad1) {
        this.actividad1 = actividad1;
    }
    public Actividad getActividad2() {
        return actividad2;
    }
    public void setActividad2(Actividad actividad2) {
        this.actividad2 = actividad2;
    }
    public Persona getPersona() {
        return persona;
    }
    public void setPersona(Persona persona) {
        this.persona = persona;
    }
    public Espacio getEspacio() {
        return espacio;
    }
    public void setEspacio(Espacio espacio) {
        this.espacio = espacio;
    }
    
    public void mostrarDetalle() {
        System.out.println("Conflicto (" + tipo + "): " + descripcion);
        
        switch (tipo) {
            case "Horario":
                System.out.println("↳ Actividades superpuestas: " + actividad1.getNombre() + 
                                 " y " + actividad2.getNombre());
                System.out.println("↳ En el espacio: " + espacio.getNombre());
                break;
                
            case "Persona":
                System.out.println("↳ Persona: " + persona.getNombre());
                System.out.println("↳ Asignada simultáneamente a: " + actividad1.getNombre() + 
                                 " y " + actividad2.getNombre());
                break;
                
            case "Espacio":
                System.out.println("↳ Espacio: " + espacio.getNombre());
                System.out.println("↳ Actividad con múltiples asignaciones: " + actividad1.getNombre());
                break;
        }
    }
}

