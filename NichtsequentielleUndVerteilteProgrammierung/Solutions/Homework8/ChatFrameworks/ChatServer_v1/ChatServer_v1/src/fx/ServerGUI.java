package fx;

import javafx.scene.paint.Color;

public interface ServerGUI {
	
	/**
	 * Writes a new message followed by a line break to the gui. 
	 * 
	 * @param msg Message to show
	 */
	void pushConsoleMessage(String msg);
	
	/**
	 * Changes the color of the little "status led".
	 * Could be used to show whether or not the server is currently available.
	 * 
	 * @param clr New Color of the Status LED
	 */
	void setSymbolColor(Color clr);
	
	/**
	 * Changes the title of the primary stage. Could be used to provide
	 * some information about the current connection.
	 * (e.g. number of connected clients, address of server, time of last message receive ..)
	 * 	  
	 * @param s New window title
	 */
	void setWindowTitle(String s);
	
	/**
	 * Adds a new client to the table on the right.
	 * 
	 * @param id			(unique) id of client
	 * @param description	client description/name
	 */
	void addClient(int id, String description);
	
	/**
	 * Removes client from table.
	 * 
	 * @param id	id of client
	 * @return		true, if client was found (and removed)
	 */
	boolean removeClient(int id);

	
}
