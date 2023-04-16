package de.platen.steganograph.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Gui extends Application {

    private static final String DATEI = "steganograph.fxml";
    private static final String TITEL = "Steganograph";
    private static final int BREITE = 600;
    private static final int HOEHE = 600;

    public void starteGui(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(DATEI)));
        Scene scene = new Scene(root, BREITE, HOEHE);
        stage.setTitle(TITEL);
        stage.setScene(scene);
        stage.show();
    }
}
