package Bar.viewModel;

import Bar.model.Producto;
import javafx.beans.property.*;

public class TilePanePAModel {
    IntegerProperty id = new SimpleIntegerProperty();
    StringProperty nombre = new SimpleStringProperty();
    DoubleProperty precio = new SimpleDoubleProperty();

    public TilePanePAModel(Producto producto) {
        id.set(producto.getId());
        nombre.set(producto.getNombre());
        precio.set(producto.getPrecio());
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getNombre() {
        return nombre.get();
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public double getPrecio() {
        return precio.get();
    }

    public DoubleProperty precioProperty() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio.set(precio);
    }
}
