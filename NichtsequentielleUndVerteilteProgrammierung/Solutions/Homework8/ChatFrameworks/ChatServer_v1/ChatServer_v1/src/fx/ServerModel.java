package fx;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

/**
 * @author Thierry Meurers
 * 
 *         This class is used to store parameters which will be used by the
 *         Controller class. Furthermore the ClientTuple class is defined
 *         and functions to add and remove Client Tuples are provided.
 *
 */

public class ServerModel {

	//Infos provieded by ServerFX
	private Stage primaryStage;
	private String networkInterface;
	private String defaultPort;

	private final String welcomeText = String.format("Control Commands: %n  /q(uit)%n%n");
	private final String lineSparator = System.getProperty("line.separator");

	//List of all ClientTuple objects. Observable in order to show the content in the GUI.
	private ObservableList<ClientTuple> clients = FXCollections.observableArrayList();


	public ServerModel(Stage primaryStage, String networkInterface, String defaultPort) {
		this.primaryStage = primaryStage;
		this.networkInterface = networkInterface;
		this.defaultPort = defaultPort;
	}

	/**
	 * Simple Getters used by the Controller.
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

	public String getDefaultPort() {
		return defaultPort;
	}

	public ObservableList<ClientTuple> getClients() {
		return clients;
	}

	/**
	 * Creates a new ClientTuple and stores it in the clients list.
	 */
	public void addClient(int id, String name){
		clients.add(new ClientTuple(id, name));
	}
	
	/**
	 * Removes a ClientTuple afrom the the clients list.
	 */
	public boolean removeClient(int id){
		return clients.remove(new ClientTuple(id, ""));
	}
	
	/**
	 * Defines the values stored in the table.
	 * Each ClientTuple consist of two StringProperties.
	 * One for the hash, one for the seed/password.
	 */
	class ClientTuple {

		int id;
		SimpleStringProperty idProperty;
		SimpleStringProperty nameProperty;

		
		ClientTuple(int id, String name) {
			this.id = id;
			idProperty = new SimpleStringProperty(id+"");
			nameProperty = new SimpleStringProperty(name);
		}

		SimpleStringProperty getIdProperty(){
			return idProperty;
		}
		
		SimpleStringProperty getNameProperty(){
			return nameProperty;
		}
		
		//In order to use the remove function inherited from list
		//the equals function is overwritten.
		@Override
		public boolean equals(Object c) {
			if (c == null)
				return false;
			if (!(c instanceof ClientTuple))
				return false;
			ClientTuple ct = (ClientTuple) c;
			return ct.id == this.id;
		}
		
		@Override 
		public int hashCode(){
			return id;
		}

	}

}
