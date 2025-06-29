package vista;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

import controlador.GestorCongreso;
import modelo.Congreso;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class VentanaCrearCongreso extends JFrame {
    private GestorCongreso gestorCongreso;

    public VentanaCrearCongreso() {
        this.gestorCongreso = new GestorCongreso();
        JFrame frame = new JFrame("Crear Congreso");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 350);
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(30, 30, 100, 25);
        panel.add(lblNombre);
        JTextField txtNombre = new JTextField();
        txtNombre.setBounds(150, 30, 200, 25);
        panel.add(txtNombre);

        // Fecha y hora de inicio y fin
        JLabel lblFechaInicio = new JLabel("Fecha de inicio (DD/MM/AAAA):");
        lblFechaInicio.setBounds(30, 70, 180, 25);
        panel.add(lblFechaInicio);
        final JFormattedTextField txtFechaInicio;
        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            txtFechaInicio = new JFormattedTextField(dateMask);
            txtFechaInicio.setBounds(220, 70, 130, 25);
            panel.add(txtFechaInicio);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        JLabel lblHoraInicio = new JLabel("Hora de inicio (HH:MM):");
        lblHoraInicio.setBounds(30, 110, 180, 25);
        panel.add(lblHoraInicio);
        final JFormattedTextField txtHoraInicio;
        try {
            MaskFormatter hourMask = new MaskFormatter("##:##");
            hourMask.setPlaceholderCharacter('_');
            txtHoraInicio = new JFormattedTextField(hourMask);
            txtHoraInicio.setBounds(220, 110, 130, 25);
            panel.add(txtHoraInicio);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        JLabel lblFechaFin = new JLabel("Fecha de fin (DD/MM/AAAA):");
        lblFechaFin.setBounds(30, 150, 180, 25);
        panel.add(lblFechaFin);
        final JFormattedTextField txtFechaFin;
        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            txtFechaFin = new JFormattedTextField(dateMask);
            txtFechaFin.setBounds(220, 150, 130, 25);
            panel.add(txtFechaFin);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        JLabel lblHoraFin = new JLabel("Hora de fin (HH:MM):");
        lblHoraFin.setBounds(30, 190, 180, 25);
        panel.add(lblHoraFin);
        final JFormattedTextField txtHoraFin;
        try {
            MaskFormatter hourMask = new MaskFormatter("##:##");
            hourMask.setPlaceholderCharacter('_');
            txtHoraFin = new JFormattedTextField(hourMask);
            txtHoraFin.setBounds(220, 190, 130, 25);
            panel.add(txtHoraFin);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(150, 250, 100, 30);
        panel.add(btnGuardar);
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(270, 250, 100, 30);
        panel.add(btnCancelar);
        panel.add(lblNombre);
        panel.add(txtNombre);
        panel.add(btnGuardar);
        panel.add(btnCancelar);
        frame.add(panel);
        frame.setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        frame.setResizable(false); // Evitar que la ventana se pueda redimensionar
        frame.setVisible(true);
        btnGuardar.addActionListener(_ -> {
            String nombre = txtNombre.getText().trim();
            String fechaInicioTexto = txtFechaInicio.getText().trim();
            String fechaFinTexto = txtFechaFin.getText().trim();
            String horaInicioTexto = txtHoraInicio.getText().trim();
            String horaFinTexto = txtHoraFin.getText().trim();
            
            if (nombre.isEmpty() || fechaInicioTexto.isEmpty() || fechaFinTexto.isEmpty() || 
                horaInicioTexto.isEmpty() || horaFinTexto.isEmpty()) {
                new VentanaError("Por favor, complete todos los campos.");
                return;
            }
            
            try {
                // Delegar el procesamiento al controlador
                boolean congresoCreado = crearCongreso(nombre, fechaInicioTexto, horaInicioTexto, 
                                                     fechaFinTexto, horaFinTexto);
                
                if (congresoCreado) {
                    new VentanaExito("Congreso creado exitosamente: " + nombre);
                    frame.dispose();
                    new VentanaPrincipal(); // Volver al menú principal
                } else {
                    new VentanaError("Error al guardar el congreso en la base de datos.");
                }
            } catch (Exception e) {
                new VentanaError("Error al crear el congreso: " + e.getMessage());
            }
        });
        btnCancelar.addActionListener(_ -> {
            frame.dispose(); // Cerrar la ventana sin guardar
            new VentanaPrincipal(); // Volver a la ventana principal
        });
    }

    private boolean crearCongreso(String nombre, String fechaInicio, String horaInicio, 
                                String fechaFin, String horaFin) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");
            
            LocalDate fechaInicioDate = LocalDate.parse(fechaInicio, formatter);
            LocalDate fechaFinDate = LocalDate.parse(fechaFin, formatter);
            LocalTime horaInicioTime = LocalTime.parse(horaInicio, formatterHora);
            LocalTime horaFinTime = LocalTime.parse(horaFin, formatterHora);
            
            int nuevoId = gestorCongreso.getCongresos().size() + 1;
            Congreso nuevoCongreso = new Congreso(nuevoId, nombre, fechaInicioDate, horaInicioTime, 
                                                fechaFinDate, horaFinTime);
            
            return gestorCongreso.agregarCongreso(nuevoCongreso);
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar las fechas: " + e.getMessage());
        }
    }

}
