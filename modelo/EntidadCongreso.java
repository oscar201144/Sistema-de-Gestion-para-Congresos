package modelo;

/**
 * Clase abstracta base para todas las entidades relacionadas con el congreso
 */
public abstract class EntidadCongreso implements OperacionesEntidad {
    protected int id;
    protected Congreso congreso;
    
    public EntidadCongreso(int id, Congreso congreso) {
        this.id = id;
        this.congreso = congreso;
    }
    
    // Getters y setters comunes
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
    
    /**
     * Método abstracto que cada entidad debe implementar
     * para definir sus reglas específicas de validación
     */
    @Override
    public abstract boolean validarDatos();
    
    /**
     * Método abstracto para obtener información resumida
     */
    @Override
    public abstract String getResumen();
    
    /**
     * Implementación común para verificar completitud básica
     */
    @Override
    public boolean estaCompleta() {
        return id > 0 && congreso != null;
    }
    
    /**
     * Método común para verificar si pertenece al congreso especificado
     */
    public boolean perteneceAlCongreso(Congreso congreso) {
        return this.congreso != null && this.congreso.getId() == congreso.getId();
    }
}
