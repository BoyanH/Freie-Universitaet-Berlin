package fu.alp4;

/**
 * Created by hristov on 5/8/17.
 */
public class Nap extends Thread{

    public static void nap(int milliSeconds) {

        try {
            Thread.sleep(milliSeconds);
        }
        catch (InterruptedException e) {
            System.out.println("Sleep was interrupted. " + e.getMessage());
        }
    }

    public static void randomNap(int minMilliSeconds, int maxMilliSeconds) {

        int randomMilliSeconds = (int) Math.round(Math.random()*(maxMilliSeconds-minMilliSeconds) + minMilliSeconds);
        nap(randomMilliSeconds);
    }
}
