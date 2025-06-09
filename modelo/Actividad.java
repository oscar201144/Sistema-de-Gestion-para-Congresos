package modelo;
public class Actividad {
    private int id;
    private Congreso congreso;
    private String nombre;
    private String tipo;
    private String fechaHoraInicio;
    private String fechaHoraFin;
    private int duracion; // en minutos
    public Actividad(int id, Congreso congreso, String nombre,String tipo,
     String fechaHoraInicio, String fechaHoraFin, int duracion) {
        this.id = id;
        this.congreso = congreso;
        this.nombre = nombre;
        this.tipo = tipo;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
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
    public String getFechaHoraInicio() {
        return fechaHoraInicio;
    }
    public void setFechaHoraInicio(String fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }
    public String getFechaHoraFin() {
        return fechaHoraFin;
    }
    public void setFechaHoraFin(String fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }
    public int getDuracion() {
        return duracion;
    }
    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }
}
