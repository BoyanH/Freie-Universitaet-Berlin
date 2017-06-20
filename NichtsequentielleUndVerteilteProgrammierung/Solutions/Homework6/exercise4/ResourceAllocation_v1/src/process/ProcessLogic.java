package process;

import java.util.ArrayList;
import java.util.Random;

import calculationUtil.CUtil;
import javafx.scene.layout.Pane;

/**
 * @author Thierry Meurers
 * 
 *         This class is used to store several different states of a process and the resources 
 *         allocated by the process, which will be calculated during the simulation. 
 *         Also functions as a Process-Object which can be accessed by the Allocation Algorithm.
 *         
 */
public class ProcessLogic implements Process {

	private int state;
	
	private ProcessView view;
	private int id;

	//Used to store 100 different states of free resources
	private int[][] pendingRes;
	private int[][] allocatedRes;
	private boolean[] finished;
	
	//List of all needed requests
	private ArrayList<AllocationRequestImpl> requests = new ArrayList<>();

	/**
	 * Initializes the arrays and the ProcessView.
	 * Furthermore a list of all needed requests is generated.
	 */
	public ProcessLogic(int[] neededRes, int id) {
		this.pendingRes = new int[100][];
		this.allocatedRes = new int[100][];
		this.finished = new boolean[100];

		this.pendingRes[state] = neededRes;
		this.allocatedRes[state] = new int[] { 0, 0, 0, 0, 0 };

		this.id = id;

		this.view = new ProcessView(neededRes, id);
		
		for(int i = 0; i < 5; i++){
			if(neededRes[i] != 0){
				int[] requestedRes = new int[] { 0, 0, 0, 0, 0 };
				requestedRes[i] = neededRes[i];
				requests.add(new AllocationRequestImpl(requestedRes, i));
			}
		}
		
	}
	
	
	/**
	 * Used by the Simulation to get a new request.
	 */
	public AllocationRequestImpl getNewRequest(){
		if(requests.isEmpty())
			return null;
		
		Random rnd = new Random();
		int i = rnd.nextInt(requests.size());
		AllocationRequestImpl res = requests.get(i);
		requests.remove(i);
		return res;
	}

	/**
	 * Getters used by the allocation algorithm.
	 * 
	 * Will return the amount of acquired or pending resources
	 * (depending on the current state). 
	 */
	@Override
	public int[] getAcquiredRes() {
		return allocatedRes[state].clone();
	}

	@Override
	public int[] getPendingRes() {
		return pendingRes[state].clone();
	}
	
	@Override
	public boolean isFinished() {
		return finished[state];
	}

	@Override
	public int getId() {
		return id;
	}
	


	/**
	 * Moves to the next state by increasing the state counter after
	 * the current allocation informations are copied to the next state.
	 * If the process is already finished pendingRes and allocatedRes
	 * are assigned with an array consisting of zeros.
	 */
	public void nextState() {
		if (finished[state]) {
			pendingRes[state + 1] = new int[] { 0, 0, 0, 0, 0 };
			allocatedRes[state + 1] = new int[] { 0, 0, 0, 0, 0 };
			finished[state + 1] = true;
		} else {
			pendingRes[state + 1] = pendingRes[state].clone();
			allocatedRes[state + 1] = allocatedRes[state].clone();
		}
		state++;
	}

	
	/**
	 * This method is called by the SimulationController in order
	 * to visualize a specific allocation state.
	 */
	public void displayState(int s) {
		view.displayState(allocatedRes[s], finished[s]);
	}

	/**
	 * Called once by the SimulationModel in order to tie the view
	 * to the SimulationView
	 */
	public Pane getProcessView() {
		return view;
	}

	
	/**
	 * Called by the AllocationRequestImpl object once a request is granted.
	 */
	private void assignRes(int[] res)  {
//		if (finished[state]) {
//			return false;
//		}else{
//			boolean finished = true;
//		for (int i = 0; i < res.length; i++) {
//			pendingRes[state][i] -= res[i];
//			allocatedRes[state][i] += res[i];
//			finished &= pendingRes[state][i] <= 0;
//		}
//		this.finished[state+1] = finished;
//		return finished;
//		}
		
		pendingRes[state] = CUtil.sub(pendingRes[state], res);
		allocatedRes[state] = CUtil.add(allocatedRes[state], res);
		finished[state+1] = CUtil.allZero(pendingRes[state]);
		
	}

	/**
	 * Allocation request send to the allocation algorithm.
	 */
	public class AllocationRequestImpl implements AllocationRequest{

		private int[] reqRes;
		private int resId;
		private boolean granted;
		
		AllocationRequestImpl(int[] reqRes, int resId){
			this.reqRes = reqRes;
			this.resId = resId;
		}
		
		@Override
		public Process getProcess() {
			return ProcessLogic.this;
		}

		@Override
		public int[] getRequestedRes() {
			return this.reqRes;
		}

		@Override
		public void grant() {
			ProcessLogic.this.assignRes(reqRes);
			granted = true;
		}
		
		public boolean isGranted(){
			return granted;
		}
		
		public int getResId(){
			return resId;
		}
		
	}
	
}
