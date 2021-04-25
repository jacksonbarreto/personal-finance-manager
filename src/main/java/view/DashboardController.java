package view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private Pane minimizeButton;



    public void closeWindow(){
        Dashboard.close();
    }
    public void minimize(){
        Dashboard.minimize();
        minimizeButton.getStyleClass().remove("minimize-button-dashboard:hover");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
