package modelo;
public class AsignacionParticipante {
    private int id;
    private Congreso congreso;
    private Persona persona;
    private Actividad actividad;
    private String fechaHoraInicio;
    private String fechaHoraFin;
    private Rol rol;

    public AsignacionParticipante(int id, Congreso congreso, Persona persona, Actividad actividad, String fechaHoraInicio, String fechaHoraFin, Rol rol) {
        this.id = id;
        this.congreso = congreso;
        this.persona = persona;
        this.actividad = actividad;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.rol = rol;
    }
    public String toString() {
        return "ID: " + id + ", Congreso: " + congreso.getNombre() +
               ", Persona: " + persona.getNombre() + ", Actividad: " + actividad.getNombre() +
               ", Rol: " + rol.getTipo();
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
    public Persona getPersona() {
        return persona;
    }
    public void setPersona(Persona persona) {
        this.persona = persona;
    }
    public Actividad getActividad() {
        return actividad;
    }
    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }
    public Rol getRol() {
        return rol;
    }
    public void setRol(Rol rol) {
        this.rol = rol;
    }
    public String getFechaHoraInicio() {
        return fechaHoraInicio;
    }
    public String getFechaHoraFin() {
        return fechaHoraFin;
    }
    public void setFechaHoraInicio(String fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }
    public void setFechaHoraFin(String fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }
}
