package fu.alp4;

import javax.swing.*;

/**
 * Created by hristov on 5/8/17.
 */

public class Gambler extends Nap {

    private CasinoBank playingInCasino;
    private int budget;
    private final int initialBudget;
    private boolean playing;
    private String name;
    private JTextArea textArea;

    public Gambler(String givenName, CasinoBank casino) {

        this.initialBudget = 5;
        this.budget = this.initialBudget;
        this.playing = true;
        this.name = givenName;
        this.textArea = new JTextArea();
        this.playingInCasino = casino;
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

        boolean justWon;

        try {
            justWon = this.playingInCasino.playAGame();

            if(justWon) {
                this.budget++;

            }
            else {
                this.budget--;
            }

            if(this.budget == 0 || this.budget == this.initialBudget*2 || !this.playingInCasino.getAvailable()) {

                this.playing = false;
            }

            this.textArea.setText(this.name + " just " +
                    (justWon ? "won" : "lost") +
                    " 1 dollar. Current budget: " + this.budget + " ; still playing: " +
                    this.isPlaying());
        }
        catch (Exception e) {
            // casino closed
            this.playing = false;
            this.textArea.setText(this.name + " is no longer playing, because the casino closed!");
        }
    }

    public boolean isPlaying() {

        return this.playing;
    }

    JTextArea getTextArea() {

        return this.textArea;
    }
}
