package fx;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Thierry Meurers
 *
 * Entry point of the MD5 Cracking Framework
 */
public class MainFX extends Application{

	
	/**
	 * Name of the used HashCracker class. The class will be loaded using the
	 * reflection api. Default is "hashCracker.SimpleCracker" and will load a 
	 * class called SimpleCracker in package hashCracker.
	 */
	final static private String crackerClass = "hashCracker.BetterCracker";
	//final static private String crackerClass = "hashCracker.OptimizedCracker";
	
	/**
	 * Defines which algorithm should be used to create random hashes and match
	 * incoming password values. (SHA-1, SHA-256, MD5)
	 */
	final static private String messageDigestInstance = "MD5";
	
	
	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		MainModel model = new MainModel(primaryStage, crackerClass, messageDigestInstance);
		new MainController(model).show();
	}
}
