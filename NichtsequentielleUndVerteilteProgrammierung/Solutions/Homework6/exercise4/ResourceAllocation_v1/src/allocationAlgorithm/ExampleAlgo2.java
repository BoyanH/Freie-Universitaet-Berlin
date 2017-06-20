package allocationAlgorithm;

import java.util.LinkedList;

import calculationUtil.CUtil;
import process.Process;
import process.Process.AllocationRequest;
import resources.Resources;

public class ExampleAlgo2 implements Algorithm {

	Resources resources;
	Process[] processes;

	LinkedList<Integer> q = new LinkedList<Integer>();

	@Override
	public void init(Resources resources, Process[] processes) {
		this.processes = processes;
		this.resources = resources;
	}

	@Override
	public String nextStep(AllocationRequest[] requests) {
		int[] free = resources.getFreeRes();

		if(!q.isEmpty() && processes[q.peek()].isFinished()) {
			q.remove();
		}

		for (AllocationRequest req : requests) {
			int id = req.getProcess().getId();

			if(!q.contains(id) &&
					(
							(q.isEmpty() && CUtil.greaterEqual(free, req.getProcess().getPendingRes())) ||
                            (q.size() >= 1 && CUtil.greaterEqual(free, CUtil.add(processes[q.get(q.size() - 1)].getPendingRes(), req.getProcess().getPendingRes())))
					)
			  ) {

				q.add(id);
			}

			if(q.size() >= 1 && id == q.get(0)){
				free = CUtil.sub(free, req.getRequestedRes());
				req.grant();
			} else if(q.size() >= 2 && id == q.get(1)){
				free = CUtil.sub(free, req.getRequestedRes());
				req.grant();
			}
		}

		return String.format("Safe state: %s", SafeChecker.checkIfSafeState(processes, resources.getFreeRes()));
	}


}
