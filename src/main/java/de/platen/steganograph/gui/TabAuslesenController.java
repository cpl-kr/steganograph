package de.platen.steganograph.gui;

import de.platen.steganograph.AktionHolenAusAudio;
import de.platen.steganograph.AktionHolenAusBild;
import de.platen.steganograph.AktionVersteckenInAudio;
import de.platen.steganograph.AktionVersteckenInBild;
import de.platen.steganograph.Aktionen;
import de.platen.steganograph.Verrauschoption;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

public class TabAuslesenController {

    private static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();

    private static final String ABLAGEORT_VERSTECKREGELN = "Ablageort Versteckregeln";
    private static final String ABLAGEORT_MEDIENDATEI = "Ablageort Mediendatei";
    private static final String ABLAGEORT_ZIELVERZEICHNIS = "Ablageort Zielverzeichnis";
    private static final String ABLAGEORT_PRIVATE_KEY = "Ablageort privater Schlüssel";

    @FXML
    private Button buttonAuslesen;
    @FXML
    private TextField textfeldVersteckregeln;
    @FXML
    private TextField textfeldPrivateKeyDatei;
    @FXML
    private TextField textfeldPasswort;
    @FXML
    private TextField textfeldMediendatei;
    @FXML
    private TextField textfeldZielverzeichnis;
    @FXML
    private TextField textfeldZieldatei;

    public void behandleButtonVersteckregeln() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(ABLAGEORT_VERSTECKREGELN);
        final File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            this.textfeldVersteckregeln.setText(file.getAbsolutePath());
        }
        checkButtonAuslesenEnable();
    }

    public void behandleButtonAuswahlMediendatei() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(ABLAGEORT_MEDIENDATEI);
        final File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            this.textfeldMediendatei.setText(file.getAbsolutePath());
        }
        checkButtonAuslesenEnable();
    }

    public void behandleButtonZielverzeichnis() {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(ABLAGEORT_ZIELVERZEICHNIS);
        final File file = directoryChooser.showDialog(null);
        if (file != null) {
            this.textfeldZielverzeichnis.setText(file.getAbsolutePath());
        }
        checkButtonAuslesenEnable();
    }

    public void behandleButtonPrivateKey() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(ABLAGEORT_PRIVATE_KEY);
        final File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            this.textfeldPrivateKeyDatei.setText(file.getAbsolutePath());
        }
    }

    public void behandleButtonVerstecken() {
        String dateiZiel = "";
        if (this.textfeldZieldatei.getText().isEmpty()) {
            dateiZiel = this.textfeldZielverzeichnis.getText();
        } else {
            dateiZiel = this.textfeldZielverzeichnis.getText() + FILE_SEPARATOR + this.textfeldZieldatei.getText();
        }
        final Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(), new AktionHolenAusBild(), new AktionHolenAusAudio());
        try {
            if (this.textfeldPrivateKeyDatei.getText().isEmpty()) {
                aktionen.hole(this.textfeldVersteckregeln.getText(), this.textfeldMediendatei.getText(), dateiZiel);
            } else {
                aktionen.hole(this.textfeldVersteckregeln.getText(), this.textfeldMediendatei.getText(), dateiZiel, this.textfeldPrivateKeyDatei.getText(), this.textfeldPasswort.getText());
            }
        } catch(IOException | RuntimeException e) {
            final Alert alert = new Alert(Alert.AlertType.ERROR, "Fehler beim Auslesen der Daten: " + e.getMessage() + ".", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void checkButtonAuslesenEnable() {
        if (!this.textfeldVersteckregeln.getText().isEmpty() &&
                !this.textfeldMediendatei.getText().isEmpty() &&
                !this.textfeldZielverzeichnis.getText().isEmpty()) {
            this.buttonAuslesen.setDisable(false);
        } else {
            this.buttonAuslesen.setDisable(false);
        }
    }
}
