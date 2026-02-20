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
                double precio = Double.parseDouble(rs.getBigDecimal("precio").toString());

                productos.add(new Producto(id, nombre, precio));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return productos;
    }
}
