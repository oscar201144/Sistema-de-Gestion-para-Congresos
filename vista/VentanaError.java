package vista;

public class VentanaError {
    public VentanaError(String mensaje) {
        javax.swing.JOptionPane.showMessageDialog(null, mensaje, "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}
