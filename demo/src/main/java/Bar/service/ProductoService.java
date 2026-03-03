package Bar.service;

import Bar.db.GestorDB;
import Bar.model.Producto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
                BigDecimal cantidadDb = rs.getBigDecimal("cantidad");
                double cantidad  = 0.0;
                if (cantidadDb != null) {
                    cantidad = cantidadDb.doubleValue();
                }
                double precio = Double.parseDouble(rs.getBigDecimal("precio").toString());
                String categoria = rs.getString("categoria");

                if (cantidadDb != null) {
                    productos.add(new Producto(id, nombre, cantidad, precio, categoria));
                } else {

                    PreparedStatement stmtComprobar = conn.prepareStatement(
                            "select min(i.stock / p.cantidad) as disponible\n" +
                                    "from productoinsumo p\n" +
                                    "join insumo i on i.id = p.id_Insumo\n" +
                                    "where p.id_Producto = ?"
                    );

                    stmtComprobar.setInt(1, id);
                    ResultSet rsComprobar = stmtComprobar.executeQuery();

                    if (rsComprobar.next()) {
                        Double disponible = rsComprobar.getObject(1, Double.class);
                        if (disponible == null) {
                            disponible = 0.0;
                        }
                        cantidad = disponible;
                    }


                    productos.add(new Producto(id, nombre, cantidad, precio, categoria));
                }
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

                PreparedStatement stmtCantidad = conn.prepareStatement(
                        "SELECT * FROM productoinsumo WHERE id_Producto = ? LIMIT 1"
                );

                stmtCantidad.setInt(1, p.getId());

                ResultSet rs = stmtCantidad.executeQuery();

                if (!rs.next()) {
                    PreparedStatement stmtNoNull = conn.prepareStatement(
                            "UPDATE producto SET cantidad = cantidad - ? WHERE id = ?"
                    );

                    stmtNoNull.setDouble(1, p.getCantidad());
                    stmtNoNull.setInt(2, p.getId());

                    stmtNoNull.executeUpdate();
                } else {

                    PreparedStatement stmtComprobar = conn.prepareStatement(
                            "select min(i.stock / p.cantidad) as disponible\n" +
                                    "from productoinsumo p\n" +
                                    "join insumo i on i.id = p.id_Insumo\n" +
                                    "where p.id_Producto = ?"
                    );

                    stmtComprobar.setInt(1, p.getId());
                    ResultSet rsDisponible = stmtComprobar.executeQuery();
                    if (rsDisponible.next()) {
                        BigDecimal disponible = rsDisponible.getBigDecimal("disponible");

                        if (disponible.doubleValue() >= p.getCantidad()) {

                            PreparedStatement stmtBuscarCantidad = conn.prepareStatement(
                                    "SELECT id_Insumo, cantidad FROM productoinsumo WHERE id_Producto = ?"
                            );

                            stmtBuscarCantidad.setInt(1, p.getId());

                            ResultSet rsBuscarCantidad = stmtBuscarCantidad.executeQuery();

                            while(rsBuscarCantidad.next()) {
                                int idInsumo = rsBuscarCantidad.getInt("id_Insumo");
                                double stock =  rsBuscarCantidad.getDouble("cantidad");

                                PreparedStatement stmtActualizar = conn.prepareStatement(
                                        "UPDATE insumo SET stock = stock - ? WHERE  id = ?"
                                );

                                stmtActualizar.setDouble(1, (stock * p.getCantidad()));
                                stmtActualizar.setInt(2, idInsumo);

                                stmtActualizar.executeUpdate();
                            }
                        }
                    }

                }
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
