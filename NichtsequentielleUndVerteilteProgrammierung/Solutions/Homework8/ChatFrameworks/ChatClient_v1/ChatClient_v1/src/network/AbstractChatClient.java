package network;

import java.util.Random;

import fx.ClientGUI;

public abstract class AbstractChatClient {

	protected ClientGUI gui;
	protected String uName;
	private String[] defNames = {"curry", "pege", "clean", "peledao", "snooc", "vanHagen"};

	/**
	 * A random user name is assigned and the gui is passed
	 * to the network class.
	 * 
	 * @param gui
	 */
	public AbstractChatClient(ClientGUI gui) {
		Random rnd = new Random();
		uName = defNames[rnd.nextInt(defNames.length)] + "-" + rnd.nextInt(100);
		this.gui = gui;
	}

	/**
	 * In order to propagate a new name to the network this method should
	 * be modified. Called whenever the user changes the name by calling
	 * "/name xyz" ur "/n xyz".
	 * 
	 * @param uName	  new user name
	 */
	public void setUName(String uName) {
		this.uName = uName;
	}

	/**
	 * Called once by the GUI right after the ChatClient is initialized.
	 * 
	 * @return	current user name
	 */
	public String getUName() {
		return uName;
	}

	/**
	 * Sends the message to the server. Called whenever the user enters and
	 * submits a message.
	 * 
	 * @param msg
	 *            Message to transmit
	 */
	public abstract void sendChatMessage(String msg);

	/**
	 * Establishes a connection to the new server. Called whenever the /connect
	 * command is entered.
	 *
	 * @param address
	 *            Address of chat server
	 * @param port
	 *            Port of chat server
	 */
	public abstract void connect(String address, String port);

	/**
	 * Closes a existing connection to the server in a reasonable way. This
	 * method is called whenever the /disconnect command is entered.
	 */
	public abstract void disconnect();

	/**
	 * Terminates the client network class in a reasonable way. This method is
	 * called whenever the /quit command is entered or the close button is
	 * toggled.
	 */
	public abstract void terminate();

}
