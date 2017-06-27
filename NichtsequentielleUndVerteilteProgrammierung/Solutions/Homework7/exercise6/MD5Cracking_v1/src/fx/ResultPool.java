package fx;

public interface ResultPool {
	
	/**
	 * All cracked passwords could be pushed to the GUI by using this 
	 * Method. When all passwords are found, the GUI will recognize this.
	 * 
	 * @param password	cracked password
	 * @return	true if the pushed password matched a hash
	 */
	boolean pushPassword(String password);
	
	
	/**
	 * Aborts the current run before all hashes are resolved.
	 */
	void abort();

}
