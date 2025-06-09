package controlador;
import modelo.Congreso;
import java.util.ArrayList;
import dao.CongresoDAO;

public class GestorCongreso {
    private ArrayList<Congreso> congresos;

    public GestorCongreso() {
        this.congresos = new ArrayList<>();
    }
    public void agregarCongreso(Congreso congreso) {
        if (congreso != null) {
            this.congresos.add(congreso);
            CongresoDAO congresoDAO = new CongresoDAO();
            congresoDAO.guardarCongreso(congreso); // Guardar el congreso en la base de datos

        }
    }
    public ArrayList<Congreso> getCongresos() {
        
        CongresoDAO congresoDAO = new CongresoDAO();
        congresoDAO.listaCongresos(); // Cargar los congresos desde la base de datos
        return this.congresos;
    }
    public Congreso buscarCongresoPorNombre(String nombre) {
        for (Congreso congreso : this.congresos) {
            if (congreso.getNombre().equalsIgnoreCase(nombre)) {
                return congreso;
            }
        }
        return null; // Si no se encuentra el congreso
    }

}
