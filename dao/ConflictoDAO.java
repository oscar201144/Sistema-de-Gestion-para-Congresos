package dao;
import java.util.ArrayList;
import java.sql.*;
import vista.VentanaError;
import vista.VentanaExito;
import modelo.*;

public class ConflictoDAO {
    public void guardarConflicto(Conflicto conflicto) {
        String sql = "INSERT INTO conflicto (id_congreso, tipo, descripcion, id_actividad_2, id_actividad_1, id_persona, id_espacio, id_asignacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Integer personaId = null;
        if (conflicto.getPersona() != null) {
            personaId = conflicto.getPersona().getId();
        }
        Integer espacioId = null;
        if (conflicto.getEspacio() != null) {
            espacioId = conflicto.getEspacio().getId();
        }
        Integer actividad2Id = null;
        if (conflicto.getActividad2() != null) {
            actividad2Id = conflicto.getActividad2().getId();
        }
        
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, conflicto.getCongreso().getId());
            pstmt.setString(2, conflicto.getTipo());
            pstmt.setString(3, conflicto.getDescripcion());
            if (actividad2Id != null) {
                pstmt.setInt(4, actividad2Id);
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }
            pstmt.setInt(5, conflicto.getActividad1().getId());
            if (personaId != null) {
                pstmt.setInt(6, personaId);
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            }
            if (espacioId != null) {
                pstmt.setInt(7, espacioId);
            } else {
                pstmt.setNull(7, java.sql.Types.INTEGER);
            }
            pstmt.setInt(8, conflicto.getIdAsignacion());
            pstmt.executeUpdate();
            new VentanaExito("Conflicto registrado exitosamente.");
        } catch (SQLException e) {
            new VentanaError("Error al registrar el conflicto: " + e.getMessage());
        }
    }

    public ArrayList<Conflicto> listarConflictosPendientes(Congreso congreso) {
        ArrayList<Conflicto> conflictos = new ArrayList<>();
        String sql = "SELECT conflicto.id_conflicto, conflicto.id_congreso, conflicto.tipo, conflicto.descripcion, conflicto.id_actividad_1, conflicto.id_actividad_2, conflicto.id_espacio, conflicto.id_asignacion,\r\n" + //
                        "actividad1.id_actividad as id_actividad_1, actividad1.nombre as nombre_actividad_1, actividad1.tipo as tipo_actividad_1, actividad1.duracion as duracion_actividad1, \r\n" + //
                        "actividad2.id_actividad as id_actividad_2, actividad2.nombre as nombre_actividad_2, actividad2.tipo as tipo_actividad_2, actividad2.duracion as duracion_actividad2,\r\n" + //
                        "participante.id_participante, participante.nombre, espacio.id_congreso, espacio.nombre, espacio.capacidad\r\n" + //
                        "FROM `conflicto`\r\n" + //
                        "inner JOIN congreso on congreso.id_congreso = conflicto.id_congreso\r\n" + //
                        "inner JOIN actividad as actividad1  on actividad1.id_actividad = conflicto.id_actividad_1\r\n" + //
                        "INNER JOIN actividad as actividad2 on actividad2.id_actividad = conflicto.id_actividad_2\r\n" + //
                        "inner JOIN participante on conflicto.id_persona = participante.id_participante\r\n" + //
                        "inner JOIN espacio on espacio.id_espacio = conflicto.id_espacio WHERE congreso.id_congreso = ?";
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
                    espacio,
                    rs.getInt("id_asignacion")
                );
                conflictos.add(conflicto);
            }
        } catch (SQLException e) {
            new VentanaError("Error al obtener conflictos: " + e.getMessage());
        }
        return conflictos;
    }

    public void eliminarConflictosPorAsignacion(int idAsignacion) {
        String sql = "DELETE FROM conflicto WHERE id_asignacion = ?";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idAsignacion);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            new VentanaError("Error al eliminar conflictos por asignación: " + e.getMessage());
        }
    }

}
