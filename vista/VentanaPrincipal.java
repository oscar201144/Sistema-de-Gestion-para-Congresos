package vista;
import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    public VentanaPrincipal() {
        JFrame frame = new JFrame("Sistema de Gestión y Planificación de Programas para Congresos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(808, 400);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        JLabel Titulo = new JLabel("Seleccione una opción");
        Titulo.setFont(new Font("Tahoma", Font.BOLD, 18));
        Titulo.setBounds(295, 43, 250, 41);
        panel.add(Titulo);
        
        JButton btnCrearCongreso = new JButton("Crear Congreso");
        btnCrearCongreso.setBounds(289, 110, 223, 44);
        btnCrearCongreso.setFocusPainted(false);
        panel.add(btnCrearCongreso);
        
        JButton btnSeleccionarCongreso = new JButton("Seleccionar Congreso");
        btnSeleccionarCongreso.setBounds(287, 170, 223, 44);
        btnSeleccionarCongreso.setFocusPainted(false);
        panel.add(btnSeleccionarCongreso);
        
        frame.add(panel);
        frame.setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        frame.setVisible(true);
        
        btnCrearCongreso.addActionListener(_ -> {
            // Aquí se abrirá la ventana para crear un congreso
            new VentanaCrearCongreso();
            frame.dispose(); // Cerrar la ventana principal al abrir la de crear congreso
        });
        
        btnSeleccionarCongreso.addActionListener(_ -> {
            // Aquí se abrirá la ventana para seleccionar un congreso
            new VentanaListaCongresos();
            frame.dispose(); // Cerrar la ventana principal al abrir la de lista de congresos
        });
    }
}