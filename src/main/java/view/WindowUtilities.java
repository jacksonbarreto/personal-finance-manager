package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class WindowUtilities {

    static Parent configureWindow(String resource, Stage primaryStage, Class<?> classToLoad) throws IOException {
        if (resource == null || resource.isEmpty() || primaryStage == null || classToLoad == null)
            throw new IllegalArgumentException();

        Parent root = FXMLLoader.load(Objects.requireNonNull(classToLoad.getResource(resource)));
        Scene scene = new Scene(root);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("file:src/main/resources/assets/img/icon.png"));
        primaryStage.show();
        try {
            classToLoad.getMethod("setStage", Stage.class).invoke(classToLoad, primaryStage);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return root;
    }

}
