package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import vista.VentanaError;

public class ConexionDB {
    private String jdbcURL = "jdbc:mysql://localhost:3307/planificacion_congreso?useSSL=false&serverTimezone=UTC";
    private String jdbcUsername = "root";
    private String jdbcPassword = "";

    public Connection conectarDB() throws SQLException {
        return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    }
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                new VentanaError("Error al cerrar la conexión a la base de datos: " + e.getMessage());
            }
        } else {
            new VentanaError("No se pudo cerrar la conexión a la base de datos. Revise las credenciales y la configuración de la base de datos.");
        }
    }
}
