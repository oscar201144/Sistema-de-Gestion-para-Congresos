package dao;
import java.util.ArrayList;
import java.sql.*;
import vista.VentanaError;
import vista.VentanaExito;
import modelo.*;

public class ConflictoDAO {
    public void guardarConflicto(Conflicto conflicto) {
        String sql = "INSERT INTO conflictos (id, tipo, descripcion,id_actividad_2,id_actividad_1,id_persona,id_espacio) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, conflicto.getId());
            pstmt.setString(2, conflicto.getTipo());
            pstmt.setString(3, conflicto.getDescripcion());
            pstmt.setInt(4, conflicto.getActividad2().getId());
            pstmt.setInt(5, conflicto.getActividad1().getId());
            pstmt.setInt(6, conflicto.getPersona().getId());
            pstmt.setInt(7, conflicto.getEspacio().getId());
            pstmt.executeUpdate();
            new VentanaExito("Conflicto registrado exitosamente.");
        } catch (SQLException e) {
            new VentanaError("Error al registrar el conflicto: " + e.getMessage());
        }
    }

    public ArrayList<Conflicto> listarConflictosPendientes(Congreso congreso) {
        ArrayList<Conflicto> conflictos = new ArrayList<>();
        String sql = "SELECT conflicto.id_conflicto, conflicto.id_congreso, conflicto.tipo, conflicto.descripcion, conflicto.id_actividad_1, conflicto.id_actividad_2, conflicto.id_espacio,\r\n" + //
                        "actividad1.id_actividad as id_actividad_1, actividad1.nombre as nombre_actividad_1, actividad1.tipo as tipo_actividad_1, actividad1.duracion as duracion_actividad1, \r\n" + //
                        "actividad2.id_actividad as id_actividad_2, actividad2.nombre as nombre_actividad_2, actividad2.tipo as tipo_actividad_2, actividad2.duracion as duracion_actividad2,\r\n" + //
                        "participante.id_participante, participante.nombre, espacio.id_congreso, espacio.nombre, espacio.capacidad\r\n" + //
                        "FROM `conflicto`\r\n" + //
                        "inner JOIN congreso on congreso.id_congreso = conflicto.id_congreso\r\n" + //
                        "inner JOIN actividad as actividad1  on actividad1.id_actividad = conflicto.id_actividad_1\r\n" + //
                        "INNER JOIN actividad as actividad2 on actividad2.id_actividad = conflicto.id_actividad_2\r\n" + //
                        "inner JOIN participante on conflicto.id_persona = participante.id_participante\r\n" + //
                        "inner JOIN espacio on espacio.id_espacio = conflicto.id_espacio";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, congreso.getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Actividad actividad1 = new Actividad(
                    rs.getInt("id_actividad_1"),
                    congreso,
                    rs.getString("nombre_actividad_1"),
                    rs.getString("tipo_actividad_1"),
                    rs.getInt("duracion_actividad1")
                );
                Actividad actividad2 = new Actividad(
                    rs.getInt("id_actividad_2"),
                    congreso,
                    rs.getString("nombre_actividad_2"),
                    rs.getString("tipo_actividad_2"),
                    rs.getInt("duracion_actividad2")
                );
                Persona persona = new Persona(
                    rs.getInt("id_participante"),
                    rs.getString("nombre")
                );
                Espacio espacio = new Espacio(
                    rs.getInt("id_espacio"),
                    congreso,
                    rs.getString("espacio.nombre"),
                    rs.getInt("espacio.capacidad")
                );
                Conflicto conflicto = new Conflicto(
                    rs.getInt("id_conflicto"),
                    congreso,
                    rs.getString("conflicto.tipo"),
                    rs.getString("conflicto.descripcion"),
                    actividad1,
                    actividad2,
                    persona,
                    espacio
                );
                conflictos.add(conflicto);
            }
        } catch (SQLException e) {
            new VentanaError("Error al obtener conflictos: " + e.getMessage());
        }
        return conflictos;
    }

    public void eliminarConflicto(int id) {
        String sql = "DELETE FROM conflictos WHERE id_conflicto = ?";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            new VentanaExito("Conflicto eliminado exitosamente.");
        } catch (SQLException e) {
            new VentanaError("Error al eliminar el conflicto: " + e.getMessage());
        }
    }


}
