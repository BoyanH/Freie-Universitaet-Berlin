package control;

import vehicle.Vehicle;

public class OneAtATimeBCMonitor implements BridgeControl {
    double maxLoad;
    boolean bridgeFree = true;

    @Override
    public void init(Double maxLoad) {
        this.maxLoad = maxLoad;
    }

    @Override
    public synchronized void requestCrossing(Vehicle v) {
        // this method is synchronized, so only
        try {

            while (!bridgeFree || v.getWeight() > maxLoad) {
                wait();
            }

            bridgeFree = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void leaveBridge(Vehicle v) {
        bridgeFree = true;
    }
}
