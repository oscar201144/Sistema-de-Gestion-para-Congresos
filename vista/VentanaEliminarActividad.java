package vista;

import java.util.ArrayList;

import javax.swing.*;
import controlador.GestorActividades;
import modelo.Actividad;
import modelo.Congreso;

public class VentanaEliminarActividad extends JFrame {
    private Congreso congreso;

    public VentanaEliminarActividad(Congreso congreso, VentanaGestionCongreso ventanaGestionCongreso) {
        this.congreso = congreso;
        setTitle("Eliminar Actividad del Congreso " + congreso.getNombre());
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblActividad = new JLabel("Seleccione la Actividad a Eliminar:");
        lblActividad.setBounds(10, 20, 250, 25);
        panel.add(lblActividad);

        JComboBox<Actividad> actividadesComboBox = new JComboBox<>();
        actividadesComboBox.setBounds(10, 50, 360, 25);
        cargarActividades(actividadesComboBox);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(100, 100, 100, 30);
        panel.add(btnEliminar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(210, 100, 100, 30);
        panel.add(btnCancelar);

        btnCancelar.addActionListener(_ -> dispose());

        panel.add(actividadesComboBox);

        add(panel);

        btnEliminar.addActionListener(_ -> {
            Actividad actividadSeleccionada = (Actividad) actividadesComboBox.getSelectedItem();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de que desea eliminar la actividad '" + actividadSeleccionada.getNombre() + "'?",
                    "Confirmación", JOptionPane.YES_NO_OPTION);
            // Aquí puedes agregar la lógica para eliminar la actividad si el usuario
            // confirma
            if (confirm == JOptionPane.YES_OPTION) {
                GestorActividades gestorActividades = new GestorActividades();
                boolean eliminado = gestorActividades.eliminarActividad(congreso, actividadSeleccionada.getId());
                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Actividad eliminada exitosamente.", "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    ventanaGestionCongreso.actividadesDisponibles(congreso);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error al eliminar la actividad. Por favor, inténtelo de nuevo.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    private void cargarActividades(JComboBox<Actividad> comboBox) {
        comboBox.removeAllItems();
        GestorActividades gestorActividades = new GestorActividades();
        ArrayList<Actividad> actividades = gestorActividades.listarActividades(congreso);
        if (actividades.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay actividades registradas en este congreso.", "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            comboBox.setEnabled(false);
        } else {
            comboBox.removeAllItems();
            for (Actividad actividad : actividades) {
                comboBox.addItem(actividad);
            }
            setVisible(true);
        }
    }
}
