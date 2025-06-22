package modelo;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;

public class AsignacionEspacio {
    private int id;
    private Congreso congreso;
    private Actividad actividad;
    private Espacio espacio;
    private LocalDate fecha;
    private LocalTime hora_inicio;
    private LocalTime hora_fin;

    public AsignacionEspacio(int id, Congreso congreso, Espacio espacio, Actividad actividad, LocalDate fecha, LocalTime hora_inicio, LocalTime hora_fin) {
        this.id = id;
        this.congreso = congreso;
        this.espacio = espacio;
        this.actividad = actividad;
        this.fecha = fecha;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
    }
    public String toString() {
        return "ID: " + id + ", Congreso: " + congreso.getNombre() +
               ", Espacio: " + espacio.getNombre() + ", Actividad: " + actividad.getNombre() +
               ", Fecha: " + fecha + ", Hora de Inicio: " + hora_inicio + ", Hora de Fin: " + hora_fin;
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
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public LocalTime getHoraInicio() {
        return hora_inicio;
    }
    public void setHoraInicio(LocalTime hora_inicio) {
        this.hora_inicio = hora_inicio;
    }
    public LocalTime getHoraFin() {
        return hora_fin;
    }
    public void setHoraFin(LocalTime hora_fin) {
        this.hora_fin = hora_fin;
    }
}