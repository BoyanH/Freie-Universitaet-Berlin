# Complexity of Cryptography

## Basics on invertability

* g . f x = id_x
  * f is injective

* f . h x = id_x
  * f ist surjective

* f is injective
  * f(a) = f(b) <=> a = b
* f: X->Y is surjective
  * for all y in Y, exists x in X: f(x) = y

## One-way-function

* f is one way function -> for z = f(x), computing x by given z is a FNP complete problem
* -> existance of a one-way-function implies that FP /= FNP
* -> P /= NP
* BUT, P /= NP does not imply the existance of a one-way function
  * (there are more problems in P and NP than just the functional ones (FP&FNP), therefore just the functional ones could be in the same set)
* IMPORTANT: k in "Introduction to Theoretical Informatics" is any number, in Wikipedia must be a positive integer! (or at least positive, >= 1)
* Deutsch - Einwegfunktion
* Analogie -> Telephonbuch eienr großen Stadt
  * Kennt man den Namen, findet man schnell die dazugehörige Telephonnummer, umgekehrt dauert es deutlich länger
  * Jedoch nicht so gute Analogie, da vermutlich beides polynomiell

## Oder -> Negligable probability = Vernachlässigbare Wahrscheinlichkeit

* Die Erfolgsrate eines Algorithmus ist genau dann vernachlässigbare, wenn es asymptotisch von oben durch ein Polynom begrenzbar ist
  * Funktion abhängig von der Eingabegroße
* D.h, auch wenn man polynomial lange den Algorithmus wiederholt, bekommt man wieder eine vernachlässigbare Erfolgswahrscheinlichkeit
* Damit ist vernachlässigbare Erfolgswahrscheinlichkeit equivalent zu nicht polynomielzeit berechenbar

### Definitionen

* in main source, length preserving one-way functions are defined, weak ones as well
* (used in script) definition from Oded defines both weak and strong one-way functions
  * they are not length preserving, small trick added to define better the polynomial time invertableness (1^n given as function parameter to TM)

## One-way permutation

* a one way function that is also surjective and injective (bijective)

## Trapdoor one way function

* a one way function, which is hard to invert (not in polynomial time), unless a secret, called a trapdoor, is known

* Deutsch -> Einwegfunktion mit Falltür

### P = NP => no one way function => no trapdoor functions => no cryptography

* Proof for P = NP => no one-way functions
  * https://crypto.stackexchange.com/questions/39878/how-to-show-that-a-one-way-function-proves-that-p-%E2%89%A0-np

### Frage an Pubklikum

* Kann eine Hashfunktion für ein "public key cryptosystem" benutzt werden? Unter welche Annahmen bzw. was kann man wann damit nicht machen?
  * gleich nach der Erklärung von den Konzepten (one-way, one-way permutation ODER erst nach RSA)

### General (from RSA paper)

#### Properties of a public-key cryptosystem

1. D(E(M)) = M
1. E and D can be computed in polynomial time
1. revealing E does not reveal D
1. E(D(M)) = M

#### Others

* trap-door one way permutation only needed for implementing "signatures" (encrypt/decrypt using decription/encription function)

### Interesting consequences

* show proof of p = np => no one way
* show that existance of one way functions is an indeed stronger assumption than  p /= np



### Structure of presentation

* Motivation (usage of cryptosystems, basic explanation of public key infrastructures)
* One-way permutation
* One-way function
* Explain relation between one-way permutation and one-way-function (I don't know this either for now)
* Trapdoor function
* Trapdoor function used by RSA
* interesting consequences
* explain p = np => both private and public key cryptosystems wouldn't be safe (work)


### Literatur

* Foundations of Cryptography, Oded Goldreich
* Construct a length preserving one way function -> https://cs.stackexchange.com/questions/10639/length-preserving-one-way-functions
