//Programm mit Kommenaren
import java.util.Scanner;//Bibliothek importieren
public class Hallo {//Klasse Hallo
	public static void main(String[] args) {//main Methode
		System.out.println("Wie heisst du?");//Konsole Ausgabe
		Scanner scanner = new Scanner(System.in);//Scanner Objekt initialisieren
		String name = scanner.nextLine();//Konsole Eingabe
		scanner.close();//Scanner ordnungsgema:ss beenden
		System.out.println("Hallo " + name + ".");//formatierte Ausgabe
	}
}

/* Test in Konsole
Wie heisst du?
Datenbanksystem 2017
Hallo Datenbanksystem 2017.
*/