package vista;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controlador.GestorActividades;
import controlador.GestorCongreso;
import controlador.GestorAsignaciones;
import modelo.AsignacionEspacio;
import modelo.Congreso;
public class VentanaGestionCongreso extends JFrame {
    private GestorCongreso gestorCongreso;
    private GestorActividades gestorActividades;
    private Congreso congreso;
    public VentanaGestionCongreso(Congreso congreso) {
        this.congreso = congreso;
        gestorCongreso = new GestorCongreso();
        gestorActividades = new GestorActividades();
        setTitle("Gestión de Congreso " + congreso.getNombre());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        // Crear el panel principal
        JPanel panel = new JPanel();
        panel.setLayout(null);
        // Crear un botón para agregar una actividad
        JButton btnParticipantes = new JButton("Registrar Participante");
        btnParticipantes.setBounds(500, 10, 200, 40);
        JButton btnActividades = new JButton("Registrar Actividades");
        btnActividades.setBounds(300, 10, 200, 40);
        JButton btnEspacios = new JButton("Registrar Espacios");
        btnEspacios.setBounds(100, 10, 200, 40);
        JButton btnAsignar = new JButton("Asignar");
        btnAsignar.setBounds(550, 100, 150, 40);
        JComboBox<String> actividadesComboBox = new JComboBox<>();
        actividadesComboBox.addItem("No se han seleccionado actividades");
        actividadesComboBox.setBounds(50, 100, 300, 40);
        JComboBox<String> espaciosComboBox = new JComboBox<>();
        espaciosComboBox.setBounds(350, 100, 200, 40);
        espaciosComboBox.addItem("No se han seleccionado espacios");
        JLabel textTabla = new JLabel("Actividades y Espacios Asignados");
        textTabla.setBounds(50, 150, 300, 30);
        JTable tabla = new JTable();
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBounds(50, 180, 700, 300);
        // Cargar las actividades del congreso en el combo box
        mostrarAsignacionesEspacio(congreso);

        // Agregar los componentes al panel
        panel.add(scrollPane);
        panel.add(btnParticipantes);
        panel.add(btnActividades);
        panel.add(actividadesComboBox);
        panel.add(btnEspacios);
        panel.add(espaciosComboBox);
        panel.add(btnAsignar);
        add(panel);
        setVisible(true);

    }

    public void mostrarAsignacionesEspacio(Congreso congreso) {

        String[] columnas = {"Actividad", "Espacio", "Tipo", "Hora Inicio", "Hora Fin"};
        GestorAsignaciones gestorAsignaciones = new GestorAsignaciones();
        ArrayList<AsignacionEspacio> asignaciones = gestorAsignaciones.GestorAsignacionesEspacios(congreso.getId());
        if (asignaciones.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay asignaciones de espacios para este congreso.");
        } else {
            String[][] datos = new String[asignaciones.size()][5];
            for (int i = 0; i < asignaciones.size(); i++) {
                AsignacionEspacio asignacion = asignaciones.get(i);
                datos[i][0] = asignacion.getActividad().getNombre();
                datos[i][1] = asignacion.getEspacio().getNombre();
                datos[i][2] = asignacion.getActividad().getTipo();
                datos[i][3] = asignacion.getActividad().getFechaHoraInicio();
                datos[i][4] = asignacion.getActividad().getFechaHoraFin();
            }
            // Crear el modelo de la tabla y asignarlo
            DefaultTableModel modelo = new DefaultTableModel(datos, columnas);
            JTable tabla = new JTable(modelo);
            JScrollPane scrollPane = new JScrollPane(tabla);
            scrollPane.setBounds(50, 180, 700, 300);
            // Limpiar el panel y agregar la tabla
            JPanel panel = (JPanel) getContentPane();
            panel.removeAll();
            panel.add(scrollPane);
            panel.revalidate();
            panel.repaint();
        }
    }

    public static void main(String[] args) {
        // Crear un congreso de ejemplo
        Congreso congreso = new Congreso(1, "Congreso de Ejemplo");
        // Abrir la ventana de gestión del congreso
        new VentanaGestionCongreso(congreso);
    }
}
