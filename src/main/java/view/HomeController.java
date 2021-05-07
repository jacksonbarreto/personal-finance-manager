package view;

import bll.entities.IUser;
import bll.repositories.UserRepository;
import bll.services.SessionService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static bll.enumerators.ERole.*;

public class HomeController implements Initializable {

    @FXML
    private Label screenTitle;

    @FXML
    private Label totCount;

    @FXML
    private Label totLabel;

    @FXML
    private Label adminCount;

    @FXML
    private Label adminLabel;

    @FXML
    private Label simpleCount;

    @FXML
    private Label simpleLabel;

    @FXML
    private Label premiumCount;

    @FXML
    private Label premiumLabel;

    @FXML
    private Label helloLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label salutationLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadLanguage();
        loadData();
    }

    private void loadLanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("lang/messages");
        this.screenTitle.setText(rb.getString("title.screen.home"));
        this.helloLabel.setText(rb.getString("helloLabel.home"));
        this.salutationLabel.setText(rb.getString("salutationLabel.home"));
        this.adminLabel.setText(rb.getString("adminLabel.home"));
        this.simpleLabel.setText(rb.getString("simpleLabel.home"));
        this.premiumLabel.setText(rb.getString("premiumLabel.home"));
        this.totLabel.setText(rb.getString("totLabel.home"));
    }

    private void loadData() {
        this.usernameLabel.setText(SessionService.getCurrentUser().getName());
        List<IUser> users = new ArrayList<>(UserRepository.getInstance().getAll());

        this.adminCount.setText(Long.toString(users.stream().filter(u -> u.getRoles().contains(ADMIN)).count()));
        this.premiumCount.setText(Long.toString(users.stream().filter(u -> u.getRoles().contains(PREMIUM)).count()));
        this.simpleCount.setText(Long.toString(users.stream().filter(u -> u.getRoles().contains(SIMPLE)).count()));
        this.totCount.setText(Long.toString(users.size()));
    }
}
