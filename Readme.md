<h1>Der Steganograph</h1>

*Eine Anwendung, um Daten in Mediendateien zu verstecken*

*Übersicht*

Mit diesem Programm können anhand von Versteckvorgaben Nutzdaten in einer Mediendatei (in der vorliegenden Version 1.0.0 eine Bilddatei mit 3 Farbkanälen RGB und einem Alphakanal z.B. als PNG-Datei) versteckt und auch wieder herausgeholt werden.
Die Versteckvorgaben selbst können mit diesem Programm ebenfalls erzeugt werden.

Das Verstecken wird blockweise durchgeführt. Jeder Block besteht aus einer Menge von Positionen (bei einem Bild sind dies Bildpunkte).
Jede Position enthält eine Menge Kanäle (bei einem Bild mit drei Farbkanälen sind dies z.B. Rot, Grün, Blau).

Jedes Byte der Nutzdaten wird in Bitbereiche zerteilt (z.B. in 4 x 2 Bits bei einer Bittiefe von 2). Jeder Bitbereich wird auf die Positionen und deren Kanäle, d.h. auf die niederwertigsten Bits entsprechend der Bittiefe der Mediendaten verteilt.

<b>Aufbau der Datei mit den Verteilregeln (Versteckvorgaben):</b>
<ul>
<li>Version (3 Byte)</li>
<li>Blockgröße (4 Byte)</li>
<li>Anzahl Kanäle (1 Byte)</li>
<li>Anzahl Bittiefe (1 Byte)</li>
<li>Anzahl Bytes Nutzdaten (4 Bytes)</li>
<li>Menge von Verteilregeln, wobei eine Verteilregel aus 4 Byte für die Blockposition und 1 Byte für die Kanalnummer besteht</li>
</ul>

<b>Kommandozeilenparameter:</b><br>
Es gibt drei Hauptparameter: <i>verteilregelgenerierung</i>, <i>verstecken</i>, <i>holen</i><br>
Zu jedem Hauptparameter gibt es obligatorische zugeordnete Parameter.<br>
Erzeugung der Verteilregeln:<br>
--verteilregelgenerierung Hauptparameter für die Erzeugung der Verteilregeln<br>
--blockgroesse \<Zahl\> Anzahl der Medienentitäten (d.h. bei einem Bild die Anzahl der Bildpunkte) pro Block<br>
--nutzdaten \<Zahl\> Anzahl der in einem Block unterzubringenen Bytes<br>
--anzahlkanaele \<Zahl\> Anzahl der Kanäle der Mediendatei (z.B. bei einem Bild drei Farbkanäle Rot, Grün, Blau)<br>
--bittiefe \<Zahl\> Die Anzahl der Bits für das Verstecken der Nutzdaten in den Mediendaten (1, 2, 4, oder 8 Bit)<br>
--dateiname \<Zu erzeugende Datei mit den Verteilregeln\><br>
Verstecken von Nutzdaten:<br>
--verstecken Hauptparameter für das Verstecken von Nutzdaten<br>
--dateiVerteilregel \<Dateiname\> Dateiname der Datei mit den Verteilregeln><br>
--dateiNutzdaten \<Dateiname\> Dateiname der Datei mit den Nutzdaten><br>
--dateiQuelle \<Dateiname\> Dateiname der Mediendatei für das Verstecken der Nutzdaten><br>
--dateiZiel \<Dateiname\> Dateiname der Mediendatei mit den versteckten Nutzdaten><br>
Holen von Nutzdaten:<br>
--holen Hauptparameter für das Holen der Nutzdaten<br>
--dateiVerteilregel \<Dateiname\> Dateiname der Datei mit den Verteilregeln><br>
--dateiNutzdaten \<Dateiname\> Dateiname der Datei mit den Nutzdaten (wird hier nur ein Verzeichnis angegeben, so wird der Dateiname aus der Datei mit den versteckten Nutzdaten genommen und in das angegebene Verzeichnis gespeichert)<<br>
--dateiQuelle \<Dateiname\> Dateiname der Mediendatei mit den versteckten Nutzdaten><br>
Beispiele:<br>
<i>java -jar Steganograph-1.0.0.jar --verteilregelgenerierung --blockgroesse 100 --nutzdaten 50  --anzahlkanaele 4 --bittiefe 2 --dateiname verteilregel</i><br>
<i>java -jar Steganograph-1.0.0.jar --verstecken --dateiVerteilregel verteilregel --dateiNutzdaten nutzdaten.txt --dateiQuelle testbild1.png --dateiZiel testbild1Versteck.png</i><br>
<i>java -jar Steganograph-1.0.0.jar --holen --dateiVerteilregel verteilregel --dateiNutzdaten nutzdatenNeu.txt --dateiQuelle testbild1Versteck.png</i><br>

<b>Ausblick auf weitere Entwicklungen:</b>
<ul>
<li>Zusätzlich zum Verrauschen der Blöcke mit Nutzdaten auch verrauschen des restlichen Bildes</li>
<li>Weitere (verlustfreie) Bildformate zum Bildformat 4 Byte RGB mit Alpha</li>
<li>Verstecken von Nutzdaten in (verlustfreien oder verlustfreien Anteilen) Audiodateien</li>
<li>Verstecken von Nutzdaten in (verlustfreien oder verlustfreien Anteilen) Videodateien</li>
<li>Verstecken von Live-Streams (Video, Audio) in Live-Streams (Video, Audio, hierbei verlustfrei oder verlustfreien Anteilen)</li>
</ul>
