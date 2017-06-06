package fu.alp4;

import javax.swing.*;

public class Main extends JPanel{

    public static void main(String[] args) {

        CasinoBank casino = new CasinoBank();

        Gambler[] gamblers = new Gambler[3];
        gamblers[0] = new Gambler("Pesho", casino);
        gamblers[1] = new Gambler("Stamat", casino);
        gamblers[2] = new Gambler("Gosho", casino);

        JFrame f = new JFrame();
        f.setContentPane(new Main());
        f.setSize(400,400);
        f.setVisible(true);

        f.add(casino.getTextArea());
        for(int i = 0; i < gamblers.length; i++) {

            gamblers[i].start();
            f.add(gamblers[i].getTextArea());
        }

    }
}
