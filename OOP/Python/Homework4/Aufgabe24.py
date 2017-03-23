ziffern = "0123456789"

def tokenize (s):
    'wandelt Text s in eine Folge von "Tokens" um.'
    Teil = ""
    erg = []
    for c in s:
        if c in ziffern:
            Teil += c
        else:
            if Teil:
                erg.append(Teil)
                Teil = ""
            erg.append(c)
    if Teil:
        erg.append(Teil)
    return erg
            
def berechne(s):
    stapel = []
    PUSH = stapel.append
    POP = stapel.pop

    def kombiniere():
        b = POP()
        op= POP()
        a = POP()
        if   op=="+": PUSH(a+b)
        elif op=="-": PUSH(a-b)
        elif op=="*": PUSH(a*b)
        else:         PUSH(a/b)

    tt = tokenize (s)
    print(tt)
    try:
        for t in ["("]+tt+[")"]:
            print(t, " ".join(str(x) for x in stapel))

            if t in "+-":
                if stapel and stapel[-1]=="(":
                    PUSH(0) # Trick fГјr einstelliges + oder -
                elif len(stapel)>=3 and stapel[-2] in "+-":
                    kombiniere()
                PUSH(t)
            elif t in "*/":
#               while len(stap)>=3 and stap[-2] in "*/":
#                   kombiniere() # nicht notwendig?
                PUSH(t)
            elif t=="(":
                PUSH(t)
            elif t==")":
                while stapel[-2]!="(":
                    kombiniere()
                a = POP()
                POP()
                PUSH(a)
            else:
                PUSH(int(t))
                if len(stapel)>=3 and stapel[-2] in "*/":
                    kombiniere()
    except ZeroDivisionError as e:
        print ("Division durch 0.",e)
        return None
    except ValueError as e:
        print ("UngГјltige Zahl.",e)
        return None
    return POP()


while True:
    s = input("Ausdruck: ")
    if s=="": break
    try:
      print(berechne(s))
    except Exception as e:
        print ("Unerwarteter Fehler.",e)

# a)
#   1. ja, bei Eingabe = ( 1 + 2 * ( 3 + 4 * ( , da * Operator stärker als + bindet und deswegen erstmal der ausgewertet werden soll. Der braucht aber 2 Zahlen,
        # deswegen muss erstaml der Ausdruck in Klammern ausgewertet werden
#   2. nein, geht nicht, da wir keine Stärker Operationen als * und / definiert haben, die schon bei 2 Zahlen auswertbar sind und die 2*3 durch 6 ersetzt wird
#   3. 1**(1+1) führt zu solche Stapel-Zustand, da jeder Operator braucht 2 Zahlen auf beide Seiten, deswegen muss erstmal der Ausdruck in Klammern ausgeglichen werden, bevor das 
                # versucht  **2 zu berechnen, was zu * multipliziert mit 2 führt und folglich zu undefinierte mal Operation für Zeichenketten
#   4. ( 1 * 2 + ( 3 * 4 + ( ? geht nicht, da die Multiplikation stärker bindet als die Addition, deswegen wird 1*2=2 erstmal ausgewertet bevor die nach dem + folgende Operationen


# b) Ja - als gesagt, werden 2 nacheinander folgende Operatoren (+,-,*,/) nicht gut behandelt, da mathematische Operationen auf eine Variable und ein Operator ausgeführt werden,
#       und diese sind für Zeichenketten nicht definiert
#       Bsp: 1**(1+1), webei * jeder Operator sein kann


# c) z.B: 1+2*(2*3)+1

    # Da wir einfach gesagt haben, dass die Multiplikation/Division am stärksten binden und immer entweder gleich ausgewertet werden, oder nur die Operation in Klammern erstmal
#       warten, haben wir ein ziemlich großes Denkfehler gemacht. Wenn wir bis zum Ende eines Ausdrucks gekommen sind, rechnen wir alle Operationen vorher, bis wir bis zum geöffnete
#       Klammer kommen. Wir setzen aber keine Klammer um die wartende * oder / Ausdrücke, testen auch nicht nach der Auswertung eines Klammer-Ausdrucks ob es wartende starke Operatore
#       gibt. Deswegen kommen wir irgendwann zu 1 + 2*6, testen nicht ob wir schon 2*6 ausrechnen können und gehen weiter mit 6+1 = 7. Aber 1+2*6+1 != 1+2*(6+1), 15 != 14


