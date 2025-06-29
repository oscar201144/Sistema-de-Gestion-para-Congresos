package vista;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import modelo.Congreso;
import controlador.GestorCongreso;

public class VentanaListaCongresos extends JFrame {
    private JList<Congreso> listaCongresos;
    private GestorCongreso gestorCongreso;

    public VentanaListaCongresos() {
        this.gestorCongreso = new GestorCongreso();
        
        setTitle("Lista de Congresos");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear el panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Obtener la lista de congresos a través del controlador
        ArrayList<Congreso> congresos = gestorCongreso.getCongresos();
        
        // Crear la lista de congresos
        this.listaCongresos = new JList<>(congresos.toArray(new Congreso[0]));
        JScrollPane scrollPane = new JScrollPane(this.listaCongresos);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botón para seleccionar un congreso
        JButton btnSeleccionar = new JButton("Seleccionar Congreso");
        btnSeleccionar.addActionListener(_ -> {
            Congreso congresoSeleccionado = this.listaCongresos.getSelectedValue();
            if (congresoSeleccionado != null) {
                    JOptionPane.showMessageDialog(VentanaListaCongresos.this,
                            "Has seleccionado: " + congresoSeleccionado.getNombre());
                    new VentanaGestionCongreso(congresoSeleccionado);
                    dispose(); // Cerrar la ventana de lista de congresos
                    
                } else {
                    JOptionPane.showMessageDialog(VentanaListaCongresos.this,
                            "Por favor, selecciona un congreso.");
                }
            });
        panel.add(btnSeleccionar, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }
}
