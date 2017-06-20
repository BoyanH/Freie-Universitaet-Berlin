package allocationAlgorithm;

import process.Process;
import process.Process.AllocationRequest;
import resources.Resources;
import calculationUtil.CUtil;

public class ExampleAlgo implements Algorithm {

	Resources resources;
	Process[] processes;

	@Override
	public void init(Resources resources, Process[] processes) {
		this.processes = processes;
		this.resources = resources;
	}

	@Override
	public String nextStep(AllocationRequest[] requests) {
		int[] free = resources.getFreeRes();

		for (AllocationRequest req : requests) {
			if(CUtil.greaterEqual(free, req.getRequestedRes())){
				free = CUtil.sub(free, req.getRequestedRes());
				req.grant();
			}
		}

		return null;
	}

}
