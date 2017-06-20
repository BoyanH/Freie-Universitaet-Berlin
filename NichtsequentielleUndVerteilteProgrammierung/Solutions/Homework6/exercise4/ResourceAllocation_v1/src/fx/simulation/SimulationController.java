package fx.simulation;

import java.text.NumberFormat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import process.ProcessLogic;

/**
 * @author Thierry Meurers
 * 
 *         The controller class is connected to the model and the view.
 * 
 * 	       It enables the user to show different states of the simulation 
 *		  (which are stored in den model)by manipulating the slide.
 * 	
 */
public class SimulationController {

	SimulationView view;
	SimulationModel model;

	/**
	 * Receives and stores the model; Initializes the view; 
	 * Creates EventHandler classes and adds them to the regarding view components;
	 * connects the view to the process views, resources view, and the StringProperty
	 * of the messages.
	 */
	public SimulationController(SimulationModel model) {
		this.model = model;
		this.view = new SimulationView(model.getIterations());

		view.getStateLB().textProperty().bindBidirectional(view.getStateSL().valueProperty(),
				NumberFormat.getNumberInstance());

		view.getNextBTN().setOnAction(new NextBTNHandler());
		view.getNextBTN().setOnKeyPressed(new NextBTNHandlerE());
		view.getPrevBTN().setOnAction(new PrevBTNHandler());
		view.getPrevBTN().setOnKeyPressed(new PrevBTNHandlerE());
		view.getStateSL().valueProperty().addListener(new StateSLHandler());

		view.getProcessesFP().getChildren().addAll(model.getProcessViews());
		view.getResInfoBP().setLeft(model.getResourcesView());
		view.getInfoMessageTA().textProperty().bind(model.getMessagesStringProperty());
	}

	public void show(Stage stg) {
		view.show(stg, model.getStageTitle());
	}

	
	/**
	 *	This handler is called whenever the value of the slider changes.
	 *  Calls the showState method.
	 */
	class StateSLHandler implements ChangeListener<Number> {
		
		@Override
		public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
			showState((int)view.getStateSL().getValue());
		}
	}

	/** 
	 * The following 4 handlers are changing the displayed state by manipulating the
	 * value of the slider --- maybe a bit dirty 
	 **/
	class NextBTNHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			view.getStateSL().increment();
		}
	}

	class NextBTNHandlerE implements EventHandler<KeyEvent> {

		public void handle(KeyEvent e) {
			if (e.getCode().equals(KeyCode.ENTER))
				view.getStateSL().increment();
		}
	}

	class PrevBTNHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			view.getStateSL().decrement();
		}
	}

	class PrevBTNHandlerE implements EventHandler<KeyEvent> {

		public void handle(KeyEvent e) {
			if (e.getCode().equals(KeyCode.ENTER))
				view.getStateSL().decrement();
		}
	}
	
	/**
	 * Displays state i by calling the displayState method on
	 * the resources, processes and messages.
	 */
	public void showState(int i){
		ProcessLogic[] processes = model.getProcesses();
		for(ProcessLogic pl : processes){
			pl.displayState(i);
		}
		model.getResources().displayState(i);
		model.getMessages().displayState(i);
		
	}

}
