package Bar.view.Login;

import Bar.app.HelloApplication;
import Bar.service.LoginService;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LoginController {
    @FXML private TextField user;
    @FXML private PasswordField password;
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;

    LoginService service = new LoginService();

    public void initialize() {
        password.setOnAction(this::Loguear);
    }

    @FXML
    public void onAceptar(Event event) {
        Loguear(event);
    }

    @FXML
    public void onCancelar() {
        System.exit(0);
    }

    public void Loguear(Event event) {
        String roll = service.getRoll(user.getText(), password.getText());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        switch(roll) {
            case "Admin": {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Bar/fxml/Admin/interfazAdmin.fxml"));
                try {
                    Parent root = loader.load();

                    stage.setScene(new Scene(root, 800, 600));
                    stage.setTitle("Admin");
                    stage.centerOnScreen();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                break;
            }

            case "Trabajador": {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Bar/fxml/Dependiente/interfaz.fxml"));
                    Parent root = loader.load();

                    stage.setScene(new Scene(root, 800, 600));
                    stage.setTitle(user.getText());
                    stage.centerOnScreen();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }

            default: {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setContentText("Credenciales incorrectas");
                alert.initStyle(StageStyle.UNDECORATED);
                alert.showAndWait();

                user.requestFocus();
            }
        }

        user.setText("");
        password.setText("");
    }

}
