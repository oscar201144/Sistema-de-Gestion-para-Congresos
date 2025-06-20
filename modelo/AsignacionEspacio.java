package modelo;
public class AsignacionEspacio {
    private int id;
    private Congreso congreso;
    private Actividad actividad;
    private Espacio espacio;
    private String fechaHoraInicio;
    private String fechaHoraFin;
    public AsignacionEspacio(int id, Congreso congreso, Espacio espacio, Actividad actividad, String fechaHoraInicio, String fechaHoraFin) {
        this.id = id;
        this.congreso = congreso;
        this.espacio = espacio;
        this.actividad = actividad;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
    }
    public String toString() {
        return "ID: " + id + ", Congreso: " + congreso.getNombre() +
               ", Espacio: " + espacio.getNombre() + ", Actividad: " + actividad.getNombre() +
               ", Fecha y Hora de Inicio: " + fechaHoraInicio + ", Fecha y Hora de Fin: " + fechaHoraFin;
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
    public Espacio getEspacio() {
        return espacio;
    }
    public void setEspacio(Espacio espacio) {
        this.espacio = espacio;
    }
    public Actividad getActividad() {
        return actividad;
    }
    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
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
}