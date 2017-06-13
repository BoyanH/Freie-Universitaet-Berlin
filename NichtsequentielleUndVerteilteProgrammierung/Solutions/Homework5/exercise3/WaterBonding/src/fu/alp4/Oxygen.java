package fu.alp4;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Oxygen extends BarrierReleaseHandler {

    static final Semaphore oxygens = new Semaphore(1);
    private CyclicBarrier barrier;

    public Oxygen(CyclicBarrier bondingBarrier) {
        super(bondingBarrier);
    }

    public void run() {

        System.out.println("Oxygen atom appeared from nowhere!");
        Hydrogen.randomNap(2000, 5000);
        System.out.println("Oxygen atom wants to bond!");
        try {
            this.oxygens.acquire();
            System.out.println("Oxygen atom is ready to bond, waiting on the barrier!");
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

        oxygens.release();
    }
}
