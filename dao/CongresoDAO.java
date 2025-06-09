package dao;
import java.sql.*;
import java.util.ArrayList;
import modelo.Congreso;
import vista.VentanaError;
import vista.VentanaExito;

public class CongresoDAO {
    public void guardarCongreso(Congreso congreso) {
        String sql = "INSERT INTO congreso (nombre) VALUES (?)";
        try (Connection connection = new ConexionDB().conectarDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
             
            preparedStatement.setString(1, congreso.getNombre());
            preparedStatement.executeUpdate();
            new VentanaExito("Congreso guardado exitosamente: " + congreso.getNombre());
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al guardar el congreso: " + e.getMessage());
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
                        nombre
                );
                congresos.add(congreso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new VentanaError("Error al obtener la lista de congresos: " + e.getMessage());
        }
        return congresos;
    }
}