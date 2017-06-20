package process;


public interface Process {

	
	/**
	 * Returns the resources held by the process.
	 * 
	 * @return		Acquired resources as int-Array
	 */
	int[] getAcquiredRes();
	
	/**
	 * Returns the outstanding amount of resources.
	 * (Which are needed in order to finish the process.)
	 * 
	 * @return		Pending resources as int-Array
	 */
	int[] getPendingRes();
	
	/**
	 * @return		True, if the process has already finished.
	 */
	boolean isFinished();

	/**
	 * Returns the unique ID of the Process. 
	 * In each simulation the lowest ID will be 0.
	 * The highest ID depends on the number of processes.
	 * 
	 * @return		Unique ID od Process
	 */
	int getId();
	
	/**
	 *	Allocation request send by process.
	 */
	public interface AllocationRequest{
		
		/**
		 * Returns the reference to the respective process 
		 * which is responsible for the request.
		 * 
		 * @return		Respective process
		 */
		Process getProcess();
		
		/**
		 * Returns the amount of resources which are charged
		 * by the request
		 * 
		 * @return		Requested resources as int-Array
		 */
		int[] getRequestedRes();
		
		/**
		 * In order to grant a request this method has to be called.
		 */
		void grant();
		
	}
	
}
