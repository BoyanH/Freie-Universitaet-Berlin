import hanoi.Turm;
import hanoi.TurmException;

public class HanoiLoesungVerallgemeinert {

	//Deklaration von Variablen, die wir brauchen werden, um Zustände zu speichern, für lesbares Code und schönere Rekursion
	private Turm turm;
	private int[] elements;
	private int elementCount;
	private int differentElementsCount;
	private StringBuilder solutionString;
	private int[][] diskRepetitions;
	private final String dashes = "----------------------------------------------------------";

	public int solutionSteps = 0;


	//den Constructor, wo die Länge von alle Elemente im Anfangsturm genommen wird, den StringBUilder initialisiert und so weiter
	public HanoiLoesungVerallgemeinert(Turm ht) {

		this.turm = ht;
		this.elements = ht.anfangsturm();
		this.elementCount = this.elements.length;
		this.solutionString = new StringBuilder(); //In Java ist String + String eine langsame Operation von eine bestimmte Länge ab, deswegen nutzt man StringBuilder
		this.differentElementsCount = this.elementCount; //fallback to default behavior if handleEqualDisks is not executed 
																										//(as we make it overwritable, some confused developer could break it)

		this.handleEqualDisks();
	}

	protected void handleEqualDisks() {

		this.differentElementsCount = 0;
		this.diskRepetitions = new int[elementCount][2]; //we save at diskRepetitions[i][0] the element and in diskRepetition[i][1] the repetitions
													//could easily be done with dictionary, I go old-school here, as I know exactly how many cells I need in my matrix



		for(int i = 0; i < elementCount; i++) {

			//thanks to || we won't really try to access elements[i-1]
			if(i == 0 || elements[i] != elements[i-1]) {

				this.differentElementsCount++;
				this.diskRepetitions[this.differentElementsCount-1][0] = elements[i];
				this.diskRepetitions[this.differentElementsCount-1][1] = 1;
			}
			else {

				this.diskRepetitions[this.differentElementsCount-1][1]++; 
			}
		}
	}

	protected int getDiskRepetitions(int disk) {

		int i = 0;

		//because every element is in this list, we will always find it!
		while(this.diskRepetitions[i][0] != disk) i++;

		return this.diskRepetitions[i][1];
	}

	//unsere Funktion, die das rekursive Aufruft und für leichteres Aufrufen sorgt
	public String getSolution() {

		//rufe die rekursive Funktion mit den Initialskonfiguration
		solve(this.differentElementsCount, 'a', 'b', 'c');

		//nachdem alles fertig ist, gib die Lösung zurück
		return this.solutionString.toString();
	}

	//hier haben wir eine statische Methode definiert, die eigentlich neues Objekt erzeugt und alles wie sonst schafft, aber ist bequemer zu benutzen
	//Nachteil ist natürlich, dass bei dieser Methode man danach die Attributen von HanoiLosung nicht mehr sehen kann (oder Vorteil? hängt davon ab was man will)
	public static String loese(Turm ht) {

		HanoiLoesungVerallgemeinert hl = new HanoiLoesungVerallgemeinert(ht);

		return hl.getSolution();
	}

	//Code von Python-Aufgabe 1 zu 1 übersetzt in Java
	public void solve(int n, char start, char middlePole, char target) {

		if(n==0) {

			return;
		}

		solve(n-1, start, target, middlePole);

		//our special, customized, verallgemeinerte Funktion
		this.moveDisk(start, target);

		solve(n-1, middlePole, start, target);
	
	}

	protected void moveDisk(char start, char target) {

		this.solutionString.append("\n" + dashes + "\n");

		int d = this.tryToMove(start, target);
		int reps = 0;

		//as we declare methods as protected to be 'overwritable', we make sure our function falls back to default behavior (move only one disk)
		try{

			reps = this.getDiskRepetitions(d) - 1; //-1 as we already made one move
		}
		catch(Exception e) {

			System.out.println("Couldn't get amount of repetitions for disk " + d + " !");
		}

		while(reps > 0) {

			this.tryToMove(start, target);
			reps--;
		}

	}

	protected int tryToMove(char start, char target) {

		int d = -1; //illegal disk, initial value

		try {
			
			//speichere das Ergebniss von setzeUm (welchen Element)
			d = this.turm.setzeUm(start, target);

			//beschreib die letzte Bewegung
			String newMovement = "Setze Scheibe "+d+ " von Stift "+start+" auf Stift "+target + "\n";

			//hänge das an dem Ergebniss an
			solutionString.append(newMovement);

			solutionSteps++;
		}
		catch(TurmException e) {

			System.out.println("Oh no, tried to make an illegal move! Try: turm.setzeUm(" + start + "," + target + ")");
			e.printStackTrace();
		}

		return d;
	}
}