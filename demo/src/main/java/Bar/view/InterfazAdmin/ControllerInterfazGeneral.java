package Bar.view.InterfazAdmin;

import Bar.model.Card;
import Bar.model.Producto;
import Bar.service.Admin.AdminService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ControllerInterfazGeneral {
    @FXML
    private DatePicker fechaInicio;
    @FXML private DatePicker fechaFin;
    @FXML private javafx.scene.control.Button btnActualizarFecha;
    @FXML private Label ventasTotales;
    @FXML private Label ticketPromedio;
    @FXML private Label cuentasActivas;
    @FXML private Label productosVendidos;
    @FXML private Label ultimaActualizacion;
    @FXML private ListView<String> listaAlertas;
    @FXML private TableView<Producto> tableInsumosCriticos;
    @FXML private TableColumn<Producto, String> colInsumo;
    @FXML private TableColumn<Producto, Number> colStock;
    @FXML private TableView<Producto> topProductos;
    @FXML private TableColumn<Producto, String> colProducto;
    @FXML private TableColumn<Producto, Number> colCantidad;
    @FXML private TableColumn<Producto, Number> colTotal;

    AdminService service = new AdminService();

    public void initialize() {
        CargarAlertasInventario();
        CargarInsumosCriticos();
        CargarProductosMasVendidos();

        fechaInicio.setValue(LocalDate.now());
        fechaFin.setValue(LocalDate.now());

        CalcularVentasTotales();
    }

    @FXML
    public void onActualizar() {
        CalcularVentasTotales();
    }

    public void CargarProductosMasVendidos() {
        ObservableList<Producto> list = FXCollections.observableArrayList();

        colProducto.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colCantidad.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty());
        colTotal.setCellValueFactory(cellData -> cellData.getValue().precioProperty());

        topProductos.setItems(list);

        list.addAll(service.getMasVendidos());
    }

    public void CargarInsumosCriticos() {
        ObservableList<Producto> list = FXCollections.observableArrayList();

        colInsumo.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colStock.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty());

        tableInsumosCriticos.setItems(list);

        list.addAll(service.getInsumos());
    }

    public void CargarAlertasInventario() {
        List<String> list = service.getAlertasInventario();

        for (String s: list) {

            listaAlertas.getItems().add(s);
        }

        if (list.isEmpty()) {
            listaAlertas.setPlaceholder(new Label("No hay productos críticos"));
        }
    }

    public void CalcularVentasTotales() {
        LocalDate inicio = fechaInicio.getValue();
        LocalDate fin = fechaFin.getValue();
        List<Card> historial = service.getHistorial();

        historial.removeIf(e -> {
            LocalDateTime fechaAux = LocalDateTime.parse(e.getFecha());
            return fechaAux.isAfter(fin.plusDays(1).atStartOfDay()) || fechaAux.isBefore(inicio.atStartOfDay());
        });

        double totalGanancias = 0.0;

        for (Card c: historial) {
            totalGanancias += c.getTotal();
        }

        ventasTotales.setText(String.valueOf(totalGanancias));

        ultimaActualizacion.setText(LocalDateTime.now().toString());
    }
}
