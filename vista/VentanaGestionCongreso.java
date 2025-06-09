package vista;
import javax.swing.*;
import controlador.GestorCongreso;
import modelo.Congreso;
public class VentanaGestionCongreso extends JFrame {
    private GestorCongreso gestorCongreso;
    private Congreso congreso;
    public VentanaGestionCongreso(Congreso congreso) {
        this.congreso = congreso;
        gestorCongreso = new GestorCongreso();
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
        // Configurar el panel
        String[] columnNames = { "Actividad", "Espacio", "Fecha y Hora" };
        Object[][] datos = {
                { "Charla de apertura", "Auditorio", "09:00 - 10:00" },
                { "Taller de Java", "Sala 1", "10:30 - 12:00" },
                { "Panel de expertos", "Sala 2", "12:30 - 14:00" }
        };
        tabla.setModel(new javax.swing.table.DefaultTableModel(datos, columnNames));
        tabla.setFillsViewportHeight(true);
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

    public static void main(String[] args) {
        // Crear un congreso de ejemplo
        Congreso congreso = new Congreso(1, "Congreso de Ejemplo");
        // Abrir la ventana de gestión del congreso
        new VentanaGestionCongreso(congreso);
    }
}
