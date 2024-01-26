package de.platen.steganograph.gui;

import de.platen.steganograph.Aktionen;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

public class TabVersteckregelnController {

    private static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();

    private static final String  VERZEICHNIS_VERSTECKREGELN = "Verzeichnis Versteckregeln";
    private static final String ABLAGEORT_PUBLIC_KEY = "Ablageort öffentlicher Schlüssel";

    private static final int VORGABE_BILD_BLOCKGROESSE = 100;
    private static final int VORGABE_BILD_NUTZDATEN = 50;
    private static final int VORGABE_BILD_ANZAHL_KANAELE = 3;
    private static final int VORGABE_BILD_BITTIEFE = 2;
    private static final int VORGABE_AUDIO_BLOCKGROESSE = 200;
    private static final int VORGABE_AUDIO_NUTZDATEN = 50;
    private static final int VORGABE_AUDIO_ANZAHL_KANAELE = 2;
    private static final int VORGABE_AUDIO_BITTIEFE = 2;

    @FXML
    private Button buttonErzeugen;
    @FXML
    private TextField textfeldBlockgroesse;
    @FXML
    private TextField textfeldNutzdaten;
    @FXML
    private TextField textfeldAnzahlKanaele;
    @FXML
    private TextField textfeldBittiefe;
    @FXML
    private TextField textfeldVerzeichnis;
    @FXML
    private TextField textfeldDatei;
    @FXML
    private ListView listeDateienPublicKey;
    @FXML
    private TextField textfeldPasswort;

    public void onKey() {
        checkButtonErzeugenEnable();
    }

    public void behandleButtonVorgabenBild() {
        this.textfeldBlockgroesse.setText(String.valueOf(VORGABE_BILD_BLOCKGROESSE));
        this.textfeldNutzdaten.setText(String.valueOf(VORGABE_BILD_NUTZDATEN));
        this.textfeldAnzahlKanaele.setText(String.valueOf(VORGABE_BILD_ANZAHL_KANAELE));
        this.textfeldBittiefe.setText(String.valueOf(VORGABE_BILD_BITTIEFE));
        checkButtonErzeugenEnable();
    }

    public void behandleButtonVorgabenAudio() {
        this.textfeldBlockgroesse.setText(String.valueOf(VORGABE_AUDIO_BLOCKGROESSE));
        this.textfeldNutzdaten.setText(String.valueOf(VORGABE_AUDIO_NUTZDATEN));
        this.textfeldAnzahlKanaele.setText(String.valueOf(VORGABE_AUDIO_ANZAHL_KANAELE));
        this.textfeldBittiefe.setText(String.valueOf(VORGABE_AUDIO_BITTIEFE));
        checkButtonErzeugenEnable();
    }

    public void behandleButtonVerzeichnisVersteckregeln() {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(VERZEICHNIS_VERSTECKREGELN);
        final File file = directoryChooser.showDialog(null);
        if (file != null) {
            this.textfeldVerzeichnis.setText(file.getAbsolutePath());
        }
        checkButtonErzeugenEnable();
    }

    public void behandleButtonPublicKeyAdd() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(ABLAGEORT_PUBLIC_KEY);
        final File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            this.listeDateienPublicKey.getItems().add(file.getAbsolutePath());
        }
    }

    public void behandleButtonPublicKeyDelete() {
        final int index = this.listeDateienPublicKey.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            this.listeDateienPublicKey.getItems().remove(index);
        }
    }

    public void behandleButtonErzeugen() {
        final Aktionen aktionen = new Aktionen(null, null, null, null);
        final String dateiname = this.textfeldVerzeichnis.getText() + FILE_SEPARATOR + this.textfeldDatei.getText();
        try {
            if (this.listeDateienPublicKey.getItems().isEmpty()) {
                aktionen.generiere(this.textfeldBlockgroesse.getText(), this.textfeldNutzdaten.getText(), this.textfeldAnzahlKanaele.getText(), this.textfeldBittiefe.getText(), dateiname);
            } else {
                List<String> publicKeys = new ArrayList<>(this.listeDateienPublicKey.getItems());
                aktionen.generiere(this.textfeldBlockgroesse.getText(), this.textfeldNutzdaten.getText(), this.textfeldAnzahlKanaele.getText(), this.textfeldBittiefe.getText(), dateiname, publicKeys, this.textfeldPasswort.getText());
            }
        } catch(IOException | RuntimeException e) {
            final Alert alert = new Alert(Alert.AlertType.ERROR, "Fehler beim Erzeugen der Versteckregeln: " + e.getMessage() + ".", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void checkButtonErzeugenEnable() {
        if (!this.textfeldBlockgroesse.getText().isEmpty() && //
            !this.textfeldNutzdaten.getText().isEmpty() && //
            !this.textfeldAnzahlKanaele.getText().isEmpty() && //
            !this.textfeldBittiefe.getText().isEmpty() && //
            !this.textfeldVerzeichnis.getText().isEmpty() &&
            !this.textfeldDatei.getText().isEmpty()) {
            this.buttonErzeugen.setDisable(false);
        } else {
            this.buttonErzeugen.setDisable(true);
        }
    }
}
