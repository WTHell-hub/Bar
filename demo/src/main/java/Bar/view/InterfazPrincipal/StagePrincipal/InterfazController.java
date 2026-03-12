    package Bar.view.InterfazPrincipal.StagePrincipal;

    import Bar.Animaciones.AnimacionesUI;
    import Bar.context.UIContextDep;
    import Bar.model.Card;
    import Bar.view.InterfazPrincipal.Cuentas.CuentaManager;
    import Bar.view.InterfazPrincipal.PanelProductos.ProductoManager;
    import Bar.viewModel.CuentaViewModel;
    import javafx.animation.PauseTransition;
    import javafx.animation.TranslateTransition;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Node;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.input.MouseEvent;
    import javafx.scene.layout.*;
    import javafx.stage.Stage;
    import javafx.stage.StageStyle;
    import javafx.util.Duration;

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
        @FXML private Button btnCerrarSeccion;
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
        @FXML private ToolBar barraInferior;
        @FXML private VBox barraInferiorReducida;
        private boolean barraVisible = false;

        private UIContextDep uiContextDep;

        private CuentaManager cuentaManager;
        private ProductoManager productoManager;


        public void initialize() throws IOException {
            uiContextDep = new UIContextDep(paneCuentas, paneProductos, tilePanePA, buscar, filtro,
                    btnCerrar, btnCerrarSeccion, panelProducto, panelObjTemp, tilePaneObjTemp,
                    btnAceptar, btnCancelar, panelPA, lblNombreCuenta, panelRetirarProducto,
                    txfCantidad, rbHecho, rbNoHecho, txtAreaJustificacion, btnAceptarEliminacion, btnCancelarEliminacion);

            //Creamos ambas, como las dos se necesitan entre sí, lo que hago es ponerle un seter a la primera, ya que si lo paso por el constructor sale una referencia null
            productoManager = new ProductoManager(uiContextDep);
            cuentaManager = new CuentaManager(uiContextDep, productoManager);
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
            //Revisar pq siempre sale el paneObjTemp siempre con cosas
            if (!uiContextDep.getPaneObjTemp().getChildren().isEmpty()) {

                productoManager.VincularProductos();
                productoManager.ActualizarCuentas();
            } else {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Nada para guardar");
                alert.setContentText("La lista está vacía");
                alert.showAndWait();
            }
        }

        @FXML
        private void onCancelarClicked() {
            AnimacionesUI.slideOutToRight(panelObjTemp, 100, 200);
            panelObjTemp.setManaged(false);
            AnimacionesUI.slideOutToRight(panelProducto, 100, 200);
            AnimacionesUI.slideOutToRight(panelPA, 100, 200);
            AnimacionesUI.slideOutToRight(panelRetirarProducto, 100, 200);
            panelRetirarProducto.setManaged(false);
        }

        @FXML
        private void onMouseEnteredBarraInferiorReducida() {
            AnimacionesUI.slideInFromRight(barraInferior, 100, 200);
            barraInferior.setManaged(true);

            AnimacionesUI.slideOutToRight(barraInferiorReducida, 100, 200);
            barraInferiorReducida.setManaged(false);
        }

        @FXML
        private void onMouseExitedBarraInferior() {
            AnimacionesUI.slideInFromRight(barraInferiorReducida, 100, 200);
            barraInferiorReducida.setManaged(true);

            AnimacionesUI.slideOutToRight(barraInferior, 100, 200);
            barraInferior.setManaged(false);
        }

        @FXML
        private void onClosedCuenta(MouseEvent event) {
            // lógica para cerrar la cuenta
        }

        @FXML
        private void onAdmin(MouseEvent event) throws IOException {
            FXMLLoader loader = new FXMLLoader(InterfazController.class.getResource("/Bar/fxml/Login/login.fxml"));
            Parent root = loader.load();

            Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stageActual.setScene(new Scene(root, 800, 600));
            stageActual.setTitle("Login");
            stageActual.centerOnScreen();
        }
    }