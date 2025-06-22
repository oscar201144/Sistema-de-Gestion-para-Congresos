package vista;

import javax.swing.*;
import java.util.ArrayList;
import controlador.GestorEspacio;
import modelo.Espacio;
import modelo.Congreso;

public class VentanaEliminarEspacio extends JFrame {
    private GestorEspacio gestorEspacio;
    private JComboBox<Espacio> espaciosComboBox;

    public VentanaEliminarEspacio(Congreso congreso, VentanaGestionCongreso ventanaPadre) {
        this.gestorEspacio = new GestorEspacio();
        setTitle("Eliminar Espacio");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblEspacio = new JLabel("Seleccione el Espacio a Eliminar:");
        lblEspacio.setBounds(10, 20, 250, 25);
        panel.add(lblEspacio);

        espaciosComboBox = new JComboBox<>();
        espaciosComboBox.setBounds(10, 50, 360, 25);
        cargarEspacios(congreso);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(100, 100, 100, 30);
        panel.add(btnEliminar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(210, 100, 100, 30);
        panel.add(btnCancelar);

        btnCancelar.addActionListener(_ -> dispose());

        panel.add(espaciosComboBox);

        add(panel);
        

        btnEliminar.addActionListener(_ -> {
            Espacio espacioSeleccionado = (Espacio) espaciosComboBox.getSelectedItem();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de que desea eliminar el espacio '" + espacioSeleccionado.getNombre() + "'?",
                    "Confirmación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean eliminado = gestorEspacio.eliminarEspacio(espacioSeleccionado.getId());
                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Espacio eliminado exitosamente.", "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    cargarEspacios(congreso);
                    ventanaPadre.espaciosDisponibles(congreso);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el espacio.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void cargarEspacios(Congreso congreso) {
        espaciosComboBox.removeAllItems();
        GestorEspacio gestorEspacio = new GestorEspacio();
        ArrayList<Espacio> espacios = gestorEspacio.listarEspacios(congreso);
        if (espacios.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay espacios disponibles para eliminar.", "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            setVisible(false);
            dispose();
            return;
        } else {
                    setVisible(true);
        }
        for (Espacio espacio : espacios) {
            espaciosComboBox.addItem(espacio);
        }
    }
    
}
