package fu.alp4;

public class Writer extends IDataUser {

    public void run() {
        while (true) {
            try {
                // take a random rest to simulate different scenarios
                randomNap(5000, 10000);

                E.acquire();

                /**
                 * If there are currently some other writers or readers, wait
                 * for an available writer spot to be freed from a reader
                 * In te given time, release E, but remember to acquire it before
                 * incrementing nw++ for synchronization
                 */
                if (nw > 0 || nr > 0) {
                    dw++;
                    System.out.println("Waits to start writing! Stop letting further readers!");
                    E.release();
                    W.acquire();
                    E.acquire();
                }

                nw++;
                System.out.printf("Started writing; nr: %s; nw: %s; dr: %s; dw: %s\n", nr, nw, dr, dw);
                E.release();

                randomNap(2000, 4000);

                E.acquire();
                nw--;

                if (dr > 0 && dw == 0) {
                    dr--;
                    R.release();
                } else if (dw > 0) {
                    /**
                     * Deferred writers have higher priority, because we thought it's important to
                     * let writers as soon as possible so readers get the latest and greatest ^^
                     *
                     * E.g if both writers and readers are waiting, let the writer in
                     */

                    dw--;
                    W.release();
                }
                System.out.printf("Finished writing; nr: %s; nw: %s; dr: %s; dw: %s\n", nr, nw, dr, dw);
                E.release();


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
