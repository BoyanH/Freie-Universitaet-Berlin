package fx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

/**
 * @author Thierry Meurers
 * 
 *         This class is used to store parameters which will be used by the
 *         Controller class. Furthermore the HashTuple class is defined
 *         and functions to add/clear/update/return the HashTuples are provided.
 *
 */
public class MainModel{

	//Infos provieded by MainFX
	private Stage primaryStage;
	private String crackerClass;
	private String messageDigestInstance;
	
	//List of all HashTuple objects. Observable in order to show the content in the GUI.
	private ObservableList<HashTuple> codes = FXCollections.observableArrayList();
	
	//True, if all passwords are found
	private boolean finished;
	
	public MainModel(Stage primaryStage, String crackerClass, String messageDigestInstance) {
		this.primaryStage = primaryStage;
		this.crackerClass = crackerClass;
		this.messageDigestInstance = messageDigestInstance;
	}

	/**
	 * Simple Getters used by the Controller.
	 */
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public String getCrackerClass() {
		return this.crackerClass;
	}
	
	public String getMessageDigestInstance(){
		return this.messageDigestInstance;
	}

	public ObservableList<HashTuple> getCodes() {
		return codes;
	}
	
	public boolean isFinished(){
		return finished;
	}
	
	/**
	 * Returns an array of all hashes as strings.
	 */
	public String[] getHashes(){
		int length = codes.size();
		String[] res = new String[length];
		for(int i = 0; i < length; i++){
			res[i] = codes.get(i).getHashCode();
		}
		return res;
	}
	
	/**
	 * Returns an array of all passwords as strings.
	 */
	public String[] getPasswords(){
		int length = codes.size();
		String[] res = new String[length];
		for(int i = 0; i < length; i++){
			res[i] = codes.get(i).getSeed();
		}
		return res;
	}
	
	/**
	 * Creates a single string consisting of all hashes and passwords.
	 */
	public String getHashAndPasswordAsString(){
		String lb = System.getProperty("line.separator");
		String[] hashes = getHashes();
		String[] passwords = getPasswords();
		StringBuffer buffer = new StringBuffer();
		
		for(int i = 0; i < passwords.length; i++){
			buffer.append(hashes[i]);
			buffer.append(" ");
			buffer.append(passwords[i]);
			buffer.append(lb);
		}
		return buffer.toString();
		
	}
	
	/**
	 * Removes all passwords which are stored in the
	 * HashTuple objects.
	 */
	public void clearPasswords(){
		int length = codes.size();
		for(int i = 0; i < length; i++){
			codes.get(i).resetPassword();
		}
		finished = false;
	}
	
	/**
	 * Adds a new HashTuple to the codes-list.
	 */
	public void addHash(String hash){
		codes.add(new HashTuple(hash));
	}
	
	/**
	 * Iterates through all hashTuples and tries to match the provided hash.
	 * Also checks whether or not all hash values are found. (If true, the allFound 
	 * value is set to true.)
	 * 
	 * @return 	true, if the provided hash was found in one of the HashTuples.
	 */
	public boolean compareHashAndSetPassword(String hash, String password){
		boolean found = false;
		boolean allFound = true;

		for (HashTuple h : codes) {
			found |= h.compareAndSet(hash, password);
			allFound &= h.isFound();
		}

		this.finished = allFound;
		
		return found;
	}
	
	/**
	 * Defines the values stored in the table.
	 * Each HashTuple consist of two StringProperties.
	 * One for the hash, one for the seed/password.
	 */
	class HashTuple {

		private StringProperty hash;
		private StringProperty password;
		private boolean found;

		public HashTuple(String hash) {
			this.hash = new SimpleStringProperty();
			this.hash.set(hash);
			this.password = new SimpleStringProperty();
		}

		public String getHashCode() {
			return hash.getValue();
		}

		public String getSeed() {
			return password.getValue();
		}

	    public StringProperty getHashProperty() {
	        return hash;
	    }
		
	    public StringProperty getSeedProperty() {
	        return password;
	    }
	    
		/**
		 * Compares a provided hash with the hash represented by
		 * the HashTuple-Object. If there is a match the seed is stored
		 * in the HashTuple-Object and true is returned.
		 */
		public boolean compareAndSet(String hash, String seed) {
			if(found)
				return false;
			if (hash.equals(this.hash.getValue())) {
				this.password.set(seed);
				found = true;
				return true;
			}
			return false;
		}
		
		public boolean isFound(){
			return found;
		}
		
		public void resetPassword(){
			password.set("");
			found = false;
		}

	}
	

}
