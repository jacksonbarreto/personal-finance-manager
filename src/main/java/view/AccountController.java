package view;

import bll.entities.ICredential;
import bll.entities.IUser;
import bll.repositories.UserRepository;
import bll.services.SessionService;
import bll.valueObjects.Email;
import bll.valueObjects.IEmail;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

import static view.TextFieldUtilities.addTextLimiter;

public class AccountController implements Initializable {

    @FXML
    private Label screenTitle;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField nameInput;

    @FXML
    private Label emailLabel;

    @FXML
    private TextField emailInput;

    @FXML
    private Label accessKeyLabel;

    @FXML
    private TextField usernameInput;

    @FXML
    private Label passwordLabel;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private Label confirmPasswordLabel;

    @FXML
    private PasswordField confirmPasswordInput;

    @FXML
    private Button saveButton;

    @FXML
    private StackPane modal;

    @FXML
    private AnchorPane messageScreen;

    @FXML
    private Label resultMessage;

    private String currentUsername;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.currentUsername = SessionService.getCurrentUser().getCredential().getAccessKeys().stream().filter(k -> !k.equals(SessionService.getCurrentUser().getName()) && !k.equals(SessionService.getCurrentUser().getEmail().getEmail())).findAny().orElse("");
        initializeScreens();
        setLimitInTextField();
        loadLanguage();
        enableSaveButton();
        observerTrim();
    }

    private void initializeScreens() {
        modal.setVisible(false);
        messageScreen.setVisible(false);
    }

    private void setLimitInTextField() {
        addTextLimiter(nameInput, IUser.MAXIMUM_NAME_SIZE);
        addTextLimiter(usernameInput, ICredential.MAXIMUM_ACCESS_KEY_SIZE);
        addTextLimiter(emailInput, 255);
        fillFields();
    }

    private void loadLanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("lang/messages");
        this.screenTitle.setText(rb.getString("title.screen.account"));
        this.nameLabel.setText(rb.getString("nameLabel.account"));
        this.emailLabel.setText(rb.getString("emailLabel.account"));
        this.accessKeyLabel.setText(rb.getString("username.account"));
        this.passwordLabel.setText(rb.getString("password.account"));
        this.confirmPasswordLabel.setText(rb.getString("confirm.password.account"));
        this.saveButton.setText(rb.getString("saveButton"));
        this.nameInput.setPromptText(rb.getString("name.prompt.account"));
        this.usernameInput.setPromptText(rb.getString("usernameInput.prompt.account"));
        this.passwordInput.setPromptText(rb.getString("passwordInput.prompt.account"));
        this.confirmPasswordInput.setPromptText(rb.getString("confirmPasswordInput.prompt.account"));
    }

    private void trim(TextField textField) {
        if (!textField.isFocused() && !textField.getText().isEmpty())
            if (textField.getText().charAt(0) == ' ' || textField.getText().charAt(textField.getText().length() - 1) == ' ')
                textField.setText(textField.getText().trim());
    }

    private void observerTrim() {
        this.nameInput.focusedProperty().addListener((observable) -> trim(nameInput));
        this.emailInput.focusedProperty().addListener((observable) -> trim(emailInput));
        this.usernameInput.focusedProperty().addListener((observable) -> trim(usernameInput));
        this.passwordInput.focusedProperty().addListener((observable) -> trim(passwordInput));
        this.confirmPasswordInput.focusedProperty().addListener((observable) -> trim(confirmPasswordInput));
    }

    private void enableSaveButton() {

        this.nameInput.textProperty().addListener((observable) -> saveButton.setDisable(fieldsAreInvalids()));

        this.usernameInput.textProperty().addListener(
                (observable) -> saveButton.setDisable(fieldsAreInvalids()));

        this.emailInput.textProperty().addListener(
                (observable) -> saveButton.setDisable(fieldsAreInvalids()));

        this.passwordInput.textProperty().addListener(
                (observable) -> saveButton.setDisable(fieldsAreInvalids()));

        this.confirmPasswordInput.textProperty().addListener(
                (observable) -> saveButton.setDisable(fieldsAreInvalids()));
    }

    private boolean fieldsAreInvalids() {
        return
                this.nameInput.getText().trim().length() < IUser.MINIMUM_NAME_SIZE ||
                        isPasswordInvalid() ||
                        IEmail.isPlainTextEmailInvalid(this.emailInput.getText().trim()) ||
                        this.usernameInput.getText().trim().isEmpty();
    }

    private boolean isPasswordInvalid() {
        return (this.passwordInput.getText().trim().length() < ICredential.MINIMUM_PASSWORD_SIZE &&
                !this.passwordInput.getText().trim().isEmpty()) ||
                !this.passwordInput.getText().trim().equals(this.confirmPasswordInput.getText());
    }

    private void fillFields() {
        this.nameInput.setText(SessionService.getCurrentUser().getName());
        this.emailInput.setText(SessionService.getCurrentUser().getEmail().getEmail());
        this.usernameInput.setText(this.currentUsername);
        this.passwordInput.setText("");
        this.confirmPasswordInput.setText("");
    }

    public void closeModal() {
        modal.setVisible(false);
        messageScreen.setVisible(false);
    }

    private void getMessageScreen(String message, String classCSS) {
        this.resultMessage.setText(message);
        this.resultMessage.getStyleClass().clear();
        this.resultMessage.getStyleClass().add(classCSS);
        modal.setVisible(true);
        messageScreen.setVisible(true);
    }


    public void save() {
        IUser currentUser = SessionService.getCurrentUser();
        IEmail newEmail = new Email(this.emailInput.getText());
        currentUser.updateName(this.nameInput.getText());
        currentUser.updateEmail(newEmail);
        currentUser.getCredential().removeAccessKey(this.currentUsername);
        currentUser.getCredential().addAccessKey(this.usernameInput.getText());
        if (!this.passwordInput.getText().isEmpty())
            currentUser.getCredential().updatePassword(this.passwordInput.getText());
        try {
            UserRepository.getInstance().update(currentUser);
            ResourceBundle rb = ResourceBundle.getBundle("lang/messages");
            getMessageScreen(rb.getString("updated.account"), "success-message");
            fillFields();
        } catch (Exception e) {
            getMessageScreen(e.getMessage(), "error-message");
        }
    }
}
