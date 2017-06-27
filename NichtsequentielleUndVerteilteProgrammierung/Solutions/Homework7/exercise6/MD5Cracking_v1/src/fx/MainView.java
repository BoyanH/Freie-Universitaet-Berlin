package fx;

import fx.MainModel.HashTuple;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Thierry Meurers
 *
 *         This class is used to initialize, style and layout all components of
 *         the gui. It is created by the controller class which will add
 *         functionalities to the components.
 *
 */

public class MainView {

	final static private int WIDTH = 500;
	final static private int HEIGHT = 700;

	private Scene scene;
	
	private TextField pathTF;
	private Button openFileChooserBTN;
	
	private Button startBTN;
	private Label timeLB;
	
	private TableView<HashTuple> hashTV;
	private TableColumn<HashTuple, String> hashTC;
	private TableColumn<HashTuple, String> passwordTC;
	
	private Button addBTN;
	private Button addRandomBTN;
	private Button clearBTN;
	private Button copyToClipboardBTN;
	
	/**
	 * In order to create the GUI a borderPane layout is used.
	 * The content of the layout sections is created by calling
	 * some init-methods.
	 */
	public MainView() {
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(initRunOptionsVB());
		borderPane.setCenter(initHashTV());
		borderPane.setBottom(initHashHB());

		scene = new Scene(borderPane);

		startBTN.requestFocus();
	}
	
	/**
	 * Called by the controller to show the GUI.
	 */
	public void show(Stage primaryStage) {
		//primaryStage.setResizable(false);
		//primaryStage.sizeToScene();
		primaryStage.setScene(scene);
		primaryStage.setTitle("MD5 Cracker");
		primaryStage.show();
		primaryStage.setWidth(WIDTH);
		primaryStage.setHeight(HEIGHT);
		primaryStage.setMinHeight(220);
		primaryStage.setMinWidth(340);
	}
	
	/**
	 * Creates the control elements located at the top of the GUI.
	 */
	private Node initRunOptionsVB(){
		HBox hBox = new HBox();
		hBox.setSpacing(5);
		hBox.setPadding(new Insets(5, 5, 5, 5));
		pathTF = new TextField();
		openFileChooserBTN = new Button("Open Wordlist");
		HBox.setHgrow(pathTF, Priority.ALWAYS);
		
		hBox.getChildren().addAll(pathTF, openFileChooserBTN);
		
		
		startBTN = new Button("START");
		startBTN.setStyle("-fx-font: 18 verdana; -fx-base: #b6e7c9;");
		startBTN.setAlignment(Pos.BASELINE_LEFT);
		startBTN.setMinWidth(80);
		BorderPane.setMargin(startBTN, new Insets(0, 5, 5, 5));
		
		timeLB = new Label("00:00:00");
		timeLB.setStyle("-fx-font: 18 verdana;");
		BorderPane.setMargin(timeLB, new Insets(5, 5, 5, 5));
		
		BorderPane borderPane = new BorderPane();
		borderPane.setLeft(startBTN);
		borderPane.setRight(timeLB);
		
		VBox vBox = new VBox();
		vBox.getChildren().addAll(hBox, borderPane);

		return vBox;
	}
	
	/**
	 * Creates the table.
	 */
	@SuppressWarnings("unchecked")
	private Node initHashTV(){
		
		hashTC = new TableColumn<HashTuple, String>("Hash");
		hashTC.setResizable(false);
		
		passwordTC = new TableColumn<HashTuple, String>("Password");
		passwordTC.setResizable(false);
		
		hashTV = new TableView<HashTuple>();
		hashTV.getColumns().addAll(hashTC, passwordTC);
		hashTV.setPlaceholder(new Label("Please add new hashes"));
		
		hashTC.prefWidthProperty().bind(hashTV.widthProperty().multiply(0.595));
		passwordTC.prefWidthProperty().bind(hashTV.widthProperty().multiply(0.4));
		
		return hashTV;
		
	}
	
	/**
	 * Creates the buttons located at the bottom of the gui.
	 */
	private Node initHashHB(){
		
		HBox hBox = new HBox();
		hBox.setSpacing(5);
		hBox.setPadding(new Insets(5, 5, 5, 5));

		addBTN = new Button("Add..");
		addRandomBTN = new Button("Add Random");
		clearBTN = new Button("Clear");
		copyToClipboardBTN = new Button("Copy to clipboard");
		hBox.getChildren().addAll(addBTN, addRandomBTN, clearBTN, copyToClipboardBTN);
		
		return hBox;
	}


	/**
	 * Getters used by the controller class.
	 */
	
	
	public TableView<HashTuple> getHashTV() {
		return hashTV;
	}

	public TableColumn<HashTuple, String> getHashTC() {
		return hashTC;
	}

	public TableColumn<HashTuple, String> getPasswordTC() {
		return passwordTC;
	}

	public TextField getPathTF() {
		return pathTF;
	}

	public Button getOpenFileChooserBTN() {
		return openFileChooserBTN;
	}

	public Button getStartBTN() {
		return startBTN;
	}

	public Button getAddBTN() {
		return addBTN;
	}

	public Button getAddRandomBTN() {
		return addRandomBTN;
	}

	public Button getClearBTN() {
		return clearBTN;
	}

	public Button getCopyToClipboardBTN() {
		return copyToClipboardBTN;
	}

	public Label getTimeLB() {
		return timeLB;
	}
	
	
}
