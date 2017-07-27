Erzählung:
Als eingeloggter Anwender, der bereits eine Reservierung vorgenommen hat,
möchte ich auf der Reservierungsseite Informationen über aktuelle Preise erhalten,
damit ich diese kostenpflichtig buchen kann.

Szenario: Initialen Preise für Reservierung einstellen
Gegeben ist eine valide Reservierung zwischen 10:00 und 10:30 Uhr
Und zwischen 10:00 Uhr und 11:00 Uhr ist der Preis 30,50 € bei 0 Mitfahrern
Und zwischen 11:00 Uhr und 12:00 Uhr ist der Preis 25,40 € bei 1 Mitfahrern
Wenn die Reservierung im Simulator hinterlegt wird
Dann gibt der Simulator eine Erfolgsmeldung zurück

Szenario: Reservierung einer Sammeltaxifahrt
Gegeben ist eine valide Reservierung zwischen 10:00 und 10:30 Uhr
Und ein eingeloggter Nutzer
Wenn ein Sammeltaxi reserviert wird
Dann ist die Reservierung erfolgreich
Und zwischen 10:00 und 11:00 Uhr beträgt der Preis 30,50 € bei 0 Mitfahrern
Und zwischen 11:00 und 12:00 Uhr beträgt der Preis 25,40 € bei 1 Mitfahrern

Szenario: Preise aktualisieren
Gegeben ist eine valide Reservierung zwischen 10:00 und 10:30 Uhr
Und zwischen 10:00 Uhr und 11:00 Uhr ist der Preis 30,50 € bei 0 Mitfahrern
Und zwischen 11:00 Uhr und 12:00 Uhr ist der Preis 22,50 € bei 2 Mitfahrern
Wenn die Reservierung im Simulator aktualisiert wird
Dann gibt der Simulator eine Erfolgsmeldung zurück

Szenario: Aktualisierte Preise anschauen
Wenn der Nutzer die Reservierungsseite öffnet
Dann ist die Reservierung erfolgreich
Und zwischen 10:00 und 11:00 Uhr beträgt der Preis 30,50 € bei 0 Mitfahrern
Und zwischen 11:00 und 12:00 Uhr beträgt der Preis 22,50 € bei 2 Mitfahrern