package vista;

import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import controlador.*;
import modelo.*;

public class VentanaGestionCongreso extends JFrame {
    private GestorActividades gestorActividades;
    private GestorEspacio gestorEspacio;
    private GestorCongreso gestorCongreso;
    private GestorAsignaciones gestorAsignaciones;

    private JComboBox<Actividad> actividadesComboBox;
    private JComboBox<Espacio> espaciosComboBox;
    private JComboBox<String> fechaComboBox;
    private JPanel panelTabla; // Panel específico para la tabla de asignaciones
    private JFormattedTextField txtHoraInicio; // Campo de hora como variable de instancia
    private JButton btnAsignar; // Botón asignar como variable de instancia
    private AsignacionEspacio asignacionSeleccionada = null; // Para tracking de la asignación seleccionada

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

        // Inicializar el campo de hora como variable de instancia
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

        // Inicializar el botón como variable de instancia
        btnAsignar = new JButton("Asignar");
        btnAsignar.setBounds(700, 120, 150, 40);

        // Botón para cancelar edición
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(700, 170, 150, 30);
        btnCancelar.setVisible(false); // Inicialmente oculto

        // Botón para eliminar asignación
        JButton btnEliminarAsignacion = new JButton("Eliminar Asignación");
        btnEliminarAsignacion.setBounds(700, 210, 150, 30);
        btnEliminarAsignacion.setVisible(false); // Inicialmente oculto

        // Crear panel específico para la tabla
        panelTabla = new JPanel();
        panelTabla.setLayout(null);
        panelTabla.setBounds(50, 250, 700, 300);

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
        panel.add(btnCancelar);
        panel.add(btnEliminarAsignacion);
        panel.add(panelTabla); // Agregar el panel de la tabla
        add(panel);
        setVisible(true);

        // Agregar acción al botón de participantes
        // btnParticipantes.addActionListener(e -> {
        // VentanaRegistrarParticipante ventana = new
        // VentanaRegistrarParticipante(congreso);
        // ventana.setVisible(true);
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

        // Agregar acción al botón cancelar
        btnCancelar.addActionListener(_ -> {
            limpiarSeleccion();
            btnCancelar.setVisible(false);
            seleccionHoraAutomatica(congreso, txtHoraInicio);
        });

        // Agregar acción al botón de eliminar asignación
        btnEliminarAsignacion.addActionListener(_ -> {
            eliminarAsignacionEspacio(asignacionSeleccionada, congreso);
            limpiarSeleccion();
            btnEliminarAsignacion.setVisible(false);
            btnCancelar.setVisible(false);
        });

        // Agregar acción al botón de asignar
        btnAsignar.addActionListener(_ -> {
            Actividad actividadSeleccionada = (Actividad) actividadesComboBox.getSelectedItem();
            Espacio espacioSeleccionado = (Espacio) espaciosComboBox.getSelectedItem();
            String fechaSeleccionada = (String) fechaComboBox.getSelectedItem();
            String horaInicio = txtHoraInicio.getText();

            if (actividadSeleccionada == null || espacioSeleccionado == null || fechaSeleccionada == null
                    || horaInicio.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (asignacionSeleccionada != null) {
                // MODO ACTUALIZACIÓN
                System.out.println("DEBUG: Actualizando asignación ID: " + asignacionSeleccionada.getId());

                // Actualizar los datos de la asignación seleccionada
                asignacionSeleccionada.setActividad(actividadSeleccionada);
                asignacionSeleccionada.setEspacio(espacioSeleccionado);
                asignacionSeleccionada.setFecha(java.time.LocalDate.parse(fechaSeleccionada));
                asignacionSeleccionada.setHoraInicio(java.time.LocalTime.parse(horaInicio));
                // Calcular nueva hora fin basada en la duración de la actividad
                asignacionSeleccionada.setHoraFin(
                        java.time.LocalTime.parse(horaInicio).plusMinutes(actividadSeleccionada.getDuracion()));

                // Actualizar en la base de datos
                String resultado = gestorAsignaciones.actualizarAsignacionEspacio(asignacionSeleccionada);

                if (resultado.equals("Actualización exitosa")) {
                    JOptionPane.showMessageDialog(this, "Asignación actualizada exitosamente.", "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    // Limpiar selección y volver al modo crear
                    limpiarSeleccion();
                    // Actualizar la vista de asignaciones
                    mostrarAsignacionesEspacio(congreso);
                } else if (resultado.equals("Conflictos detectados")) {
                    // Si la actividad ya está asignada a otro espacio, preguntar al usuario si desea forzar la actualización
                    int confirmacion = JOptionPane.showConfirmDialog(this,
                            "La actividad ya está asignada a otro espacio. ¿Desea forzar la actualización?",
                            "Confirmar actualización", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (confirmacion == JOptionPane.YES_OPTION) {
                        // Forzar la actualización
                        gestorAsignaciones.forzarActualizacionAsignacion(asignacionSeleccionada);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar la asignación.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // MODO CREACIÓN (código original)
                System.out.println("DEBUG: Creando nueva asignación");

                int id = gestorAsignaciones.obtenerAsignacionesEspacios(congreso.getId()).size() + 1;
                AsignacionEspacio nuevaAsignacion = new AsignacionEspacio(
                        id,
                        congreso,
                        espacioSeleccionado,
                        actividadSeleccionada,
                        java.time.LocalDate.parse(fechaSeleccionada),
                        java.time.LocalTime.parse(horaInicio),
                        java.time.LocalTime.parse(horaInicio)
                                .plusMinutes(actividadSeleccionada.getDuracion()));
                String verificacionConflictos = gestorAsignaciones.procesarNuevaAsignacionEspacios(nuevaAsignacion);
                if (verificacionConflictos.equals("Asignación exitosa")) {
                    JOptionPane.showMessageDialog(this, verificacionConflictos, "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, verificacionConflictos, "Conflictos detectados",
                            JOptionPane.WARNING_MESSAGE);        
                }
                                    // Limpiar selección y volver al modo crear
                    limpiarSeleccion();
                    // Actualizar la vista de asignaciones
                    mostrarAsignacionesEspacio(congreso);
            }
        });

    }

    public void mostrarAsignacionesEspacio(Congreso congreso) {
        String[] columnas = { "Fecha", "Actividad", "Espacio", "Tipo", "Hora Inicio", "Hora Fin" };
        // Usar el gestor de la clase en lugar de crear uno nuevo
        ArrayList<AsignacionEspacio> asignaciones = gestorAsignaciones.obtenerAsignacionesEspacios(congreso.getId());

        // Debug: mostrar cuántas asignaciones se encontraron
        System.out.println("DEBUG: Se encontraron " + asignaciones.size() + " asignaciones para el congreso ID: "
                + congreso.getId());

        // Limpiar solo el panel de la tabla
        panelTabla.removeAll();

        if (asignaciones.isEmpty()) {
            // Si no hay asignaciones, mostrar un mensaje
            JLabel mensaje = new JLabel("No hay asignaciones realizadas para este congreso.");
            mensaje.setBounds(0, 0, 700, 30);
            panelTabla.add(mensaje);
            System.out.println("DEBUG: Mostrando mensaje de 'no hay asignaciones'");
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
                System.out.println("DEBUG: Asignación " + (i + 1) + ": " + datos[i][1] + " en " + datos[i][2] + " el "
                        + datos[i][0]);
            }
            // Crear el modelo de la tabla y asignarlo
            DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // La tabla ya no es editable directamente
                    return false;
                }
            };

            JTable tabla = new JTable(modelo);

            // Agregar listener para selección de filas
            tabla.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    int filaSeleccionada = tabla.getSelectedRow();
                    if (filaSeleccionada >= 0) {
                        cargarDatosEnInputs(asignaciones.get(filaSeleccionada), txtHoraInicio);
                    }
                }
            });

            // Configurar selección de filas
            tabla.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            tabla.setRowSelectionAllowed(true);
            tabla.setColumnSelectionAllowed(false);

            // Configurar colores para indicar que las filas son seleccionables
            tabla.setSelectionBackground(new java.awt.Color(173, 216, 230)); // Azul claro
            tabla.setSelectionForeground(java.awt.Color.BLACK);

            JScrollPane scrollPane = new JScrollPane(tabla);
            scrollPane.setBounds(0, 0, 700, 300);
            panelTabla.add(scrollPane);
            System.out.println("DEBUG: Tabla creada y agregada al panel");
        }

        // Forzar la actualización del panel de la tabla
        panelTabla.revalidate();
        panelTabla.repaint();
        // También actualizar el panel padre para asegurar que se vea
        this.revalidate();
        this.repaint();
        System.out.println("DEBUG: Panel actualizado");
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
            JOptionPane.showMessageDialog(this, "No hay actividades registradas para este congreso.", "Información",
                    JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "No hay espacios registrados para este congreso.", "Información",
                    JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "No hay fechas disponibles para este congreso.", "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            fechaComboBox.removeAllItems();
            for (String fecha : fechas) {
                fechaComboBox.addItem(fecha);
            }
            fechaComboBox.setEnabled(true);
        }
    }

    /**
     * Método para cargar los datos de una asignación seleccionada en los inputs
     */
    private void cargarDatosEnInputs(AsignacionEspacio asignacion, JFormattedTextField txtHoraInicio) {
        // Guardar referencia a la asignación seleccionada
        this.asignacionSeleccionada = asignacion;

        // Cargar datos en los ComboBoxes
        actividadesComboBox.setSelectedItem(asignacion.getActividad());
        espaciosComboBox.setSelectedItem(asignacion.getEspacio());
        fechaComboBox.setSelectedItem(asignacion.getFecha().toString());

        // Cargar hora de inicio
        txtHoraInicio.setText(asignacion.getHoraInicio().toString());

        // Cambiar el texto del botón para indicar que se va a actualizar
        btnAsignar.setText("Actualizar");
        btnAsignar.setBackground(new java.awt.Color(255, 193, 7)); // Color amarillo para indicar actualización

        // Mostrar botón cancelar
        JPanel panel = (JPanel) getContentPane().getComponent(0);
        for (java.awt.Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                JButton boton = (JButton) comp;
                if (boton.getText().equals("Cancelar") || boton.getText().equals("Eliminar Asignación")) {
                    comp.setVisible(true);
                }
            }
        }

        System.out.println("DEBUG: Datos cargados en inputs para asignación ID: " + asignacion.getId());
    }

    /**
     * Método para limpiar la selección y volver al modo de creación
     */
    private void limpiarSeleccion() {
        this.asignacionSeleccionada = null;
        btnAsignar.setText("Asignar");
        btnAsignar.setBackground(null); // Volver al color por defecto
    }

    /**
     * Método para eliminar una asignación de espacio
     */
    private void eliminarAsignacionEspacio(AsignacionEspacio asignacion, Congreso congreso) {
        if (asignacion != null) {
            gestorAsignaciones.eliminarAsignacionEspacio(asignacion.getId());
            // Actualizar la tabla o lista que muestra las asignaciones
            mostrarAsignacionesEspacio(congreso);
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
