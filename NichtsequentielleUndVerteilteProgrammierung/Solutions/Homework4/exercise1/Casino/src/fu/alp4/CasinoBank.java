package fu.alp4;

import javax.swing.*;

public class CasinoBank {

    private int money;
    private boolean playing;
    private JTextArea textArea;
    private Object moneyLock;

    public CasinoBank() {
        this.money = 20;
        this.textArea = new JTextArea();
        this.moneyLock = new Object();
        this.playing = true;
    }


    public boolean getAvailable() {
        return this.playing;
    }

    public boolean playAGame() throws Exception {

        boolean playerWon;


        double random = Math.round(Math.random());
        playerWon = random <= 0.4;

        synchronized (moneyLock) {

            if (!this.playing) {
                throw new Exception("Can't play further games. Casino closed!");
            }

            if (playerWon) {
                this.money--;
                this.playing = this.money > 0;
            } else {
                this.money++;
            }
        }

        this.textArea.setText(String.format("Casino has %s dollars. Casino playing: %s", this.money, this.playing));

        return playerWon;
    }

    JTextArea getTextArea() {

        return this.textArea;
    }
}
