package Bar.view.InterfazPrincipal.Cuentas;

import Bar.Animaciones.AnimacionesUI;
import Bar.context.UIContextDep;
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

    private UIContextDep uiContextDep;
    private ProductosAsociados productosAsociados;
    private ProductoManager productoManager;
    private CuentaViewModel vm;

    public void setData(CuentaViewModel vm, UIContextDep uiContextDep, ProductoManager productoManager) {
        ID.textProperty().bind(vm.idProperty().asString());
        lblNombre.textProperty().bind(vm.nombreProperty());
        lblFecha.textProperty().bind(vm.fechaProperty());
        lblConsumo.textProperty().bind(vm.totalProperty().asString());

        this.uiContextDep = uiContextDep;
        this.productoManager = productoManager;
        productosAsociados = new ProductosAsociados(uiContextDep, productoManager.getCuentaManager());
        this.vm = vm;
    }

    @FXML
    public void onMouseClicked() {
        VBox panelProducto = uiContextDep.getPanelProducto();
        VBox panelPA = uiContextDep.getPanelPA();

        AnimacionesUI.slideInFromRight(panelProducto, 100, 200);

        try {
            TilePane tilePaneObjTemp = uiContextDep.getTilePaneObjTemp();

            productoManager.BorrarElementos();
            tilePaneObjTemp.getChildren().clear();

            productoManager.CargarProductos(Integer.parseInt(ID.getText()));

            AnimacionesUI.slideInFromRight(panelPA, 100, 400);
            uiContextDep.getLblNombreCuenta().setText("Cuenta: "+lblNombre.getText());
            productosAsociados.CargarProductosAsociados(Integer.parseInt(ID.getText()));

            //Esto lo hacemos para que al cambiar de cuenta funcione también como un cancelar del panel para retirar productos, ya que no se estaría tratando con la nueva cuenta
            VBox panelRetirarProducto = uiContextDep.getPanelRetirarProducto();
            if (panelRetirarProducto.isVisible()) {
                AnimacionesUI.slideOutToRight(panelRetirarProducto, 100, 200);
                panelRetirarProducto.setManaged(false);
            }

            //Se hace para que cuando se cambie de cuenta asegurarse que los objetos temporales sean de esa cuenta
            //además de que se ve feo q el tilePane se quede inmóvil mientras se cambia de cuenta
            VBox paneObjTemp = uiContextDep.getPaneObjTemp();
            if (tilePaneObjTemp.isVisible()) {
                AnimacionesUI.slideOutToRight(paneObjTemp, 100, 200);
                paneObjTemp.setManaged(false);
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void ActualizarTotal(double total) {
        vm.totalProperty().set(total);
    }
}
