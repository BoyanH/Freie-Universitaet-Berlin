package control;

import vehicle.Vehicle;
import vehicle.Vehicle.VOrigin;

import java.util.concurrent.Semaphore;

public class FairBCHandleLowriders implements BridgeControl {

    static VOrigin lowridersOrigin;
    static int lowridersInDirection;

    double maxLoad;
    Semaphore weight;
    Object lowriderHandlerLock;
    Object lowriderReleaserLock;
    Object changeLowriderDirectionLock;

    @Override
    public void init(Double maxLoad) {

        lowridersInDirection = 0;
        this.maxLoad = maxLoad;
        weight = new Semaphore(transformWeight(maxLoad, false), true);
        lowriderHandlerLock = new Object();
    }

    @Override
    public void requestCrossing(Vehicle v) {

        try {
            weight.acquire(transformWeight(v.getWeight(), true));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (v.isLowrider()) {

            synchronized (lowriderHandlerLock) {

                try {
                    if (lowridersOrigin == null) {
                        lowridersOrigin = v.getOrigin();
                    } else {
                        while (lowridersOrigin != v.getOrigin() && lowridersInDirection != 0) {
                            lowriderHandlerLock.wait();
                        }
                        lowridersOrigin = v.getOrigin();
                    }

                    lowridersInDirection++;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void leaveBridge(Vehicle v) {

        weight.release(transformWeight(v.getWeight(), true));

        if (v.isLowrider()) {

            synchronized (lowriderHandlerLock) {

                lowridersInDirection--;

                if (lowridersInDirection == 0) {
                    lowriderHandlerLock.notifyAll();
                }
            }

        }
    }

    private int transformWeight(double weight, boolean roundUp) {

        double multipliedWeight = weight * 100;

        if (roundUp) {
            return (int) Math.ceil(multipliedWeight);
        }

        return (int) Math.floor(multipliedWeight);
    }

}
