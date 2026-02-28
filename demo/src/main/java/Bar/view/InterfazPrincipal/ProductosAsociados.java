package Bar.view.InterfazPrincipal;

import Bar.Animaciones.AnimacionesUI;
import Bar.context.UIContext;
import Bar.model.Producto;
import Bar.service.ProductoXCuentaService;
import Bar.viewModel.CardPAViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductosAsociados {
    private final TilePane tilePanePA;
    private final ProductoXCuentaService service = new ProductoXCuentaService();
    private List<Producto> productos = new ArrayList<>();

    public ProductosAsociados(UIContext uiContext) {
        this.tilePanePA = uiContext.getTilePanePA();
    }

    public void CargarProductosAsociados(int id) throws IOException {
        productos.clear();
        productos.addAll(service.ObtenerProductos(id));

        tilePanePA.getChildren().clear();

        for (Producto p: productos) {
            CardPAViewModel vm = new CardPAViewModel(p);
            MostrarProductos(vm);
        }
    }

    public void MostrarProductos(CardPAViewModel vm) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Bar/fxml/cardPA.fxml"));
        Pane pane = loader.load();

        Label lblNombre = (Label) pane.lookup("#lblNombre");
        lblNombre.textProperty().bind(vm.nombreProperty());

        Label lblCantidad = (Label) pane.lookup("#lblCantidad");
        lblCantidad.textProperty().bind(vm.cantidadProperty().asString());

        Label lblPrecio = (Label) pane.lookup("#lblPrecio");
        lblPrecio.textProperty().bind(vm.precioProperty().asString());

        Label lblCategoria = (Label) pane.lookup("#lblCategoria");
        lblCategoria.textProperty().bind(vm.categoriaProperty());

        tilePanePA.getChildren().add(pane);

        AnimacionesUI.fadeIn(pane, 200);
    }
}
