    package Bar.view.InterfazPrincipal.StagePrincipal;

    import Bar.Animaciones.AnimacionesUI;
    import Bar.context.UIContext;
    import Bar.model.Card;
    import Bar.model.Producto;
    import Bar.view.InterfazPrincipal.Cuentas.CuentaManager;
    import Bar.view.InterfazPrincipal.PanelProductos.ProductoManager;
    import Bar.viewModel.CuentaViewModel;
    import javafx.fxml.FXML;
    import javafx.scene.Node;
    import javafx.scene.control.*;
    import javafx.scene.input.MouseEvent;
    import javafx.scene.layout.FlowPane;
    import javafx.scene.layout.TilePane;
    import javafx.scene.layout.VBox;
    import javafx.stage.StageStyle;

    import java.io.IOException;
    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;
    import java.util.*;

    public class InterfazController {

        @FXML private FlowPane paneCuentas;
        @FXML private TilePane paneProductos;
        @FXML private TilePane tilePanePA;
        @FXML private TextField buscar;
        @FXML private ComboBox<String> filtro;
        @FXML private Button btnCerrar;
        @FXML private Button  btnAggProducto;
        @FXML private VBox panelProducto;
        @FXML private VBox panelObjTemp;
        @FXML private TilePane tilePaneObjTemp;
        @FXML private Button btnAceptar;
        @FXML private Button btnCancelar;
        @FXML private VBox panelPA;
        @FXML private Label lblNombreCuenta;
        @FXML private VBox panelRetirarProducto;
        @FXML private TextField txfCantidad;
        @FXML private RadioButton rbHecho;
        @FXML private RadioButton rbNoHecho;
        @FXML private TextArea txtAreaJustificacion;
        @FXML private Button btnAceptarEliminacion;
        @FXML private Button btnCancelarEliminacion;

        private UIContext uiContext;

        private CuentaManager cuentaManager;
        private ProductoManager productoManager;


        public void initialize() throws IOException {
            uiContext = new UIContext(paneCuentas, paneProductos, tilePanePA, buscar, filtro,
                    btnCerrar, btnAggProducto, panelProducto, panelObjTemp, tilePaneObjTemp,
                    btnAceptar, btnCancelar, panelPA, lblNombreCuenta, panelRetirarProducto,
                    txfCantidad, rbHecho, rbNoHecho, txtAreaJustificacion, btnAceptarEliminacion, btnCancelarEliminacion);

            //Creamos ambas, como las dos se necesitan entre sí, lo que hago es ponerle un seter a la primera, ya que si lo paso por el constructor sale una referencia null
            productoManager = new ProductoManager(uiContext);
            cuentaManager = new CuentaManager(uiContext, productoManager);
            productoManager.setCuentaManager(cuentaManager);

            cuentaManager.CargarCuenta();

            buscar.textProperty().addListener((obj, oldValue, newValue)-> productoManager.FiltrarProductos(newValue));

            filtro.getItems().addAll("Todos", "Comidas", "Bebidas", "Postres");
            filtro.setValue("Todos");

            filtro.valueProperty().addListener((obj, oldValue, newValue) -> {
                if (newValue.equals("Todos")) {
                    productoManager.MostrarTodosLosProductos();
                } else {
                    productoManager.MostrarConFiltro(newValue);
                }
            });
        }

        @FXML
        public void onAddCardClick() throws IOException {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("Ingrese el nombre de la mesa");
            dialog.setContentText("Nombre:");
            dialog.initStyle(StageStyle.UNDECORATED);

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {

                //Guardamos la cuenta en la base de datos y recogemos id
                int id = cuentaManager.GuardarCuenta(result.get());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String fecha = LocalDateTime.now().format(formatter);

                CuentaViewModel vm = new CuentaViewModel(new Card(id, result.get(), fecha, 0.0));
                cuentaManager.MostrarCard(vm);
            }
        }

        @FXML
        private void onAceptarClicked() {
            productoManager.VincularProductos();
            productoManager.ActualizarCuentas();
        }

        @FXML
        private void onCancelarClicked() {
            AnimacionesUI.slideOutToRight(panelObjTemp, 100, 200);
            AnimacionesUI.slideOutToRight(panelProducto, 100, 200);
            AnimacionesUI.slideOutToRight(panelPA, 100, 200);
            AnimacionesUI.slideOutToRight(panelRetirarProducto, 100, 200);
        }

        @FXML
        private void onClosedCuenta(MouseEvent event) {
            // lógica para cerrar la cuenta
        }

        @FXML
        private void onAddProducto(MouseEvent event) {
            // lógica para sacar la interfaz de los productos
        }
    }