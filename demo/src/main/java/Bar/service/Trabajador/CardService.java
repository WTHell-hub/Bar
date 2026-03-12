package Bar.service.Trabajador;

import Bar.db.GestorDB;
import Bar.model.Card;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CardService {

    public List<Card> CargarCardDB() {
        List<Card> cards = new ArrayList<>();

        Connection conn = GestorDB.getConn();

        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM cuenta WHERE estado = ?"
        )) {

            stmt.setString(1, "abierta");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                LocalDateTime fecha = rs.getTimestamp("fecha").toLocalDateTime();
                double total = rs.getBigDecimal("total").doubleValue();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

                cards.add(new Card(id, nombre, fecha.format(formatter), total));
            }

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error de base de datos");
            alert.setHeaderText("No se pudo cerrar la cuenta");
            alert.setContentText("Fallo de la comunicación con la base de datos");
            alert.showAndWait();

            throw new RuntimeException(e);
        }

        return cards;
    }

    public int GuardarCuentaDB(String nombre) {
        Connection conn = GestorDB.getConn();

        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO cuenta (nombre, fecha, estado, total) VALUES (?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS
        )) {

            stmt.setString(1, nombre);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(3, "abierta");
            stmt.setDouble(4, 0.0);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int id = -1;
            while (rs.next()) {
                id = rs.getInt(1);
            }

            return id;

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error de base de datos");
            alert.setHeaderText("Error al guardar la cuenta");
            alert.setContentText("Fallo de la comunicación con la base de datos");
            alert.showAndWait();

            throw new RuntimeException(e);
        }

    }

    public void CerrarCuentaDB(Integer id) {
        Connection conn = GestorDB.getConn();

        try (PreparedStatement stmt = conn.prepareStatement(
                "UPDATE cuenta set estado = ? WHERE id = ? AND estado = ?"
        )) {

            stmt.setString(1, "cerrada");
            stmt.setInt(2, id);
            stmt.setString(3, "abierta");

            stmt.executeUpdate();

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error de base de datos");
            alert.setHeaderText("Error al cerrar la cuenta");
            alert.setContentText("Fallo de la comunicación con la base de datos");
            alert.showAndWait();

            throw new RuntimeException(e.getMessage());
        }
    }

    public void ActualizarTotal(int idCuenta, double total) {
        Connection conn = GestorDB.getConn();

        try (PreparedStatement stmt = conn.prepareStatement(
                "UPDATE cuenta SET total = ? WHERE id = ?"
        )) {

            stmt.setDouble(1, total);
            stmt.setInt(2, idCuenta);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
