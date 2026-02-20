package Bar.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GestorDB {
    private static String url = "jdbc:mysql://localhost:3306/bar";
    private static String user = "usuarioBar";
    private static String password = "123";
    private static Connection conn = null;

    public static void conectar() {
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConn() {
        try {
            if (conn == null || conn.isClosed()) {
                conectar();
            }
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void cerrar() {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void live() {
        conectar();
    }
}
