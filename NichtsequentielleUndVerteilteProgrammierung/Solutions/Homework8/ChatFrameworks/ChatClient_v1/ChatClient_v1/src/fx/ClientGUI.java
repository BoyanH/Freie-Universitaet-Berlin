package fx;

import javafx.scene.paint.Color;

/**
 * @author Thierry Meurers
 * 
 * Client GUI interface. All listed functionalities are provided by the GUI and can be used by the network class.
 *
 */
public interface ClientGUI {

	/**
	 * Writes a new message followed by a line break to the gui. 
	 * 
	 * @param msg Message to show
	 */
	void pushChatMessage(String msg);
	
	/**
	 * Changes the color of the little "status led".
	 * Could be used to show whether or not the client is currently connected.
	 * 
	 * @param clr New Color of the Status LED
	 */
	void setSymbolColor(Color clr);
	
	/**
	 * Changes the title of the primary stage. Could be used to provide
	 * some information about the current connection or user state.
	 * (e.g. name of client, address of server, time of last message receive ..)
	 * 	  
	 * @param s New window title
	 */
	void setWindowTitle(String s);
	
}
