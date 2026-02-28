package Bar.service;

import Bar.db.GestorDB;
import Bar.model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {

    public List<Producto> CargarProductosDB() {
        List<Producto> productos = new ArrayList<>();

        try {
            Connection conn = GestorDB.getConn();

            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM producto"
            );

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double cantidad = rs.getDouble("cantidad");
                double precio = Double.parseDouble(rs.getBigDecimal("precio").toString());
                String categoria = rs.getString("categoria");

                productos.add(new Producto(id, nombre, cantidad, precio, categoria));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return productos;
    }

    //El subtotal está calculado desde la interfaz de productos, ya que está ya lo va calculando y lo entrega en él p.getPrecio
    public Connection VincularProductos(int id, List<Producto> lista) {
        Connection conn = null;

        try {
            conn = GestorDB.getConn();
            conn.setAutoCommit(false);

            for (Producto p: lista) {
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO detallecuenta (id_cuenta, id_producto, cantidad, subtotal) VALUES (?,?,?,?)"
                );

                stmt.setInt(1, id);
                stmt.setInt(2, p.getId());
                stmt.setDouble(3, p.getCantidad());
                stmt.setDouble(4, (p.getPrecio()));

                stmt.executeUpdate();
            }

            return conn;

        } catch (SQLException e) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public void RestarProductos(Connection conn, List<Producto> lista) {
        try {
            for (Producto p: lista) {
                PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE producto SET cantidad = cantidad - ? WHERE id = ?"
                );

                stmt.setDouble(1, p.getCantidad());
                stmt.setInt(2, p.getId());

                stmt.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);

        } catch (SQLException e) {

            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                throw new RuntimeException(e.getMessage());
            }

            throw new RuntimeException(e);

        } finally {

            try {
                conn.setAutoCommit(true);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public double CalcularTotal(int id) {
        try {
            Connection conn = GestorDB.getConn();

            PreparedStatement stmt = conn.prepareStatement(
                    "select sum(dc.cantidad * p.precio) as total\n" +
                            "from detallecuenta dc\n" +
                            "inner join producto p on p.id = dc.id_producto\n" +
                            "where dc.id_cuenta = ?"
            );

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            double total = 0;

            if (rs.next()) {
                total = rs.getDouble("total");
            }

            return total;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
