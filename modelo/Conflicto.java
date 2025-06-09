package modelo;
public class Conflicto {
    private int id;
    private Congreso congreso;
    private String tipo; // "Persona", "Espacio", etc.
    private String descripcion;
    private Actividad actividad1;
    private Actividad actividad2;
    private Persona persona;    // solo si aplica
    private Espacio espacio;    // solo si aplica
    public Conflicto(int id, Congreso congreso, String tipo, String descripcion,
                     Actividad actividad1, Actividad actividad2,
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
        if (actividad1 != null && actividad2 != null) {
            System.out.println("↳ Entre: " + actividad1.getNombre() + " y " +
            actividad2.getNombre());
        }
        if (persona != null) {
            System.out.println("↳ Persona involucrada: " + persona.getNombre());
        }
        if (espacio != null) {
            System.out.println("↳ Espacio en conflicto: " + espacio.getNombre());
        }
    }
}

