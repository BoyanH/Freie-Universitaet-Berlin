class C:
	pass # "leere" Klasse
exemplar1 = C()
exemplar2 = C()


#a)
#Nein, es hat keine Auswirkungen, da exemplar1 zu einem Objekt in Speicher zeigt, und exemplar2 zu einem anderen. Die Klasse C wird dadurch nicht veränder, da
#es zu eine total andere Adresse im Speicher zeigt, nämlich die Klasse selbst und ist damit kein Objekt.

exemplar1.a = 2
print('exemplar1.a = 2')
print('C.a exists: ', 'a' in vars(C) )
print('exemplar1.a exists: ', 'a' in vars(exemplar1))
print('exemplar2.a exists: ', 'a' in vars(exemplar2))

print('\n', '-'*20, '\n')

del exemplar1.a #wir löschen wieder diesen Attribut, damit wir die weitere Aufgaben lösen können

#b) Die Klasse funktioniert wie ein Muster für alle Objekte, die davon erzeugt werden. Einmal erzeugt, haben diese nur die Attribute, die initial vorhanden waren.
# Wenn wir ein neues Objekt erzeugen, hat dieser immer noch kein Attribut a. Es ist so, weil wir nur den Objekt C ändern, aber nicht die Klasse. Das heißt wir ändern
# nur das statische Objekt C.

C.a = 2
print('C.a = 4')
print('C.a exists: ', 'a' in vars(C) )
print('exemplar1.a exists: ', 'a' in vars(exemplar1))
print('exemplar2.a exists: ', 'a' in vars(exemplar2))

exemplar3 = C()
print('exemplar3 = C()')
print('exemplar3.a exists: ', 'a' in vars(exemplar3))

print('\n', '-'*20, '\n')

del C.a #wieder Änderungen löschen

#c)Soweit ich die Aufgabe verstehe, sollen wir das Attribut a auf dem selben Objekt definieren. In der ersten Zeile wird dann das Attribut a für das Objekt exemplar1 definiert.
# In der zweiten Zeile haben wir schon diesen Attribut, deswegen wird nur das Wert von a verändert, da beide Attributen im selben Objekt definiert sind.

#Wenn wir ABER ein Attribut mit gleiche Name in zweie verschiedene Objekte definieren, dann haben wir in diese 2 Objekte zwei verschiedene Werte, da diese von einander völlig
#unabhängig sind (in a) und b) gezeigt)

exemplar1.a = 2
exemplar1.a = 4
print('exemplar1.a = 2')
print('exemplar1.a = 4')
print('exemplar1.a: ', exemplar1.a)