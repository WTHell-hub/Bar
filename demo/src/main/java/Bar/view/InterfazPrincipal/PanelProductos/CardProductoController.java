package Bar.view.InterfazPrincipal.PanelProductos;

import Bar.Animaciones.AnimacionesUI;
import Bar.viewModel.ProductoViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.Map;

public class CardProductoController {
    @FXML private Label lblNombre;
    @FXML private Label lblPrecio;
    private Map<String, Object> componentes;

    private ProductoViewModel vm;

    public void setData(ProductoViewModel vm, Map<String, Object> componentes) {
        this.vm = vm;
        lblNombre.textProperty().bind(vm.nombreProperty());
        lblPrecio.textProperty().bind(vm.precioProperty().asString());
        this.componentes = componentes;
    }

    @FXML
    private void onMouseClick(MouseEvent event) {
        VBox panel = (VBox) componentes.get("paneObjTemp");

        if (!panel.isVisible()) {
            AnimacionesUI.slideInFromRight(panel, 50, 150);
        }
    }
}
