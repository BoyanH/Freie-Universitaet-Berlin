package fu.alp4;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Hydrogen extends BarrierReleaseHandler {

    static final Semaphore hydrogens = new Semaphore(2);

    public Hydrogen (CyclicBarrier bondingBarrier) {
        super(bondingBarrier);
    }

    public void run() {

        System.out.println("Hydrogen atom appeared from nowhere!");
        Hydrogen.randomNap(2000, 5000);
        System.out.println("Hydrogen atom wants to bond!");
        try {
            /**
             * allow only up to 2 hydrogens to wait at the barrier
             */
            this.hydrogens.acquire();
            System.out.println("Hydrogen atom is ready to bond, waiting on the barrier!");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        try {
            this.awaitBarrier();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        // release back the available spots after molecule bonding
        hydrogens.release();
    }
}
