main :: IO () --TypDefinition
main = do -- main Funktion mit Kommentaren
    print ("Wie heisst du?") --console Ausgabe
    strName <- getLine --console String eingabe
    print ("Hallo " ++ strName ++ ".") --formatierte Ausgabe

{-Test in Konsole
habib@llangollen:~$ ./hallo
"Wie heisst du?"
Datenbanksystem 2017
"Hallo Datenbanksystem 2017."-}
