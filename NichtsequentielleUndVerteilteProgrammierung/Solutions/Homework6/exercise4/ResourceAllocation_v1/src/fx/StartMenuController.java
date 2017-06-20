package fx;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import allocationAlgorithm.Algorithm;
import fx.simulation.SimulationController;
import fx.simulation.SimulationModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import simulation.Simulation;

/**
 * @author Thierry Meurers
 *
 * Controller class for the StartMenu window. All fields are injected by fxml and are tied 
 * to the layout defined in the BridgeOptions.fxml file.
 */
public class StartMenuController {

	@FXML
	private Slider rCountSL;

	@FXML
	private Button startBTN;

	@FXML
	private Slider pGreedSL;

	@FXML
	private CheckBox rCountCB;

	@FXML
	private Slider rSupplySL;

	@FXML
	private Slider pCountSL;

	@FXML
	private CheckBox pCountCB;

	@FXML
	private CheckBox pGreedCB;

	@FXML
	private CheckBox rSupplyCB;

	@FXML
	private ComboBox<String> algoCB;

	
	/**
	 * This method is called on start by the StartMenuFX-class. 
	 * All algorithm classes are, announced in StartMenuFX, are set to the Combobox.
	 * 
	 * @param mutexClasses	Array of possible mutex classes
	 */
	public void setAlgorithmClasses(String[] algorithmClasses) {
		algoCB.getItems().addAll(algorithmClasses);
		if (algorithmClasses.length > 0) {
			algoCB.setValue(algorithmClasses[0]);
		}
	}

	/**
	 * Poor proof of the fact, that not all planned features where 
	 * actually implemented by the framework. Besides newRun there
	 * should be a feature called loadedRun... nevermind.
	 */
	public void startRun() {
		startNewRun();
	}

	/**
	 * Starts a new run by reading all options from the GUI and creating a new
	 * Simulation-Object. This object will be used to create a new SimulationModel
	 * and -Controller (in order to show the result).
	 */
	private void startNewRun() {
		
		//Read all options from GUI
		String algoName = algoCB.getValue();
		int pCount = pCountCB.isSelected() ? -1 : (int) pCountSL.getValue();
		double pGreed = pGreedCB.isSelected() ? -1 : pGreedSL.getValue();
		int rCount = rCountCB.isSelected() ? -1 : (int) rCountSL.getValue();
		double rSupply = rSupplyCB.isSelected() ? -1 : rSupplySL.getValue();
		
		try {
			//Instantiate the allocation algorithm and start the simulation
			Algorithm algo = initAllocationAlgorithm(algoName);
			Simulation sim = new Simulation(algo, algoName, pCount, pGreed, rCount, rSupply);
			
			//Create new SimulationModel and -Controller to show the result
			SimulationModel model = new SimulationModel(sim);
			new SimulationController(model).show(new Stage());

		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException
				| InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {

			// This exceptions are caused by the reflection 
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Reflection failed");
			alert.setContentText("Could not load \"" + algoName + "\".");
			alert.showAndWait();
		}

	}

	/**
	 * Initializes the allocation algorithm  by using the java reflection api.
	 * 1. Searching for a class with the denoted name
	 * 2. Get the default constructor
	 * 3. Using the constructor to instantiate the BridgeControl class
	 */
	private Algorithm initAllocationAlgorithm(String classname)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> c = Class.forName(classname);
		Constructor<?> constructor = c.getConstructor();
		return (Algorithm) constructor.newInstance();
	}

	/**
	 * All settings (except for the algo class) are saved to a external file.
	 * Called whenever the PrimaryStage is closed.
	 */
	public void saveOptions() {
		try (OutputStream output = new FileOutputStream("AllocationFX.properties")) {

			Properties prop = new Properties();

			prop.setProperty("pCountSL", pCountSL.getValue() + "");
			prop.setProperty("pGreedSL", pGreedSL.getValue() + "");
			prop.setProperty("rCountSL", rCountSL.getValue() + "");
			prop.setProperty("rSupplySL", rSupplySL.getValue() + "");

			prop.setProperty("pCountCB", pCountCB.isSelected() + "");
			prop.setProperty("pGreedCB", pGreedCB.isSelected() + "");
			prop.setProperty("rCountCB", rCountCB.isSelected() + "");
			prop.setProperty("rSupplyCB", rSupplyCB.isSelected() + "");

			prop.store(output, "StartMenuFX Options File");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read all options from external file to the GUI.
	 */
	public void readOptions() {
		Properties prop = new Properties();

		try (InputStream input = new FileInputStream("AllocationFX.properties")) {

			prop.load(input);

			pCountSL.setValue(Double.valueOf(prop.getProperty("pCountSL")));
			pGreedSL.setValue(Double.valueOf(prop.getProperty("pGreedSL")));
			rCountSL.setValue(Double.valueOf(prop.getProperty("rCountSL")));
			rSupplySL.setValue(Double.valueOf(prop.getProperty("rSupplySL")));

			pCountCB.setSelected(Boolean.valueOf(prop.getProperty("pCountCB")));
			pGreedCB.setSelected(Boolean.valueOf(prop.getProperty("pGreedCB")));
			rCountCB.setSelected(Boolean.valueOf(prop.getProperty("rCountCB")));
			rSupplyCB.setSelected(Boolean.valueOf(prop.getProperty("rSupplyCB")));

		} catch (IOException e) {
			System.err.println("Could not read (all) Options from file (BridgeFX.properties)");
		}

	}

}
