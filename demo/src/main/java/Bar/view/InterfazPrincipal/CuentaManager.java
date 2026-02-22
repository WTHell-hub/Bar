package Bar.view.InterfazPrincipal;

import Bar.Animaciones.AnimacionesUI;
import Bar.model.Card;
import Bar.service.CardService;
import Bar.view.InterfazPrincipal.PanelProductos.ProductoManager;
import Bar.viewModel.CuentaViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CuentaManager {
    private final CardService service = new CardService();
    private final FlowPane flowPane;
    private final Map<String, Object> componentes;

    public CuentaManager(Map<String, Object> componentes) {
        this.flowPane = (FlowPane) componentes.get("flowPane");
        this.componentes = new HashMap<>(componentes);
    }

    public void CargarCuenta() throws IOException {
        List<Card> cards = service.CargarCardDB();

        for (Card c: cards) {
            CuentaViewModel vm = new CuentaViewModel(c);
            MostrarCard(vm);
        }
    }

    public void MostrarCard(CuentaViewModel vm) throws IOException {
        //Obtención de recursos
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Bar/fxml/cardCuenta.fxml"));
        Pane card = loader.load();

        //Asignación de valores
        Label lblNombre = (Label) card.lookup("#lblNombre");
        lblNombre.textProperty().bind(vm.nombreProperty());

        Label lblFecha = (Label) card.lookup("#lblFecha");
        lblFecha.textProperty().bind(vm.fechaProperty());

        Label lblConsumo = (Label) card.lookup("#lblConsumo");
        lblConsumo.textProperty().bind(vm.totalProperty().asString());

        card.setUserData(vm);

        //Agregar el menú desplegable de cada card
        card.setOnContextMenuRequested(e -> {

            //Creación del menú desplegable y de las funciones que va a tener
            ContextMenu menu = new ContextMenu();
            MenuItem cerrar = new MenuItem("Cerrar");
            MenuItem agregar = new MenuItem("Agregar");

            menu.getItems().addAll(cerrar, agregar);

            //Función a los botones del menú desplegable
            cerrar.setOnAction(_ -> {
                CuentaViewModel viewModel = (CuentaViewModel) card.getUserData();

                service.CerrarCuentaDB(viewModel.idProperty().getValue());
                flowPane.getChildren().remove(card);
            });

            agregar.setOnAction(_ -> {
                VBox panelProducto = (VBox) componentes.get("panelProducto");
                ProductoManager productoManager = (ProductoManager) componentes.get("productoManager");

                AnimacionesUI.slideInFromRight(panelProducto, 500, 300);

                try {
                    productoManager.BorrarElementos();

                    productoManager.CargarProductos();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            menu.show(card, e.getScreenX(), e.getScreenY());
        });

        flowPane.getChildren().add(card);

        AnimacionesUI.slideInFromRight(card, 100, -160);
    }

    public int GuardarCuenta(String nombre) {
        return service.GuardarCuentaDB(nombre);
    }
}
