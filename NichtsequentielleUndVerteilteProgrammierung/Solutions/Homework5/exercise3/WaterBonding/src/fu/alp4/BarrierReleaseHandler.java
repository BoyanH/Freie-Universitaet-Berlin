package fu.alp4;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class BarrierReleaseHandler extends Nap {

    static int waterMolecules = 0;
    private Lock incrementLock = new ReentrantLock();
    private CyclicBarrier barrier;

    public BarrierReleaseHandler(CyclicBarrier bondingBarrier) {
        this.barrier = bondingBarrier;
    }

    protected void awaitBarrier() throws BrokenBarrierException, InterruptedException {
        int turn = this.barrier.await();

        /**
         * The first thread to arrive at the barrier will be the one to create the water molecule.
         * Method created to share functionality between Hydrogen and Oxygen classes
         */

        if (turn == 0) {
            incrementLock.lock();
            BarrierReleaseHandler.waterMolecules++;
            System.out.printf("A new water molecule was created! Water molecules: %d\n", BarrierReleaseHandler.waterMolecules);
            incrementLock.unlock();
        }
    }
}
