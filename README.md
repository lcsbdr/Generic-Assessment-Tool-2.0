Generic-Assessment-Tool-2.0
===========================

#Einrichten der Entwicklungsumgebung:

1. Einrichten von git:

- Ändern der Konfiguration:

$ - git config --global user.name "Your Name Here"
// Sets the default name for git to use when you commit

$ - git config --global user.email "your_email@youremail.com"
// Sets the default email for git to use when you commit

// EGIT von Eclipse aus

New -> Import -> Git -> Projects from Git -> URI

URI: https://github.com/lcsbdr/Generic-Assessment-Tool-2.0.git
Authentification: Die eigenen Userdaten

Ansonsten werden die Felder automatisch korrekt ausgefüllt.

Anschließend müssen keine Änderungen vorgenommen werden und das Projekt wird geklont.

// Kommandozeile
2. Clonen des github repos:

- Gehe in lokales Entwicklungsverzeichnis

- Klone repo:

$ - git clone https://github.com/lcsbdr/Generic-Assessment-Tool-2.0.git
// Clones your fork of the repo into the current directory in terminal

Beim Klonen des Repositories wird automatisch ein remote repo origin master angelegt über welches
Commits gepusht werden können.