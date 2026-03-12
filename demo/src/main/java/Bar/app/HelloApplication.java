package Bar.app;

import Bar.db.GestorDB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Optional;

public class HelloApplication extends Application{
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/Bar/fxml/Login/login.fxml"));
        Parent root = loader.load();

        stage.setScene(new Scene(root, 400, 450));
        stage.setTitle("Login");
        stage.initStyle(StageStyle.UNDECORATED);

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getBounds();

        stage.setY(bounds.getMinY());
        stage.setX(bounds.getMinX());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());

//        PrevenirAlCerrar(stage);
        GestorDB.live();

        stage.show();
    }

    public void PrevenirAlCerrar(Stage stage) {
        stage.setOnCloseRequest(event -> {

            //Alerta para salir de la aplicación
            Alert alertSalir = new Alert(Alert.AlertType.CONFIRMATION);

            alertSalir.initStyle(StageStyle.UNDECORATED);
            alertSalir.setHeaderText("Warning");
            alertSalir.setContentText("Desea salir de la aplicación?");

            Optional<ButtonType> resultSalir = alertSalir.showAndWait();
            if (resultSalir.isPresent() && resultSalir.get() == ButtonType.OK) {

                GestorDB.cerrar();

            } else if (resultSalir.isPresent() && resultSalir.get() == ButtonType.CANCEL) {
                event.consume();
            }
        });
    }

}