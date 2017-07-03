package fx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

/**
 * @author Thierry Meurers
 *
 *         This class is used to initialize, style and layout all components of
 *         the gui. It is created by the controller class which will add
 *         functionalities to the components.
 */
public class ClientView {

	final static private int WIDTH = 800;
	final static private int HEIGHT = 500;

	private Scene scene;

	private Circle statusCL;
	private Label uNameLB;

	private TextArea chatTA;

	private Button sendBTN;
	private TextField msgTF;

	/**
	 * In order to create the GUI a borderPane layout is used.
	 * The content of the layout sections is created by calling
	 * some init-methods.
	 */
	public ClientView() {
		BorderPane borderPane = new BorderPane();

		borderPane.setTop(initStatusHB());
		borderPane.setCenter(initChatPN());
		borderPane.setBottom(initControlHB());
		scene = new Scene(borderPane);

		msgTF.requestFocus();
	}

	/**
	 * Inits the GUI components located at the top.
	 */
	private Node initStatusHB() {
		HBox statusHB = new HBox();
		BorderPane.setAlignment(statusHB, Pos.CENTER);
		statusHB.setPrefHeight(30);
		statusHB.setSpacing(5);
		statusHB.setPadding(new Insets(5, 5, 5, 5));

		statusCL = new Circle(10, 10, 5);
		statusCL.setFill(Color.RED);
		statusCL.setStroke(Color.GRAY);
		
		uNameLB = new Label("unnamed");
		uNameLB.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

		statusHB.getChildren().addAll(new Pane(statusCL), uNameLB);

		return statusHB;
	}

	/**
	 * Inits the GUI components located at the bottom.
	 */
	private Node initControlHB() {
		HBox controlHB = new HBox();
		BorderPane.setAlignment(controlHB, Pos.CENTER);
		controlHB.setPrefHeight(55);
		controlHB.setSpacing(5);
		controlHB.setPadding(new Insets(5, 5, 5, 5));

		sendBTN = new Button("Send");
		sendBTN.setPrefHeight(38);

		msgTF = new TextField();
		msgTF.setPrefHeight(30);
		msgTF.setStyle("-fx-font-size: 18;");
		HBox.setHgrow(msgTF, Priority.ALWAYS);

		controlHB.getChildren().addAll(msgTF, sendBTN);

		return controlHB;
	}

	/**
	 * Inits the centered TextArea.
	 */
	private Node initChatPN() {

		chatTA = new TextArea();
		chatTA.setStyle("-fx-font-size: 18px;");
		chatTA.setEditable(false);
		chatTA.setWrapText(true);
		BorderPane.setMargin(chatTA, new Insets(5, 5, 5, 5));
		chatTA.setFocusTraversable(false);
		
		return chatTA;
	}

	/**
	 * Called by the controller to show the GUI.
	 */
	public void show(Stage primaryStage) {
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

	public Label getUNameLB() {
		return uNameLB;
	}

	public TextArea getChatTA() {
		return chatTA;
	}

	public Button getSendBTN() {
		return sendBTN;
	}

	public TextField getMsgTF() {
		return msgTF;
	}

}
