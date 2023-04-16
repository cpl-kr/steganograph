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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

public class TabVersteckenController {

    private static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();

    private static final String ABLAGEORT_VERSTECKREGELN = "Ablageort Versteckregeln";
    private static final String ABLAGEORT_PRIVATE_KEY = "Ablageort privater Schlüssel";
    private static final String ABLAGEORT_MEDIENDATEI_QUELLE = "Ablageort Mediendatei Quelle";
    private static final String ABLAGEORT_QUELLDATEI = "Ablageort Quelldatei";
    private static final String ABLAGEORT_VERZEICHNIS_MEDIENDATEI = "Verzeichnis Mediendatei Ziel";

    @FXML
    private Button buttonVerstecken;
    @FXML
    private TextField textfeldVersteckregeln;
    @FXML
    private TextField textfeldPrivateKeyDatei;
    @FXML
    private TextField textfeldPasswort;
    @FXML
    private TextField textfeldQuelldatei;
    @FXML
    private TextField textfeldMediendateiQuelle;
    @FXML
    private TextField textfeldMedienverzeichnis;
    @FXML
    private TextField textfeldMediendateiZiel;
    @FXML
    private RadioButton radioButtonNutzdaten;
    @FXML
    private RadioButton radioButtonAlles;

    public void onKey() {
        checkButtonVersteckenEnable();
    }

    public void behandleButtonVersteckregeln() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(ABLAGEORT_VERSTECKREGELN);
        final File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            this.textfeldVersteckregeln.setText(file.getAbsolutePath());
        }
        checkButtonVersteckenEnable();
    }

    public void behandleButtonPrivateKey() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(ABLAGEORT_PRIVATE_KEY);
        final File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            this.textfeldPrivateKeyDatei.setText(file.getAbsolutePath());
        }
    }

    public void behandleButtonQuelldatei() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(ABLAGEORT_QUELLDATEI);
        final File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            this.textfeldQuelldatei.setText(file.getAbsolutePath());
        }
        checkButtonVersteckenEnable();
    }

    public void behandleButtonMediendateiQuelle() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(ABLAGEORT_MEDIENDATEI_QUELLE);
        final File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            this.textfeldMediendateiQuelle.setText(file.getAbsolutePath());
        }
        checkButtonVersteckenEnable();
    }

    public void behandleButtonMedienverzeichnis() {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(ABLAGEORT_VERZEICHNIS_MEDIENDATEI);
        final File file = directoryChooser.showDialog(null);
        if (file != null) {
            this.textfeldMedienverzeichnis.setText(file.getAbsolutePath());
        }
        checkButtonVersteckenEnable();
    }

    public void behandleButtonVerstecken() {
        final String dateiZiel = this.textfeldMedienverzeichnis.getText() + FILE_SEPARATOR + this.textfeldMediendateiZiel.getText();
        Verrauschoption verrauschoption = Verrauschoption.OHNE;
        if (this.radioButtonNutzdaten.isSelected()) {
            verrauschoption = Verrauschoption.NUTZDATENBEREICH;
        }
        if (this.radioButtonAlles.isSelected()) {
            verrauschoption = Verrauschoption.ALLES;
        }
        final Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(), new AktionHolenAusBild(), new AktionHolenAusAudio());
        try {
            if (this.textfeldPrivateKeyDatei.getText().isEmpty()) {
                aktionen.verstecke(this.textfeldVersteckregeln.getText(), this.textfeldQuelldatei.getText(), this.textfeldMediendateiQuelle.getText(), dateiZiel, verrauschoption);
            } else {
                aktionen.verstecke(this.textfeldVersteckregeln.getText(), this.textfeldQuelldatei.getText(), this.textfeldMediendateiQuelle.getText(), dateiZiel, verrauschoption, this.textfeldPrivateKeyDatei.getText(), this.textfeldPasswort.getText());
            }
        } catch(IOException | RuntimeException e) {
            final Alert alert = new Alert(Alert.AlertType.ERROR, "Fehler beim Verstecken der Daten: " + e.getMessage() + ".", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void checkButtonVersteckenEnable() {
        if (!this.textfeldVersteckregeln.getText().isEmpty() && //
                !this.textfeldQuelldatei.getText().isEmpty() && //
                !this.textfeldMediendateiQuelle.getText().isEmpty() && //
                !this.textfeldMedienverzeichnis.getText().isEmpty() && //
                !this.textfeldMediendateiZiel.getText().isEmpty() ) {
            this.buttonVerstecken.setDisable(false);
        } else {
            this.buttonVerstecken.setDisable(true);
        }
    }
}
