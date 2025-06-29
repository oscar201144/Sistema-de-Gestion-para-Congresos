package modelo;
public class Espacio implements OperacionesEntidad {
    private int id;
    private Congreso congreso;
    private String nombre;
    private int capacidad;
    public Espacio(int id, Congreso congreso, String nombre, int capacidad) {
        this.id = id;
        this.congreso = congreso;
        this.nombre = nombre;
        this.capacidad = capacidad;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getCapacidad() {
        return capacidad;
    }
    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }
    public Congreso getCongreso() {
        return congreso;
    }
    public void setCongreso(Congreso congreso) {
        this.congreso = congreso;
    }
    
    // Implementación de la interfaz OperacionesEntidad
    @Override
    public boolean validarDatos() {
        return nombre != null && !nombre.trim().isEmpty() && 
               capacidad > 0 && congreso != null;
    }
    
    @Override
    public String getResumen() {
        return String.format("%s (Capacidad: %d)", nombre, capacidad);
    }
    
    @Override
    public boolean estaCompleta() {
        return id > 0 && validarDatos();
    }
    
    @Override
    public String toString() {
        return getResumen(); // Usa el método de la interfaz para mayor información
    }
}
