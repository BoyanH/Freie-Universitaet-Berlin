package fx;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import hashCracker.HashCracker;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;

/**
 * @author Thierry Meurers
 * 
 *         The controller class is connected to the model, the view and the
 *         cracker class.
 * 
 *         It realizes the interaction to the cracker class (by implementing
 *         ResultPool) and the interaction to the user (by tying methods to the
 *         GUI components).
 */
public class MainController implements ResultPool {

	final private FileChooser fileChooser = new FileChooser();

	private MainModel model;
	private MainView view;
	private String lastWorkingPath;

	private String[] wordlist;
	private boolean running;

	private MessageDigest md;
	private HashCracker cracker;

	/**
	 * Receives and stores the model; Initializes the view; Creates EventHandler
	 * classes and adds them to the regarding view components; Limits the
	 * FileChooser to text files; Binds the table to the content of the
	 * codes-list located in the model; Reads the last working path to the
	 * wordlist and initalizes the hashCracler; Initalizes the digset instance
	 * used to calculate hashes.
	 */
	public MainController(MainModel model) {
		this.model = model;
		this.view = new MainView();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);

		view.getOpenFileChooserBTN().setOnAction(new FileChooseBTNHandler());
		view.getStartBTN().setOnAction(new StartBTNHandler());
		view.getAddBTN().setOnAction(new AddBTNHandler());
		view.getAddRandomBTN().setOnAction(new AddRandomBTNHandler());
		view.getClearBTN().setOnAction(new ClearBTNHandler());
		view.getCopyToClipboardBTN().setOnAction(new CopyToClipboardBTNHandler());
		model.getPrimaryStage().setOnCloseRequest(new CloseBTNHandler());

		view.getHashTC().setCellValueFactory(cellData -> cellData.getValue().getHashProperty());
		view.getPasswordTC().setCellValueFactory(cellData -> cellData.getValue().getSeedProperty());
		view.getHashTV().setItems(model.getCodes());

		readOptions();
		cracker = initHashCracker(model.getCrackerClass());

		try {
			md = MessageDigest.getInstance(model.getMessageDigestInstance());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Show the file chooser dialog to select a new wordlist.
	 */
	class FileChooseBTNHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			File file = fileChooser.showOpenDialog(model.getPrimaryStage());
			if (file != null) {
				loadWordlist(file);
			}
		}
	}

	/**
	 * Copies all hashes and the regarding passwords to the clipboard using some
	 * AWT components.
	 */
	class CopyToClipboardBTNHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			Transferable transferable = new StringSelection(model.getHashAndPasswordAsString());
			clipboard.setContents(transferable, null);
		}
	}

	/**
	 * Combines two Functions: Start a new run Stop a current run
	 */
	class StartBTNHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {

			if (!running) {
				/**
				 * If the cracker is currently not running, a new run is
				 * started: 1. Check if the wordlist and hashlist is not empty
				 * 2. Put the GUi into running state (change the START button,
				 * title) 3. Start the stopwatch task 4. Start the cracker
				 * (within an extra thread)
				 */
				if (wordlist == null) {
					PopUpDialogs.displayNoWordlistAlret(model.getPrimaryStage().getX(), model.getPrimaryStage().getY());
				} else if (model.getHashes().length == 0) {
					PopUpDialogs.displayNoHashesAlret(model.getPrimaryStage().getX(), model.getPrimaryStage().getY());
				} else {
					running = true;
					view.getStartBTN().setText("STOP");
					view.getStartBTN().setStyle("-fx-font: 18 verdana; -fx-base: #DC114A;");
					model.clearPasswords();
					model.getPrimaryStage().setTitle("MD5 Cracker (running)");

					StopWatchTask stopWatchTask = new StopWatchTask();
					view.getTimeLB().textProperty().bind(stopWatchTask.messageProperty());
					Thread stopWatchThread = new Thread(stopWatchTask);
					stopWatchThread.setDaemon(true);
					stopWatchThread.start();

					Thread crackerThread = new Thread(new CrackerThread());
					crackerThread.setDaemon(true);
					crackerThread.start();
				}

			} else {
				/**
				 * If the cracker is currently running: 1. Put the GUi into
				 * not-running state (change the START button, title) 2. Stop
				 * the stopwatch 3. call stop on cracker class
				 */
				running = false;
				view.getStartBTN().setText("START");
				view.getStartBTN().setStyle("-fx-font: 18 verdana; -fx-base: #b6e7c9;");
				model.getPrimaryStage().setTitle("MD5 Cracker (stopped)");
				view.getTimeLB().textProperty().unbind();
				cracker.stop();
			}

		}

	}

	/**
	 * Called whenever the gui closes. Saves the options; calls the cracker to
	 * stop.
	 */
	class CloseBTNHandler implements EventHandler<WindowEvent> {

		public void handle(WindowEvent e) {
			saveOptions();
			cracker.stop();
		}
	}

	/**
	 * Add new hash(es) by showing the popup dialog.
	 */
	class AddBTNHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			if (!running) {
				String hashes = PopUpDialogs.displayAddHash(model.getPrimaryStage().getX(),
						model.getPrimaryStage().getY());
				readAddedHashes(hashes);
			} else {
				PopUpDialogs.displayRunningAlret(model.getPrimaryStage().getX(), model.getPrimaryStage().getY());
			}
		}

	}

	/**
	 * Add new random hash value.
	 */
	class AddRandomBTNHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			if (!running) {
				addRandomHash();
			} else {
				PopUpDialogs.displayRunningAlret(model.getPrimaryStage().getX(), model.getPrimaryStage().getY());
			}
		}

	}

	/**
	 * Remove all hashes from the table.
	 */
	class ClearBTNHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			if (!running) {
				model.getCodes().clear();
			} else {
				PopUpDialogs.displayRunningAlret(model.getPrimaryStage().getX(), model.getPrimaryStage().getY());
			}
		}

	}

	private HashCracker initHashCracker(String classname) {
		try {
			Class<?> c = Class.forName(classname);
			Constructor<?> constructor = c.getConstructor();
			HashCracker hashCracker = (HashCracker) constructor.newInstance();
			return hashCracker;
		} catch (ClassNotFoundException | NoSuchMethodException | NullPointerException | SecurityException
				| InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			PopUpDialogs.displayCouldNotLoadCrackerAlret(model.getPrimaryStage().getX(), model.getPrimaryStage().getY(),
					model.getCrackerClass());
			Platform.exit();
			return null;
		}
	}

	/**
	 * Used by MainFX to show the GUI. Passes the primaryStage to the view.
	 */
	public void show() {
		view.show(model.getPrimaryStage());
	}

	/**
	 * Used to read the new hashes (added by the Add.. Dialog) to the model.
	 */
	private void readAddedHashes(String hashes) {
		Scanner scanner = new Scanner(hashes);
		String hash;
		while (scanner.hasNextLine()) {
			hash = scanner.nextLine().trim();
			if (!hash.equals(""))
				model.addHash(hash);
		}
		scanner.close();
	}

	/**
	 * Receives the path to the wordlist. Tries to load the wordlist. If the
	 * file was not found or is empty an error message is displayed. If the
	 * wordlist was loaded with success the lastWorkingPath value is updated.
	 */
	private void loadWordlist(File path) {
		try {
			String[] temp = readFile(path);
			if (temp.length == 0) {
				PopUpDialogs.displayCouldNotLoadWordlistAlret(model.getPrimaryStage().getX(),
						model.getPrimaryStage().getY(), path.toString());
			} else {
				wordlist = temp;
				lastWorkingPath = path.getAbsolutePath();
				view.getPathTF().setText(lastWorkingPath);
			}
		} catch (IOException e) {
			PopUpDialogs.displayCouldNotLoadWordlistAlret(model.getPrimaryStage().getX(),
					model.getPrimaryStage().getY(), path.toString());
		}
	}

	/**
	 * Receives a path to a text file and loads the content (line by line) to a
	 * String array.
	 */
	private static String[] readFile(File path) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(path));
		LinkedList<String> list = new LinkedList<String>();

		for (String line; (line = reader.readLine()) != null;) {
			list.add(line);
		}

		reader.close();
		return list.toArray(new String[list.size()]);
	}

	/**
	 * Creates a random hash and adds it to the Table. If no wordlist is
	 * provided, an error message is displayed
	 */
	private void addRandomHash() {
		if (wordlist == null) {
			PopUpDialogs.displayNoWordlistAlret(model.getPrimaryStage().getX(), model.getPrimaryStage().getY());
		} else {
			Random rnd = new Random();
			model.addHash(getHash(wordlist[rnd.nextInt(wordlist.length)] + wordlist[rnd.nextInt(wordlist.length)]));
		}
	}

	/**
	 * Calculates the hash value for a provided seed value. Used by
	 * addRandomHash and pushPassword.
	 * 
	 * Found on: http://stackoverflow.com/a/6565597
	 */
	private String getHash(String seed) {
		byte[] array = md.digest(seed.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
			sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString();
	}

	/**
	 * Called when the close button is triggeres. Saves the last working
	 * wordlist-path to the crackerFX.properties file.
	 */
	private void saveOptions() {
		try (OutputStream output = new FileOutputStream("crackerFX.properties")) {
			Properties prop = new Properties();
			prop.setProperty("wordlistPath", lastWorkingPath);
			prop.store(output, "Cracker Options File");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Called on application start. Checks the crackerFX.properties for the last
	 * working wordlist-path.
	 */
	private void readOptions() {
		try (InputStream input = new FileInputStream("crackerFX.properties")) {
			Properties prop = new Properties();
			prop.load(input);
			lastWorkingPath = prop.getProperty("wordlistPath");
			if (lastWorkingPath != "")
				loadWordlist(new File(lastWorkingPath));
		} catch (IOException ex) {
			System.err.println("Could not read path from file (CrackerFX.properties)");
		}
	}

	/**
	 * Used by the hashCracker to send a cracked password. Calls the
	 * compareHashAndSetPassword method (located in the model) to check if the
	 * password actually matches a hash. If it does - true is returned.
	 * 
	 * If all passwords are found, this method "stops" the GUI. In order to keep
	 * this call thread safe the changes made to the GUI (change button layout
	 * and text) are performed using Platform.runLater.
	 */
	@Override
	public boolean pushPassword(String password) {
		String hash = getHash(password);
		boolean found = false;

		if (running)
			found = model.compareHashAndSetPassword(hash, password);

		if (model.isFinished()) {
			running = false;
			Platform.runLater(() -> {
				view.getStartBTN().setText("START");
				view.getStartBTN().setStyle("-fx-font: 18 verdana; -fx-base: #b6e7c9;");
				model.getPrimaryStage().setTitle("MD5 Cracker (finished)");
				view.getTimeLB().textProperty().unbind();
			});
		}
		return found;
	}

	/**
	 * Used by the hashCracker to abort the current run. In order to keep this
	 * call thread safe the changes made to the GUI (change button layout and
	 * text) are performed using Platform.runLater.
	 */
	@Override
	public void abort() {
		running = false;
		Platform.runLater(() -> {
			view.getStartBTN().setText("START");
			view.getStartBTN().setStyle("-fx-font: 18 verdana; -fx-base: #b6e7c9;");
			model.getPrimaryStage().setTitle("MD5 Cracker (aborted)");
			view.getTimeLB().textProperty().unbind();
		});

	}

	/**
	 * StopWatch Thread which updates the GUI every 10 ms to show the passed
	 * time. The time is updated by using the updateMessage. Its important to
	 * extends Task (not Thread or Runnable) in order to create a JavaFX
	 * threadsave solution.
	 */
	class StopWatchTask extends Task<Void> {

		private long startTime;

		@Override
		protected Void call() throws Exception {
			startTime = System.currentTimeMillis();
			while (running) {
				long passedTime = System.currentTimeMillis() - startTime;

				long milliSeconds = passedTime % 1000 / 10;
				long seconds = passedTime / 1000 % 60;
				long minutes = passedTime / 60000 % 60;

				updateMessage(String.format("%02d:%02d:%02d", minutes, seconds, milliSeconds));
				Thread.sleep(10);
			}
			return null;
		}

	}

	/**
	 * To prevent the GUI from blocking the HashCracker is called within an
	 * Thread.
	 */
	class CrackerThread implements Runnable {

		@Override
		public void run() {
			cracker.start(wordlist.clone(), model.getHashes(), MainController.this);
		}

	}

}
