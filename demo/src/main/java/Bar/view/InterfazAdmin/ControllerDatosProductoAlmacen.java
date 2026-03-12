package Bar.view.InterfazAdmin;

import Bar.Animaciones.AnimacionesUI;
import Bar.model.Producto;
import Bar.service.Admin.ProductoAlmacenService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class ControllerDatosProductoAlmacen {
    @FXML private Button btnNuevoProducto;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private TextField textBuscar;
    @FXML private TableView<Producto> tableAlmacen;
    @FXML private TableColumn<Producto, Number> colID;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, Number> colCantidad;
    @FXML private TableColumn<Producto, Number> colPrecio;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TextField txtNombre;
    @FXML private TextField txtStock;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtCategoria;
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;
    @FXML private VBox panelLateralAggProducto;

    ProductoAlmacenService service = new ProductoAlmacenService();
    ObservableList<Producto> list = FXCollections.observableArrayList();

    public void initialize() {
        CargarTabla();
    }

    public void CargarTabla() {

        colID.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colCantidad.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty());
        colPrecio.setCellValueFactory(cellData -> cellData.getValue().precioProperty());
        colCategoria.setCellValueFactory(cellData -> cellData.getValue().categoriaProperty());

        tableAlmacen.setItems(list);

        list.addAll(service.getProductos());
    }

    @FXML
    public void onNuevoProducto() {
        panelLateralAggProducto.setManaged(true);
        AnimacionesUI.slideInFromRight(panelLateralAggProducto, 100, 200);

        txtNombre.requestFocus();
    }

    @FXML
    public void onEliminar() {
        System.out.println(tableAlmacen.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void onAceptarProducto() {
        try {Double.parseDouble(txtStock.getText());} catch (NumberFormatException e) {throw new RuntimeException(e);}
        try {Double.parseDouble(txtPrecio.getText());} catch (NumberFormatException e) {throw new RuntimeException(e);}

        String nombre = txtNombre.getText();
        double stock = Double.parseDouble(txtStock.getText());
        double precio = Double.parseDouble(txtPrecio.getText());
        String categoria = txtCategoria.getText();

        int idGenerado = service.aggProducto(nombre, stock, precio, categoria);

        boolean existia = false;
        for (Producto p: list) {
            if (p.getNombre().equalsIgnoreCase(nombre) && p.getPrecio() == precio && p.getCategoria().equals(categoria)) {
                p.setCantidad(p.getCantidad() + stock);
                existia = true;
            }
        }

        if (!existia) {
            list.add(new Producto(idGenerado, nombre, stock, precio, categoria));
        }

        AnimacionesUI.slideOutToRight(panelLateralAggProducto, 100, 200);
        panelLateralAggProducto.setManaged(false);
    }

    @FXML
    public void onCancelarProducto() {
        AnimacionesUI.slideOutToRight(panelLateralAggProducto, 100, 200);
        panelLateralAggProducto.setManaged(false);
    }
}
