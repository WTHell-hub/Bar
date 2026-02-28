package Bar.view.InterfazPrincipal.Cuentas;

import Bar.Animaciones.AnimacionesUI;
import Bar.context.UIContext;
import Bar.view.InterfazPrincipal.PanelProductos.ProductoManager;
import Bar.view.InterfazPrincipal.ProductosAsociados;
import Bar.viewModel.CuentaViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CuentaController {
    @FXML private Label ID;
    @FXML private Label lblNombre;
    @FXML private Label lblFecha;
    @FXML private Label lblConsumo;

    private UIContext uiContext;
    private ProductosAsociados productosAsociados;
    private ProductoManager productoManager;
    private CuentaViewModel vm;

    public void setData(CuentaViewModel vm, UIContext uiContext, ProductoManager productoManager) {
        ID.textProperty().bind(vm.idProperty().asString());
        lblNombre.textProperty().bind(vm.nombreProperty());
        lblFecha.textProperty().bind(vm.fechaProperty());
        lblConsumo.textProperty().bind(vm.totalProperty().asString());

        this.uiContext = uiContext;
        this.productoManager = productoManager;
        productosAsociados = new ProductosAsociados(uiContext);
        this.vm = vm;
    }

    @FXML
    public void onMouseClicked() {
        VBox panelProducto = uiContext.getPanelProducto();
        VBox panelPA = uiContext.getPanelPA();

        AnimacionesUI.slideInFromRight(panelProducto, 100, 200);

        try {
            TilePane tilePaneObjTemp = uiContext.getTilePaneObjTemp();

            productoManager.BorrarElementos();
            tilePaneObjTemp.getChildren().clear();

            productoManager.CargarProductos(Integer.parseInt(ID.getText()));

            AnimacionesUI.slideInFromRight(panelPA, 100, 400);
            uiContext.getLblNombreCuenta().setText("Cuenta: "+lblNombre.getText());
            productosAsociados.CargarProductosAsociados(Integer.parseInt(ID.getText()));

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void ActualizarTotal(double total) {
        vm.totalProperty().set(total);
    }
}
