package Bar.view.InterfazPrincipal;

import Bar.model.Card;
import Bar.model.Producto;
import Bar.viewModel.CuentaViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class InterfazController {

    @FXML private FlowPane flowPane;
    @FXML private TilePane listaProductos;
    @FXML private TableView<Producto> table;
    @FXML private TextField buscar;
    @FXML private ComboBox<String> filtro;
    @FXML private Button btnCerrar;
    @FXML private Button  btnAggProducto;

    private CuentaManager cuentaManager;
    private ProductoManager productoManager;


    public void initialize() throws IOException {
        cuentaManager = new CuentaManager(flowPane);
        cuentaManager.CargarCuenta();

        productoManager = new ProductoManager(listaProductos);
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