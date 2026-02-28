package Bar.service;

import Bar.db.GestorDB;
import Bar.model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
