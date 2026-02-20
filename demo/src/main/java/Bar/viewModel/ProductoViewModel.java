package Bar.viewModel;

import Bar.model.Producto;
import javafx.beans.property.*;

public class ProductoViewModel {
    IntegerProperty id = new SimpleIntegerProperty();
    StringProperty nombre = new SimpleStringProperty();
    DoubleProperty precio = new SimpleDoubleProperty();

    public ProductoViewModel(Producto producto) {
        id.set(producto.getId());
        nombre.set(producto.getNombre());
        precio.set(producto.getPrecio());
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public DoubleProperty precioProperty() {
        return precio;
    }
}
