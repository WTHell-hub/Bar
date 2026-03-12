package Bar.service;

import Bar.db.GestorDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginService {

    public String getRoll(String nombre, String pass) {
        Connection conn = GestorDB.getConn();

        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT roll FROM usuario WHERE nombre = ? AND password = ?"
        )) {

            stmt.setString(1, nombre);
            stmt.setString(2, pass);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("roll");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return "";
    }
}
