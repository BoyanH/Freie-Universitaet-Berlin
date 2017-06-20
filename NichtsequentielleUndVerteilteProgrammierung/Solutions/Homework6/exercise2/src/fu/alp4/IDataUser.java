package fu.alp4;

import java.util.concurrent.Semaphore;

public abstract class IDataUser extends Nap {

        static int nr = 0;
        static int nw = 0;
        static int dr = 0;
        static int dw = 0;
        static final Semaphore E = new Semaphore(1);
        static final Semaphore R = new Semaphore(0);
        static final Semaphore W = new Semaphore(0);
}
