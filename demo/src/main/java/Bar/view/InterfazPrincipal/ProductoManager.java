package Bar.view.InterfazPrincipal;

import Bar.model.Producto;
import Bar.service.ProductoService;
import Bar.viewModel.ProductoViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

import java.io.IOException;
import java.util.List;

public class ProductoManager {
    private final ProductoService service = new ProductoService();
    private final TilePane tilePane;
    private final TableManager tableManager;

    public ProductoManager(TilePane tilePane, TableView<Producto> tableView) {
        this.tilePane = tilePane;
        this.tableManager = new TableManager(tableView);
    }

    public void CargarProductos() throws IOException {
        List<Producto> productos = service.CargarProductosDB();

        for (Producto p: productos) {
            ProductoViewModel vm = new ProductoViewModel(p);
            MostrarProducto(vm);
        }
    }

    public void MostrarProducto(ProductoViewModel vm) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Bar/fxml/cardProducto.fxml"));
        Pane pane = loader.load();

        Label lblNombre = (Label) pane.lookup("#lblNombre");
        lblNombre.textProperty().bind(vm.nombreProperty());

        Label lblPrecio = (Label) pane.lookup("#lblPrecio");
        lblPrecio.textProperty().bind(vm.precioProperty().asString());

        pane.setUserData(vm);

        pane.setOnContextMenuRequested(e -> {
            ContextMenu menu = new ContextMenu();
            MenuItem agregar = new MenuItem("Agregar");

            menu.getItems().add(agregar);

            agregar.setOnAction(_ -> {
                tableManager.AggProductoALaCuenta(vm);
            });

            menu.show(pane, e.getScreenX(), e.getScreenY());
        });

        tilePane.getChildren().add(pane);
    }
}
