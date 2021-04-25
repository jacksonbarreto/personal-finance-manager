package view;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;

import static view.WindowUtilities.configureWindow;

public class Dashboard extends Application {
    private static Stage stage;
    private static double xOffset;
    private static double yOffset;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = configureWindow("/pages/dashboard.fxml", primaryStage, Dashboard.class);

        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
    }

    public static void setStage(Stage stage){
        Dashboard.stage = stage;
    }

    public static Stage getStage(){
        return stage;
    }
    public static void close(){stage.close();}
    public static void minimize(){stage.setIconified(true);}
}
