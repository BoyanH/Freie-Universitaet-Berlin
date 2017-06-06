package control;

import vehicle.Vehicle;

import java.util.concurrent.locks.ReentrantLock;

public class OneAtATimeBCReentrantLock implements BridgeControl {
    double maxLoad;
    final ReentrantLock lock = new ReentrantLock();

    @Override
    public void init(Double maxLoad) {
        this.maxLoad = maxLoad;
    }

    @Override
    public void requestCrossing(Vehicle v) {
        if (v.getWeight() <= maxLoad) {
            lock.lock(); // acquire the lock, no one else can pass in the same time
        }
        else {
            try {
                if (v.getWeight() > maxLoad) {
                    Thread.sleep(Long.MAX_VALUE);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void leaveBridge(Vehicle v) {
        lock.unlock(); // release the lock once the auto has passed the bridge
    }
}
