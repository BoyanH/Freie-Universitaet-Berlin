package fu.alp4;

import com.sun.java.swing.plaf.windows.WindowsDesktopPaneUI;
import javax.swing.*;

/**
 * Created by hristov on 5/8/17.
 */

public class Gambler extends Nap {

    private int budget;
    private final int initialBudget;
    private boolean playing;
    private String name;
    private JTextArea textArea;

    WindowsDesktopPaneUI window = new WindowsDesktopPaneUI();

    public Gambler(String givenName) {

        this.initialBudget = 5;
        this.budget = this.initialBudget;
        this.playing = true;
        this.name = givenName;
        this.textArea = new JTextArea();
    }

    public void run() {

        while(this.isPlaying()) {

            this.think();
            this.bet();
        }
    }

    public void think() {

        randomNap(500, 1500);
    }

    public void bet() {

        boolean justWon = this.didWin();

        if(justWon) {
            this.budget++;

        }
        else {
            this.budget--;
        }

        if(this.budget == 0 || this.budget == this.initialBudget*2) {

            this.playing = false;
        }

        this.textArea.setText(this.name + " just " +
                (justWon ? "won" : "lost") +
                " 1 dollar. Current budget: " + this.budget + " ; still playing: " +
                this.isPlaying());
    }

    public boolean didWin() {

        int randomOneOrZero = (int) Math.round(Math.random());
        return randomOneOrZero == 1;
    }

    public boolean isPlaying() {

        return this.playing;
    }

    JTextArea getTextArea() {

        return this.textArea;
    }
}
