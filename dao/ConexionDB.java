package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
                // Solo registrar el error, no mostrar ventanas desde la capa DAO
                e.printStackTrace();
            }
        }
    }
}
