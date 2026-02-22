    package Bar.view.InterfazPrincipal;

    import Bar.model.Card;
    import Bar.view.InterfazPrincipal.PanelProductos.ProductoManager;
    import Bar.viewModel.CuentaViewModel;
    import javafx.fxml.FXML;
    import javafx.scene.control.*;
    import javafx.scene.input.MouseEvent;
    import javafx.scene.layout.FlowPane;
    import javafx.scene.layout.TilePane;
    import javafx.scene.layout.VBox;
    import javafx.stage.StageStyle;

    import java.io.IOException;
    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.Optional;

    public class InterfazController {

        @FXML private FlowPane flowPane;
        @FXML private TilePane tilePane;
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

        private final Map<String, Object> componentes = new HashMap<>();

        private CuentaManager cuentaManager;
        private ProductoManager productoManager;
        private TilePanePAManager tilePanePAManager;


        public void initialize() throws IOException {
            componentes.put("flowPane", flowPane);
            componentes.put("tilePane", tilePane);
            componentes.put("tilePanePA", tilePanePA);
            componentes.put("panelProducto", panelProducto);
            componentes.put("buscar", buscar);
            componentes.put("filtro", filtro);
            componentes.put("paneObjTemp", panelObjTemp);
            componentes.put("tilePaneObjTemp", tilePaneObjTemp);
            componentes.put("btnAceptar", btnAceptar);
            componentes.put("btnCancelar", btnCancelar);

            productoManager = new ProductoManager(componentes);
            componentes.put("productoManager", productoManager);
            cuentaManager = new CuentaManager(componentes);


            cuentaManager.CargarCuenta();
            productoManager.CargarProductos();
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
        private void onClosedCuenta(MouseEvent event) {
            // lógica para cerrar la cuenta
        }

        @FXML
        private void onAddProducto(MouseEvent event) {
            // lógica para sacar la interfaz de los productos
        }

    }