package simulation;

import java.util.ArrayList;
import java.util.Random;

import allocationAlgorithm.Algorithm;
import calculationUtil.CUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import process.Process.AllocationRequest;
import process.ProcessLogic;
import process.ProcessLogic.AllocationRequestImpl;
import resources.ResourcesLogic;

public class Simulation {

	final private static int maxIterations = 95;

	//Required in order to generate a new scenario
	private int processCount;
	private double processGreed;
	private int resourceCount;
	private double resourceSupply;
	private Random rnd = new Random();
	private int nextId;


	//This informations will be used by the SimulationModel (and the simulation itself)
	private ProcessLogic[] processes;
	private ResourcesLogic resources;
	private InfoMessages messages = new InfoMessages();
	private int iterations;
	private boolean overloaded;
	private boolean finished;
	private String algoName;

	//Only used for the process of simulation
	private ArrayList<AllocationRequestImpl> requests = new ArrayList<>();
	private ArrayList<Integer> processIDs = new ArrayList<>();
	private Algorithm algo;
	private String debugMessage;

	/**
	 * Prepares and starts the Simulation
	 */
	public Simulation(Algorithm algo, String algoName, int processCount, double processGreed, int resourceCount,
			double resourceSupply) {

		this.algo = algo;
		this.algoName = algoName;
		
		// Find some random values if needed
		this.processCount = processCount == -1 ? rnd.nextInt(16) + 1 : processCount;
		this.processGreed = processGreed < -0.1 ? rnd.nextDouble() * (0.8) + 0.2 : processGreed;
		this.resourceCount = resourceCount == -1 ? rnd.nextInt(5) + 1 : resourceCount;
		this.resourceSupply = resourceSupply < -0.1 ? rnd.nextDouble() * (0.8) + 0.2 : resourceSupply;

		//Create some Resources and Processes and pass them to the algo
		createResources();
		createProcesses();
		algo.init(resources, processes);
		
		//Start the simulation procedure
		simulate();
	}

	/**
	 * Instantiates the ResourceLogic object for the current simulation.
	 */
	private void createResources() {
		
		int r1 = (resourceCount > 0) ? rnd.nextInt((int)(12*resourceSupply)) + (int)(10*resourceSupply): 0;
		int r2 = (resourceCount > 1) ? rnd.nextInt((int)(12*resourceSupply)) + (int)(10*resourceSupply): 0;
		int r3 = (resourceCount > 2) ? rnd.nextInt((int)(12*resourceSupply)) + (int)(10*resourceSupply): 0;
		int r4 = (resourceCount > 3) ? rnd.nextInt((int)(12*resourceSupply)) + (int)(10*resourceSupply): 0;
		int r5 = (resourceCount > 4) ? rnd.nextInt((int)(12*resourceSupply)) + (int)(10*resourceSupply): 0;

		int[] totalRes = new int[] { r1, r2, r3, r4, r5 };
		resources = new ResourcesLogic(totalRes);
	}
	
	/**
	 * Instantiates the ProcessLogic objects for the current simulation.
	 * All process id's are also stored in an randomized array list.
	 * (Which is used by the the method refreshRequests)
	 */
	private void createProcesses() {
		processes = new ProcessLogic[processCount];
		for (int i = 0; i < processCount; i++) {
			
			int r1 = (Math.random() < processGreed && resourceCount > 0) ? rnd.nextInt(6) + 1 : 0;
			int r2 = (Math.random() < processGreed && resourceCount > 1) ? rnd.nextInt(6) + 1 : 0;
			int r3 = (Math.random() < processGreed && resourceCount > 2) ? rnd.nextInt(6) + 1 : 0;
			int r4 = (Math.random() < processGreed && resourceCount > 3) ? rnd.nextInt(6) + 1 : 0;
			int r5 = (Math.random() < processGreed && resourceCount > 4) ? rnd.nextInt(6) + 1 : 0;

			int[] neededRes = new int[] { r1, r2, r3, r4, r5 };
			if (r1 + r2 + r3 + r4 + r5 == 0) {
				neededRes = new int[] { 0, 0, 0, 0, 0 };
				neededRes[rnd.nextInt(resourceCount)] = 1;
			}
			processes[i] = new ProcessLogic(neededRes, nextId);
			processIDs.add(nextId++);
		}
		
		Integer temp;
		for (int i = 0; i < processIDs.size(); i++) {
			int randomInt = rnd.nextInt(processIDs.size());
			temp = processIDs.get(randomInt);
			processIDs.set(randomInt, processIDs.get(i));
			processIDs.set(i, temp);
		}
	}
	

	/**
	 * Refreshed the list of request. Is called in each iteration.
	 */
	private void refreshRequests() {
		ArrayList<AllocationRequestImpl> newRequests = new ArrayList<>();
		
		/** Collect all unfinished requests from last iteration **/
		for (AllocationRequestImpl req : requests) {
			if (!req.isGranted())
				newRequests.add(req);
		}

		/** Ask for one new request by looking into the unfinishedProcesses list **/
		for (Integer i : processIDs) {
			AllocationRequestImpl req = processes[i].getNewRequest();
			if (req != null) {
				newRequests.add(req);
				break;
			}
		}

		/** Add some more request in a randomized manner **/
		for (Integer i : processIDs) {
			if (Math.random() < 0.25) {
				AllocationRequestImpl req = processes[i].getNewRequest();
				if (req != null) {
					newRequests.add(req);
				}
			}
		}
		requests = newRequests;
	}


	
	/**
	 * This method is actually calling the allocation algorithm and is responsible for
	 * putting the resources, processes and infoMessages to the next state.
	 */
	public void simulate() {
		boolean finished = false;
		boolean overloaded = false;
		int i = 0;
		
		while (!finished && i < maxIterations) {
			finished = true;

			//All processes are put to the next state
			for (ProcessLogic p : processes) {
				p.nextState();
				finished &= p.isFinished();
			}
			
			//Look how many resources are acquired by the processes
			int[] allocatedRes = new int[] { 0, 0, 0, 0, 0 };
			for (ProcessLogic p : processes) {
				allocatedRes = CUtil.add(allocatedRes, p.getAcquiredRes());
			}
			//Put the resources to the next state and check for a overload
			overloaded |= resources.nextState(allocatedRes);
			
			//Put infoMessage to next state
			messages.nextState();
			
			//gather requests for new iteration
			refreshRequests();
			
			//calculate the next step by calling the AllocationAlgo 
			debugMessage = algo.nextStep(requests.toArray(new AllocationRequest[requests.size()]));

			//some mystic stuff
			i++;
		}
		
		this.iterations = i;
		this.overloaded = overloaded;
		this.finished = finished;
	}
	
	/** Some getters used by the SimulationModel in order to access the data. **/
	public ProcessLogic[] getProcesses() {
		return processes;
	}

	public ResourcesLogic getResources() {
		return resources;
	}

	public int getIterations() {
		return iterations;
	}
	
	public InfoMessages getMessages() {
		return messages;
	}
	
	public boolean isOverloaded() {
		return overloaded;
	}

	public boolean isFinished() {
		return finished;
	}

	public String getAlgoName() {
		return algoName;
	}



	/**
	 * This class is used to store several infoMessage which will be
	 * calculated during the simulation. Each message is tied to a specific state/step.
	 *
	 */
	public class InfoMessages{
		
		final private String[] resNames = { "PURPLE", "GREEN", "PINK", "ORANGE", "BLUE" };
		private String[] messages = new String[100];
		private int state;
		private StringProperty sp = new SimpleStringProperty("<init>");
		
		/**
		 * Collects the debugMessage and all Requests; stores them;
		 * goes to the next state.
		 */
		void nextState() {
			String lb = System.getProperty("line.separator");
			StringBuffer logMsg = new StringBuffer();
			
			//Collect DebugMessage
			if(debugMessage != null){
				logMsg.append(debugMessage);
				logMsg.append(lb);
				debugMessage = null;
			}

			//Collect informations about current requests
			for (AllocationRequestImpl req : requests) {
				if (req.isGranted()) {
					logMsg.append("(granted)   ");
				} else {
					logMsg.append("(rejeceted) ");
				}
				logMsg.append("Process P ");
				logMsg.append(req.getProcess().getId());
				logMsg.append(" is asking for ");
				logMsg.append(resNames[req.getResId()]);
				logMsg.append(lb);
			}
			
			//Save message and go to next state
			messages[state] = logMsg.toString();
			state++;
		}
		
		/**
		 * Returns the StringProperty which will be used to
		 * display the current infoMessage. (It is important to
		 * use a String>Property< object in order to establish
		 * a link to the GUI)
		 */
		public StringProperty getStringProperty(){
			return sp;
		}
		
		/**
		 * Displays the infoMessage of state i.
		 * (by changing the content of the StringProperty)
		 */
		public void displayState(int i){
			sp.setValue(messages[i]);
		}
	}

}
