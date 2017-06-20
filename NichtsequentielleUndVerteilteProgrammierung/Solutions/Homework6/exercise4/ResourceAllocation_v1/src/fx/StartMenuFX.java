package fx;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author Thierry Meurers
 *
 * Entry point of the Resource Allocation Framework.
 */
public class StartMenuFX extends Application {

	/**
	 * All the classes listed in the algorithmClasses array will be displayed in the
	 * ComboBox. By pressing the Start button, the selected class will be loaded 
	 * using the reflection api.
	 */
	final static private String[] algorithmClasses = {"allocationAlgorithm.BankersAlgo", "allocationAlgorithm.ExampleAlgo", "allocationAlgorithm.ExampleAlgo2"};

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Creates and displays the StartMenu
	 */
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("StartMenuLayout.fxml"));

		Parent root = null;
		try {
			root = (loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		StartMenuController startMenuController = loader.getController();
		startMenuController.setAlgorithmClasses(algorithmClasses);
		startMenuController.readOptions();

		Scene scene = new Scene(root, 600, 460);
		scene.getStylesheets().add("fx/Themen.css");
		
		stage.setTitle("Resource Allocation Simulator (v1.0)");
		stage.setScene(scene);
		stage.show();
		
		//Whenever the stage is closed -> save all options
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				startMenuController.saveOptions();
			}
		});
	}

	
}
