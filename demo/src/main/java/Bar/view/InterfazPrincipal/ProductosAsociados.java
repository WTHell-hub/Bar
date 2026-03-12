package Bar.view.InterfazPrincipal;

import Bar.Animaciones.AnimacionesUI;
import Bar.context.UIContextDep;
import Bar.model.Producto;
import Bar.service.Trabajador.CardService;
import Bar.service.Trabajador.ProductoService;
import Bar.service.Trabajador.ProductoXCuentaService;
import Bar.view.InterfazPrincipal.Cuentas.CuentaManager;
import Bar.viewModel.CardPAViewModel;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductosAsociados {
    private final TilePane tilePanePA;
    private final ProductoXCuentaService service = new ProductoXCuentaService();
    private List<Producto> productos = new ArrayList<>();
    private UIContextDep uiContextDep;
    private int idCuenta;
    private CuentaManager cuentaManager;

    public ProductosAsociados(UIContextDep uiContextDep, CuentaManager cuentaManager) {
        this.tilePanePA = uiContextDep.getTilePanePA();
        this.uiContextDep = uiContextDep;
        this.cuentaManager = cuentaManager;
    }

    public void CargarProductosAsociados(int idCuenta) throws IOException {
        productos.clear();
        productos.addAll(service.ObtenerProductos(idCuenta));
        this.idCuenta = idCuenta;

        tilePanePA.getChildren().clear();

        for (Producto p: productos) {
            CardPAViewModel vm = new CardPAViewModel(p);
            MostrarProductos(vm);
        }
    }

    public void MostrarProductos(CardPAViewModel vm) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Bar/fxml/Dependiente/cardPA.fxml"));
        Pane pane = loader.load();

        Label lblNombre = (Label) pane.lookup("#lblNombre");
        lblNombre.textProperty().bind(vm.nombreProperty());

        Label lblCantidad = (Label) pane.lookup("#lblCantidad");
        lblCantidad.textProperty().bind(vm.cantidadProperty().asString());

        Label lblPrecio = (Label) pane.lookup("#lblPrecio");
        lblPrecio.textProperty().bind(vm.precioProperty().asString());

        Label lblCategoria = (Label) pane.lookup("#lblCategoria");
        lblCategoria.textProperty().bind(vm.categoriaProperty());

        pane.setOnContextMenuRequested(e -> {
            ContextMenu menu = new ContextMenu();
            MenuItem eliminar = new MenuItem("Eliminar");
            menu.getItems().add(eliminar);

            eliminar.setOnAction(_ -> {
                VBox panelRetirarProducto = uiContextDep.getPanelRetirarProducto();
                VBox panelObjTemp = uiContextDep.getPaneObjTemp();

                if (panelObjTemp.isVisible()) {
                    AnimacionesUI.slideOutToRight(panelObjTemp, 100, 200);
                    panelObjTemp.setManaged(false);
                }

                panelRetirarProducto.setManaged(true);
                AnimacionesUI.slideInFromRight(panelRetirarProducto, 100, 200);

                ProcesoDeEliminacion(vm.getNombre(), vm.cantidadProperty(), vm.precioProperty());
            });

            menu.show(pane, e.getScreenX(), e.getScreenY());
        });

        tilePanePA.getChildren().add(pane);

        AnimacionesUI.fadeIn(pane, 200);
    }


    private void ProcesoDeEliminacion(String nombre, DoubleProperty vmCantidad, DoubleProperty vmPrecio) {
        Button btnAceptarEliminacion = uiContextDep.getBtnAceptarEliminacion();
        Button btnCancelarEliminacion = uiContextDep.getBtnCancelarEliminacion();
        TextField txfCantidad = uiContextDep.getTxfCantidad();

        LimpiarComponentes();

        btnAceptarEliminacion.setDisable(true);

        txfCantidad.textProperty().addListener((_, _, _) -> {
            try {
                int valorIngresado = Integer.parseInt(txfCantidad.getText());

                if (valorIngresado > 0) {
                    btnAceptarEliminacion.setDisable(false);
                }
            } catch (RuntimeException e) {
                txfCantidad.setText("");
                btnAceptarEliminacion.setDisable(true);
            }
        });

        btnAceptarEliminacion.setOnAction(_ -> Aceptar(nombre, vmCantidad, vmPrecio, Integer.parseInt(txfCantidad.getText())));

        btnCancelarEliminacion.setOnAction(_ -> {
            VBox panelObjTemp = uiContextDep.getPaneObjTemp();
            VBox panelPA = uiContextDep.getPanelPA();
            VBox panelProductos = uiContextDep.getPanelProducto();
            VBox panelRetirarProducto = uiContextDep.getPanelRetirarProducto();

            if (panelObjTemp.isVisible()) {
                AnimacionesUI.slideOutToRight(panelObjTemp, 100, 200);
                panelObjTemp.setManaged(false);
            }

            if (panelPA.isVisible()) {
                AnimacionesUI.slideOutToRight(panelPA, 100, 200);
            }

            if (panelProductos.isVisible()) {
                AnimacionesUI.slideOutToRight(panelProductos, 100, 200);
            }

            if (panelRetirarProducto.isVisible()) {
                AnimacionesUI.slideOutToRight(panelRetirarProducto, 100, 200);
                panelRetirarProducto.setManaged(false);
            }
        });

    }

    public void LimpiarComponentes() {
        TextField txfCantidad = uiContextDep.getTxfCantidad();
        TextArea txtAreaJustificacion = uiContextDep.getTxtAreaJustificacion();

        txfCantidad.setText("");
        txtAreaJustificacion.setText("");
    }

    public void Aceptar(String nombre, DoubleProperty vmCantidad, DoubleProperty vmPrecio, int valorIngresado) {
        TextArea txtAreaJustificacion = uiContextDep.getTxtAreaJustificacion();
        RadioButton rbHecho = uiContextDep.getRbHecho();

        if (rbHecho.isSelected()) {
            if (!txtAreaJustificacion.getText().isEmpty()) {
                //Se encarga de borrar de la tabla detallecuenta para quitar las referencias de los productos a la cuenta
                int idProducto = service.QuitarProductoDeLaCuenta(idCuenta, nombre, valorIngresado);
                //En caso de que los productos que borramos lleguen a 0 este se encarga de borrarlo para que no salga en los productos agregados
                service.BorrarVinculosNulos();

                //Se encarga de bajar el contador de los productos agregados en caso de que no desaparezcan por completo
                vmCantidad.set(vmCantidad.get() - valorIngresado);

                //Se utiliza el service de los productos para poder calcular el total directamente desde la base de datos
                ProductoService productoService = new ProductoService();
                double total = productoService.CalcularTotal(idCuenta);

                CardService cardService = new CardService();

                //Se encarga de Actualizar los totales de las cuentas en la base de datos
                cardService.ActualizarTotal(idCuenta, total);
                //Actualizar total en las cuentas en la interfaz
                cuentaManager.ActualizarTotal(idCuenta, total);

                service.HacerReporte(idCuenta, idProducto, valorIngresado, txtAreaJustificacion.getText());

                //Se retiran los panes, para que se puedan actualizar al volverlos a llamar
                VBox panelRetirarProducto = uiContextDep.getPanelRetirarProducto();
                AnimacionesUI.slideOutToRight(panelRetirarProducto, 100, 200);
                panelRetirarProducto.setManaged(false);

                VBox panePA = uiContextDep.getPanelPA();
                AnimacionesUI.slideOutToRight(panePA, 100, 200);

                VBox panelProducto = uiContextDep.getPanelProducto();
                if (panelProducto.isVisible()) {
                    AnimacionesUI.slideOutToRight(panelProducto, 100, 200);
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("Justificación necesaria");
                alert.setHeaderText("Para los productos ya hechos es necesario una justificación");
                alert.setContentText("Al final debe ir el nombre del trabajador");
                alert.show();
            }

        } else {

            int idProducto = service.QuitarProductoDeLaCuenta(idCuenta, nombre, valorIngresado);
            service.BorrarVinculosNulos();
            vmCantidad.set(vmCantidad.get() - valorIngresado);

            //Se utiliza el service de los productos para poder calcular el total directamente desde la base de datos
            ProductoService productoService = new ProductoService();
            double total = productoService.CalcularTotal(idCuenta);

            CardService cardService = new CardService();

            //Se encarga de Actualizar los totales de las cuentas en la base de datos
            cardService.ActualizarTotal(idCuenta, total);
            //Actualizar total en las cuentas en la interfaz
            cuentaManager.ActualizarTotal(idCuenta, total);

            productoService.DevolverProductos(idProducto, valorIngresado);

            service.HacerReporte(idCuenta, idProducto, valorIngresado, txtAreaJustificacion.getText());

            //Se retiran los panes, para que se puedan actualizar al volverlos a llamar
            VBox panelRetirarProducto = uiContextDep.getPanelRetirarProducto();
            AnimacionesUI.slideOutToRight(panelRetirarProducto, 100, 200);
            panelRetirarProducto.setManaged(false);

            VBox panePA = uiContextDep.getPanelPA();
            AnimacionesUI.slideOutToRight(panePA, 100, 200);

            VBox panelProducto = uiContextDep.getPanelProducto();
            if (panelProducto.isVisible()) {
                AnimacionesUI.slideOutToRight(panelProducto, 100, 200);
            }
        }
    }
}
