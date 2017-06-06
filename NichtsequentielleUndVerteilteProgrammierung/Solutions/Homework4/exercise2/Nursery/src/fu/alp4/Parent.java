package fu.alp4;

public class Parent extends Nap {

    private Kita kita;

    public Parent(String name, Kita kita) {
        this.kita = kita;
    }

    public void run() {

        while(true) {
            Parent.randomNap(5000, 15000);
            this.sendBabyToNursery();
            Parent.randomNap(20000, 50000);
            this.takeBabyFromNursery();
        }
    }

    public void sendBabyToNursery() {
        this.kita.giveBabyToNursery(this);
    }

    public void takeBabyFromNursery() {
        this.kita.takeBabyFromNursery(this);
    }
}
