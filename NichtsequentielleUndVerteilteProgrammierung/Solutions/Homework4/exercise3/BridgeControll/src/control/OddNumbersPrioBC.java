package control;

import vehicle.Vehicle;
import vehicle.Vehicle.VOrigin;

import java.util.concurrent.Semaphore;

/**
 * Simple example. Only light vehicles coming from west will be able to pass the Bridge.
 */
public class OddNumbersPrioBC implements BridgeControl {

    double maxLoad;
    Semaphore weight;

    @Override
    public void init(Double maxLoad) {

        this.maxLoad = maxLoad;
        weight = new Semaphore(transformWeight(maxLoad, false), true);
    }

    @Override
    public void requestCrossing(Vehicle v) {


        try {
            weight.acquire(transformWeight(v.getWeight(), true));

            // If the vehicle has an even id, it has to wait two times more
            if (v.getVehicleId() % 2 == 0) {
                weight.release(transformWeight(v.getWeight(), true));
                weight.acquire(transformWeight(v.getWeight(), true));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void leaveBridge(Vehicle v) {

        weight.release(transformWeight(v.getWeight(), true));
    }

    private int transformWeight(double weight, boolean roundUp) {

        double multipliedWeight = weight * 100;

        if (roundUp) {
            return (int) Math.ceil(multipliedWeight);
        }

        return (int) Math.floor(multipliedWeight);
    }

}
