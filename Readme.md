<h1>Der Steganograph</h1>

*Eine Anwendung, um Daten in Mediendateien zu verstecken*

*Übersicht*

Mit diesem Programm können anhand von Versteckvorgaben Nutzdaten in einer Mediendatei (in der vorliegenden Version eine Bilddatei als PNG- und BMP-Datei und eine Audiodatei als wav-Datei) versteckt und auch wieder herausgeholt werden.
Die Versteckvorgaben selbst können mit diesem Programm ebenfalls erzeugt werden.

Was unterscheidet dieses Programm von anderen Steganographieprogrammen?
Andere Programme dieser Art stellen einen symmetrischen privaten Schlüssel dar. D.h. die Versteckvorgaben sind im Programm selbst implementiert.
Wenn man also eine Mediandatei im Verdacht hat, dass sie versteckte Daten enthält, kann man nacheinander die Steganographieprogramme dieser Art auf diese Mediendatei ansetzen und erhält so die versteckten Daten.
Das vorliegende Staganographieprogramm verfolgt den Ansatz, die Versteckvorgaben vom Programm selbst zu trennen.
Sie liegen dass als separate Datai außerhalb des Proggramms vor.

Das Verstecken wird blockweise durchgeführt. Jeder Block besteht aus einer Menge von Positionen (bei einem Bild sind dies Bildpunkte).
Jede Position enthält eine Menge Kanäle (bei einem Bild mit drei Farbkanälen sind dies z.B. Rot, Grün, Blau).

Jedes Byte der Nutzdaten wird in Bitbereiche zerteilt (z.B. in 4 x 2 Bits bei einer Bittiefe von 2). Jeder Bitbereich wird auf die Positionen und deren Kanäle, d.h. auf die niederwertigsten Bits entsprechend der Bittiefe der Mediendaten verteilt.

<b>Aufbau der Datei mit den Verteilregeln (Versteckvorgaben):</b>

<ul>
<li>Version der Verteilregeln (nicht des Programms) (3 Byte)</li>
<li>Blockgröße (4 Byte)</li>
<li>Anzahl Kanäle (1 Byte)</li>
<li>Anzahl Bittiefe (1 Byte)</li>
<li>Anzahl Bytes Nutzdaten (4 Bytes)</li>
<li>Menge von Verteilregeln, wobei eine Verteilregel aus 4 Byte für die Blockposition und 1 Byte für die Kanalnummer besteht</li>
</ul>

<b>Die Funktionen des Programms:</b><br>

<ul>
<li>Erzeugen von Versteckregeln:<br>
Die erzeugten Versteckregeln werden in eine Datei gespeichert.<br>
Optional kann diese Datei mit einem öffentlichen Schlüssel oder mehreren öffentlichen Schlüsseln verschlüsselt werden.</li>
<li>Verstecken von Daten in einer Mediandatei (Bild als Bmp- und Png-Datei, Audio als Wav-Datei) anhand von Versteckregeln.<br>
Optional können die Versteckregeln verschlüsselt sein.</li>
<li>Holen von versteckten Daten aus einer Mediandatei (Bild als Bmp- und Png-Datei, Audio als Wav-Datei) anhand von Versteckregeln.<br>
Optional können die Versteckregeln verschlüsselt sein.</li>
<li>Erzeugen eines Schlüsselpaares:<br>
Das Schlüsselpaar besteht aus einem privatem und einem öffentlichen Schlüssel.
Der private Schlüssel ist zum Entschlüsseln der Datei mit den Versteckregeln vorgesehen.
Der öffentliche Schlüssel ist zum Verschlüsseln der Datei mit den Versteckregeln vorgesehen.
Der öffentliche Schlüssel kannn an andere Personen weitergegeben werden,
falls man von diesen die Versteckregeln nutzen und deren Dateien mit den versteckten Daten verwenden möchte.</li>
</ul>

<b>Kommandozeilenparameter:</b><br>

Es gibt vier Hauptparameter: <i>verteilregelgenerierung</i>, <i>verstecken</i>, <i>holen</i>, <i>key</i><br>
Zu jedem Hauptparameter gibt es zugeordnete Parameter, obligatorisch und optional.<br>

<ul>
<li>Erzeugung der Verteilregeln:<br>
--verteilregelgenerierung Hauptparameter für die Erzeugung der Verteilregeln<br>
--blockgroesse &lt;Zahl> Anzahl der Medienentitäten (d.h. bei einem Bild die Anzahl der Bildpunkte) pro Block<br>
--nutzdaten &lt;Zahl> Anzahl der in einem Block unterzubringenen Bytes<br>
--anzahlkanaele &lt;Zahl> Anzahl der Kanäle der Mediendatei (z.B. bei einem Bild drei Farbkanäle Rot, Grün, Blau)<br>
--bittiefe &lt;Zahl> Die Anzahl der Bits für das Verstecken der Nutzdaten in den Mediendaten (1, 2, 4, oder 8 Bit)<br>
--dateiname &lt;Zu erzeugende Datei mit den Verteilregeln><br>
--verschluesselungsdateien &lt;Dateinamen> Komma separierte Liste von Dateinamen von öffentlichen Schlüsseln (optional)
--passwort &lt;Passwort> Passwort (optional)</li>
<li>Verstecken von Daten:<br>
--verstecken Hauptparameter für das Verstecken von Nutzdaten<br>
--dateiVerteilregel &lt;Dateiname> Dateiname der Datei mit den Verteilregeln><br>
--dateiNutzdaten &lt;Dateiname> Dateiname der Datei mit den Nutzdaten><br>
--dateiQuelle &lt;Dateiname> Dateiname der Mediendatei für das Verstecken der Nutzdaten><br>
--dateiZiel &lt;Dateiname> Dateiname der Mediendatei mit den versteckten Nutzdaten><br>
--verrauschen [ohne|nutzdatenbereich|alles] Optionale Angabe zum Verrauschen<br>
--entschluesselungsdatei &lt;Dateiname> Dateiname des privaten Schlüssels (optional)
--passwort &lt;Passwort> Passwort (optional)</li>
<li>Holen von Daten:<br>
--holen Hauptparameter für das Holen der Nutzdaten<br>
--dateiVerteilregel &lt;Dateiname> Dateiname der Datei mit den Verteilregeln><br>
--dateiNutzdaten &lt;Dateiname> Dateiname der Datei mit den Nutzdaten (wird hier nur ein Verzeichnis angegeben, so wird der Dateiname aus der Datei mit den versteckten Nutzdaten genommen und in das angegebene Verzeichnis gespeichert)<<br>
--dateiQuelle &lt;Dateiname> Dateiname der Mediendatei mit den versteckten Nutzdaten><br>
--entschluesselungsdatei &lt;Dateiname> Dateiname des privaten Schlüssels (optional)
--passwort &lt;Passwort> Passwort (optional)</li>
<li>Erzeugung eines Schlüsselpaares:<br>
--key Hauptparameter für das Erzeugen eines Schlüsselpaares<br>
--id &lt;Id> Die Id für das Schlüsselpaar
--dateiPublicKey &lt;Dateiname> Dateiname des öffentlichen Schlüssels
--dateiPrivateKey &lt;Dateiname> Dateiname des privaten Schlüssels
--passwort &lt;Passwort> Passwort (optional)</li>
</ul>

Beispiele:<br>
<i>java -jar Steganograph-2.1.0.jar --verteilregelgenerierung --blockgroesse 100 --nutzdaten 50  --anzahlkanaele 4 --bittiefe 2 --dateiname verteilregel --verschluesselungsdateien public1.pgp,public2.pgp --passwort passwort</i><br>
<i>java -jar Steganograph-2.1.0.jar --verstecken --dateiVerteilregel verteilregel --dateiNutzdaten nutzdaten.txt --dateiQuelle testbild1.png --dateiZiel testbild1Versteck.png --entschluesselungsdatei private1.pgp --passwort passwort</i><br>
<i>java -jar Steganograph-2.1.0.jar --holen --dateiVerteilregel verteilregel --dateiNutzdaten nutzdatenNeu.txt --dateiQuelle testbild1Versteck.png --entschluesselungsdatei private1.pgp --passwort passwort</i><br>
<i>java -jar Steganograph-2.1.0.jar --key --id person1 --dateiPublicKey public1.pgp  --dateiPrivateKey private1.pgp --passwort passwort</i><br>

<b>Ausblick auf weitere Entwicklungen:</b>
<ul>
<li>Beschleunigung des Laufzeitverhaltens</li>
<li>Graphische Benutzeroberfläche</li>
<li>Verstecken von Nutzdaten in (verlustfreien oder verlustfreien Anteilen) Videodateien</li>
<li>Verstecken von Live-Streams (Video, Audio) in Live-Streams (Video, Audio, hierbei verlustfrei oder verlustfreien Anteilen)</li>
</ul>
