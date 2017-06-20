package resources;

public interface Resources {


	/**
	 * Returns the total amount (free+allocated) of resources.
	 * 
	 * @return		Total amount of resources provided as int-array
	 */
	int[] getTotalRes();
	
	/**
	 * Returns the currently free amount of resources.
	 * 
	 * @return		Free Rresources provided as int-array
	 */
	int[] getFreeRes();
	
}
