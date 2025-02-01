package de.platen.steganograph.gui;

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

import java.io.File;
import java.nio.file.FileSystems;

public class TabAusDateiController {

    private static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();

    private static final String ABLAGEORT_ZUFALLSDATEI = "Ablageort Datei mit versteckter Datei";
    private static final String ABLAGEORT_VERZEICHNIS = "Ablageort Verzeichnis für ausgelesene Datei";

    @FXML
    private Button buttonAuslesen;
    @FXML
    private TextField textfeldZufallsdatei;
    @FXML
    private TextField textfeldPosition;
    @FXML
    private TextField textfeldLaenge;
    @FXML
    private TextField textfeldVerzeichnis;
    @FXML
    private TextField textfeldZieldatei;

    private long groesseZufallsdatei = 0L;
    private long position = 0L;
    private long laenge = 0L;

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
        checkButtonAuslesenEnable();
    }

    public void onKeyLaenge() {
        final String eingabe = this.textfeldLaenge.getText();
        if (!eingabe.isEmpty()) {
            final String eingabeBereinigt = this.bereinigeZahleneingabe(eingabe);
            if (!eingabeBereinigt.isEmpty()) {
                if (!eingabe.equals(eingabeBereinigt)) {
                    this.textfeldLaenge.setText(eingabeBereinigt);
                }
                this.laenge = Long.parseLong(eingabeBereinigt);
            } else {
                this.laenge = 0L;
            }
        } else {
            this.laenge = 0L;
        }
        checkButtonAuslesenEnable();
    }

    public void onKeyZieldatei() {
        final String eingabe = this.textfeldZieldatei.getText();
        if (!eingabe.isEmpty()) {
            final String eingabeBereinigt = this.bereinigeEingabeDateiname(eingabe);
            if (!eingabeBereinigt.isEmpty()) {
                if (!eingabe.equals(eingabeBereinigt)) {
                    this.textfeldZieldatei.setText(eingabeBereinigt);
                }
            }
        }
        checkButtonAuslesenEnable();
    }

    public void behandleButtonZufallsdatei() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(ABLAGEORT_ZUFALLSDATEI);
        final File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            this.textfeldZufallsdatei.setText(file.getAbsolutePath());
            this.groesseZufallsdatei = file.length();

        }
        checkButtonAuslesenEnable();
    }

    public void behandleButtonVerzeichnis() {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(ABLAGEORT_VERZEICHNIS);
        final File file = directoryChooser.showDialog(null);
        if (file != null) {
            this.textfeldVerzeichnis.setText(file.getAbsolutePath());
        }
        checkButtonAuslesenEnable();
    }

    public void behandleButtonAuslesen() {
        final String dateiZiel = this.textfeldVerzeichnis.getText() + FILE_SEPARATOR + this.textfeldZieldatei.getText();
        final Aktionen aktionen = new Aktionen(new AktionVersteckenInBild(), new AktionVersteckenInAudio(), new AktionHolenAusBild(), new AktionHolenAusAudio(), new AktionZufallsdatei(), new AktionInDatei(), new AktionAusDatei());
        try {
            aktionen.hole(this.textfeldZufallsdatei.getText(), dateiZiel, String.valueOf(this.position), String.valueOf(this.laenge));
            final Alert alert = new Alert(Alert.AlertType.INFORMATION, "Auslesen erfolgt", ButtonType.OK);
            alert.showAndWait();
        } catch (RuntimeException e) {
            final Alert alert = new Alert(Alert.AlertType.ERROR, "Fehler beim Auslesen der Daten: " + e.getMessage() + ".", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void checkButtonAuslesenEnable() {
        if (!this.textfeldZufallsdatei.getText().isEmpty() &&
                !this.textfeldPosition.getText().isEmpty() &&
                !this.textfeldLaenge.getText().isEmpty() &&
                !this.textfeldVerzeichnis.getText().isEmpty() &&
                !this.textfeldZieldatei.getText().isEmpty() &&
                this.sindWerteOk()) {
            this.buttonAuslesen.setDisable(false);
        } else {
            this.buttonAuslesen.setDisable(false);
        }
    }

    private boolean sindWerteOk() {
        if (this.groesseZufallsdatei > 0L) {
            return (this.position + this.laenge) <= this.groesseZufallsdatei;
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
