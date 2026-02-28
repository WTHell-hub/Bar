package Bar.viewModel;

import Bar.model.Producto;
import javafx.beans.property.*;

public class ProdTempViewModel {
    IntegerProperty id = new SimpleIntegerProperty();
    StringProperty nombre = new SimpleStringProperty();
    DoubleProperty cantidad = new SimpleDoubleProperty();
    DoubleProperty precio = new SimpleDoubleProperty();
    DoubleProperty total = new SimpleDoubleProperty();
    StringProperty categoria = new SimpleStringProperty();

    public ProdTempViewModel(Producto producto) {
        id.set(producto.getId());
        nombre.set(producto.getNombre());
        cantidad.set(producto.getCantidad());
        precio.set(producto.getPrecio());
        total.set(producto.getCantidad() * producto.getPrecio());
        categoria.set(producto.getCategoria());
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

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public double getPrecio() {
        return precio.get();
    }

    public void setPrecio(double precio) {
        this.precio.set(precio);
    }

    public double getCantidad() {
        return cantidad.get();
    }

    public DoubleProperty cantidadProperty() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad.set(cantidad);
    }

    public double getTotal() {
        return total.get();
    }

    public DoubleProperty totalProperty() {
        return total;
    }

    public String getCategoria() {
        return categoria.get();
    }

    public StringProperty categoriaProperty() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria.set(categoria);
    }
}
