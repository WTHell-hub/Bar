package Bar.service.Trabajador;

import Bar.db.GestorDB;
import Bar.model.Producto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {

    public List<Producto> CargarProductosDB() {
        List<Producto> productos = new ArrayList<>();

        Connection conn = GestorDB.getConn();

        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM producto"
        )) {

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
        Connection conn = GestorDB.getConn();

        try {
            conn.setAutoCommit(false);

            for (Producto p: lista) {
                try (PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO detallecuenta (id_cuenta, id_producto, cantidad, subtotal) VALUES (?,?,?,?)"
                )) {

                    stmt.setInt(1, id);
                    stmt.setInt(2, p.getId());
                    stmt.setDouble(3, p.getCantidad());
                    stmt.setDouble(4, (p.getPrecio()));

                    stmt.executeUpdate();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
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

                try (PreparedStatement stmtCantidad = conn.prepareStatement(
                        "SELECT * FROM productoinsumo WHERE id_Producto = ? LIMIT 1"
                )) {

                    stmtCantidad.setInt(1, p.getId());

                    ResultSet rs = stmtCantidad.executeQuery();

                    if (!rs.next()) {
                        try (PreparedStatement stmtNoNull = conn.prepareStatement(
                                "UPDATE producto SET cantidad = cantidad - ? WHERE id = ?"
                        )) {

                            stmtNoNull.setDouble(1, p.getCantidad());
                            stmtNoNull.setInt(2, p.getId());

                            stmtNoNull.executeUpdate();

                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                    } else {

                        try (PreparedStatement stmtComprobar = conn.prepareStatement(
                                "select min(i.stock / p.cantidad) as disponible\n" +
                                        "from productoinsumo p\n" +
                                        "join insumo i on i.id = p.id_Insumo\n" +
                                        "where p.id_Producto = ?"
                        )) {

                            stmtComprobar.setInt(1, p.getId());
                            ResultSet rsDisponible = stmtComprobar.executeQuery();
                            if (rsDisponible.next()) {
                                BigDecimal disponible = rsDisponible.getBigDecimal("disponible");

                                if (disponible.doubleValue() >= p.getCantidad()) {

                                    try (PreparedStatement stmtBuscarCantidad = conn.prepareStatement(
                                            "SELECT id_Insumo, cantidad FROM productoinsumo WHERE id_Producto = ?"
                                    )) {

                                        stmtBuscarCantidad.setInt(1, p.getId());

                                        ResultSet rsBuscarCantidad = stmtBuscarCantidad.executeQuery();

                                        while(rsBuscarCantidad.next()) {
                                            int idInsumo = rsBuscarCantidad.getInt("id_Insumo");
                                            double stock =  rsBuscarCantidad.getDouble("cantidad");

                                            try (PreparedStatement stmtActualizar = conn.prepareStatement(
                                                    "UPDATE insumo SET stock = stock - ? WHERE  id = ?"
                                            )) {

                                                stmtActualizar.setDouble(1, (stock * p.getCantidad()));
                                                stmtActualizar.setInt(2, idInsumo);

                                                stmtActualizar.executeUpdate();

                                            } catch (SQLException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
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
        }
    }

    public double CalcularTotal(int id) {
        try (Connection conn = GestorDB.getConn()) {

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

    public void DevolverProductos(int idProducto, double valorIngresado) {
        Connection conn = null;
        try {
            conn = GestorDB.getConn();
            conn.setAutoCommit(false);

            PreparedStatement stmtVerificar = conn.prepareStatement(
                    "SELECT * FROM producto WHERE id = ?"
            );

            stmtVerificar.setInt(1, idProducto);
            ResultSet rsVerificar = stmtVerificar.executeQuery();

            BigDecimal cantidadDB = null;
            if (rsVerificar.next()) {
                cantidadDB = rsVerificar.getBigDecimal("cantidad");
            }

            if (cantidadDB == null) {
                PreparedStatement stmtObtenerCantidad = conn.prepareStatement(
                        "SELECT * FROM productoinsumo WHERE id_Producto = ?"
                );

                stmtObtenerCantidad.setInt(1, idProducto);
                ResultSet rsCantidad = stmtObtenerCantidad.executeQuery();

                while(rsCantidad.next()) {
                    int idInsumo = rsCantidad.getInt("id_Insumo");
                    double cantidadMinima = rsCantidad.getDouble("cantidad");

                    PreparedStatement stmtUpdateInsumo = conn.prepareStatement(
                            "UPDATE insumo SET stock = stock + ? WHERE id = ?"
                    );

                    stmtUpdateInsumo.setDouble(1, (cantidadMinima * valorIngresado));
                    stmtUpdateInsumo.setInt(2, idInsumo);

                    stmtUpdateInsumo.executeUpdate();
                }

            } else {
                PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE producto SET cantidad = cantidad + ? WHERE id = ?"
                );

                stmt.setDouble(1, valorIngresado);
                stmt.setInt(2, idProducto);

                stmt.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);

        } catch (SQLException e) {

            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            throw new RuntimeException(e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
