package vista;
import javax.swing.*;
import controlador.GestorCongreso;
import modelo.Congreso;

public class VentanaCrearCongreso extends JFrame {
    private GestorCongreso gestorCongreso;
    public VentanaCrearCongreso() {
        this.gestorCongreso = new GestorCongreso();
        JFrame frame = new JFrame("Crear Congreso");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(10, 20, 80, 25);
        panel.add(lblNombre);
        JTextField txtNombre = new JTextField();
        txtNombre.setBounds(100, 20, 200, 25);
        panel.add(txtNombre);
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(100, 60, 100, 30);
        panel.add(btnGuardar);
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(210, 60, 100, 30);
        panel.add(btnCancelar);
        panel.add(lblNombre);
        panel.add(txtNombre);
        panel.add(btnGuardar);
        panel.add(btnCancelar);
        frame.add(panel);
        frame.setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        frame.setResizable(false); // Evitar que la ventana se pueda redimensionar
        frame.setVisible(true);
        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                new VentanaError("Por favor, complete todos los campos.");
                return;
            }
            int nuevoId = gestorCongreso.getCongresos().size() + 1;
            Congreso nuevoCongreso = new Congreso(nuevoId, nombre);
            try {
                gestorCongreso.agregarCongreso(nuevoCongreso);
            } catch (Exception ex) {
                new VentanaError("Error al guardar el congreso: " + ex.getMessage());
                return;
            }
            frame.dispose(); // Cerrar la ventana después de guardar
            new VentanaGestionCongreso(nuevoCongreso); // Abrir la ventana de gestión del congreso recién creado
        });
        btnCancelar.addActionListener(e -> {
            frame.dispose(); // Cerrar la ventana sin guardar
            new VentanaPrincipal(); // Volver a la ventana principal
        });
    }
    
}
