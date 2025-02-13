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
<li>Verstecken von Daten in einer Mediendatei (Bild als Bmp- und Png-Datei, Audio als Wav-Datei) anhand von Versteckregeln.<br>
Optional können die Versteckregeln verschlüsselt sein.</li>
<li>Auslesen von versteckten Daten aus einer Mediendatei (Bild als Bmp- und Png-Datei, Audio als Wav-Datei) anhand von Versteckregeln.<br>
Optional können die Versteckregeln verschlüsselt sein.</li>
<li>Erzeugen eines Schlüsselpaares:<br>
Das Schlüsselpaar besteht aus einem privatem und einem öffentlichen Schlüssel.
Der private Schlüssel ist zum Entschlüsseln der Datei mit den Versteckregeln vorgesehen.
Der öffentliche Schlüssel ist zum Verschlüsseln der Datei mit den Versteckregeln vorgesehen.
Der öffentliche Schlüssel kannn an andere Personen weitergegeben werden,
falls man von diesen die Versteckregeln nutzen und deren Dateien mit den versteckten Daten verwenden möchte.</li>
<li>Erzeugung einer Datei mit Zufallswerten</li>
<li>Verstecken einer Datei in einer Datei (mit Zufallswerten)</li>
<liu>Auslesen einer Datei aus einer Datei (mit Zufallswerten)</liu>
</ul>

<b>Kommandozeilenparameter:</b><br>

Es gibt acht Hauptparameter: <i>verteilregelgenerierung</i>, <i>verstecken</i>, <i>holen</i>, <i>key</i>, <i>gui</i>, <i>zufallsdatei</i>, <i>inDatei</i>, <i>ausDatei</i><br>
Zu jedem Hauptparameter (bis auf <i>gui</i>) gibt es zugeordnete Parameter, obligatorisch und optional.<br>
Der Aufruf ohne Parameter entspricht <i>gui</i>.

<li>
<li>Erzeugung der Verteilregeln:<br>
--verteilregelgenerierung Hauptparameter für die Erzeugung der Verteilregeln<br>
--blockgroesse &lt;Zahl> Anzahl der Medienentitäten (d.h. bei einem Bild die Anzahl der Bildpunkte) pro Block<br>
--nutzdaten &lt;Zahl> Anzahl der in einem Block unterzubringenen Bytes<br>
--anzahlkanaele &lt;Zahl> Anzahl der Kanäle der Mediendatei (z.B. bei einem Bild drei Farbkanäle Rot, Grün, Blau)<br>
--bittiefe &lt;Zahl> Die Anzahl der Bits für das Verstecken der Nutzdaten in den Mediendaten (1, 2, 4, oder 8 Bit)<br>
--dateiname &lt;Zu erzeugende Datei mit den Verteilregeln><br>
--verschluesselungsdateien &lt;Dateinamen> Komma separierte Liste von Dateinamen von öffentlichen Schlüsseln (optional)
--passwort &lt;Passwort> Passwort (optional)</li>
<li>Verstecken von Daten 1:<br>
--verstecken Hauptparameter für das Verstecken von Nutzdaten<br>
--dateiVerteilregel &lt;Dateiname> Dateiname der Datei mit den Verteilregeln><br>
--dateiNutzdaten &lt;Dateiname> Dateiname der Datei mit den Nutzdaten><br>
--dateiQuelle &lt;Dateiname> Dateiname der Mediendatei für das Verstecken der Nutzdaten><br>
--dateiZiel &lt;Dateiname> Dateiname der Mediendatei mit den versteckten Nutzdaten><br>
--verrauschen [ohne|nutzdatenbereich|alles] Optionale Angabe zum Verrauschen<br>
--entschluesselungsdatei &lt;Dateiname> Dateiname des privaten Schlüssels (optional)
--passwort &lt;Passwort> Passwort (optional)</li>
<li>Auslesen von Daten 1:<br>
--holen Hauptparameter für das Holen der Nutzdaten<br>
--dateiVerteilregel &lt;Dateiname> Dateiname der Datei mit den Verteilregeln><br>
--dateiNutzdaten &lt;Dateiname> Dateiname der Datei mit den Nutzdaten (wird hier nur ein Verzeichnis angegeben, so wird der Dateiname aus der Datei mit den versteckten Nutzdaten genommen und in das angegebene Verzeichnis gespeichert)<<br>
--dateiQuelle &lt;Dateiname> Dateiname der Mediendatei mit den versteckten Nutzdaten><br>
--entschluesselungsdatei &lt;Dateiname> Dateiname des privaten Schlüssels (optional)<br>
--passwort &lt;Passwort> Passwort (optional)</li>
<li>Erzeugung eines Schlüsselpaares:<br>
--key Hauptparameter für das Erzeugen eines Schlüsselpaares<br>
--id &lt;Id> Die Id für das Schlüsselpaar<br>
--dateiPublicKey &lt;Dateiname> Dateiname des öffentlichen Schlüssels<br>
--dateiPrivateKey &lt;Dateiname> Dateiname des privaten Schlüssels<br>
--passwort &lt;Passwort> Passwort (optional)</li>
<li>Grafische Benutzeroberfläche:<br>
--gui Parameter für den Start mit grafischer Benutzeroberfläche</li>
<li>Erzeugung einer Datei mit Zufallswerten<br>
--zufallsdatei Hauptparameter für die Erzeugung einer Datei mit Zufallswerten<br>
--name &lt;Dateiname> Der Dateiname für die Zufallswerte<br>
--laenge &lt;Länge> Dateilänge<li>
<li>Verstecken einer Datei in einer Datei<br>
--inDatei Hauptparameter für das Verstecken einer Datei in einer Datei<br>
--quelldatei &lt;Dateiname> Der Quelldateiname<br>
--zieldatei &lt;Dateiname> Der Zieldateiname<br>
--laenge &lt;Länge> Dateilänge der Datei in der versteckt wird<br>
--offset &lt;Offset> Offset in der Datei, in der versteckt wird<br>
--erzeugung &lt;"true" oder "false"> Angabe, ob eine Datei mit Zufallswerten erzeugt werden soll<li>
<li>Auslesen einer Datei aus einer Datei<br>
--ausDatei Hauptparameter für das Auslesen einer Datei aus einer Datei<br>
--quelldatei &lt;Dateiname> Der Quelldateiname<br>
--zieldatei &lt;Dateiname> Der Zieldateiname<br>
--offset &lt;Offset> Offset der Datei, aus der gelesen wird<br>
--laenge &lt;Länge> Dateilänge der Datei, die gelesen gelesen wird<br>
--erzeugung &lt;Wert für ja oder nein> Angabe, ob eine Datei mit Zufallswerten erzeugt werden soll<li>
</li>

Beispiele:<br>
<i>java -jar Steganograph.jar --verteilregelgenerierung --blockgroesse 100 --nutzdaten 50  --anzahlkanaele 4 --bittiefe 2 --dateiname verteilregel --verschluesselungsdateien public1.pgp,public2.pgp --passwort passwort</i><br>
<i>java -jar Steganograph.jar --verstecken --dateiVerteilregel verteilregel --dateiNutzdaten nutzdaten.txt --dateiQuelle testbild1.png --dateiZiel testbild1Versteck.png --entschluesselungsdatei private1.pgp --passwort passwort</i><br>
<i>java -jar Steganograph.jar --holen --dateiVerteilregel verteilregel --dateiNutzdaten nutzdatenNeu.txt --dateiQuelle testbild1Versteck.png --entschluesselungsdatei private1.pgp --passwort passwort</i><br>
<i>java -jar Steganograph.jar --key --id person1 --dateiPublicKey public1.pgp  --dateiPrivateKey private1.pgp --passwort passwort</i><br>
<i>java -jar Steganograph.jar --gui</i><br>
<i>java -jar Steganograph.jar --zufallsdatei --name heuhaufen --laenge 1000</i>
<i>java -jar Steganograph.jar --inDatei --quelldatei versteckregel --ztieldatei heuhaufen --laenge 1000 --offset 500 --erzeugung true</i>
<i>java -jar Steganograph.jar --inDatei --quelldatei heuhaufen --zieldatei versteckregel --offset 500 --laenge 1000</i>

<b>Ausblick auf weitere Entwicklungen:</b>
<ul>
<li>Beschleunigung des Laufzeitverhaltens</li>
<li>Verstecken von Nutzdaten in (verlustfreien oder verlustfreien Anteilen) Videodateien</li>
<li>Verstecken von Live-Streams (Video, Audio) in Live-Streams (Video, Audio, hierbei verlustfrei oder verlustfreien Anteilen)</li>
</ul>
