package fx;

import java.lang.reflect.InvocationTargetException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;
import network.AbstractChatClient;

/**
 * @author Thierry Meurers
 * 
 *         The controller class is connected to the model, the view and the
 *         network class.
 * 
 *         It realizes the interaction between network class and view by
 *         providing the methods declared in the interface ClientGUI and using
 *         the methods declared in the abstract class AbstractChatClient.
 * 
 */
public class ClientController implements ClientGUI {

	private ClientView view;
	private ClientModel model;

	private AbstractChatClient client;

	/**
	 * Receives and stores the model; Initializes the view; Initializes the
	 * network class by using the reflection api and the name of the requested
	 * network class (provided by the model); Creates EventHandler classes and
	 * adds them to the regarding view components.
	 * 
	 * @param model
	 */
	public ClientController(ClientModel model) {
		this.model = model;
		view = new ClientView();

		initNetworkInterface();
		view.getUNameLB().setText(client.getUName());

		view.getChatTA().appendText(model.getWelcomeText());

		view.getSendBTN().setOnAction(new SendBTNHandler());
		view.getSendBTN().setOnKeyPressed(new SendBTNHandlerE());
		model.getPrimaryStage().setOnCloseRequest(new CloseBTNHandler());
		view.getMsgTF().setOnKeyPressed(new SendBTNHandlerE());
	}

	
	/**
	 * Calls the readChatMessage method and is triggered whenever the send button is pressed.
	 */
	class SendBTNHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			readChatMessage();
		}
	}

	/**
	 * Calls the readChatMessage method and is triggered whenever the Enter key is pressed
	 * and the send button or TextField is focused.
	 */
	class SendBTNHandlerE implements EventHandler<KeyEvent> {

		public void handle(KeyEvent e) {
			if (e.getCode().equals(KeyCode.ENTER))
				readChatMessage();
		}
	}


	/**
	 * Called whenever the GUI closes. Informs the network class to close
	 * all connections reasonable.
	 */
	class CloseBTNHandler implements EventHandler<WindowEvent> {

		public void handle(WindowEvent e) {
			client.terminate();
		}
	}
	
	/**
	 * Initializes the network class by:
	 * 1. Searching for a class with the denoted name
	 * 2. Searching for a constructor which receives a ClientGUI as parameter
	 * 3. Using the constructor to instantiate the network class
	 */
	private void initNetworkInterface() {
		try {
			client = (AbstractChatClient) Class.forName(model.getNetworkInterface())
					.getConstructor(new Class[] { Class.forName("fx.ClientGUI") }).newInstance(this);

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Processes the user input by looking for predefined commands and
	 * triggering the requested functions or just pushing the message
	 * to the network class for transmission.
	 */
	private void readChatMessage() {
		TextField msgTF = view.getMsgTF();
		String msg = msgTF.getText().trim();
		String[] splitMsg = msg.split("\\s+|,|:");

		if (msg.replaceAll("\\s", "").isEmpty()) {
			msgTF.clear();
			return;
		}

		switch (splitMsg[0]) {
		case "/n":
		case "/name":
			if (splitMsg.length > 1) {
				client.setUName(splitMsg[1]);
				view.getUNameLB().setText(splitMsg[1]);
			} else {
				pushChatMessage("INFO: Please use \"/n(ame) my_new_Name\" to change your Name");
			}
			break;

		case "/q":
		case "/quit":
			client.terminate();
			model.getPrimaryStage().close();
			break;

		case "/d":
		case "/disconnect":
			client.disconnect();
			break;

		case "/c":
		case "/connect":
			if (splitMsg.length == 1) {
				pushChatMessage("INFO: Connecting to " + model.getDefaultAddress() + ":" + model.getDefaultPort()
						+ " (default server)");
				client.connect(model.getDefaultAddress(), model.getDefaultPort());
			} else if (splitMsg.length == 2) {
				pushChatMessage("INFO: Please use \"/c(onnect) 1.3.3.7 5050\" to connect");
			} else if (splitMsg.length >= 3) {
				pushChatMessage("INFO: Connecting to " + splitMsg[1] + ":" + splitMsg[2]);
				client.connect(splitMsg[1], splitMsg[2]);
			}
			break;

		default:
			client.sendChatMessage(msg);
		}
		msgTF.clear();
	}

	/**
	 * Used by MainFX to show the GUI. Passes the primaryStage to the view.
	 */
	public void show() {
		view.show(model.getPrimaryStage());
	}

	
	/** 
	 * This methods are called by the network class in order to update the gui with
	 * new informations. The updates are executed within a Platform.runLater statement
	 * to ensure thread safety.
	 */
	@Override
	public void pushChatMessage(String msg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				view.getChatTA().appendText(msg + model.getLineSparator());
			}
		});
	}

	@Override
	public void setSymbolColor(Color clr) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				view.getStatusCL().setFill(clr);
			}
		});
	}

	@Override
	public void setWindowTitle(String s) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				model.getPrimaryStage().setTitle(s);
			}
		});
	}

}
