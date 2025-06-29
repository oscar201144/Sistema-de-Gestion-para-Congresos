package vista;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controlador.GestorAsignaciones;
import controlador.GestorConflictos;
import modelo.Congreso;
import modelo.Conflicto;

public class VentanaConflictos extends JFrame {
    
    public VentanaConflictos(Congreso congreso, GestorAsignaciones gestorAsignaciones) {
        setTitle("Conflictos del Congreso: " + congreso.getNombre());
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        
        // Título
        JLabel lblTitulo = new JLabel("Lista de Conflictos Detectados");
        lblTitulo.setBounds(20, 10, 300, 30);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(16f));
        panel.add(lblTitulo);
        
        // Obtener conflictos usando GestorConflictos
        GestorConflictos gestorConflictos = new GestorConflictos();
        ArrayList<Conflicto> conflictos = gestorConflictos.obtenerConflictosPendientes(congreso);
        
        if (conflictos.isEmpty()) {
            // No hay conflictos
            JLabel mensaje = new JLabel("¡Excelente! No se detectaron conflictos en este congreso.");
            mensaje.setBounds(20, 60, 500, 30);
            mensaje.setForeground(new Color(0, 150, 0)); // Verde
            panel.add(mensaje);
            
            JButton btnCerrar = new JButton("Cerrar");
            btnCerrar.setBounds(250, 300, 100, 30);
            btnCerrar.addActionListener(_ -> dispose());
            panel.add(btnCerrar);
        } else {
            // Crear tabla de conflictos
            String[] columnas = {"Tipo", "Descripción", "Actividad 1", "Actividad 2"};
            String[][] datos = new String[conflictos.size()][4];
            
            for (int i = 0; i < conflictos.size(); i++) {
                Conflicto conflicto = conflictos.get(i);
                datos[i][0] = conflicto.getTipo();
                datos[i][1] = conflicto.getDescripcion();
                datos[i][2] = conflicto.getActividad1() != null ? conflicto.getActividad1().getNombre() : "N/A";
                datos[i][3] = conflicto.getActividad2() != null ? conflicto.getActividad2().getNombre() : "N/A";
            }
            
            DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            JTable tabla = new JTable(modelo);
            tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            
            JScrollPane scrollPane = new JScrollPane(tabla);
            scrollPane.setBounds(20, 50, 700, 250);
            panel.add(scrollPane);
            
            // Botón cerrar
            JButton btnCerrar = new JButton("Cerrar");
            btnCerrar.setBounds(350, 320, 100, 30);
            btnCerrar.addActionListener(_ -> dispose());
            panel.add(btnCerrar);
        }
        
        add(panel);
        setVisible(true);
    }
}
