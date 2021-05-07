package view;

import bll.entities.FormOfPayment;
import bll.entities.IFormOfPayment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static bll.repositories.FormOfPaymentRepository.defaultFormOfPaymentRepository;
import static view.TextFieldUtilities.addTextLimiter;

public class FormOfPaymentController implements Initializable {

    @FXML
    private TableView<IFormOfPayment> table;
    @FXML
    private TableColumn<IFormOfPayment, String> columnFormOfPayment;
    @FXML
    private StackPane modal;
    @FXML
    private AnchorPane interactiveModal;
    @FXML
    private AnchorPane resultModel;
    @FXML
    private AnchorPane deleteModal;
    @FXML
    private TextField formOfPaymentName;

    @FXML
    private Label modalTitle;
    @FXML
    private Button saveButton;
    @FXML
    private Button continueButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label resultMessage;
    @FXML
    private Label screenTitle;
    @FXML
    private Button newFormOfPaymentButton;
    @FXML
    private Label nameFormOfPaymentLabel;

    @FXML
    private Label confirmDeleteFormOfPayment;

    private IFormOfPayment currentFormOfPayment;
    private ObservableList<IFormOfPayment> items;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadLanguage();
        initializesScreens();
        initializesItems();
        addTextLimiter(formOfPaymentName, 30);
        enableSaveButton();
        startTable();
    }

    private void loadLanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("lang/messages");
        this.screenTitle.setText(rb.getString("title.screen.formOfPayment"));
        this.newFormOfPaymentButton.setText(rb.getString("newFormOfPaymentButton"));
        this.saveButton.setText(rb.getString("saveButton"));
        this.continueButton.setText(rb.getString("continueButton"));
        this.deleteButton.setText(rb.getString("deleteButton"));
        this.cancelButton.setText(rb.getString("cancelButton"));
        this.nameFormOfPaymentLabel.setText(rb.getString("nameFormOfPaymentLabel"));
        this.confirmDeleteFormOfPayment.setText(rb.getString("confirmDeleteFormOfPayment"));
        this.table.setPlaceholder(new Label(rb.getString("tableFormOfPaymentEmpty")));
    }

    private void enableSaveButton() {
        this.formOfPaymentName.textProperty().addListener(
                (observable) -> saveButton.setDisable(this.formOfPaymentName.getText().length() < 3));
    }

    private void startTable() {
        this.columnFormOfPayment.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.table.getColumns().add(
                new TableColumnButtonsFactory<IFormOfPayment>("", buttonsFactory()).getColumn());
        this.table.setRowFactory(new TableRowFactory<IFormOfPayment>().getRowFactory());
        this.table.setItems(this.items);
    }

    private void initializesScreens() {
        this.modal.setVisible(false);
        this.interactiveModal.setVisible(false);
        this.resultModel.setVisible(false);
        this.deleteModal.setVisible(false);
    }

    private void initializesItems() {
        this.items = FXCollections.observableArrayList();
        this.items.addAll(defaultFormOfPaymentRepository().getAll().stream().sorted().collect(Collectors.toList()));
    }

    private void refreshItems() {
        Iterator<IFormOfPayment> it = this.items.iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
        this.items.addAll(defaultFormOfPaymentRepository().getAll().stream().sorted().collect(Collectors.toList()));
        this.table.refresh();
    }

    private Supplier<HBox> buttonsFactory() {
        return
                () -> {
                    final Button edit = new Button();
                    final Button trash = new Button();
                    final HBox buttons = new HBox(edit, trash);

                    edit.setGraphic(new FontIcon("bi-pencil-square"));
                    edit.getStyleClass().add("table-button");
                    edit.setOnAction((ActionEvent event) -> {
                        this.currentFormOfPayment = table.getSelectionModel().getSelectedItem();
                        configureModalForEditing();
                    });

                    trash.setGraphic(new FontIcon("bi-trash"));
                    trash.getStyleClass().add("table-button-danger");
                    trash.setOnAction((ActionEvent event) -> {
                        this.currentFormOfPayment = table.getSelectionModel().getSelectedItem();
                        getDeleteModal();
                    });
                    buttons.setStyle("-fx-alignment: bottom-right; -fx-spacing: 10px;");
                    return buttons;
                };
    }

    private void openModal() {
        this.modal.setVisible(true);
    }

    public void closeModal() {
        this.modal.setVisible(false);
    }

    public void closeInteractiveModal() {
        this.interactiveModal.setVisible(false);
        closeModal();
    }

    public void activateWithEnter(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER)
            this.saveButton.fire();
    }

    public void closeResultModal() {
        this.resultModel.setVisible(false);
        closeModal();
    }

    public void closeDeleteModal() {
        this.deleteModal.setVisible(false);
        closeModal();
    }

    private void getResultModal(String message, String classCSS) {
        this.resultMessage.setText(message);
        this.resultMessage.getStyleClass().clear();
        this.resultMessage.getStyleClass().add(classCSS);
        openModal();
        this.resultModel.setVisible(true);
    }

    private void getDeleteModal() {
        openModal();
        this.deleteModal.setVisible(true);
    }

    private void configureModalForEditing() {
        ResourceBundle rb = ResourceBundle.getBundle("lang/messages");
        this.modalTitle.setText(rb.getString("update.formOfPayment"));
        this.formOfPaymentName.setText(this.currentFormOfPayment.getName());
        this.saveButton.setOnAction((ActionEvent event) -> {
            try {
                this.currentFormOfPayment.updateName(this.formOfPaymentName.getText().trim());
                defaultFormOfPaymentRepository().update(this.currentFormOfPayment);
                interactiveModal.setVisible(false);
                getResultModal(rb.getString("updated.formOfPayment"), "success-message");
                refreshItems();
            } catch (Exception e) {
                interactiveModal.setVisible(false);
                getResultModal(e.getMessage(), "error-message");
            }
        });
        this.interactiveModal.setVisible(true);
        openModal();
        this.formOfPaymentName.requestFocus();
    }

    public void configureModalForNewCategory() {
        ResourceBundle rb = ResourceBundle.getBundle("lang/messages");
        this.modalTitle.setText(rb.getString("new.formOfPayment"));
        this.formOfPaymentName.setText("");
        this.saveButton.setOnAction((ActionEvent event) -> {
            try {
                IFormOfPayment newFormOfPayment = new FormOfPayment(this.formOfPaymentName.getText().trim());
                defaultFormOfPaymentRepository().add(newFormOfPayment);
                this.interactiveModal.setVisible(false);
                getResultModal(rb.getString("created.formOfPayment"), "success-message");
                refreshItems();
            } catch (Exception e) {
                this.interactiveModal.setVisible(false);
                getResultModal(e.getMessage(), "error-message");
            }
        });
        this.interactiveModal.setVisible(true);
        openModal();
        this.formOfPaymentName.requestFocus();
    }

    public void deleteCategory() {
        ResourceBundle rb = ResourceBundle.getBundle("lang/messages");
        try {
            defaultFormOfPaymentRepository().remove(this.currentFormOfPayment);
            refreshItems();
            closeDeleteModal();
            getResultModal(rb.getString("deleted.formOfPayment"), "success-message");
        } catch (Exception e) {
            getResultModal(e.getMessage(), "error-message");
        }
    }
}
