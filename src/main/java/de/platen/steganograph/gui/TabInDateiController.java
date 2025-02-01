package de.platen.steganograph.gui;

import java.io.File;
import java.nio.file.FileSystems;

import de.platen.steganograph.AktionAusDatei;
import de.platen.steganograph.AktionHolenAusAudio;
import de.platen.steganograph.AktionHolenAusBild;
import de.platen.steganograph.AktionInDatei;
import de.platen.steganograph.AktionVersteckenInAudio;
import de.platen.steganograph.AktionVersteckenInBild;
import de.platen.steganograph.AktionZufallsdatei;
import de.platen.steganograph.Aktionen;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class TabInDateiController {

    private static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();

    private static final String ABLAGEORT_VERSTECKDATEI = "Ablageort der zu versteckenden Datei";
    private static final String ABLAGEORT_VERZEICHNIS_ZUFALLSDATEI = "Verzeichnis der Zufallsdatei";

    private static final long FAKTOR_KB = 1024L;

    @FXML
    private Button buttonVerstecken;
    @FXML
    private TextField textfeldVersteckdatei;
    @FXML
    private TextField textfeldGroesseVersteckdatei;
    @FXML
    private TextField textfeldZufallsdateiverzeichnis;
    @FXML
    private TextField textfeldZufallsdateiname;
    @FXML
    private TextField textfeldGroesse;
    @FXML
    private TextField textfeldPosition;

    private long groesseVersteckdatei = 0L;
    private long groesseZufallsdatei = 0L;
    private long position = 0L;

    public void onKeyName() {
        final String eingabe = this.textfeldZufallsdateiname.getText();
        if (!eingabe.isEmpty()) {
            final String eingabeBereinigt = this.bereinigeEingabeDateiname(eingabe);
            if (!eingabeBereinigt.isEmpty()) {
                if (!eingabe.equals(eingabeBereinigt)) {
                    this.textfeldZufallsdateiname.setText(eingabeBereinigt);
                }
            }
        }
        checkButtonVersteckenEnable();
    }

    public void onKeyGroesse() {
        final String eingabe = this.textfeldGroesse.getText();
        if (!eingabe.isEmpty()) {
            final String eingabeBereinigt = this.bereinigeZahleneingabe(eingabe);
            if (!eingabeBereinigt.isEmpty()) {
                if (!eingabe.equals(eingabeBereinigt)) {
                    this.textfeldGroesse.setText(eingabeBereinigt);
                }
                this.groesseZufallsdatei = Long.parseLong(eingabeBereinigt) * FAKTOR_KB;
            } else {
                this.groesseZufallsdatei = 0L;
            }
        } else {
            this.groesseZufallsdatei = 0L;
        }
        checkButtonVersteckenEnable();
    }

    public void onKeyPosition() {
        final String eingabe = this.textfeldPosition.getText();
        if (!eingabe.isEmpty()) {
            final String eingabeBereinigt = this.bereinigeZahleneingabe(eingabe);
            if (!eingabeBereinigt.isEmpty()) {
                if (!eingabe.equals(eingabeBereinigt)) {
                    this.textfeldPosition.setText(eingabeBereinigt);
                }
                this.position = Long.parseLong(eingabeBereinigt);
            } else {
                this.position = 0L;
            }
        } else {
            this.position = 0L;
        }
        checkButtonVersteckenEnable();
    }

    public void behandleButtonVersteckdatei() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(ABLAGEORT_VERSTECKDATEI);
        final File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            this.textfeldVersteckdatei.setText(file.getAbsolutePath());
            this.groesseVersteckdatei = file.length();
            this.textfeldGroesseVersteckdatei.setText(String.valueOf(this.groesseVersteckdatei));
        }
        checkButtonVersteckenEnable();
    }

    public void behandleButtonZufallsdateiverzeichnis() {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(ABLAGEORT_VERZEICHNIS_ZUFALLSDATEI);
        final File file = directoryChooser.showDialog(null);
        if (file != null) {
            this.textfeldZufallsdateiverzeichnis.setText(file.getAbsolutePath());
        }
        checkButtonVersteckenEnable();
    }

    public void behandleButtonVerstecken() {
        final String dateiZiel = this.textfeldZufallsdateiverzeichnis.getText() + FILE_SEPARATOR + this.textfeldZufallsdateiname.getText();
        final Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(), new AktionHolenAusBild(), new AktionHolenAusAudio(), new AktionZufallsdatei(), new AktionInDatei(), new AktionAusDatei());
        try {
            aktionen.verstecke(this.textfeldVersteckdatei.getText(), dateiZiel, String.valueOf(this.groesseZufallsdatei), String.valueOf(this.position), "true");
            final Alert alert = new Alert(Alert.AlertType.INFORMATION, "Verstecken erfolgt", ButtonType.OK);
            alert.showAndWait();
        } catch (RuntimeException e) {
            final Alert alert = new Alert(Alert.AlertType.ERROR, "Fehler beim Verstecken der Daten: " + e.getMessage() + ".", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void checkButtonVersteckenEnable() {
        this.buttonVerstecken.setDisable(this.textfeldVersteckdatei.getText().isEmpty() || //
                this.textfeldZufallsdateiverzeichnis.getText().isEmpty() || //
                this.textfeldZufallsdateiname.getText().isEmpty() || //
                this.textfeldGroesse.getText().isEmpty() || //
                this.textfeldPosition.getText().isEmpty() || //
                !sindWerteOk());
    }

    private boolean sindWerteOk() {
        if ((this.groesseVersteckdatei > 0L) && (this.groesseZufallsdatei > 0L)) {
            return (this.position + this.groesseVersteckdatei) <= this.groesseZufallsdatei;
        }
        return false;
    }

    private String bereinigeEingabeDateiname(final String eingabe) {
        final StringBuilder stringBuilderr = new StringBuilder();
        for (char c : eingabe.toCharArray()) {
            if (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')) || ((c >= '0') && (c <= '9')) || (c == '.')) {
                stringBuilderr.append(c);
            }
        }
        return stringBuilderr.toString();
    }

    private String bereinigeZahleneingabe(final String eingabe) {
        final StringBuilder stringBuilderr = new StringBuilder();
        for (char c : eingabe.toCharArray()) {
            if ((c >= '0') && (c <= '9')) {
                stringBuilderr.append(c);
            }
        }
        return stringBuilderr.toString();
    }
}
