package fu.alp4;

import javax.swing.*;

public class Main extends JPanel{

    public static void main(String[] args) {

        Gambler[] gamblers = new Gambler[3];
        gamblers[0] = new Gambler("Pesho");
        gamblers[1] = new Gambler("Stamat");
        gamblers[2] = new Gambler("Gosho");

        JFrame f = new JFrame();
        f.setContentPane(new Main());
        f.setSize(400,400);
        f.setVisible(true);

        for(int i = 0; i < gamblers.length; i++) {

            gamblers[i].start();
            f.add(gamblers[i].getTextArea());
        }

    }
}
