package network;

import fx.ServerGUI;

public abstract class AbstractChatServer{

	protected ServerGUI gui;
	
	public AbstractChatServer(ServerGUI gui){
		this.gui = gui;
	}
	
	/**
	 * Called whenever a instruction is submitted. 
	 * The instruction is split up into the command and the value.
	 * For Example: "/kick curry peledao snooc"
	 * Command: "kick"
	 * Value: "curry peledao snooc"
	 * 
	 * @param command	First part of instruction - the command
	 * @param msg		Second part of instruction - the value
	 */
	public abstract void receiveConsoleCommand(String command, String msg);
	
	/**
	 * Starts the service on the provided port. Called whenever the
	 * Start button is toggled.
	 * 
	 * @param port		Port to access the service
	 */
	public abstract void start(String port);
	
	/**
	 * Stops the service in a reasonable way. Called whenever the 
	 * Stop Button is toggled.
	 */
	public abstract void stop();
	
	/**
	 * Terminates the server network class in a reasonable way. This method is
	 * called whenever the /quit command is entered or the close button is
	 * toggled.
	 */
	public abstract void terminate();
	
}
