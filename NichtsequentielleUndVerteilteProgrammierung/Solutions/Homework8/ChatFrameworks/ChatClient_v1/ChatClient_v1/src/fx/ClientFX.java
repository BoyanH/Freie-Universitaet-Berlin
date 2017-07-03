package fx;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Thierry Meurers
 *
 * Entry point of the Chat Client Framework.
 */
public class ClientFX extends Application {

	/**
	 * Name of the used network class. The class will be loaded using the
	 * reflection api. Default is "network.ClientTCP" and will load a class
	 * called ClientTCP in package network.
	 */
	final static private String networkInterface = "network.ClientTCP";

	/**
	 * Default address and port for faster testing. Whenever the command
	 * /connect is called without further parameters, this default parameters will
	 * be used.
	 *
	 */
	final static private String defaultAddress = "localhost";
	final static private String defaultPort = "13337";

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		ClientModel model = new ClientModel(primaryStage, networkInterface, defaultAddress, defaultPort);
		new ClientController(model).show();
	}

}
