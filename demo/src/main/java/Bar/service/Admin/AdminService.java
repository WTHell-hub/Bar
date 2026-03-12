package Bar.service.Admin;

import Bar.db.GestorDB;
import Bar.model.Card;
import Bar.model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdminService {

    public List<Card> getHistorial() {
     List<Card> historial = new ArrayList<>();

        try {
            Connection conn = GestorDB.getConn();

            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM cuenta"
            )) {

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    LocalDateTime fecha = rs.getTimestamp("fecha").toLocalDateTime();
                    String estado = rs.getString("estado");
                    double total = rs.getBigDecimal("total").doubleValue();

                    historial.add(new Card(id, nombre, fecha.toString(), total));
                }
            }

            return historial;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getAlertasInventario() {
        List<String> list = new ArrayList<>();

        try {
            Connection conn = GestorDB.getConn();

            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT nombre FROM producto WHERE cantidad < 30"
            )) {

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    list.add(rs.getString("nombre"));
                }
            }

            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Producto> getInsumos() {
        List<Producto> list = new ArrayList<>();

        try {
            Connection conn = GestorDB.getConn();

            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM insumo"
            )) {
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    double stock = rs.getDouble("stock");

                    list.add(new Producto(id, nombre, stock, 0.0, ""));
                }

                return list;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Producto> getMasVendidos() {
        List<Producto> list = new ArrayList<>();

        try {
            Connection conn = GestorDB.getConn();

            try (PreparedStatement stmt = conn.prepareStatement(
                    "select p.nombre, sum(d.cantidad) as maximo, sum(d.cantidad * p.precio) as total\n" +
                            "from detallecuenta d\n" +
                            "inner join producto p on p.id = d.id_producto\n" +
                            "group by p.nombre\n" +
                            "order by maximo desc"
            )) {
                ResultSet rs = stmt.executeQuery();

                while(rs.next()) {
                    String nombre = rs.getString("nombre");
                    double maximo = rs.getDouble("maximo");
                    Double total = rs.getDouble("total");

                    list.add(new Producto(0, nombre, maximo, total, ""));
                }

                return list;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
