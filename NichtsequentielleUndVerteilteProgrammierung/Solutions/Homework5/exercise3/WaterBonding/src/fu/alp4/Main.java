package fu.alp4;

import java.util.concurrent.CyclicBarrier;

public class Main {

    public static void main(String[] args) {
        final CyclicBarrier barrier = new CyclicBarrier(3);

        while (true) {
            double random = Math.random();

            if (random > 0.4) {
                new Hydrogen(barrier).start();
            } else {
                new Oxygen(barrier).start();
            }

            Nap.randomNap(1000, 3000);
        }

    }
}
