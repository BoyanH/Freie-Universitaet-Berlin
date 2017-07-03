package fx;

import javafx.stage.Stage;

/**
 * @author Thierry Meurers
 * 
 *         This class is used to store parameters which will be used by the
 *         Controller class.
 *
 */
public class ClientModel {

	private Stage primaryStage;
	
	private String networkInterface;
	private String defaultAddress;
	private String defaultPort;

	private final String welcomeText = String
			.format("Control Commands: %n  /n(ame) <new_name>%n  /c(onnect) ip:port%n  /d(isconnect)%n  /q(uit)%n%n");
	private final String lineSparator = System.getProperty("line.separator");

	public ClientModel(Stage primaryStage, String networkInterface, String defaultAddress, String defaultPort) {
		this.primaryStage = primaryStage;
		this.networkInterface = networkInterface;
		this.defaultAddress = defaultAddress;
		this.defaultPort = defaultPort;
	}

	/**
	 * Getters used by the controller.
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public String getWelcomeText() {
		return welcomeText;
	}

	public String getLineSparator() {
		return lineSparator;
	}

	public String getNetworkInterface() {
		return networkInterface;
	}

	public String getDefaultAddress() {
		return defaultAddress;
	}

	public String getDefaultPort() {
		return defaultPort;
	}

}
