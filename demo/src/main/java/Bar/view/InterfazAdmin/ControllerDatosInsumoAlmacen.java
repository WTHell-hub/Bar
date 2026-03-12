package Bar.view.InterfazAdmin;

import Bar.model.Producto;
import Bar.service.Admin.ProductoAlmacenService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ControllerDatosInsumoAlmacen {
    @FXML private Button btnNuevoProducto;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private TextField textBuscar;
    @FXML private TableView<Producto> tableAlmacen;
    @FXML private TableColumn<Producto, Number> colID;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, Number> colCantidad;

    ProductoAlmacenService service = new ProductoAlmacenService();
    ObservableList<Producto> list = FXCollections.observableArrayList();

    public void initialize() {
        CargarTabla();
    }

    public void CargarTabla() {

        colID.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colCantidad.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty());

        tableAlmacen.setItems(list);

        list.addAll(service.getInsumos());
    }
}
