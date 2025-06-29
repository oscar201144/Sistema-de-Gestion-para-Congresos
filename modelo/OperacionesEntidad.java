package modelo;

// Interfaz que define operaciones básicas para entidades del sistema
public interface OperacionesEntidad {
    // Valida que los datos de la entidad sean correctos
    boolean validarDatos();
    
    // Obtiene un resumen de la entidad para mostrar en interfaces
    String getResumen();

    // Verifica si la entidad está completa y lista para usar
    boolean estaCompleta();
}
