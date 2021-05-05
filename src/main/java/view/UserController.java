package view;

import bll.entities.IUser;
import bll.repositories.UserRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static bll.enumerators.ERole.*;

public class UserController implements Initializable {
    @FXML
    private Label screenTitle;

    @FXML
    private Pagination pagination;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<IUser> table;

    @FXML
    private StackPane modal;

    @FXML
    private AnchorPane editModal;

    @FXML
    private AnchorPane resultModel;

    @FXML
    private Label resultMessage;

    @FXML
    private Label modalEditTitle;

    @FXML
    private Button saveButton;

    @FXML
    private CheckBox adminChekBox;

    @FXML
    private CheckBox premiumChekBox;

    @FXML
    private CheckBox simpleChekBox;

    @FXML
    private Button searchButton;

    private ObservableList<IUser> itemsObservable;
    private IUser currentUser;
    private final int rowsPerPAge = 5;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadLanguage();
        initializesItems();
        initializesScreens();
        startTable();
        this.pagination.setPageFactory(this::createPage);
    }


    private void loadLanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("lang/messages");
        this.screenTitle.setText(rb.getString("title.screen.user"));
        this.modalEditTitle.setText(rb.getString("title.modal.editPermission.user"));
        this.searchField.setPromptText(rb.getString("prompt.searchField.user"));
        this.saveButton.setText(rb.getString("saveButton"));
        this.adminChekBox.setText(rb.getString("adminChekBox"));
        this.premiumChekBox.setText(rb.getString("premiumChekBox"));
        this.simpleChekBox.setText(rb.getString("simpleChekBox"));
    }

    private void startTable() {
        this.table = new TableView<>();
        TableColumn<IUser, String> columnName = new TableColumn<>();
        TableColumn<IUser, String> columnEmail = new TableColumn<>();
        TableColumn<IUser, HBox> ColumnButtons = new TableColumnButtonsFactory<IUser>("", buttonsFactory()).getColumn();
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnEmail.setCellValueFactory(edf -> new SimpleStringProperty(edf.getValue().getEmail().getEmail()));
        //this.table.getColumns().addAll(columnName, columnEmail, ColumnButtons);
        this.table.getColumns().add(columnName);
        this.table.getColumns().add(columnEmail);
        this.table.getColumns().add(ColumnButtons);
        this.table.setRowFactory(new TableRowFactory<IUser>().getRowFactory());
        this.table.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/table.css")).toExternalForm());
        columnName.setStyle("-fx-pref-width: 370px;");
        columnEmail.setStyle("-fx-pref-width: 220px;");
        this.table.setStyle("-fx-pref-width: 640px; -fx-min-width: 640px;");
        ResourceBundle rb = ResourceBundle.getBundle("lang/messages");
        this.table.setPlaceholder(new Label(rb.getString("tableUsersEmpty")));
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * this.rowsPerPAge;
        int toIndex = Math.min(fromIndex + rowsPerPAge, this.itemsObservable.size());
        this.table.setItems(FXCollections.observableList(this.itemsObservable.subList(fromIndex, toIndex)));
        this.pagination.setPageCount(getPageCount());
        this.pagination.setMaxPageIndicatorCount(getMaxPageIndicatorCount());
        return this.table;
    }

    private int getMaxPageIndicatorCount() {
        return Math.min((int) Math.ceil((double) this.itemsObservable.size() / this.rowsPerPAge), 10) == 0 ? 1 : Math.min((int) Math.ceil((double) this.itemsObservable.size() / this.rowsPerPAge), 10);
    }

    private int getPageCount() {
        return (int) Math.ceil((double) this.itemsObservable.size() / this.rowsPerPAge) == 0 ? 1 : (int) Math.ceil((double) this.itemsObservable.size() / this.rowsPerPAge);
    }

    private void initializesItems() {
        this.itemsObservable = FXCollections.observableList(new ArrayList<>(UserRepository.getInstance().getAll()));
    }

    private void initializesScreens() {
        this.modal.setVisible(false);
        this.editModal.setVisible(false);
        this.resultModel.setVisible(false);
    }

    public void closeEditModal() {
        this.modal.setVisible(false);
        this.editModal.setVisible(false);
    }

    public void closeMessageModal() {
        this.modal.setVisible(false);
        this.resultModel.setVisible(false);
    }

    private Supplier<HBox> buttonsFactory() {
        return
                () -> {
                    final Button edit = new Button();
                    final HBox buttons = new HBox(edit);

                    edit.setGraphic(new FontIcon("bi-shield-lock-fill"));
                    edit.getStyleClass().add("table-button");
                    edit.setOnAction((ActionEvent event) -> {
                        this.currentUser = table.getSelectionModel().getSelectedItem();
                        configureModalForEditing();
                    });

                    buttons.setStyle("-fx-alignment: bottom-right; -fx-spacing: 10px;");
                    return buttons;
                };
    }

    private void configureModalForEditing() {
        this.modal.setVisible(true);
        this.editModal.setVisible(true);
        this.adminChekBox.setSelected(this.currentUser.getRoles().contains(ADMIN));
        this.premiumChekBox.setSelected(this.currentUser.getRoles().contains(PREMIUM));
        this.simpleChekBox.setSelected(this.currentUser.getRoles().contains(SIMPLE));
    }

    public void activateWithEnter(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER)
            this.searchButton.fire();
    }

    public void search() {
        Predicate<IUser> predicate = u -> u.getCredential().getAccessKeys().stream().anyMatch(s -> s.matches("(?i).*" + this.searchField.getText() + ".*"));
        refreshItems();
        this.itemsObservable.addAll(FXCollections.observableList(new ArrayList<>(UserRepository.getInstance().get(predicate))));
        this.pagination.setPageFactory(this::createPage);
    }

    private void refreshItems() {
        Iterator<IUser> it = this.itemsObservable.iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    public void save() {
        try {
            if (this.premiumChekBox.isSelected()) {
                if (!this.currentUser.getRoles().contains(PREMIUM))
                    this.currentUser.addRole(PREMIUM);
            } else {
                if (this.currentUser.getRoles().contains(PREMIUM))
                    this.currentUser.removeRole(PREMIUM);
            }

            if (this.adminChekBox.isSelected()) {
                if (!this.currentUser.getRoles().contains(ADMIN))
                    this.currentUser.addRole(ADMIN);
            }
                else {
                if (this.currentUser.getRoles().contains(ADMIN))
                    this.currentUser.removeRole(ADMIN);
            }

                if (this.simpleChekBox.isSelected()){
                    if (!this.currentUser.getRoles().contains(SIMPLE))
                        this.currentUser.addRole(SIMPLE);
                }else {
                    if (this.currentUser.getRoles().contains(SIMPLE))
                        this.currentUser.removeRole(SIMPLE);
                }

            ResourceBundle rb = ResourceBundle.getBundle("lang/messages");
            UserRepository.getInstance().update(this.currentUser);
            getResultModal(rb.getString("updated.permission.user"), "success-message");
        } catch (Exception e) {
            getResultModal(e.getMessage(), "error-message");
        } finally {
            this.editModal.setVisible(false);
        }

    }

    private void getResultModal(String message, String classCSS) {
        this.resultMessage.setText(message);
        this.resultMessage.getStyleClass().clear();
        this.resultMessage.getStyleClass().add(classCSS);
        this.resultModel.setVisible(true);
    }

}
