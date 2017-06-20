package fu.alp4;

public class Main {

    public static void main(String[] args) {

        for (int i = 0; i < 5; i++) {
            if (i < 4) {
                new Reader().start();
            } else {
                new Writer().start();
            }
        }
    }
}
