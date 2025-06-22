package vista;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import controlador.GestorActividades;
import controlador.GestorAsignaciones;
import controlador.GestorEspacio;
import controlador.GestorCongreso;
import modelo.AsignacionEspacio;
import modelo.Congreso;
import modelo.Actividad;
import modelo.Espacio;


public class VentanaGestionCongreso extends JFrame {
    private GestorActividades gestorActividades;
    private GestorEspacio gestorEspacio;
    private GestorCongreso gestorCongreso;
    private GestorAsignaciones gestorAsignaciones;

    private JComboBox<Actividad> actividadesComboBox;
    private JComboBox<Espacio> espaciosComboBox;
    private JComboBox<String> fechaComboBox;
    

    public VentanaGestionCongreso(Congreso congreso) {
        gestorActividades = new GestorActividades();
        gestorEspacio = new GestorEspacio();
        gestorCongreso = new GestorCongreso();
        gestorAsignaciones = new GestorAsignaciones();
        setTitle("Gestión de Congreso " + congreso.getNombre());
        setSize(900, 600);
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



        JLabel lblActividadesDisponibles = new JLabel("Actividades Disponibles:");
        lblActividadesDisponibles.setBounds(50, 100, 200, 20);
        actividadesComboBox = new JComboBox<>();
        actividadesComboBox.setBounds(50, 120, 300, 40);
        actividadesDisponibles(congreso);

        JLabel lblEspaciosDisponibles = new JLabel("Espacios Disponibles:");
        lblEspaciosDisponibles.setBounds(350, 100, 200, 20);
        espaciosComboBox = new JComboBox<>();
        espaciosComboBox.setBounds(350, 120, 200, 40);
        espaciosDisponibles(congreso);

        JLabel lblFechaAAsignar = new JLabel("Fecha y Hora de Asignación:");
        lblFechaAAsignar.setBounds(50, 160, 200, 20);
        fechaComboBox = new JComboBox<>();
        fechaComboBox.setBounds(50, 180, 120, 40);
        fechaDisponible(congreso);
        

        final JFormattedTextField txtHoraInicio;
        try {
            MaskFormatter hourMask = new MaskFormatter("##:##");
            hourMask.setPlaceholderCharacter('_');
            txtHoraInicio = new JFormattedTextField(hourMask);
            txtHoraInicio.setBounds(200, 180, 50, 40);
            panel.add(txtHoraInicio);

            fechaComboBox.addActionListener(_ -> {
                seleccionHoraAutomatica(congreso, txtHoraInicio);
            });
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        seleccionHoraAutomatica(congreso, txtHoraInicio);

        JButton btnAsignar = new JButton("Asignar");
        btnAsignar.setBounds(700, 120, 150, 40);

        mostrarAsignacionesEspacio(congreso);
        // Agregar los componentes al panel
        panel.add(lblFechaAAsignar);
        panel.add(fechaComboBox);
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
        btnEspacios.addActionListener(_ -> {
            VentanaAgregarEspacio ventana = new VentanaAgregarEspacio(congreso, this);
            ventana.setVisible(true);
        });

        btnEliminarEspacio.addActionListener(_ -> {
            new VentanaEliminarEspacio(congreso, this);
        });

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
        String[] columnas = {"Fecha","Actividad", "Espacio", "Tipo", "Hora Inicio", "Hora Fin"};
        GestorAsignaciones gestorAsignaciones = new GestorAsignaciones();
        ArrayList<AsignacionEspacio> asignaciones = gestorAsignaciones.GestorAsignacionesEspacios(congreso.getId());
        if (asignaciones.isEmpty()) {
            // Si no hay asignaciones, mostrar un mensaje
            JPanel panel = (JPanel) getContentPane();
            panel.removeAll();
            JLabel mensaje = new JLabel("No hay asignaciones realizadas para este congreso.");
            mensaje.setBounds(50, 250, 700, 30);
            panel.add(mensaje);
            panel.revalidate();
            panel.repaint();
        } else {
            String[][] datos = new String[asignaciones.size()][6];
            for (int i = 0; i < asignaciones.size(); i++) {
                AsignacionEspacio asignacion = asignaciones.get(i);
                datos[i][0] = asignacion.getFecha().toString();
                datos[i][1] = asignacion.getActividad().getNombre();
                datos[i][2] = asignacion.getEspacio().getNombre();
                datos[i][3] = asignacion.getActividad().getTipo();
                datos[i][4] = asignacion.getHoraInicio().toString();
                datos[i][5] = asignacion.getHoraFin().toString();
            }
            // Crear el modelo de la tabla y asignarlo
            DefaultTableModel modelo = new DefaultTableModel(datos, columnas);
            JTable tabla = new JTable(modelo);
            JScrollPane scrollPane = new JScrollPane(tabla);
            scrollPane.setBounds(50, 250, 700, 300);
            // Limpiar el panel y agregar la tabla
            JPanel panel = (JPanel) getContentPane();
            panel.removeAll();
            panel.add(scrollPane);
            panel.revalidate();
            panel.repaint();
        }
    }

    public void seleccionHoraAutomatica(Congreso congreso, JFormattedTextField txtHoraInicio) {
        String fechaSeleccionada = (String) fechaComboBox.getSelectedItem();
                txtHoraInicio.setText(gestorAsignaciones.obtenerHoraTempranaDisponible(congreso, fechaSeleccionada)); 
    }

    public void actividadesDisponibles(Congreso congreso) {
        // Método para mostrar las actividades disponibles en un JComboBox
        ArrayList<Actividad> actividades = gestorActividades.listarActividades(congreso);
        if (actividades.isEmpty()) {
            actividadesComboBox.removeAllItems();
            actividadesComboBox.setEnabled(false);
            JOptionPane.showMessageDialog(this, "No hay actividades registradas para este congreso.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            actividadesComboBox.removeAllItems();
            for (Actividad actividad : actividades) {
                actividadesComboBox.addItem(actividad);
            }
            actividadesComboBox.setEnabled(true);
        }
    }
    public void espaciosDisponibles(Congreso congreso) {
        ArrayList<Espacio> espacios = gestorEspacio.listarEspacios(congreso);
        if (espacios.isEmpty()) {
            espaciosComboBox.removeAllItems();
            espaciosComboBox.setEnabled(false);
            JOptionPane.showMessageDialog(this, "No hay espacios registrados para este congreso.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            espaciosComboBox.removeAllItems();
            for (Espacio espacio : espacios) {
                espaciosComboBox.addItem(espacio);
            }
            espaciosComboBox.setEnabled(true);
        }
    }

    public void fechaDisponible(Congreso congreso) {
        // Método para mostrar las fechas disponibles en un JComboBox
        ArrayList<String> fechas = gestorCongreso.listarFechasDisponibles(congreso);
        if (fechas.isEmpty()) {
            fechaComboBox.removeAllItems();
            fechaComboBox.setEnabled(false);
            JOptionPane.showMessageDialog(this, "No hay fechas disponibles para este congreso.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            fechaComboBox.removeAllItems();
            for (String fecha : fechas) {
                fechaComboBox.addItem(fecha);
            }
            fechaComboBox.setEnabled(true);
        }
    }


    public static void main(String[] args) {
        // Crear un congreso de ejemplo
        Congreso congreso = new Congreso(1, "Congreso de Ejemplo",
                java.time.LocalDate.of(2023, 10, 1),
                java.time.LocalTime.of(9, 0),
                java.time.LocalDate.of(2023, 10, 3),
                java.time.LocalTime.of(17, 0));
        // Abrir la ventana de gestión del congreso
        new VentanaGestionCongreso(congreso);
    }
}
