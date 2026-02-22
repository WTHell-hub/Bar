package Bar.view.InterfazPrincipal.PanelProductos;

import Bar.Animaciones.AnimacionesUI;
import Bar.model.Producto;
import Bar.service.ProductoService;
import Bar.viewModel.ProductoViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoManager {
    private final ProductoService service = new ProductoService();
    private final TilePane tilePane;
    private final Map<String, Object> componentes;
    private List<Producto> productos;

    public ProductoManager(Map<String, Object> componentes) {
        this.tilePane = (TilePane) componentes.get("tilePane");
        this.componentes = componentes;
        productos = service.CargarProductosDB();
    }

    public void CargarProductos() throws IOException {
        if (productos.isEmpty()) {
            productos = service.CargarProductosDB();
        }

        for (Producto p: productos) {
            ProductoViewModel vm = new ProductoViewModel(p);
            MostrarProducto(vm);
        }
    }

    public void MostrarProducto(ProductoViewModel vm) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Bar/fxml/cardProducto.fxml"));
        Pane pane = loader.load();

        CardProductoController controller = loader.getController();

        controller.setData(vm, componentes);

        pane.setOnContextMenuRequested(e -> {
            ContextMenu menu = new ContextMenu();
            MenuItem agregar = new MenuItem("Agregar");

            menu.getItems().add(agregar);

            agregar.setOnAction(_ -> {

            });

            menu.show(pane, e.getScreenX(), e.getScreenY());
        });

        tilePane.getChildren().add(pane);

        AnimacionesUI.slideInFromRight(pane, 150, 500);
    }

    public void BorrarElementos() {
        tilePane.getChildren().clear();
    }
}
