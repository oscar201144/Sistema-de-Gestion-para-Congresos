package vista;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controlador.GestorActividades;
import controlador.GestorAsignaciones;
import modelo.AsignacionEspacio;
import modelo.Congreso;
import modelo.Actividad;

public class VentanaGestionCongreso extends JFrame {
    private GestorActividades gestorActividades;
    private JComboBox<Actividad> actividadesComboBox;
    public VentanaGestionCongreso(Congreso congreso) {
        gestorActividades = new GestorActividades();
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
        JButton btnEliminarParticipante = new JButton("Eliminar Participante");
        btnEliminarParticipante.setBounds(500, 50, 200, 40);

        JButton btnActividades = new JButton("Registrar Actividades");
        btnActividades.setBounds(300, 10, 200, 40);
        JButton btnEliminarActividad = new JButton("Eliminar Actividad");
        btnEliminarActividad.setBounds(300, 50, 200, 40);

        JButton btnEspacios = new JButton("Registrar Espacios");
        btnEspacios.setBounds(100, 10, 200, 40);
        JButton btnEliminarEspacio = new JButton("Eliminar Espacio");
        btnEliminarEspacio.setBounds(100, 50, 200, 40);

        JButton btnAsignar = new JButton("Asignar");
        btnAsignar.setBounds(550, 120, 150, 40);

        JLabel lblActividadesDisponibles = new JLabel("Actividades Disponibles:");
        lblActividadesDisponibles.setBounds(50, 100, 200, 20);
        actividadesComboBox = new JComboBox<>();
        actividadesComboBox.setBounds(50, 120, 300, 40);
        actividadesDisponibles(congreso);

        JLabel lblEspaciosDisponibles = new JLabel("Espacios Disponibles:");
        lblEspaciosDisponibles.setBounds(350, 100, 200, 20);
        JComboBox<String> espaciosComboBox = new JComboBox<>();
        espaciosComboBox.setBounds(350, 120, 200, 40);
        // espaciosDisponibles(congreso);

        mostrarAsignacionesEspacio(congreso);
        // Agregar los componentes al panel
        panel.add(btnEliminarParticipante);
        panel.add(btnEliminarEspacio);
        panel.add(lblActividadesDisponibles);
        panel.add(lblEspaciosDisponibles);
        panel.add(btnEliminarActividad);
        panel.add(btnParticipantes);
        panel.add(btnActividades);
        panel.add(btnEspacios);
        panel.add(actividadesComboBox);
        panel.add(espaciosComboBox);
        panel.add(btnAsignar);
        add(panel);
        setVisible(true);

        // Agregar acción al botón de participantes
        // btnParticipantes.addActionListener(e -> {
        //     VentanaRegistrarParticipante ventana = new VentanaRegistrarParticipante(congreso);
        //     ventana.setVisible(true);
        // });

        // Agregar acción al botón de actividades
        btnActividades.addActionListener(_ -> {
            VentanaAgregarActividad ventana = new VentanaAgregarActividad(congreso, this);
            ventana.setVisible(true);
        });

        btnEliminarActividad.addActionListener(_ -> {
            new VentanaEliminarActividad(congreso, this);
        });

        // Agregar acción al botón de espacios
        // btnEspacios.addActionListener(e -> {
        //     VentanaRegistrarEspacio ventana = new VentanaRegistrarEspacio(congreso);
        //     ventana.setVisible(true);
        // });

        // Agregar acción al botón de asignar
        btnAsignar.addActionListener(_ -> {
            Actividad actividadSeleccionada = (Actividad) actividadesComboBox.getSelectedItem();
            String espacioSeleccionado = (String) espaciosComboBox.getSelectedItem();
            if (actividadSeleccionada != null && espacioSeleccionado != null) {
                // Lógica para asignar el espacio a la actividad
                JOptionPane.showMessageDialog(this, "Espacio " + espacioSeleccionado + " asignado a la actividad " + actividadSeleccionada.getNombre());
            }
        });

    }

    public void mostrarAsignacionesEspacio(Congreso congreso) {
        String[] columnas = {"Actividad", "Espacio", "Tipo", "Hora Inicio", "Hora Fin"};
        GestorAsignaciones gestorAsignaciones = new GestorAsignaciones();
        ArrayList<AsignacionEspacio> asignaciones = gestorAsignaciones.GestorAsignacionesEspacios(congreso.getId());
        if (asignaciones.isEmpty()) {
            // Si no hay asignaciones, mostrar un mensaje
            JPanel panel = (JPanel) getContentPane();
            panel.removeAll();
            JLabel mensaje = new JLabel("No hay asignaciones realizadas para este congreso.");
            mensaje.setBounds(50, 180, 700, 30);
            panel.add(mensaje);
            panel.revalidate();
            panel.repaint();
        } else {
            String[][] datos = new String[asignaciones.size()][5];
            for (int i = 0; i < asignaciones.size(); i++) {
                AsignacionEspacio asignacion = asignaciones.get(i);
                datos[i][0] = asignacion.getActividad().getNombre();
                datos[i][1] = asignacion.getEspacio().getNombre();
                datos[i][2] = asignacion.getActividad().getTipo();
                datos[i][3] = asignacion.getFechaHoraInicio();
                datos[i][4] = asignacion.getFechaHoraFin();
            }
            // Crear el modelo de la tabla y asignarlo
            DefaultTableModel modelo = new DefaultTableModel(datos, columnas);
            JTable tabla = new JTable(modelo);
            JScrollPane scrollPane = new JScrollPane(tabla);
            scrollPane.setBounds(50, 180, 700, 300);
            // Limpiar el panel y agregar la tabla
            JPanel panel = (JPanel) getContentPane();
            panel.removeAll();
            panel.add(scrollPane);
            panel.revalidate();
            panel.repaint();
        }
    }
    public void actividadesDisponibles(Congreso congreso) {
        // Método para mostrar las actividades disponibles en un JComboBox
        ArrayList<Actividad> actividades = gestorActividades.listarActividades(congreso);
        if (actividades.isEmpty()) {
            actividadesComboBox.removeAllItems();
            actividadesComboBox.setEnabled(false);
        } else {
            actividadesComboBox.removeAllItems();
            for (Actividad actividad : actividades) {
                actividadesComboBox.addItem(actividad);
            }
            actividadesComboBox.setEnabled(true);
        }
    }

    public static void main(String[] args) {
        // Crear un congreso de ejemplo
        Congreso congreso = new Congreso(1, "Congreso de Ejemplo");
        // Abrir la ventana de gestión del congreso
        new VentanaGestionCongreso(congreso);
    }
}
