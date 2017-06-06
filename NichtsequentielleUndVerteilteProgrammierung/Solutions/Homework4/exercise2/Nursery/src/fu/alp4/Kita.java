package fu.alp4;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Kita {

    private int babies;
    private List<Parent> parents;
    private List<Nurse> nurses;

    private Semaphore freeBabySlots;

    public Kita() {
        this.parents = new LinkedList<>();
        this.nurses = new LinkedList<>();
        this.babies = 0;
        this.freeBabySlots = new Semaphore(0, true);
    }

    public void giveBabyToNursery(Parent parent) {

        System.out.println("New baby on the horizon!");
        try {
            this.freeBabySlots.acquire();
            this.babies++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.parents.add(parent);
        System.out.printf("New baby accepted!");
        this.printState();
    }

    public void takeBabyFromNursery(Parent parent) {
        this.freeBabySlots.release();
        this.parents.add(parent);
        this.babies--;
        System.out.println("Baby taken from nursery!");
        this.printState();
    }

    public void requestNewNurse(Nurse nurse) {
        this.freeBabySlots.release(5);
        nurses.add(nurse);
        System.out.println("A new nurse came. Things are going well.");
        this.printState();
    }

    public void requestSendNurseHome(Nurse nurse) {
        System.out.println("A nurse want to abandon ship!");
        try {
            this.freeBabySlots.acquire(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        nurses.remove(nurse);
        System.out.println("The nurse left the vessel.");
        this.printState();
    }

    public void printState() {
        System.out.printf("#Nurses: %s; #Babies: %s; Babies capacity left: %s\n\n",
                this.nurses.size(), this.babies, this.freeBabySlots.availablePermits());
    }
}
