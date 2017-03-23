import hanoi.Turm;
import hanoi.TurmException;

public class TestSolution {

	public static void main(String [] args) {
		

		int[] sizes = {1,2,3,4,5,6,7};
		Turm hanoiTurm;

		try {
			hanoiTurm = new Turm(sizes);
			HanoiLoesung solveHanoi = new HanoiLoesung(hanoiTurm);
			String solution = solveHanoi.getSolution();

			System.out.println(solution);

			// and keep everybody happy, this works as well; Hier haben wir aber unser voriges Code gelassen, 
			// da man eigentlich besser versteht wie wir die Lösung konstruiert haben
			// System.out.println(HanoiLoesung.loese(hanoiTurm));
		}
		catch(TurmException e) {

			System.out.println("An error ocurred while getting solution: " + e.getMessage());
		}
		
	}
}