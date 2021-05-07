package view;

import bll.exceptions.AccessDeniedBlockedUserException;
import bll.exceptions.AccessDeniedUnconfirmedEmailException;
import bll.services.AuthenticationService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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

    @FXML
    private Label keyLabel;

    @FXML
    private Label passLabel;

    @FXML
    private Button loginButton;

    @FXML
    private Label titleScreen;

    @FXML
    private Label slogan;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        message.setVisible(false);
        loadLanguage();
    }

    private void loadLanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("lang/messages");
        this.keyLabel.setText(rb.getString("keyLabel.login"));
        this.passLabel.setText(rb.getString("passLabel.login"));
        this.loginButton.setText(rb.getString("loginButton.login"));
        this.titleScreen.setText(rb.getString("titleScreen.login"));
        this.slogan.setText(rb.getString("slogan.login"));
    }

    public void passwordPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER)
            login();
    }

    public void login() {
        message.setVisible(false);
        ResourceBundle rb = ResourceBundle.getBundle("lang/errors");
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
                    message.setText(rb.getString("invalid.data.login"));
                }
            } catch (AccessDeniedBlockedUserException e) {
                message.setText(rb.getString("blocked.user.login"));
            } catch (AccessDeniedUnconfirmedEmailException e) {
                message.setText(rb.getString("inactive.login"));
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
        ResourceBundle rb = ResourceBundle.getBundle("lang/errors");
        if (password.getText().isEmpty() || accessKey.getText().isEmpty()) {
            String alert;
            if (password.getText().isEmpty() && accessKey.getText().isEmpty()) {
                alert = rb.getString("fill.pass.key.login");
                password.getStyleClass().add("default-field-error");
                accessKey.getStyleClass().add("default-field-error");
            } else if (password.getText().isEmpty()) {
                alert = rb.getString("fill.pass.login");
                password.getStyleClass().add("default-field-error");
            } else {
                alert = rb.getString("fill.key.login");
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
