package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    
    // URL de tu base de datos (asegurate de que se llame 'centro_imagenes' en tu MySQL)
    private static final String URL = "jdbc:mysql://localhost:3306/centro_imagenes?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Facu_131005"; // Poné tu contraseña de MySQL si tenés una
    
    private static Connection conexion;

    private ConexionDB() {}

    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("¡Conexión a la base de datos exitosa!");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Error: No se encontró el Driver de MySQL en las librerías.");
            }
        }
        return conexion;
    }

    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}