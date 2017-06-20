package allocationAlgorithm;

import calculationUtil.CUtil;
import process.Process;
import resources.Resources;

public class SafeChecker {

    public static boolean checkIfSafeState(Resources resources, Process[] processes) {
        return checkIfSafeState(processes, getAvailableResources(resources, processes));
    }

    public static boolean checkIfSafeState(Process[] processes, int[] availableResources) {
        int[][] pendingResources = getPendingResources(processes);
        int[][] acquiredResources = getAcquiredResources(processes);

        return checkIfSafeState(pendingResources, acquiredResources, availableResources);
    }

    public static boolean checkIfSafeState(int[][] pendingResources, int[][] acquiredRes, int[] availableResources) {
        assert pendingResources.length == acquiredRes.length;

        boolean[] ready = new boolean[acquiredRes.length];
        boolean changed = true;
        int counter = 0;
        int[] localAvailableRes = availableResources.clone();

        // until there are changes and not all processes are servived
        while (changed && counter < pendingResources.length) {
            changed = false; // a new cycle began, see if there are changes this time

            for (int i = 0; i < pendingResources.length; i++) {
                // check if the current process can be serviced, if so mark it as ready and acquire resources
                if (CUtil.greaterEqual(localAvailableRes, pendingResources[i]) && !ready[i]) {
                    localAvailableRes = CUtil.add(localAvailableRes, acquiredRes[i]);
                    ready[i] = true;
                    changed = true;
                    counter++;
                }
            }
        }

        return counter == pendingResources.length; // are all processes marked as serviced?
    }

    public static int[] getAvailableResources(Resources resources, Process[] processes) {
        int[] totalResources = resources.getTotalRes();
        int[] availableResources = totalResources.clone();

        for (int i = 0; i < processes.length; i++) {
            availableResources = CUtil.sub(availableResources, processes[i].getAcquiredRes());
        }

        return availableResources;
    }

    public static int[][] getPendingResources(Process[] processes) {
        int[][] pendingResources = new int[processes.length][];

        for (int i = 0; i < processes.length; i++) {
            pendingResources[i] = processes[i].getPendingRes().clone();
        }

        return pendingResources;
    }

    public static int[][] getAcquiredResources(Process[] processes) {
        int[][] acquiredResources = new int[processes.length][];

        for (int i = 0; i < processes.length; i++) {
            acquiredResources[i] = processes[i].getAcquiredRes().clone();
        }

        return acquiredResources;
    }

    public static void updateAcquired(int[][] acquiredResources, Process[] processes) {
        for (int i = 0; i < acquiredResources.length; i++) {
            if (processes[i].isFinished()) {
                acquiredResources[i] = CUtil.sub(acquiredResources[i], acquiredResources[i]);
            }
        }
    }
}
