package fu.alp4;

public class Main {

    public static void main(String[] args) {
	// write your code here

        Kita kita = new Kita();

        for (int j = 0; j < 15; j++) {
            String crntParentName = "Parent #" + (j+1);
            Parent crntParent = new Parent(crntParentName, kita);
            crntParent.start();
        }

        for (int i = 0; i < 3; i++) {
            String crntNurseName = "Nurse #" + (i+1);
            Nurse crntNurse = new Nurse(crntNurseName, kita );
            crntNurse.start();
        }
    }
}
