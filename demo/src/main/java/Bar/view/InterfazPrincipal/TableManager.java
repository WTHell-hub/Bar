package Bar.view.InterfazPrincipal;

import Bar.model.Producto;
import Bar.viewModel.ProductoViewModel;
import javafx.scene.control.TableView;

public class TableManager {
    private TableView<Producto> lista;

    public TableManager(TableView<Producto> lista) {
        this.lista = lista;
    }

    public void AggProductoALaCuenta(ProductoViewModel vm) {
        int id = Integer.parseInt(vm.idProperty().toString());
        String nombre = String.valueOf(vm.nombreProperty());
        double precio = Double.parseDouble(vm.precioProperty().toString());

        Producto producto = new Producto(id, nombre, precio);

        lista.getItems().add(producto);
    }
}
