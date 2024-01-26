package de.platen.steganograph.gui;

import de.platen.steganograph.Aktionen;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.nio.file.FileSystems;

public class TabKeyController {

    private static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();

    private static final String VERZEICHNIS_PUBLIC_KEY = "Verzeichnis öffentlicher Schlüssel";
    private static final String VERZEICHNIS_PRIVATE_KEY = "Verzeichnis privater Schlüssel";

    @FXML
    private Button buttonErzeugen;
    @FXML
    private TextField textfeldPublicKeyOrdner;
    @FXML
    private TextField textfeldPublicKeyDatei;
    @FXML
    private TextField textfeldPrivateKeyOrdner;
    @FXML
    private TextField textfeldPrivateKeyDatei;
    @FXML
    private TextField textfeldPasswort;
    @FXML
    private TextField textfeldId;

    public void onKey() {
        checkButtonErzeugenEnable();
    }

    public void behandleButtonPublicKey() {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(VERZEICHNIS_PUBLIC_KEY);
        final File file = directoryChooser.showDialog(null);
        if (file != null) {
            this.textfeldPublicKeyOrdner.setText(file.getAbsolutePath());
        }
        checkButtonErzeugenEnable();
    }

    public void behandleButtonPrivateKey() {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(VERZEICHNIS_PRIVATE_KEY);
        final File file = directoryChooser.showDialog(null);
        if (file != null) {
            this.textfeldPrivateKeyOrdner.setText(file.getAbsolutePath());
        }
        checkButtonErzeugenEnable();
    }

    public void behandleButtonErzeugen() {
        final Aktionen aktionen = new Aktionen(null, null, null, null);
        final String dateiPublicKey = this.textfeldPublicKeyOrdner.getText() + FILE_SEPARATOR + this.textfeldPublicKeyDatei.getText();
        final String dateiPrivateKey = this.textfeldPrivateKeyOrdner.getText() + FILE_SEPARATOR + this.textfeldPrivateKeyDatei.getText();
        try {
            if (this.textfeldPasswort.getText().isEmpty()) {
                aktionen.erzeugeKeyPaar(this.textfeldId.getText(), dateiPublicKey, dateiPrivateKey);
            } else {
                aktionen.erzeugeKeyPaar(this.textfeldId.getText(), dateiPublicKey, dateiPrivateKey, this.textfeldPasswort.getText());
            }
        } catch (RuntimeException e) {
            final Alert alert = new Alert(Alert.AlertType.ERROR, "Fehler beim Erzeugen des Schlüsselpaares: " + e.getMessage() + ".", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void checkButtonErzeugenEnable() {
        if (!this.textfeldPublicKeyOrdner.getText().isEmpty() && //
                !this.textfeldPrivateKeyOrdner.getText().isEmpty() && //
                !this.textfeldPublicKeyDatei.getText().isEmpty() && //
                !this.textfeldPrivateKeyDatei.getText().isEmpty() && //
                !this.textfeldId.getText().isEmpty()) {
            this.buttonErzeugen.setDisable(false);
        } else {
            this.buttonErzeugen.setDisable(true);
        }
    }
}
