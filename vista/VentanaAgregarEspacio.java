package vista;
import javax.swing.*;
import controlador.GestorEspacio;
import modelo.Congreso;
import modelo.Espacio;

public class VentanaAgregarEspacio extends JFrame {

    public VentanaAgregarEspacio(Congreso congreso, VentanaGestionCongreso ventanaPadre) {
        setTitle("Registrar Espacio");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Crear el panel principal
        JPanel panel = new JPanel();
        panel.setLayout(null);
        
        // Etiquetas y campos de texto
        JLabel lblNombre = new JLabel("Nombre del Espacio:");
        lblNombre.setBounds(20, 20, 150, 25);
        JTextField txtNombre = new JTextField();
        txtNombre.setBounds(180, 20, 200, 25);
        
        JLabel lblCapacidad = new JLabel("Capacidad:");
        lblCapacidad.setBounds(20, 60, 150, 25);
        JTextField txtCapacidad = new JTextField();
        txtCapacidad.setBounds(180, 60, 200, 25);
        
        // Botón para registrar el espacio
        JButton btnRegistrar = new JButton("Registrar Espacio");
        btnRegistrar.setBounds(120, 100, 150, 30);
        
        // Agregar componentes al panel
        panel.add(lblNombre);
        panel.add(txtNombre);
        panel.add(lblCapacidad);
        panel.add(txtCapacidad);
        panel.add(btnRegistrar);
        
        // Agregar el panel a la ventana
        add(panel);
        
        // Acción del botón Registrar
        btnRegistrar.addActionListener(_ -> {
            String nombre = txtNombre.getText();
            String capacidadStr = txtCapacidad.getText();
            if (!nombre.isEmpty() && !capacidadStr.isEmpty()) {
                try {
                    GestorEspacio gestorEspacio = new GestorEspacio();
                    int capacidad = Integer.parseInt(capacidadStr);
                    int nuevoIDEspacio = gestorEspacio.listarEspacios(congreso).size() + 1; // Asignar un ID único basado en el tamaño actual
                    Espacio nuevoEspacio = new Espacio(nuevoIDEspacio, congreso, nombre, capacidad);
                    gestorEspacio.agregarEspacio(congreso, nuevoEspacio);
                    ventanaPadre.espaciosDisponibles(congreso);
                    dispose(); // Cerrar la ventana después de registrar
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Capacidad debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
    }
    
}
