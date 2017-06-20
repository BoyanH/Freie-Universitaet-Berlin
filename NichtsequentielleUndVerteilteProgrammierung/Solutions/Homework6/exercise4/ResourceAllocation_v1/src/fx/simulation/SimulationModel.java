package fx.simulation;

import java.util.ArrayList;

import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;
import process.ProcessLogic;
import resources.ResourcesLogic;
import simulation.Simulation;
import simulation.Simulation.InfoMessages;

/**
 * @author Thierry Meurers
 * 
 *         This class is used to store all data calculated by the simulation.
 *         The data is accessed by the controller and is displayed using the view.
 *
 */
public class SimulationModel {

	private ProcessLogic[] processes;
	private ResourcesLogic resources;
	private InfoMessages messages;
	private int iterations;
	private String stageTitle;
	
	/** Stores all the important data and creates a title for the stage. **/
	public SimulationModel(Simulation sim) {
		this.processes = sim.getProcesses();
		this.resources = sim.getResources();
		this.iterations	= sim.getIterations();
		this.messages = sim.getMessages();
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(sim.getAlgoName());
		if(sim.isFinished()){
			buffer.append(" - finished after ");
			buffer.append(iterations);
		}	else{
			buffer.append(" - aborted after ");
			buffer.append(iterations);
		}
		buffer.append(" iterations");
		if(sim.isOverloaded())
			buffer.append(" (OVERLOADED)");
		this.stageTitle = buffer.toString();
	}
	
	/** 
	 * The following 3 method are used to connect the ProcessView's, ResourcesView and 
	 * StringProperty of the messages to the SimulationView 
	 * **/
	public ArrayList<Pane> getProcessViews() {
		ArrayList<Pane> pViews = new ArrayList<Pane>();
		for(ProcessLogic pl : processes){
			pViews.add(pl.getProcessView());
		}
		return pViews;
	}
	
	public Pane getResourcesView(){
		return resources.getProcessView();
	}
	
	public StringProperty getMessagesStringProperty() {
		return messages.getStringProperty();
	}
	
	/**
	 * Getters used by the controller class
	 */
	public ProcessLogic[] getProcesses() {
		return processes;
	}

	public ResourcesLogic getResources() {
		return resources;
	}
	
	public InfoMessages getMessages() {
		return messages;
	}
	
	public int getIterations() {
		return iterations;
	}
	
	public String getStageTitle() {
		return stageTitle;
	}
	
}
