package Bar.view.InterfazPrincipal;

import Bar.model.Card;
import Bar.service.CardService;
import Bar.viewModel.CuentaViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;

public class CuentaManager {
    private final CardService service = new CardService();
    private final FlowPane flowPane;

    public CuentaManager(FlowPane flowPane) {
        this.flowPane = flowPane;
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

            menu.getItems().addAll(cerrar);

            //Función a los botones del menú desplegable
            cerrar.setOnAction(_ -> {
                CuentaViewModel viewModel = (CuentaViewModel) card.getUserData();

                service.CerrarCuentaDB(viewModel.idProperty().getValue());
                flowPane.getChildren().remove(card);
            });

            menu.show(card, e.getScreenX(), e.getScreenY());
        });

        flowPane.getChildren().add(card);
    }

    public int GuardarCuenta(String nombre) {
        return service.GuardarCuentaDB(nombre);
    }
}
