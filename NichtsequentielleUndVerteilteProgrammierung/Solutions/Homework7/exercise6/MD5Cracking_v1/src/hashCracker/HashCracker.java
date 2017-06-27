package hashCracker;

import fx.ResultPool;

public interface HashCracker {

	/**
	 * Called whenever the start button is triggered.
	 * 
	 * Cracks the provided hashes by performing a brute-force search.
	 * 
	 * @param wordList	List of all possible words which could be part of a password
	 * @param hashList	List of hashes to crack
	 * @param resPool	Result pool to push the cracked passwords
	 */
	void start(String[] wordList, String[] hashList, ResultPool resPool);
	
	/**
	 * Called whenever the Stop button is triggered or the program is closed.
	 * 
	 * Terminates all running tasks/threads ... whatever you've started.
	 */
	void stop();
}
