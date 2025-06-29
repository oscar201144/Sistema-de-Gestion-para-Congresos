package dao;
import java.sql.*;
import java.util.ArrayList;
import modelo.Congreso;

public class CongresoDAO {
    public boolean guardarCongreso(Congreso congreso) {
        String sql = "INSERT INTO congreso (nombre, fecha_inicio, hora_inicio, fecha_fin, hora_fin) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, congreso.getNombre());
            preparedStatement.setDate(2, java.sql.Date.valueOf(congreso.getFechaInicio()));
            preparedStatement.setTime(3, java.sql.Time.valueOf(congreso.getHoraInicio()));
            preparedStatement.setDate(4, java.sql.Date.valueOf(congreso.getFechaFin()));
            preparedStatement.setTime(5, java.sql.Time.valueOf(congreso.getHoraFin()));
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ArrayList<Congreso> listaCongresos() {
        ArrayList<Congreso> congresos = new ArrayList<>();
        String sql = "SELECT * FROM congreso";
        try (Connection connection = new ConexionDB().conectarDB();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                int id = resultSet.getInt("id_congreso");

                Congreso congreso = new Congreso(
                        id,
                        nombre,
                        resultSet.getDate("fecha_inicio").toLocalDate(),
                        resultSet.getTime("hora_inicio").toLocalTime(),
                        resultSet.getDate("fecha_fin").toLocalDate(),
                        resultSet.getTime("hora_fin").toLocalTime()
                );
                congresos.add(congreso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // En lugar de mostrar ventana de error, solo registramos el error
            // La vista será responsable de manejar los errores
        }
        return congresos;
    }
}