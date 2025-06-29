package vista;

import controlador.GestorAsignaciones;
import controlador.GestorPersona;
import controlador.GestorActividades;
import modelo.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class VentanaAsignacionParticipantes extends JFrame {
    private GestorAsignaciones gestorAsignaciones;
    private GestorPersona gestorPersona;
    private GestorActividades gestorActividades;
    private Congreso congreso;
    
    private JComboBox<String> comboParticipantes;
    private JComboBox<String> comboActividades;
    private JComboBox<String> comboRoles;
    private DefaultListModel<String> modeloAsignaciones;
    private JList<String> listaAsignaciones;
    
    private ArrayList<Persona> participantes;
    private ArrayList<Actividad> actividades;
    private ArrayList<Rol> roles;
    private ArrayList<AsignacionParticipante> asignaciones;
    private int asignacionSeleccionadaId = -1;

    public VentanaAsignacionParticipantes(Congreso congreso) {
        this.congreso = congreso;
        this.gestorAsignaciones = new GestorAsignaciones();
        this.gestorPersona = new GestorPersona();
        this.gestorActividades = new GestorActividades();
        this.modeloAsignaciones = new DefaultListModel<>();
        this.participantes = new ArrayList<>();
        this.actividades = new ArrayList<>();
        this.roles = new ArrayList<>();
        this.asignaciones = new ArrayList<>();
        
        configurarVentana();
        crearComponentes();
        cargarDatos();
    }

    private void configurarVentana() {
        setTitle("Asignación de Participantes - " + congreso.getNombre());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void crearComponentes() {
        // Panel superior - Formulario de asignación
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Nueva Asignación de Participante"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Participante
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Participante:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        comboParticipantes = new JComboBox<>();
        panelFormulario.add(comboParticipantes, gbc);

        // Actividad
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Actividad:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        comboActividades = new JComboBox<>();
        comboActividades.addActionListener(this::cargarHorariosActividad);
        panelFormulario.add(comboActividades, gbc);

        // Rol
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Rol:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        comboRoles = new JComboBox<>();
        panelFormulario.add(comboRoles, gbc);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        JButton btnAsignar = new JButton("Asignar");
        btnAsignar.addActionListener(this::asignarParticipante);
        btnAsignar.setBackground(new Color(100, 255, 100));
        panelBotones.add(btnAsignar);
        
        JButton btnModificar = new JButton("Modificar");
        btnModificar.addActionListener(this::modificarAsignacion);
        panelBotones.add(btnModificar);
        
        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(this::limpiarCampos);
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panelFormulario.add(panelBotones, gbc);

        // Panel central - Lista de asignaciones
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createTitledBorder("Asignaciones Actuales"));
        
        listaAsignaciones = new JList<>(modeloAsignaciones);
        listaAsignaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaAsignaciones.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarAsignacion();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(listaAsignaciones);
        scrollPane.setPreferredSize(new Dimension(0, 250));
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior - Botones de acción
        JPanel panelInferior = new JPanel(new FlowLayout());
        
        JButton btnEliminar = new JButton("Eliminar Asignación");
        btnEliminar.addActionListener(this::eliminarAsignacion);
        btnEliminar.setBackground(new Color(255, 100, 100));
        panelInferior.add(btnEliminar);
        
        JButton btnRefrescar = new JButton("Refrescar Lista");
        btnRefrescar.addActionListener(this::refrescarLista);
        panelInferior.add(btnRefrescar);


        // Agregar paneles a la ventana
        add(panelFormulario, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        cargarParticipantes();
        cargarActividades();
        cargarRoles();
        cargarAsignaciones();
    }

    private void cargarParticipantes() {
        participantes = gestorPersona.obtenerTodosLosParticipantes();
        comboParticipantes.removeAllItems();
        
        for (Persona participante : participantes) {
            comboParticipantes.addItem(participante.getId() + " - " + participante.getNombre());
        }
    }

    private void cargarActividades() {
        actividades = gestorActividades.listarActividades(congreso);
        comboActividades.removeAllItems();
        
        for (Actividad actividad : actividades) {
            comboActividades.addItem(actividad.getId() + " - " + actividad.getNombre() + " (" + actividad.getDuracion() + " min)");
        }
    }

    private void cargarRoles() {
        roles = gestorAsignaciones.obtenerTodosLosRoles();
        comboRoles.removeAllItems();
        
        for (Rol rol : roles) {
            comboRoles.addItem(rol.getId() + " - " + rol.getTipo());
        }
    }

    private void cargarAsignaciones() {
        asignaciones = gestorAsignaciones.obtenerAsignacionesParticipantes(congreso.getId());
        modeloAsignaciones.clear();
        
        for (AsignacionParticipante asignacion : asignaciones) {
            String texto = String.format("ID:%d - %s (%s) → %s (%d min)",
                asignacion.getId(),
                asignacion.getPersona().getNombre(),
                asignacion.getRol().getTipo(),
                asignacion.getActividad().getNombre(),
                asignacion.getActividad().getDuracion()
            );
            modeloAsignaciones.addElement(texto);
        }
        
        if (asignaciones.isEmpty()) {
            modeloAsignaciones.addElement("No hay asignaciones registradas");
        }
    }

    private void cargarHorariosActividad(ActionEvent e) {
        // Ya no necesitamos manejar horarios aquí
        // Solo mostrar información sobre la actividad seleccionada
        int indiceActividad = comboActividades.getSelectedIndex();
        
        if (indiceActividad >= 0 && indiceActividad < actividades.size()) {
            Actividad actividadSeleccionada = actividades.get(indiceActividad);
            
            // Mostrar información en tooltip
            if (actividadSeleccionada.getDuracion() > 0) {
                comboActividades.setToolTipText("Duración: " + actividadSeleccionada.getDuracion() + " minutos");
            }
        }
    }

    private void asignarParticipante(ActionEvent e) {
        if (!validarCampos()) {
            return;
        }

        try {
            AsignacionParticipante nuevaAsignacion = crearAsignacionDesdeFormulario();
            String resultado = gestorAsignaciones.procesarNuevaAsignacionParticipante(nuevaAsignacion);
            
            if (resultado.contains("exitosa")) {
                new VentanaExito(resultado);
                limpiarCampos();
                cargarAsignaciones();
            } else {
                // Mostrar advertencia pero permitir continuar
                int opcion = JOptionPane.showConfirmDialog(
                    this,
                    resultado + "\n¿Desea continuar de todas formas?",
                    "Conflictos Detectados",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (opcion == JOptionPane.YES_OPTION) {
                    cargarAsignaciones();
                }
            }
        } catch (Exception ex) {
            new VentanaError("Error al crear la asignación: " + ex.getMessage());
        }
    }

    private void modificarAsignacion(ActionEvent e) {
        if (asignacionSeleccionadaId == -1) {
            new VentanaError("Debe seleccionar una asignación para modificar");
            return;
        }

        if (!validarCampos()) {
            return;
        }

        try {
            AsignacionParticipante asignacionModificada = crearAsignacionDesdeFormulario();
            asignacionModificada.setId(asignacionSeleccionadaId);
            
            String resultado = gestorAsignaciones.actualizarAsignacionParticipante(asignacionModificada);
            
            if (resultado.contains("exitosa")) {
                new VentanaExito(resultado);
                limpiarCampos();
                cargarAsignaciones();
            } else {
                new VentanaError(resultado);
            }
        } catch (Exception ex) {
            new VentanaError("Error al modificar la asignación: " + ex.getMessage());
        }
    }

    private void eliminarAsignacion(ActionEvent e) {
        if (asignacionSeleccionadaId == -1) {
            new VentanaError("Debe seleccionar una asignación para eliminar");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de que desea eliminar esta asignación?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            String resultado = gestorAsignaciones.eliminarAsignacionParticipante(asignacionSeleccionadaId);
            
            if (resultado.contains("exitosamente")) {
                new VentanaExito(resultado);
                limpiarCampos();
                cargarAsignaciones();
            } else {
                new VentanaError(resultado);
            }
        }
    }

    private boolean validarCampos() {
        if (comboParticipantes.getSelectedIndex() == -1) {
            new VentanaError("Debe seleccionar un participante");
            return false;
        }
        
        if (comboActividades.getSelectedIndex() == -1) {
            new VentanaError("Debe seleccionar una actividad");
            return false;
        }
        
        if (comboRoles.getSelectedIndex() == -1) {
            new VentanaError("Debe seleccionar un rol");
            return false;
        }
        
        return true;
    }

    private AsignacionParticipante crearAsignacionDesdeFormulario() {
        Persona participanteSeleccionado = participantes.get(comboParticipantes.getSelectedIndex());
        Actividad actividadSeleccionada = actividades.get(comboActividades.getSelectedIndex());
        Rol rolSeleccionado = roles.get(comboRoles.getSelectedIndex());
        
        // Sin fechas y horas específicas - se usarán las de la actividad cuando se asigne espacio
        return new AsignacionParticipante(
            0, // ID será asignado por la base de datos
            congreso,
            participanteSeleccionado,
            actividadSeleccionada,
            rolSeleccionado
        );
    }

    private void seleccionarAsignacion() {
        int indiceSeleccionado = listaAsignaciones.getSelectedIndex();
        
        if (indiceSeleccionado >= 0 && indiceSeleccionado < asignaciones.size()) {
            AsignacionParticipante asignacionSeleccionada = asignaciones.get(indiceSeleccionado);
            asignacionSeleccionadaId = asignacionSeleccionada.getId();
            
            // Llenar formulario con datos de la asignación seleccionada
            for (int i = 0; i < participantes.size(); i++) {
                if (participantes.get(i).getId() == asignacionSeleccionada.getPersona().getId()) {
                    comboParticipantes.setSelectedIndex(i);
                    break;
                }
            }
            
            for (int i = 0; i < actividades.size(); i++) {
                if (actividades.get(i).getId() == asignacionSeleccionada.getActividad().getId()) {
                    comboActividades.setSelectedIndex(i);
                    break;
                }
            }
            
            for (int i = 0; i < roles.size(); i++) {
                if (roles.get(i).getId() == asignacionSeleccionada.getRol().getId()) {
                    comboRoles.setSelectedIndex(i);
                    break;
                }
            }
        } else {
            asignacionSeleccionadaId = -1;
        }
    }

    private void limpiarCampos() {
        comboParticipantes.setSelectedIndex(-1);
        comboActividades.setSelectedIndex(-1);
        comboRoles.setSelectedIndex(-1);
        asignacionSeleccionadaId = -1;
        listaAsignaciones.clearSelection();
    }

    private void limpiarCampos(ActionEvent e) {
        limpiarCampos();
    }

    private void refrescarLista(ActionEvent e) {
        cargarDatos();
    }

}
