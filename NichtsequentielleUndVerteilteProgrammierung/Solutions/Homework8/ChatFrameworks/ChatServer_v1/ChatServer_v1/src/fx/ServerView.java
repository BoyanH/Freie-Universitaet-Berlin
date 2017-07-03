package fx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import fx.ServerModel.ClientTuple;

/**
 * @author Thierry Meurers
 *
 *         This class is used to initialize, style and layout all components of
 *         the gui. It is created by the controller class which will add
 *         functionalities to the components.
 */
public class ServerView {
	
	final static private int WIDTH = 800;
	final static private int HEIGHT = 500;
	
	private Scene scene;
	
	private Circle statusCL;
	private Label srvLB;
	private Button startBTN;
	private Button stopBTN;
	private TextField portTF;
	
	private TextArea chatTA;
	
	private Button submitBTN;
	private TextField msgTF;
	
	private TableView<ClientTuple> clientTV;
	private TableColumn<ClientTuple, String> clientIdTC;
	private TableColumn<ClientTuple, String> clientNameTC;

	/**
	 * In order to create the GUI a borderPane layout is used.
	 * The content of the layout sections is created by calling
	 * some init-methods.
	 */
	public ServerView(){
		BorderPane borderPane = new BorderPane();
		
		borderPane.setTop(initTopBP());
		borderPane.setCenter(initCenterBP());
		borderPane.setRight(initRightBP());

		scene = new Scene(borderPane);

		msgTF.requestFocus();
	}

	/**
	 * Inits the GUI components located at the top.
	 */
	private Node initTopBP() {
		BorderPane topBP = new BorderPane();
		
		HBox statusHB = new HBox();
		statusHB.setPrefHeight(30);
		statusHB.setSpacing(10);
		statusHB.setPadding(new Insets(5, 5, 0, 5));

		srvLB = new Label("Server");
		srvLB.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		
		statusCL = new Circle(10, 10, 5);
		statusCL.setFill(Color.RED);
		statusCL.setStroke(Color.GRAY);
		
		statusHB.getChildren().addAll(new Pane(statusCL), srvLB);

		
		HBox connectHB = new HBox();
		connectHB.setPrefHeight(30);
		connectHB.setSpacing(10);
		connectHB.setPadding(new Insets(5, 5, 0, 5));
		
		Label portLB = new Label("Port");
		portLB.setFont(Font.font("Verdana", 15));
		HBox.setMargin(portLB, (new Insets(3, 0, 0, 0)));
		
		portTF = new TextField();
		portTF.setPrefWidth(60);
		
		startBTN = new Button("Start");
		stopBTN = new Button("Stop");
		
		connectHB.getChildren().addAll(portLB, portTF, startBTN, stopBTN);
		
		topBP.setLeft(statusHB);
		topBP.setRight(connectHB);
		
		return topBP;
	}

	/**
	 * Inits the GUI components located at the center and the bottom.
	 */
	private Node initCenterBP() {
		BorderPane centerBP = new BorderPane(); 
		
		chatTA = new TextArea();
		chatTA.setStyle("-fx-font-size: 18;");
		chatTA.setWrapText(true);
		chatTA.setEditable(false);
		BorderPane.setMargin(chatTA, new Insets(5, 5, 5, 5));
		
		HBox controlHB = new HBox();
		BorderPane.setAlignment(controlHB, Pos.CENTER);
		controlHB.setPrefHeight(55);
		controlHB.setSpacing(10);
		controlHB.setPadding(new Insets(5, 5, 5, 5));

		submitBTN = new Button("Submit");
		submitBTN.setPrefHeight(38);
		
		msgTF = new TextField();
		msgTF.setPrefHeight(30);
		msgTF.setStyle("-fx-font-size: 18;");
		HBox.setHgrow(msgTF, Priority.ALWAYS);
		
		controlHB.getChildren().addAll(msgTF, submitBTN);

		centerBP.setCenter(chatTA);
		centerBP.setBottom(controlHB);
		
		return centerBP;
	}

	/**
	 * Inits the area shown on the right.
	 */
	private Node initRightBP() {
		BorderPane rightBP = new BorderPane(); 
		rightBP.setPrefWidth(300);
		
		clientTV = initClientTV();
		BorderPane.setMargin(clientTV, new Insets(5, 5, 5, 5));
		
		HBox controlHB = new HBox();
		BorderPane.setAlignment(controlHB, Pos.CENTER);
		controlHB.setPrefHeight(55);
		controlHB.setSpacing(10);
		controlHB.setPadding(new Insets(5, 5, 5, 5));

		rightBP.setCenter(clientTV);
		rightBP.setBottom(controlHB);
		
		return rightBP;
	}
	
	/**
	 * Inits the table used to show the connected clients.
	 */
	@SuppressWarnings("unchecked")
	private TableView<ClientTuple> initClientTV(){
		clientIdTC = new TableColumn<ClientTuple, String>("Id");
		clientIdTC.setResizable(false);
		
		clientNameTC = new TableColumn<ClientTuple, String>("Name");
		clientNameTC.setResizable(false);
		
		clientTV = new TableView<ClientTuple>();
		clientTV.getColumns().addAll(clientIdTC, clientNameTC);
		clientTV.setPlaceholder(new Label("No Clients connected"));
		
		clientIdTC.prefWidthProperty().bind(clientTV.widthProperty().multiply(0.2));
		clientNameTC.prefWidthProperty().bind(clientTV.widthProperty().multiply(0.79));
		
		return clientTV;
	}
	
	/**
	 * Called by the controller to show the GUI.
	 */
	public void show(Stage primaryStage){
		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.setMinHeight(220);
		primaryStage.setMinWidth(340);
		
		primaryStage.setHeight(HEIGHT);
		primaryStage.setWidth(WIDTH);
	}
	
	/**
	 * Getters used by the controller.
	 */
	public Circle getStatusCL() {
		return statusCL;
	}

	public TextArea getChatTA() {
		return chatTA;
	}

	public Button getSubmitBTN() {
		return submitBTN;
	}

	public TextField getMsgTF() {
		return msgTF;
	}

	public Button getStartBTN() {
		return startBTN;
	}
	
	public Button getStopBTN() {
		return stopBTN;
	}
	
	public TextField getPortTF() {
		return portTF;
	}

	public TableView<ClientTuple> getClientTV() {
		return clientTV;
	}

	public TableColumn<ClientTuple, String> getClientIdTC() {
		return clientIdTC;
	}

	public TableColumn<ClientTuple, String> getClientNameTC() {
		return clientNameTC;
	}

	
}
