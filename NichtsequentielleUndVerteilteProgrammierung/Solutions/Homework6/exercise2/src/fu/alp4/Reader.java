package fu.alp4;

public class Reader extends IDataUser {

    public void run() {
        while (true) {
            // take a random rest to simulate different scenarios
            randomNap(500, 2000);
            try {
                E.acquire();
                // skip waiting only if there are no deferred or non-deferred writers!
                // if a writer is waiting, he has the priority
                if (nw > 0 || dw > 0) {
                    dr++;
                    E.release();
                    R.acquire();
                    E.acquire();
                }

                nr++;
                // again, waiting writers have priority, don't let further readers in this case
                if (dr > 0 && dw == 0) {
                    dr--;
                    R.release();
                }

                System.out.printf("Started reading; nr: %s; nw: %s; dr: %s; dw: %s\n", nr, nw, dr, dw);
                E.release();

                // read
                randomNap(500, 2000);


                E.acquire();
                nr--;
                if (nr == 0 && dw > 0) {
                    dw--;
                    W.release();
                }

                System.out.printf("Finished reading; nr: %s; nw: %s; dr: %s; dw: %s\n", nr, nw, dr, dw);
                E.release();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
