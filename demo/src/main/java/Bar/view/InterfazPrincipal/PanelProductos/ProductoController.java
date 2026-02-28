package Bar.view.InterfazPrincipal.PanelProductos;

import Bar.Animaciones.AnimacionesUI;
import Bar.context.UIContext;
import Bar.model.Producto;
import Bar.viewModel.ProdTempViewModel;
import Bar.viewModel.ProductoViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ProductoController {
    @FXML private Label lblNombre;
    @FXML private Label lblCantidad;
    @FXML private Label lblPrecio;
    @FXML private Label lblCategoria;

    private TilePane tilePaneObjTemp;
    private UIContext uiContext;
    private ProductoViewModel vm;
    private double EstadoAnterior;

    public void setData(ProductoViewModel vm, UIContext uiContext) {
        lblNombre.textProperty().bind(vm.nombreProperty());
        lblCantidad.textProperty().bind(vm.cantidadProperty().asString());
        lblPrecio.textProperty().bind(vm.precioProperty().asString());
        lblCategoria.textProperty().bind(vm.categoriaProperty());

        this.uiContext = uiContext;
        tilePaneObjTemp = uiContext.getTilePaneObjTemp();
        this.vm = vm;
    }

    @FXML
    private void onMouseClicked(MouseEvent event) throws IOException {
        VBox panel = uiContext.getPaneObjTemp();

        if (Double.parseDouble(lblCantidad.getText()) > 0) {

            if (!panel.isVisible()) {
                AnimacionesUI.slideInFromRight(panel, 50, 150);
            }

            EstadoAnterior = Double.parseDouble(lblCantidad.getText());
            vm.cantidadProperty().set(EstadoAnterior - 1.0);

            MostrarProducto();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setHeaderText("Error");
            alert.setContentText("No queda: "+lblNombre.getText());
            alert.showAndWait();
        }
    }

    public void MostrarProducto() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Bar/fxml/cardPA.fxml"));
        Pane pane = loader.load();

        ProdTempViewModel vmTemp = new ProdTempViewModel(new Producto(vm.getId(), vm.getNombre(), 1, vm.getPrecio(), vm.getCategoria()));

        Label ID = (Label) pane.lookup("#ID");
        ID.textProperty().bind(vmTemp.idProperty().asString());

        Label lblNombre = (Label) pane.lookup("#lblNombre");
        lblNombre.textProperty().bind(vmTemp.nombreProperty());

        Label lblCantidad = (Label) pane.lookup("#lblCantidad");
        lblCantidad.textProperty().bind(vmTemp.cantidadProperty().asString());

        Label lblPrecio = (Label) pane.lookup("#lblPrecio");
        lblPrecio.textProperty().bind(vmTemp.totalProperty().asString());

        pane.setOnContextMenuRequested(event -> {
            ContextMenu menu = new ContextMenu();
            MenuItem eliminar = new MenuItem("Eliminar");

            menu.getItems().add(eliminar);

            eliminar.setOnAction(_ -> {
                Label nombre = (Label) pane.lookup("#lblNombre");

                tilePaneObjTemp.getChildren().removeIf(e -> {

                    if (e.lookup("#lblNombre").equals(nombre)) {
                        Label cantidadEliminada = (Label) e.lookup("#lblCantidad");

                        vm.setCantidad(vm.getCantidad() + Double.parseDouble(cantidadEliminada.getText()));
                    }

                    return e.lookup("#lblNombre").equals(nombre);
                });
            });

            menu.show(pane, event.getScreenX(), event.getScreenY());
        });

        for (int i = 0; i < tilePaneObjTemp.getChildren().size(); i++) {
            Label nombre = (Label) tilePaneObjTemp.getChildren().get(i).lookup("#lblNombre");

            if (nombre.getText().equalsIgnoreCase(vmTemp.getNombre())) {
                Label cantidad = (Label) tilePaneObjTemp.getChildren().get(i).lookup("#lblCantidad");

                double nuevaCantidad = Double.parseDouble(cantidad.getText()) + 1.0;
                vmTemp.cantidadProperty().set(nuevaCantidad);
                vmTemp.totalProperty().set(nuevaCantidad * vmTemp.precioProperty().get());

                tilePaneObjTemp.getChildren().removeIf(e -> e.lookup("#lblNombre").equals(nombre));
            }
        }

        tilePaneObjTemp.getChildren().add(pane);

        AnimacionesUI.slideInFromRight(pane, 100, 200);
    }
}
