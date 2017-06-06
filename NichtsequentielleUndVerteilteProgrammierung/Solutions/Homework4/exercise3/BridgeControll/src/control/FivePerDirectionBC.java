package control;

import vehicle.Vehicle;
import vehicle.Vehicle.VOrigin;

import java.util.concurrent.Semaphore;

public class FivePerDirectionBC implements BridgeControl {

    static final int autosPerWave = 5;
    static VOrigin currentWaveOrigin;
    static int autosPassedFromCurrentWave;

    Semaphore[] slotsByOrigin;
    Semaphore weight;

    Semaphore leaveBridgeMutex;
    Semaphore initializeMutex;
    Semaphore weightHandlerMutex;

    double maxLoad;

    /**
     * Initialize all variables and semaphores, set currentLoad and autosPassedFromCurrentWave to 0,
     * set currentWave to West and add 5 available slots for cars on the west side
     *
     * @param maxLoad	Max load (depending on users input)
     */

    @Override
    public synchronized void init(Double maxLoad) {

        this.maxLoad = maxLoad;
        weight = new Semaphore((int) Math.floor(maxLoad*100), true);
        leaveBridgeMutex = new Semaphore(1, true);
        initializeMutex = new Semaphore(1, true);
        weightHandlerMutex = new Semaphore(1, true);
        autosPassedFromCurrentWave = 0;

        slotsByOrigin = new Semaphore[VOrigin.values().length];
        for (int i = 0; i < slotsByOrigin.length; i++) {
            slotsByOrigin[i] = new Semaphore(0, true);
        }

        try {
            initializeMutex.acquire();
            setWave(VOrigin.WEST);
            initializeMutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * Each vehicle that wants to pass the bridge acquires from the semaphore for it's side.
     * If it does indeed get a slot, it checks if the bridge can sustain it.
     * If not, the thread goes to sleep for a looong time.
     *
     * Further each vehicle with a slot checks if the bridge can sustain it and all vehicle on the bridge and
     * waits until the condition is met.
     *
     * @param v		Vehicle asking for the permission to cross the bridge.
     */

    @Override
    public void requestCrossing(Vehicle v) {

        try {
            getSlotsByOrigin(v.getOrigin()).acquire();

            weightHandlerMutex.acquire();

            if (v.getWeight() > maxLoad) {
                getSlotsByOrigin(v.getOrigin()).release();
                weightHandlerMutex.release();
                Thread.sleep(Long.MAX_VALUE);
            }

            weightHandlerMutex.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            weight.acquire(getWeightFromVehicle(v));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    /**
     *
     * When leaving the bridge each auto adds to the count of total vehicles passed. That way, we are able to change
     * the current wave (west or east at the time) exactly when the last vehicle has left the bridge.
     *
     * Further we remove its weight from the calculation.
     *
     * @param v		Vehicle finished the crossing
     */

    @Override
    public void leaveBridge(Vehicle v) {

        try {
            leaveBridgeMutex.acquire();
            autosPassedFromCurrentWave++;
            weight.release(getWeightFromVehicle(v));

            if (autosPassedFromCurrentWave == autosPerWave) {
                autosPassedFromCurrentWave = 0;
                setWave(getNextOrigin(currentWaveOrigin));
            }
            leaveBridgeMutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * Gets the next origin from the sorted VOrigin.values() array. We treat this array as a circular array.
     *
     * @param origin VOrigin
     * @return VOrigin
     */
    private VOrigin getNextOrigin(VOrigin origin) {

        VOrigin[] vOrigins = VOrigin.values();
        int currentOriginIndex = java.util.Arrays.binarySearch(vOrigins, origin);

        if (currentOriginIndex != vOrigins.length - 1) {
            return vOrigins[currentOriginIndex + 1];
        }

        return vOrigins[0];
    }

    /**
     * Gets all the origins but the current one from VOrigin.values() array
     *
     * @param origin
     * @return
     */
    private static VOrigin[] getNonCurrentOrigins(VOrigin origin) {

        VOrigin[] vOrigins = VOrigin.values();
        VOrigin[] nonCurrentOrigins = new VOrigin[vOrigins.length - 1];
        int ncoCounter = 0;

        for (int i = 0; i < vOrigins.length; i++) {

            if (vOrigins[i] != origin) {
                nonCurrentOrigins[ncoCounter] = vOrigins[i];
                ncoCounter++;
            }
        }

        return nonCurrentOrigins;
    }

    /**
     * Maps each item's index in VOrigins.values() to a Semaphore in slotsByOrigin
     *
     * @param origin
     * @return
     */
    private Semaphore getSlotsByOrigin(VOrigin origin) {

        VOrigin[] vOrigins = VOrigin.values();

        return slotsByOrigin[java.util.Arrays.binarySearch(vOrigins, origin)];
    }

    /**
     * Removes all available slots for other waves (not really used at the moment as available slots for current wave
     * are already 0 but could be useful in the future)
     *
     * Adds autosPerWave amount of slots to the current wave
     *
     * @param wave
     */
    private void setWave(VOrigin wave) {

        currentWaveOrigin = wave;
        VOrigin[] nonCurrentOrigins = getNonCurrentOrigins(currentWaveOrigin);

        for (int i = 0; i < nonCurrentOrigins.length; i++) {
            getSlotsByOrigin(nonCurrentOrigins[i]).drainPermits();
        }

        getSlotsByOrigin(currentWaveOrigin).release(autosPerWave);
    }

    /**
     * Used to make acquiring and releasing of weight easier. Weight of vehicle is multiplied by 100 and ceiled to
     * make a precise enough integer way of measuring weight
     * @param v Vehicle
     * @return
     */
    private int getWeightFromVehicle(Vehicle v) {
        return (int) Math.ceil(v.getWeight() * 100);
    }

}
