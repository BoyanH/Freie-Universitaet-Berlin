package allocationAlgorithm;

import calculationUtil.CUtil;
import process.Process;
import process.Process.AllocationRequest;
import resources.Resources;

public class BankersAlgo implements Algorithm {

	Resources resources;
	Process[] processes;

	int[][] acquiredRes;
	int[][] pendingRes;

	@Override
	public void init(Resources resources, Process[] processes) {
		this.resources = resources;
		this.processes = processes;
		this.pendingRes = SafeChecker.getPendingResources(processes);
		this.acquiredRes = SafeChecker.getAcquiredResources(processes);
	}

	@Override
	public String nextStep(AllocationRequest[] requests) {

		int[] freeRes = resources.getFreeRes();
		SafeChecker.updateAcquired(acquiredRes, processes);

		for (AllocationRequest req : requests) {

			int[] freeResSimulation = freeRes.clone();
			int[][] pendingResSimulation = pendingRes.clone();
			int[][] acquiredResSimulation = acquiredRes.clone();
			int processIdx = this.getProcessIndex(req);

			// simulate resource allocation
			pendingResSimulation[processIdx] = CUtil.sub(pendingResSimulation[processIdx], req.getRequestedRes());
			acquiredResSimulation[processIdx] = CUtil.add(acquiredResSimulation[processIdx], req.getRequestedRes());
			freeResSimulation = CUtil.sub(freeResSimulation, req.getRequestedRes());

			// if state is safe after allocation, grant permission
			if (SafeChecker.checkIfSafeState(pendingResSimulation, acquiredResSimulation, freeResSimulation)) {
				freeRes = CUtil.sub(freeRes, req.getRequestedRes());
				this.pendingRes[processIdx] = CUtil.sub(this.pendingRes[processIdx], req.getRequestedRes());
				this.acquiredRes[processIdx] = CUtil.add(this.acquiredRes[processIdx], req.getRequestedRes());
				req.grant();
			}
		}

		return String.format("safe: %s", SafeChecker.checkIfSafeState(resources, processes));
	}

	public int getProcessIndex(AllocationRequest req) throws IllegalArgumentException {
		for (int i = 0; i < processes.length; i++) {
			if (req.getProcess().getId() == processes[i].getId()) {
				return i;
			}
		}

		throw new IllegalArgumentException("Invalid request!");
	}

}
