package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Configuración para SQL Server
    private static final String URL = "jdbc:sqlserver://localhost:49766;databaseName=Facturas;trustServerCertificate=true";
    private static final String USUARIO = "sa";
    private static final String PASSWORD = "molina98fuentes"; // Cambiar por tu contraseña

    public static Connection getConnection() throws SQLException {
        try {
            // Driver para SQL Server
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver de SQL Server no encontrado: " + e.getMessage());
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}
