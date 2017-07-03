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
import network.AbstractChatServer;

/**
 * @author Thierry Meurers
 * 
 *         The controller class is connected to the model, the view and the
 *         network class.
 * 
 *         It realizes the interaction between network class and view by
 *         providing the methods declared in the interface ServerGUI and using
 *         the methods declared in the abstract class ServerChatClient.
 * 
 */
public class ServerController implements ServerGUI {

	private ServerView view;
	private ServerModel model;

	private AbstractChatServer server;

	/**
	 * Receives and stores the model; Initializes the view; Initializes the
	 * network class by using the reflection api and the name of the requested
	 * network class (provided by the model); Creates EventHandler classes and
	 * adds them to the regarding view components; Binds the table to the content 
	 * of the clients-list located in the model;
	 * 
	 * @param model
	 */
	public ServerController(ServerModel model) {
		this.model = model;
		view = new ServerView();

		initNetworkInterface();

		view.getChatTA().appendText(model.getWelcomeText());
		view.getPortTF().setText(model.getDefaultPort());

		view.getStartBTN().setOnAction(new StartBTNHandler());
		view.getStartBTN().setOnKeyPressed(new StartBTNHandlerE());
		view.getStopBTN().setOnAction(new StopBTNHandler());
		view.getStopBTN().setOnKeyPressed(new StopBTNHandlerE());

		view.getSubmitBTN().setOnAction(new SubmitBTNHandler());
		view.getSubmitBTN().setOnKeyPressed(new SubmitBTNHandlerE());
		model.getPrimaryStage().setOnCloseRequest(new CloseBTNHandler());
		view.getMsgTF().setOnKeyPressed(new SubmitBTNHandlerE());
		
		view.getClientIdTC().setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
		view.getClientNameTC().setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
		view.getClientTV().setItems(model.getClients());
	}
	
	
	/**
	 * Calls the readChatMessage method and is triggered whenever the submit button is pressed.
	 */
	class SubmitBTNHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			readChatMessage();
		}
	}

	/**
	 * Calls the readChatMessage method and is triggered whenever the Enter key is pressed
	 * and the send button or TextField is focused.
	 */
	class SubmitBTNHandlerE implements EventHandler<KeyEvent> {

		public void handle(KeyEvent e) {
			if (e.getCode().equals(KeyCode.ENTER))
				readChatMessage();
		}
	}

	/**
	 * Called whenever the Start Button is triggered. 
	 */
	class StartBTNHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			server.start(view.getPortTF().getText());
		}
	}

	class StartBTNHandlerE implements EventHandler<KeyEvent> {

		public void handle(KeyEvent e) {
			if (e.getCode().equals(KeyCode.ENTER))
				server.start(view.getPortTF().getText());
		}
	}

	/**
	 * Called whenever the Stop Button is triggered. 
	 */
	class StopBTNHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			server.stop();
		}
	}

	class StopBTNHandlerE implements EventHandler<KeyEvent> {

		public void handle(KeyEvent e) {
			if (e.getCode().equals(KeyCode.ENTER))
				server.stop();
		}
	}

	/**
	 * Called whenever the GUI closes. Informs the network class to close
	 * all connections reasonable.
	 */
	class CloseBTNHandler implements EventHandler<WindowEvent> {

		public void handle(WindowEvent e) {
			server.terminate();
		}
	}

	/**
	 * Initializes the network class by:
	 * 1. Searching for a class with the denoted name
	 * 2. Searching for a constructor which receives a ServerGUI as parameter
	 * 3. Using the constructor to instantiate the network class
	 */
	private void initNetworkInterface() {
		try {
			server = (AbstractChatServer) Class.forName(model.getNetworkInterface())
					.getConstructor(new Class[] { Class.forName("fx.ServerGUI") }).newInstance(this);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Processes the user input by splitting a instruction into the
	 * command and its value. 
	 * 
	 * Example 1: 
	 * input: "/kick curry"
	 * command: "/kick"
	 * value: "curry"
	 * 
	 * Example 2:
	 * input: "hello"
	 * command: ""
	 * value: "hello"
	 */
	private void readChatMessage() {
		TextField msgTF = view.getMsgTF();
		String msg = msgTF.getText().trim();

		if (msg.replaceAll("\\s", "").isEmpty()) {
			msgTF.clear();
			return;
		}

		String[] splitMsg = msg.split("\\s+|,|:");

		if (splitMsg[0].equals("/q") || splitMsg[0].equals("/quit")) {
			server.terminate();
			model.getPrimaryStage().close();
		} else if (msg.startsWith("/")) {
			server.receiveConsoleCommand(splitMsg[0], msg.substring(splitMsg[0].length()).trim());
		} else {
			server.receiveConsoleCommand("", msg);
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
	public void pushConsoleMessage(String msg) {
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

	@Override
	public boolean removeClient(int id) {
		return model.removeClient(id);
	}



	@Override
	public void addClient(int id, String name) {
		model.addClient(id, name);
	}

}
