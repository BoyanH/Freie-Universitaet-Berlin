package allocationAlgorithm;

import resources.Resources;
import process.Process;
import process.Process.AllocationRequest;

public interface Algorithm {

	void init(Resources resources, Process[] processes);
	
	String nextStep(AllocationRequest[] requests);
	
}
