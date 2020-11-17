package fireworks;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;


public class FireworksPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1266778429484392409L;
    private LinkedList<Spark> sparks = new LinkedList<Spark>();
    private final Dimension MAX_DIMENSION = new Dimension(800, 800);
    private Random generator = new Random();
    
    private Timer explosionTimer;
    private Timer fireworksTimer;

    public FireworksPanel() {
        this.setPreferredSize(MAX_DIMENSION);
        this.setLayout(null);

        //for the actual explosions
        fireworksTimer = new Timer(15, this);
        explosionTimer = new Timer(1000, this);      
    }
    
    public void startFireworks() {
    	fireworksTimer.start();
        explosionTimer.start();
    }
    
    public void stopFireworks() {
    	fireworksTimer.stop();
    	explosionTimer.stop();
    }

    public int sparksLeft() {
        return sparks.size();
    }

    public boolean removeSpark(Spark s) {
        return this.sparks.remove(s);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        Rectangle clip = g.getClip().getBounds();
        g.fillRect(0, 0, clip.width, clip.height);

        Graphics2D g2d = (Graphics2D)g;
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 45));
        g2d.drawString("YOU WIN!", clip.width/2-100, clip.width/2);

        Spark array[] = sparks.toArray(new Spark[0]);

        for(Spark s : array) {
            s.draw(g2d);
        }
    }

    private void explode(int x, int y) {
        int sparkCount = 50 + generator.nextInt(20);
        Color c = new Color(generator.nextInt(255), generator.nextInt(255), generator.nextInt(255));
        long lifespan = 1000 + generator.nextInt(1000);

        int choice = generator.nextInt(100);

        if (choice < 20) {
            createCircleSpark(x, y, sparkCount, c, lifespan);
        } else if (choice < 40) {
            createPerfectCircleSpark(x, y, sparkCount, c, lifespan);
        } else if (choice < 60) {
            createMovingSpark(x, y, sparkCount, c, lifespan);
        } else if (choice < 80) {
            createBubbleSpark(x, y, sparkCount, c, lifespan);
        } else {
            createTrigSpark(x, y, sparkCount, c, lifespan);
        }
    }

    private void createCircleSpark(int x, int y, int sparkCount, Color c, long lifespan) {
        for (int i = 0; i < sparkCount; i++) {
            double direction = 360 * generator.nextDouble();
            double speed = 10 * generator.nextDouble() + 5;
            sparks.addLast(new CircleSpark(this, direction, x, y, c, lifespan, speed));
        }
    }

    private void createPerfectCircleSpark(int x, int y, int sparkCount, Color c, long lifespan) {
        sparkCount *= 2;

        lifespan /= 2;

        double speed = 20 * generator.nextDouble() + 5;

        for (int i = 0; i < sparkCount; i++) {
            double direction = 360 * generator.nextDouble();
            sparks.addLast(new PerfectCircleSpark(this, direction, x, y, c, lifespan, speed));
        }
    }

    private void createTrigSpark(int x, int y, int sparkCount, Color c, long lifespan) {
        for (int i = 0; i < sparkCount; i++) {
            double direction = 360 * generator.nextDouble();
            double speed = 10 * generator.nextDouble() + 5;
            sparks.addLast(new TrigSpark(this, direction, x, y, c, lifespan, speed));
        }
    }

    private void createMovingSpark(int x, int y, int sparkCount, Color c, long lifespan) {
        for (int i = 0; i < sparkCount; i++) {
            double direction = 360 * generator.nextDouble();
            double speed = 10 * generator.nextDouble() + 5;
            sparks.addLast(new MovingSpark(this, direction, x, y, c, lifespan, speed));
        }
    }

    private void createBubbleSpark(int x, int y, int sparkCount, Color c, long lifespan) {
        for (int i = 0; i < sparkCount; i++) {
            double direction = 360 * generator.nextDouble();
            double speed = 10 * generator.nextDouble() + 5;
            sparks.addLast(new BubbleSpark(this, direction, x, y, c, lifespan, speed));
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == fireworksTimer) {
			if(sparksLeft() > 0) {
                repaint();
			}
		}
		else if(e.getSource() == explosionTimer) {
			explode(generator.nextInt(400)+200, generator.nextInt(400)+200);
		}
	}
}