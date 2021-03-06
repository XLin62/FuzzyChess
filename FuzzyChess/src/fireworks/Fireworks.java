package fireworks;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;


public class Fireworks extends JFrame implements Runnable {
    private static final long serialVersionUID = 1266778429484392409L;

    public FireworksPanel panel;

    /*public static void main(String[] args) {
    	 Fireworks f = new Fireworks();
    }*/

    public void run() {
        this.repaint();
    }

    public Fireworks() {
        super();

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.panel = new FireworksPanel();

        this.setContentPane(panel);

        //Show the window.
        this.pack();
        this.validate();
        this.setVisible(true);
    }

}