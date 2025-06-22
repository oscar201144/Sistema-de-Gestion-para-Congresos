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
            if (nombre.isEmpty()) {
                new VentanaError("Por favor, complete todos los campos.");
                return;
            }
            int nuevoId = gestorCongreso.getCongresos().size() + 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaInicio = LocalDate.parse(txtFechaInicio.getText().trim(), formatter);
            LocalDate fechaFin = LocalDate.parse(txtFechaFin.getText().trim(), formatter);
            DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime horaInicio = LocalTime.parse(txtHoraInicio.getText().trim(), formatterHora);
            LocalTime horaFin = LocalTime.parse(txtHoraFin.getText().trim(), formatterHora);
            try {
                Congreso nuevoCongreso = new Congreso(nuevoId, nombre, fechaInicio, horaInicio, fechaFin, horaFin);
                try {
                    gestorCongreso.agregarCongreso(nuevoCongreso);
                } catch (Exception ex) {
                    new VentanaError("Error al guardar el congreso: " + ex.getMessage());
                    return;
                }
                frame.dispose(); // Cerrar la ventana después de guardar
                new VentanaGestionCongreso(nuevoCongreso); // Abrir la ventana de gestión del congreso recién creado
            } catch (Exception e) {
                new VentanaError("Error al crear el congreso: " + e.getMessage());
                return;
            }

        });
        btnCancelar.addActionListener(_ -> {
            frame.dispose(); // Cerrar la ventana sin guardar
            new VentanaPrincipal(); // Volver a la ventana principal
        });
    }

}
