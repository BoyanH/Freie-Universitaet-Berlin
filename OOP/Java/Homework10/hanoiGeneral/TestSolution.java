import hanoi.Turm;
import hanoi.TurmException;

public class TestSolution {


	public static void main(String[] args) {


		int[] a = {1,2,2,2};
		Turm hanoiTurm;
		HanoiLoesungVerallgemeinert solver;
		String solution;

		try {
			hanoiTurm = new Turm(a);
			// solution = HanoiLoesungVerallgemeinert.loese(hanoiTurm); geht auch

			solver = new HanoiLoesungVerallgemeinert(hanoiTurm);
			solution = solver.getSolution();
			
			System.out.println(solution);
			System.out.println("Fertig: " + hanoiTurm.fertig());	
			System.out.println("Bewegungen: " + solver.solutionSteps);		
		}
		catch(TurmException e) {

			System.out.println("An error ocurred while getting solution: " + e.getMessage());
		}

	}
}