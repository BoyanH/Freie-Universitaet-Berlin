package fx.simulation;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Thierry Meurers
 *
 * This class defines how the Simulation Tab looks. All components are
 * created, styled and assigned to the GUI.
 * Furthermore get-Methods are provided.
 */
public class SimulationView {

	final static private int WIDTH = 980;
	final static private int HEIGHT = 610;
	
	//All ProcessViews are stored in a FlowPane;
	//The ResourcesView is part of a BorderPane layout.
	private FlowPane processesFP;
	private BorderPane resInfoBP;
	
	private TextArea infoMessageTA;
	
	private Slider stateSL;
	private Button prevBTN;
	private Button nextBTN;
	private Label stateLB;
	
	private Scene scene;
	
	
	public SimulationView(int iterations){
		BorderPane borderPane = new BorderPane();
		scene = new Scene(borderPane);
		scene.getStylesheets().add("fx/Themen.css");

		HBox hBox = new HBox();
		BorderPane.setAlignment(hBox, Pos.CENTER);
		hBox.setPrefHeight(40);
		hBox.setMinHeight(40);
		hBox.setSpacing(10);
		hBox.setPadding(new Insets(5, 5, 5, 5));
		BorderPane.setMargin(hBox, new Insets(0, 0, 10, 0));
		
		stateSL = new Slider();
		stateSL.setMax(iterations);
		stateSL.setMinorTickCount(4);
		stateSL.setMajorTickUnit(5);
		stateSL.snapToTicksProperty().set(true);
		stateSL.setShowTickLabels(true);
		stateSL.setShowTickMarks(true);
		stateSL.setBlockIncrement(1);
		HBox.setHgrow(stateSL, Priority.ALWAYS);
		
		prevBTN = new Button("<<");
		nextBTN = new Button(">>");
		
		stateLB = new Label("state");
		stateLB.setPrefWidth(40);
		stateLB.setStyle("-fx-font-weight: bold;");
		
		hBox.getChildren().addAll(prevBTN, stateSL, stateLB, nextBTN);

		processesFP = new FlowPane();
		processesFP.setPadding(new Insets(10, 10, 10,10));
		processesFP.setVgap(5);
		processesFP.setHgap(5);
		
		
		resInfoBP = new BorderPane();
		
		infoMessageTA = new TextArea("Test");
		infoMessageTA.setPrefHeight(160);
		
		resInfoBP.setCenter(infoMessageTA);
		resInfoBP.setPadding(new Insets(10, 10, 10,10));
		
		BorderPane.setMargin(infoMessageTA, new Insets(0, 0, 0, 10));
		
		VBox vBox = new VBox();
		vBox.getChildren().addAll(processesFP, resInfoBP);
		
		borderPane.setCenter(vBox);
		borderPane.setBottom(hBox);
		
		stateSL.requestFocus();
		
	}
	
	public void show(Stage stage, String title) {
		stage.setScene(scene);
		stage.show();
		//In order to display the right size on linux machines 
		//the size is also set to the stage (not only the scene)
		stage.setHeight(HEIGHT);
		stage.setWidth(WIDTH);
		stage.setMinHeight(220);
		stage.setMinWidth(340);
		
		stage.setTitle(title);
	}

	
	
	/**
	 * Getters used by the SeimulationController
	 */
	public FlowPane getProcessesFP() {
		return processesFP;
	}

	public Slider getStateSL() {
		return stateSL;
	}

	public Button getPrevBTN() {
		return prevBTN;
	}

	public Button getNextBTN() {
		return nextBTN;
	}

	public Label getStateLB() {
		return stateLB;
	}
	
	public BorderPane getResInfoBP() {
		return resInfoBP;
	}
	
	public TextArea getInfoMessageTA(){
		return infoMessageTA;
	}
	
}
