package vista;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import modelo.Congreso;
import dao.CongresoDAO;

public class VentanaListaCongresos extends JFrame {
    private JList<Congreso> listaCongresos;

    CongresoDAO congresoDAO = new CongresoDAO();
    ArrayList<Congreso> listaCongresosDAO = congresoDAO.listaCongresos();

    public VentanaListaCongresos() {
        setTitle("Lista de Congresos");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear el panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Crear la lista de congresos
        listaCongresos = new JList<>(listaCongresosDAO.toArray(new Congreso[0]));
        JScrollPane scrollPane = new JScrollPane(listaCongresos);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botón para seleccionar un congreso
        JButton btnSeleccionar = new JButton("Seleccionar Congreso");
        btnSeleccionar.addActionListener(e -> {
            Congreso congresoSeleccionado = listaCongresos.getSelectedValue();
            if (congresoSeleccionado != null) {
                    JOptionPane.showMessageDialog(VentanaListaCongresos.this,
                            "Has seleccionado: " + congresoSeleccionado.getNombre());
                    VentanaGestionCongreso ventanaGestion = new VentanaGestionCongreso(congresoSeleccionado);
                    ventanaGestion.setVisible(true);
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
