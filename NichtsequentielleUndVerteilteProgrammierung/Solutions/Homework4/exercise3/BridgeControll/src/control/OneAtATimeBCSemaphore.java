package control;

import vehicle.Vehicle;

import java.util.concurrent.Semaphore;

public class OneAtATimeBCSemaphore implements BridgeControl {

    double maxLoad;
    final Semaphore bridgeCrossingSlot = new Semaphore(1, true); // one car can pass at a time, fair queue

    @Override
    public void init(Double maxLoad) {
        this.maxLoad = maxLoad;
    }

    @Override
    public void requestCrossing(Vehicle v) {

        try {
            if (v.getWeight() > maxLoad) {
                Thread.sleep(Long.MAX_VALUE);
            }

            bridgeCrossingSlot.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void leaveBridge(Vehicle v) {
        bridgeCrossingSlot.release();
    }

}
