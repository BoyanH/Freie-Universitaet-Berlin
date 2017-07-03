package fx;

import javafx.application.Application;
import javafx.stage.Stage;

public class ServerFX extends Application {

	/**
	 * Name of the used network class. The class will be loaded using the
	 * reflection api. Default is "network.ServerUDP" and will load a class
	 * called ServerUDP in package network.
	 */
	final static private String networkInterface = "network.MyServer";

	/**
	 * Default port for faster testing. This port will be written to the gui
	 * whenever the program starts.
	 */
	final static private String defaultPort = "13337";
	
	
	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		
		ServerModel model = new ServerModel(primaryStage, networkInterface, defaultPort);
		new ServerController(model).show();
		
	}

}
