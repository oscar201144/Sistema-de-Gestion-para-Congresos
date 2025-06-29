package modelo;
public class Actividad implements OperacionesEntidad {
    private int id;
    private Congreso congreso;
    private String nombre;
    private String tipo;
    private int duracion; // en minutos
    
    public Actividad(int id, Congreso congreso, String nombre, String tipo, int duracion) {
        this.id = id;
        this.congreso = congreso;
        this.nombre = nombre;
        this.tipo = tipo;
        this.duracion = duracion;
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
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public int getDuracion() {
        return duracion;
    }
    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }
    
    // Implementación de la interfaz OperacionesEntidad
    @Override
    public boolean validarDatos() {
        return nombre != null && !nombre.trim().isEmpty() && 
               tipo != null && !tipo.trim().isEmpty() && 
               duracion > 0 && congreso != null;
    }
    
    @Override
    public String getResumen() {
        return String.format("%s (%s) - %d min", nombre, tipo, duracion);
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
