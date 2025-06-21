package modelo;
public class Espacio {
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
    @Override
    public String toString() {
        return nombre;
    }
}
