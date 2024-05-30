@echo off
setlocal

REM Définir le chemin absolu de la classe Main.class
set MAIN_CLASS_PATH="C:\Users\HP\OneDrive\Bureau\TP_DESIGNPATTERNS\ForceBrute\Main.class"

REM Vérifier si le chemin vers Main.class est valide
if not exist %MAIN_CLASS_PATH% (
    echo Le chemin vers Main.class est invalide.
    exit /b 1
)

REM Exécuter la classe Main avec les arguments fournis
java -cp %~dp0 Main %*
