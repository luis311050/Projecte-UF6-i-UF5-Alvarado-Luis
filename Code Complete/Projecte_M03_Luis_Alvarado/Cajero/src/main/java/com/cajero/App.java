package com.cajero;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(Parent root) {
        scene.setRoot(root);
    }

    static TertiaryController getTertiaryController() {
        Scene currentScene = scene;
        if (currentScene != null) {
            Parent root = currentScene.getRoot();
            if (root != null) {
                Object controller = root.getProperties().get("controller");
                if (controller instanceof TertiaryController) {
                    return (TertiaryController) controller;
                }
            }
        }
        return null;
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent root = fxmlLoader.load();
        root.getProperties().put("controller", fxmlLoader.getController());
        return root;
    }

    public static void main(String[] args) {
        launch();
    }
}
