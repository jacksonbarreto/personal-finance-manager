package view;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;

import static view.WindowUtilities.configureWindow;


public class Main extends Application {
    private static Stage stage;
    private static double xOffset;
    private static double yOffset;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = configureWindow("/pages/main.fxml", primaryStage, Main.class);

        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

    }

    public static void setStage(Stage stage) {
        Main.stage = stage;
    }

    public static Stage getStage() {
        return stage;
    }

    public static void close() {
        stage.close();
    }

    public static void minimize() {
        stage.setIconified(true);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
