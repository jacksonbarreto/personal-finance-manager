package view;

import bll.exceptions.AccessDeniedBlockedUserException;
import bll.exceptions.AccessDeniedUnconfirmedEmailException;
import bll.services.AuthenticationService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static bll.enumerators.ERole.*;

public class MainController implements Initializable {
    @FXML
    private TextField accessKey;
    @FXML
    private PasswordField password;
    @FXML
    private Label message;
    @FXML
    private Pane minimizeButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        message.setVisible(false);
    }

    public void passwordPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER)
            login();
    }

    public void login() {
        message.setVisible(false);
        resetFieldStyle();
        if (validFields()) {
            try {
                if (AuthenticationService.authenticationServiceDefault().authenticate(accessKey.getText(),
                        password.getText(), ADMIN)) {
                    closeWindow();
                    try {
                        new Dashboard().start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    message.setText("Chave de acesso ou senha inválidos. Tente novamente.");
                }
            } catch (AccessDeniedBlockedUserException e) {
                message.setText("Utilizador bloqueado. Clique em: Esqueceu sua senha?, para recuperar o acesso.");
            } catch (AccessDeniedUnconfirmedEmailException e) {
                message.setText("Ative a sua conta através do e-mail de confirmação enviado.");
            } finally {
                message.setVisible(true);
            }
        }
    }

    public void closeWindow() {
        Main.close();
    }

    public void minimize() {
        Main.minimize();
        minimizeButton.getStyleClass().remove("minimize-button:hover");
    }


    private boolean validFields() {
        if (password.getText().isEmpty() || accessKey.getText().isEmpty()) {
            String alert;
            if (password.getText().isEmpty() && accessKey.getText().isEmpty()) {
                alert = "A chave de acesso e a senha devem ser preenchidos.";
                password.getStyleClass().add("default-field-error");
                accessKey.getStyleClass().add("default-field-error");
            } else if (password.getText().isEmpty()) {
                alert = "A senha deve ser preenchida.";
                password.getStyleClass().add("default-field-error");
            } else {
                alert = "A chave de acesso deve ser preenchida";
                accessKey.getStyleClass().add("default-field-error");
            }
            message.setText(alert);
            message.setVisible(true);
            return false;
        }
        return true;
    }

    private void resetFieldStyle() {
        password.getStyleClass().remove("default-field-error");
        accessKey.getStyleClass().remove("default-field-error");
    }
}
