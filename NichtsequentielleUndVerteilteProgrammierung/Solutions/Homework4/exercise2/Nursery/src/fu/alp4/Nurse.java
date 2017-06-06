package fu.alp4;

public class Nurse extends Nap {

    private Kita kita;

    public Nurse(String name, Kita kita) {
        this.kita = kita;
    }

    public void run() {

        while(true) {
            Nurse.randomNap(5000, 15000);
            this.goToWork();
            Nurse.randomNap(30000, 70000);
            this.goHome();
        }
    }

    public void goHome() {
        kita.requestSendNurseHome(this);
    }

    public void goToWork() {
        this.kita.requestNewNurse(this);
    }
}
