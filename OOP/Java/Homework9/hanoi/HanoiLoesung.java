import hanoi.Turm;
import hanoi.TurmException;

public class HanoiLoesung {

	//Deklaration von Variablen, die wir brauchen werden, um Zustände zu speichern, für lesbares Code und schönere Rekursion
	Turm turm;
	int elmnts;
	StringBuilder solutionString;


	//den Constructor, wo die Länge von alle Elemente im Anfangsturm genommen wird, den StringBUilder initialisiert und so weiter
	public HanoiLoesung(Turm ht) {

		this.turm = ht;
		this.elmnts = ht.anfangsturm().length;
		this.solutionString = new StringBuilder(); //In Java ist String + String eine langsame Operation von eine bestimmte Länge ab, deswegen nutzt man StringBuilder
	}

	//unsere Funktion, die das rekursive Aufruft und für leichteres Aufrufen sorgt
	public String getSolution() {

		//rufe die rekursive Funktion mit den Initialskonfiguration
		solve(elmnts, 'a', 'b', 'c');

		//nachdem alles fertig ist, gib die Lösung zurück
		return this.solutionString.toString();
	}

	//hier haben wir eine statische Methode definiert, die eigentlich neues Objekt erzeugt und alles wie sonst schafft, aber ist bequemer zu benutzen
	//Nachteil ist natürlich, dass bei dieser Methode man danach die Attributen von HanoiLosung nicht mehr sehen kann (oder Vorteil? hängt davon ab was man will)
	public static String loese(Turm ht) {

		HanoiLoesung hl = new HanoiLoesung(ht);

		return hl.getSolution();
	}

	//Code von Python-Aufgabe 1 zu 1 übersetzt in Java
	public void solve(int n, char start, char middlePole, char target) {

		if(n==0) {

			return;
		}

		solve(n-1, start, target, middlePole);

		try {
			
			//speichere das Ergebniss von setzeUm (welchen Element)
			int d = turm.setzeUm(start, target);

			//beschreib die letzte Bewegung
			String newMovement = "Setze Scheibe "+d+ " von Stift "+start+" auf Stift "+target + "\n";

			//hänge das an dem Ergebniss an
			solutionString.append(newMovement);
		}
		catch(TurmException e) {


		}

		solve(n-1, middlePole, start, target);
	
	}
}