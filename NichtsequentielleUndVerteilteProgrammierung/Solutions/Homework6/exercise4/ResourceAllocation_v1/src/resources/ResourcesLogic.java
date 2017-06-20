package resources;

import calculationUtil.CUtil;
import javafx.scene.layout.Pane;
import resources.Resources;

/**
 * @author Thierry Meurers
 * 
 *         This class is used to store several different states
 *         of the allocated resources, which will be calculated during the simulation. 
 *         Also functions as the Resources-Object which can be accessed by the
 *         Allocation Algorithm.
 *         
 */
public class ResourcesLogic implements Resources {

	private ResourcesView view;
	
	private int state;
	private int[] totalRes;
	
	//Used to store 100 different states of free resources
	private int[][] freeRes;
	private boolean[] overloaded;


	/**
	 * Initializes the arrays and the ResourcesView
	 */
	public ResourcesLogic(int[] totalRes) {
		this.totalRes = totalRes;
		this.freeRes = new int[100][];
		this.overloaded = new boolean[100];
		this.freeRes[state] = totalRes;
		this.view = new ResourcesView(totalRes);
	}

	
	/**
	 * Moves to the next state by increasing the state counter after
	 * the current allocation information are copied to the next state
	 */
	public boolean nextState(int[] allocatedRes) {

		freeRes[state] = CUtil.sub(totalRes, allocatedRes);
		overloaded[state] = !CUtil.greaterEqual(totalRes, allocatedRes);
		freeRes[state + 1] = freeRes[state];
		overloaded[state + 1] = overloaded[state];
		state++;
		return overloaded[state];
	}
	
	
	/**
	 * This method is called by the SimulationController in order
	 * to visualize a specific allocation state.
	 */
	public void displayState(int s) {
		view.displayState(freeRes[s], overloaded[s]);
	}

	
	/**
	 * Called once by the SimulationModel in order to tie the view
	 * to the SimulationView
	 */
	public Pane getProcessView() {
		return view;
	}

	
	/**
	 * Getters used by the allocation algorithm.
	 * Will return the total amount of res or
	 * the currently free resources (depending on the current state)
	 */
	@Override
	public int[] getFreeRes() {
		return freeRes[state].clone();
	}

	@Override
	public int[] getTotalRes() {
		return totalRes.clone();
	}

}
