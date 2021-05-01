package view;

import bll.entities.IMovementCategory;
import bll.repositories.MovementCategoryRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import static bll.entities.MovementCategory.createPublicCategory;
import static view.TextFieldUtilities.addTextLimiter;

public class categoryController implements Initializable {
    @FXML
    private TableView<IMovementCategory> table;
    @FXML
    private TableColumn<IMovementCategory, String> columnCategory;
    @FXML
    private StackPane modal;
    @FXML
    private AnchorPane interactiveModal;
    @FXML
    private AnchorPane resultModel;
    @FXML
    private AnchorPane deleteModal;
    @FXML
    private TextField categoryName;
    @FXML
    private TextField URI;
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
    private Button newCategoryButton;
    @FXML
    private Label nameCategoryLabel;
    @FXML
    private Label uriCategoryLabel;
    @FXML
    private Label confirmDeleteCategory;

    private IMovementCategory currentCategory;
    private ObservableList<IMovementCategory> items;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadLanguage();
        initializesScreens();
        initializesItems();
        addTextLimiter(categoryName, 30);
        enableSaveButton();
        startTable();
    }

    private void loadLanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("lang/messages");
        this.screenTitle.setText(rb.getString("title.screen.category"));
        this.newCategoryButton.setText(rb.getString("newCategoryButton"));
        this.saveButton.setText(rb.getString("saveButton"));
        this.continueButton.setText(rb.getString("continueButton"));
        this.deleteButton.setText(rb.getString("deleteButton"));
        this.cancelButton.setText(rb.getString("cancelButton"));
        this.nameCategoryLabel.setText(rb.getString("nameCategoryLabel"));
        this.uriCategoryLabel.setText(rb.getString("uriCategoryLabel"));
        this.confirmDeleteCategory.setText(rb.getString("confirmDeleteCategory"));
        this.table.setPlaceholder(new Label(rb.getString("tableCategoryEmpty")));
    }

    private void enableSaveButton() {
        this.categoryName.textProperty().addListener(
                (observable) -> saveButton.setDisable(categoryName.getText().length() < 3));
    }

    private void startTable() {
        this.columnCategory.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.table.getColumns().add(
                new TableColumnButtonsFactory<IMovementCategory>("", buttonsFactory()).getColumn());
        this.table.setRowFactory(new TableRowFactory<IMovementCategory>().getRowFactory());
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
        this.items.addAll(MovementCategoryRepository.getInstance().getOnlyPublic());
    }

    private void refreshItems() {
        Iterator<IMovementCategory> it = this.items.iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
        this.items.addAll(MovementCategoryRepository.getInstance().getOnlyPublic());
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
                        this.currentCategory = table.getSelectionModel().getSelectedItem();
                        configureModalForEditing();
                    });

                    trash.setGraphic(new FontIcon("bi-trash"));
                    trash.getStyleClass().add("table-button-danger");
                    trash.setOnAction((ActionEvent event) -> {
                        this.currentCategory = table.getSelectionModel().getSelectedItem();
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
        this.modalTitle.setText(rb.getString("update.category"));
        this.categoryName.setText(currentCategory.getName());
        this.URI.setText(currentCategory.getImage().getPath());
        this.saveButton.setOnAction((ActionEvent event) -> {
            try {
                currentCategory.updateImage(new URI(URI.getText().trim()));
                currentCategory.updateName(categoryName.getText().trim());
                MovementCategoryRepository.getInstance().update(currentCategory);
                interactiveModal.setVisible(false);
                getResultModal(rb.getString("updated.category"), "success-message");
                refreshItems();
            } catch (Exception e) {
                interactiveModal.setVisible(false);
                getResultModal(e.getMessage(), "error-message");
            }
        });
        this.interactiveModal.setVisible(true);
        openModal();
        this.categoryName.requestFocus();
    }

    public void configureModalForNewCategory() {
        ResourceBundle rb = ResourceBundle.getBundle("lang/messages");
        this.modalTitle.setText(rb.getString("new.category"));
        this.categoryName.setText("");
        this.URI.setText("");
        this.saveButton.setOnAction((ActionEvent event) -> {
            try {
                IMovementCategory newCategory = createPublicCategory(this.categoryName.getText().trim(), new URI(this.URI.getText().trim()));
                MovementCategoryRepository.getInstance().add(newCategory);
                this.interactiveModal.setVisible(false);
                getResultModal(rb.getString("created.category"), "success-message");
                refreshItems();
            } catch (Exception e) {
                this.interactiveModal.setVisible(false);
                getResultModal(e.getMessage(), "error-message");
            }
        });
        this.interactiveModal.setVisible(true);
        openModal();
        this.categoryName.requestFocus();
    }

    public void deleteCategory() {
        ResourceBundle rb = ResourceBundle.getBundle("lang/messages");
        try {
            MovementCategoryRepository.getInstance().remove(this.currentCategory);
            refreshItems();
            closeDeleteModal();
            getResultModal(rb.getString("deleted.category"), "success-message");
        } catch (Exception e) {
            getResultModal(e.getMessage(), "error-message");
        }
    }
}
