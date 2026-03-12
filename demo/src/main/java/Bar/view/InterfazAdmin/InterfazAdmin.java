package Bar.view.InterfazAdmin;

import Bar.Animaciones.AnimacionesUI;
import Bar.context.UiContextAdm;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class InterfazAdmin {
    @FXML private BorderPane ventanaInformacion;
    @FXML private VBox panelLateralReducido;
    @FXML private VBox panelLateral;


    UiContextAdm uiContextAdm;

    public void initialize() {
        CargarFXML("/Bar/fxml/Admin/general.fxml");
    }

    @FXML
    public void onProductosInterfaz() {
        CargarFXML("/Bar/fxml/Admin/productos.fxml");
    }

    @FXML
    public void onGeneralInterfaz() {
        CargarFXML("/Bar/fxml/Admin/general.fxml");
    }

    @FXML
    public void onInsumosInterfaz() {
        CargarFXML("/Bar/fxml/Admin/insumos.fxml");
    }

    @FXML
    public void onMouseEntered() {
        AnimacionesUI.slideOutToRight(panelLateralReducido, 100, -200);
        panelLateralReducido.setManaged(false);

        AnimacionesUI.slideInFromRight(panelLateral, 100, - 200);
        panelLateral.setManaged(true);
    }

    @FXML
    public void onMouseExited() {
        AnimacionesUI.slideOutToRight(panelLateral, 100, -200);
        panelLateral.setManaged(false);

        AnimacionesUI.slideInFromRight(panelLateralReducido, 100, -200);
        panelLateralReducido.setManaged(true);
    }

    @FXML
    public void onSalir(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Bar/fxml/Login/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root, 400, 450));
            stage.setTitle("Login");
            stage.centerOnScreen();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void CargarFXML(String ruta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
            Node vista = loader.load();

            ventanaInformacion.setCenter(vista);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}