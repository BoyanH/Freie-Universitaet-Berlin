package fx;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Thierry Meurers
 * 
 * Collection of used (PopUp)dialogs.
 * 
 * Every dialog is created by calling one of the following 
 * static methods. The primaryStage's position is provided 
 * to all dialogs - so they can be shown in the front of 
 * the primaryStage.
 *
 */
public class PopUpDialogs {

	private static String hashCodes;

	/**
	 * Dialog to add new hashes.
	 * 
	 * @return		String containing the added hashes.
	 */
	static String displayAddHash(double xPos, double yPos) {
		hashCodes = "";
		Stage stage = new Stage();
		stage.setTitle("Add hashes ...");
		stage.setX(xPos + 100);
		stage.setY(yPos + 100);

		TextArea hashTA = new TextArea();
		BorderPane.setMargin(hashTA, new Insets(5, 5, 5, 5));

		Button submitBTN = new Button("Submit");
		submitBTN.setOnAction(e -> {
			hashCodes = hashTA.getText();
			stage.close();
		});
		BorderPane.setMargin(submitBTN, new Insets(0, 5, 5, 5));

		BorderPane root = new BorderPane();
		root.setCenter(hashTA);
		root.setBottom(submitBTN);

		stage.setScene(new Scene(root));
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();

		return hashCodes;

	}

	
	
	/**
	 * Dialogs to inform the user of some kind of error / problem / wrong usage.
	 */
	
	static void displayRunningAlret(double xPos, double yPos) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setX(xPos + 100);
		alert.setY(yPos + 100);
		alert.setTitle("Information");
		alert.setHeaderText("Cracker running");
		alert.setContentText("Could not edit hashes while the cracker is running.");
		alert.showAndWait();
	}

	static void displayNoWordlistAlret(double xPos, double yPos) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setX(xPos + 100);
		alert.setY(yPos + 100);
		alert.setTitle("Information");
		alert.setHeaderText("No wordlist loaded");
		alert.setContentText("Could not create random hash / start new run without wordlist.");
		alert.showAndWait();
	}

	static void displayNoHashesAlret(double xPos, double yPos) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setX(xPos + 100);
		alert.setY(yPos + 100);
		alert.setTitle("Information");
		alert.setHeaderText("No hashes");
		alert.setContentText("In order to start a new run at least one hash is required.");
		alert.showAndWait();
	}

	static void displayCouldNotLoadWordlistAlret(double xPos, double yPos, String path) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setX(xPos + 100);
		alert.setY(yPos + 100);
		alert.setTitle("Error");
		alert.setHeaderText("Cant load wordlist");
		alert.setContentText("The selected wordlist (" + path + ") was not found or is empty.");
		alert.showAndWait();
	}

	static void displayCouldNotLoadCrackerAlret(double xPos, double yPos, String crackerClass) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Reflection failed");
		alert.setContentText("Could not load \"" + crackerClass + "\".");
		alert.showAndWait();
	}

}
