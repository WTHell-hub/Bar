package Bar.viewModel;

import Bar.model.Producto;
import javafx.beans.property.*;

public class CardPAViewModel {
    StringProperty nombre = new SimpleStringProperty();
    DoubleProperty cantidad = new SimpleDoubleProperty();
    DoubleProperty precio = new SimpleDoubleProperty();
    StringProperty categoria = new SimpleStringProperty();

    public CardPAViewModel(Producto producto) {
        nombre.set(producto.getNombre());
        cantidad.set(producto.getCantidad());
        precio.set(producto.getPrecio());
        categoria.set(producto.getCategoria());
    }

    public DoubleProperty cantidadProperty() {
        return cantidad;
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public DoubleProperty precioProperty() {
        return precio;
    }

    public double getCantidad() {
        return cantidad.get();
    }

    public void setCantidad(int cantidad) {
        this.cantidad.set(cantidad);
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

    public void setCantidad(double cantidad) {
        this.cantidad.set(cantidad);
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
