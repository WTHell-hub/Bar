package Bar.view.InterfazPrincipal.PanelProductos;

import Bar.Animaciones.AnimacionesUI;
import Bar.context.UIContextDep;
import Bar.model.Producto;
import Bar.viewModel.ProdTempViewModel;
import Bar.viewModel.ProductoViewModel;
import javafx.event.Event;
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
    private UIContextDep uiContextDep;
    private ProductoViewModel vm;

    public void setData(ProductoViewModel vm, UIContextDep uiContextDep) {
        lblNombre.textProperty().bind(vm.nombreProperty());
        lblCantidad.textProperty().bind(vm.cantidadProperty().asString());
        lblPrecio.textProperty().bind(vm.precioProperty().asString());
        lblCategoria.textProperty().bind(vm.categoriaProperty());

        this.uiContextDep = uiContextDep;
        tilePaneObjTemp = uiContextDep.getTilePaneObjTemp();
        this.vm = vm;
    }

    @FXML
    private void onMouseClicked(MouseEvent event) throws IOException {
        VBox panel = uiContextDep.getPaneObjTemp();

        if (Double.parseDouble(lblCantidad.getText()) >= 1) {

            if (!panel.isVisible()) {
                panel.setManaged(true);
                AnimacionesUI.slideInFromRight(panel, 50, 150);
            }

            double estadoAnterior = Double.parseDouble(lblCantidad.getText());
            vm.cantidadProperty().set(estadoAnterior - 1.0);

            MostrarProducto();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setHeaderText("Error");
            alert.setContentText("No queda: "+lblNombre.getText());
            alert.showAndWait();
        }

        VBox panelRetirarProducto = uiContextDep.getPanelRetirarProducto();
        if (panelRetirarProducto.isVisible()) {
            AnimacionesUI.slideOutToRight(panelRetirarProducto, 100, 200);
            panelRetirarProducto.setManaged(false);
        }
    }

    public void MostrarProducto() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Bar/fxml/Dependiente/cardPA.fxml"));
        Pane pane = loader.load();

        ProdTempViewModel vmTemp = new ProdTempViewModel(new Producto(vm.getId(), vm.getNombre(), 1, vm.getPrecio(), vm.getCategoria()));

        Label ID = (Label) pane.lookup("#ID");
        ID.textProperty().bind(vmTemp.idProperty().asString());

        Label lblNombre = (Label) pane.lookup("#lblNombre");
        lblNombre.textProperty().bind(vmTemp.nombreProperty());

        Label lblCantidad = (Label) pane.lookup("#lblCantidad");
        lblCantidad.textProperty().bind(vmTemp.cantidadProperty().asString());

        Label lblCategoria = (Label) pane.lookup("#lblCategoria");
        lblCategoria.textProperty().bind(vmTemp.categoriaProperty());

        Label lblPrecio = (Label) pane.lookup("#lblPrecio");
        lblPrecio.textProperty().bind(vmTemp.totalProperty().asString());

        pane.setOnContextMenuRequested(event -> {
            ContextMenu menu = new ContextMenu();
            MenuItem eliminar = new MenuItem("Eliminar 1");
            MenuItem eliminarTodo = new MenuItem("Eliminar todo");

            menu.getItems().addAll(eliminar, eliminarTodo);

            eliminarTodo.setOnAction(_ -> {

                Label nombre = (Label) pane.lookup("#lblNombre");

                tilePaneObjTemp.getChildren().removeIf(e -> {

                    if (e.lookup("#lblNombre").equals(nombre)) {
                        Label cantidadEliminada = (Label) e.lookup("#lblCantidad");

                        vm.setCantidad(vm.getCantidad() + Double.parseDouble(cantidadEliminada.getText()));
                    }

                    return e.lookup("#lblNombre").equals(nombre);
                });
            });

            eliminar.setOnAction(_ -> {

                if (vmTemp.getCantidad() >= 1) {

                    vmTemp.setCantidad(vmTemp.getCantidad() - 1);

                    vm.setCantidad(vm.getCantidad() + 1);

                    if (vmTemp.getCantidad() < 1) {
                        tilePaneObjTemp.getChildren().removeIf(node -> {
                            Label lblID = (Label) node.lookup("#ID");
                            int id = Integer.parseInt(lblID.getText());

                            return id == vm.getId();
                        });
                    }
                }
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
