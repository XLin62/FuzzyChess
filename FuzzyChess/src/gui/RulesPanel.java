package gui;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class RulesPanel extends JPanel {
	int WIDTH = 760;
	int HEIGHT = 760;
	
	String howToMove;
	String howToAttack;
	String aboutCommanders;
	String howToWin;
	GameResources resources;
	ArrayList<BufferedImage> images;
	
	public RulesPanel() {
		setVisible(true);
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(resources.getBackgroundColor());
		g.setColor(resources.getBackgroundColor());
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(resources.getForegroundColor());
		g.setFont(resources.getFontStyle());
		g.drawString("HOW TO PLAY", WIDTH/2 - 100, 40);
		if(images != null) {
			g.drawImage(images.get(1), 10, 60, resources.getBackgroundColor(), null);
		}
		g.dispose();
	}
	
	public void setResources(GameResources g) {
		resources = g;
		images = resources.getRulesImages();
		repaint();
	}
}
