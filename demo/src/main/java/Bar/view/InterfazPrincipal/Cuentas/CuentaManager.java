package Bar.view.InterfazPrincipal.Cuentas;

import Bar.Animaciones.AnimacionesUI;
import Bar.context.UIContextDep;
import Bar.model.Card;
import Bar.service.Trabajador.CardService;
import Bar.view.InterfazPrincipal.PanelProductos.ProductoManager;
import Bar.viewModel.CuentaViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CuentaManager {
    private final CardService service = new CardService();
    private final FlowPane paneCuentas;
    private final UIContextDep uiContextDep;
    private final ProductoManager productoManager;
    private Map<Integer, CuentaController> controllers = new HashMap<>();

    public CuentaManager(UIContextDep uiContextDep, ProductoManager productoManager) {
        this.paneCuentas = uiContextDep.getPaneCuentas();
        this.uiContextDep = uiContextDep;
        this.productoManager = productoManager;
    }

    public void CargarCuenta() {
        List<Card> cards = service.CargarCardDB();

        for (Card c: cards) {
            CuentaViewModel vm = new CuentaViewModel(c);
            try {
                MostrarCard(vm);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void MostrarCard(CuentaViewModel vm) throws IOException {
        //Obtención de recursos
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Bar/fxml/Dependiente/cardCuenta.fxml"));
        Pane card = loader.load();

        //Asignación de valores
        CuentaController controller = loader.getController();

        controller.setData(vm, uiContextDep, productoManager);
        controllers.put(vm.idProperty().get(), controller);

        //Agregar el menú desplegable de cada card
        card.setOnContextMenuRequested(e -> {

            //Creación del menú desplegable y de las funciones que va a tener
            ContextMenu menu = new ContextMenu();
            MenuItem cerrar = new MenuItem("Cerrar");

            menu.getItems().addAll(cerrar);

            //Función a los botones del menú desplegable
            cerrar.setOnAction(_ -> {

                service.CerrarCuentaDB(vm.idProperty().getValue());

                paneCuentas.getChildren().remove(card);

                //Cerrar paneles para que no hallan errores como q se agg productos a cuentas fantasmas o ya cerradas
                AnimacionesUI.slideOutToRight(uiContextDep.getPanelPA(), 100, 200);
                AnimacionesUI.slideOutToRight(uiContextDep.getPanelProducto(), 100, 200);
            });

            menu.show(card, e.getScreenX(), e.getScreenY());
        });

        paneCuentas.getChildren().add(card);

        AnimacionesUI.slideInFromRight(card, 100, -160);
    }

    public int GuardarCuenta(String nombre) {
        return service.GuardarCuentaDB(nombre);
    }

    public void ActualizarTotal(int id, double total) {
        CuentaController controller = controllers.get(id);

        controller.ActualizarTotal(total);
    }

    public void BorrarCuentas() {
        paneCuentas.getChildren().clear();
    }
}