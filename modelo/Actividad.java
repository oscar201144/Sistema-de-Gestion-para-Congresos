package modelo;
public class Actividad {
    private int id;
    private Congreso congreso;
    private String nombre;
    private String tipo;
    private String duracion; // en minutos
    public Actividad(int id, Congreso congreso, String nombre,String tipo,
     String duracion) {
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
    public String getDuracion() {
        return duracion;
    }
    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
    @Override
    public String toString() {
        return nombre;
    }

}
