package Bar.service.Admin;

import Bar.db.GestorDB;
import Bar.model.Producto;

import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoAlmacenService {

    public List<Producto> getProductos() {

        Connection conn = GestorDB.getConn();
        List<Producto> list = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM  producto"
        )) {

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                BigDecimal cantidadDB = rs.getBigDecimal("cantidad");
                double precio = rs.getDouble("precio");
                String categoria = rs.getString("categoria");

                if (cantidadDB != null) {
                    double cantidad = cantidadDB.doubleValue();

                    list.add(new Producto(id, nombre, cantidad, precio, categoria));
                }
            }

            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Producto> getInsumos() {

        Connection conn = GestorDB.getConn();
        List<Producto> list = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM  insumo"
        )) {

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double stock = rs.getDouble("stock");

                list.add(new Producto(id, nombre, stock, 0.0, ""));
            }

            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int aggProducto(String nombre, double stock, double precio, String categoria) {
        Connection conn = GestorDB.getConn();
        int existe = 0;

        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT 1 FROM producto WHERE nombre = ? AND precio = ? AND categoria = ?"
        )) {
            stmt.setString(1, nombre);
            stmt.setDouble(2, precio);
            stmt.setString(3, categoria);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                existe = rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (existe != 1) {

            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO producto (nombre, cantidad, precio, categoria) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                stmt.setString(1, nombre);
                stmt.setDouble(2, stock);
                stmt.setDouble(3, precio);
                stmt.setString(4, categoria);

                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();

                if (rs.next()) {
                    return rs.getInt(1);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else {

            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE producto SET cantidad = cantidad + ? WHERE nombre = ? AND precio = ? AND categoria = ?"
            )) {
                stmt.setDouble(1, stock);
                stmt.setString(2, nombre);
                stmt.setDouble(3, precio);
                stmt.setString(4, categoria);

                stmt.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return -1;
    }
}
