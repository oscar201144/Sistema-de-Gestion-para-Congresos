package vista;
import javax.swing.*;
import controlador.GestorActividades;
import modelo.Actividad;
import modelo.Congreso;

public class VentanaAgregarActividad extends JFrame {
    private GestorActividades gestorActividades;

    public VentanaAgregarActividad(Congreso congreso, VentanaGestionCongreso ventanaPadre) {
        this.gestorActividades = new GestorActividades();
        setTitle("Agregar Actividad al Congreso " + congreso.getNombre());
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblNombre = new JLabel("Nombre de la Actividad:");
        lblNombre.setBounds(10, 20, 150, 25);
        panel.add(lblNombre);

        JTextField txtNombre = new JTextField();
        txtNombre.setBounds(170, 20, 200, 25);
        panel.add(txtNombre);

        JLabel lblTipo = new JLabel("Tipo de Actividad:");
        lblTipo.setBounds(10, 60, 150, 25);
        panel.add(lblTipo);

        JTextField txtTipo = new JTextField();
        txtTipo.setBounds(170, 60, 200, 25);
        panel.add(txtTipo);

        JLabel lblDuracion = new JLabel("Duración (min):");
        lblDuracion.setBounds(10, 100, 150, 25);
        panel.add(lblDuracion);

        JTextField txtDuracion = new JTextField();
        txtDuracion.setBounds(170, 100, 200, 25);
        panel.add(txtDuracion);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(100, 140, 100, 30);
        panel.add(btnGuardar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(210, 140, 100, 30);
        panel.add(btnCancelar);

        btnGuardar.addActionListener(_ -> {
            String nombre = txtNombre.getText().trim();
            String tipo = txtTipo.getText().trim();
            String duracion = txtDuracion.getText().trim();

            if (nombre.isEmpty() || tipo.isEmpty() || duracion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int nuevoId = gestorActividades.getActividades().size() + 1;
            Actividad nuevaActividad = new Actividad(nuevoId, congreso, nombre, tipo, duracion);
            try {
                gestorActividades.agregarActividad(congreso, nuevaActividad);
                ventanaPadre.actividadesDisponibles(congreso); // Actualizar la lista de actividades en la ventana principal
                dispose(); // Cerrar la ventana después de guardar
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar la actividad: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(_ -> dispose());

        add(panel);
        setVisible(true);
    }


    public static void main(String[] args) {
        // Para probar la ventana de agregar actividad
        Congreso congreso = new Congreso(1, "Congreso de Prueba", 
                java.time.LocalDate.now(), 
                java.time.LocalTime.now(), 
                java.time.LocalDate.now().plusDays(1), 
                java.time.LocalTime.now().plusHours(2));
        new VentanaAgregarActividad(congreso, null);
    }
}