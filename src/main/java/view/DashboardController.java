package view;

import bll.services.SessionService;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import static view.WindowUtilities.loadScreenInCenter;

public class DashboardController implements Initializable {
    @FXML
    private Pane minimizeButton;

    @FXML
    private BorderPane dashboard;

    @FXML
    private Button home;

    private Set<Button> menuButtons;

    private Timeline timeline;


    public void closeWindow() {
        Dashboard.close();
    }

    public void minimize() {
        Dashboard.minimize();
        this.minimizeButton.getStyleClass().remove("minimize-button-dashboard:hover");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.home.getStyleClass().add("menu-button-selected");
        loadScreenInCenter(DashboardController.class, "home", this.dashboard);
        this.menuButtons = new HashSet<>();
        this.menuButtons.add(home);
        autoClose();
    }

    private void autoClose() {
        this.timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (!SessionService.isValid()) {
                this.timeline.stop();
                SessionService.killSession();
                closeWindow();
                try {
                    new Main().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
        this.timeline.setCycleCount(Animation.INDEFINITE);
        this.timeline.play();
        this.dashboard.addEventFilter(MouseEvent.ANY, (EventHandler<Event>) event -> {
            SessionService.keepActive();
            this.timeline.playFromStart();
        });
    }

    public void router(Event event) {
        Button button = (Button) event.getSource();
        this.menuButtons.add(button);
        this.menuButtons.forEach(b -> b.getStyleClass().remove("menu-button-selected"));
        loadScreenInCenter(DashboardController.class, button.getId(), dashboard);
        button.getStyleClass().add("menu-button-selected");
    }
}
