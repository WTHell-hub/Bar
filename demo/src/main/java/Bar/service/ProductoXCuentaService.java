package Bar.service;

import Bar.db.GestorDB;
import Bar.model.Producto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductoXCuentaService {
    public List<Producto> ObtenerProductos(int idCard) {
        List<Producto> list = new ArrayList<>();

        try {
            ResultSet rs = getResultSet(idCard);

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double cantidad = rs.getDouble("cantidad");
                double precio = rs.getBigDecimal("dinero").doubleValue();
                String categoria = rs.getString("categoria");

                list.add(new Producto(id, nombre, cantidad, precio, categoria));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    private static ResultSet getResultSet(int idCard) throws SQLException {
        Connection conn = GestorDB.getConn();

        PreparedStatement stmt = conn.prepareStatement(
                "select p.id, p.nombre, sum(dc.cantidad) as cantidad, sum(dc.subtotal) as dinero, p.categoria\n" +
                        "from detallecuenta dc\n" +
                        "inner join producto p on p.id = dc.id_producto\n" +
                        "where dc.id_cuenta = ?\n" +
                        "group by p.id, p.nombre, p.precio"
        );

        stmt.setInt(1, idCard);

        return stmt.executeQuery();
    }

    public int QuitarProductoDeLaCuenta(int idCuenta, String nombreProducto, int valorIngresado) {
        try {
            Connection conn = GestorDB.getConn();

            PreparedStatement stmtId = conn.prepareStatement(
                    "SELECT id, precio FROM producto WHERE nombre = ?"
            );

            stmtId.setString(1, nombreProducto);
            ResultSet rs = stmtId.executeQuery();

            int idProducto = 0;
            double precio = 0.0;
            if (rs.next()) {
                idProducto = rs.getInt("id");
                precio = rs.getDouble("precio");
            }

            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE detallecuenta SET cantidad = cantidad - ?, subtotal = cantidad * ? WHERE id_cuenta = ? AND id_producto = ? AND cantidad >= ? LIMIT 1"
            );

            stmt.setInt(1, valorIngresado);
            stmt.setDouble(2, precio);
            stmt.setInt(3, idCuenta);
            stmt.setInt(4, idProducto);
            stmt.setInt(5, valorIngresado);

            stmt.executeUpdate();

            return idProducto;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void HacerReporte(int idCuenta, int idProducto, int cantidad, String razon) {
        try {
            Connection conn = GestorDB.getConn();

            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO productodevuelto (id_cuenta, id_producto, cantidad, fecha, razon) VALUES(?,?,?,?,?)"
            );

            stmt.setInt(1, idCuenta);
            stmt.setInt(2, idProducto);
            stmt.setInt(3, cantidad);
            stmt.setDate(4, Date.valueOf(LocalDate.now()));
            stmt.setString(5, razon);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void BorrarVinculosNulos() {
        try {
            Connection conn = GestorDB.getConn();

            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM detallecuenta WHERE cantidad = ?"
            );

            stmt.setInt(1, 0);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
