package gui;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class FuzzyChessDisplay {
	private GameResources theme;
	private JFrame display;
	private StatusPanel statusPanel;
	private CapturePanel capturePanel1;
	private CapturePanel capturePanel2;
	private GamePanel gamePanel;
	private AttackPanel attackPanel;

	
	public FuzzyChessDisplay() {
		display = new JFrame();
		display.setTitle("Medieval Warfare");
		display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		display.getContentPane().setLayout(new BorderLayout());
		display.setVisible(true);
		
		initMenu();		
		statusPanel = new StatusPanel();
		capturePanel1 = new CapturePanel("White Captures");
		capturePanel2 = new CapturePanel("Black Captures");
		gamePanel = new GamePanel();
		attackPanel = new AttackPanel();
		
		display.getContentPane().add(statusPanel, BorderLayout.SOUTH);
		display.getContentPane().add(capturePanel1, BorderLayout.WEST);
		display.getContentPane().add(capturePanel2, BorderLayout.EAST);
		display.getContentPane().add(gamePanel, BorderLayout.CENTER);
		display.getContentPane().add(attackPanel, BorderLayout.NORTH);
		display.pack();
		
		setTheme("Default");
	}
	
	
	//just for display purposes as of now...
	public void initMenu() {
		JMenuBar menubar = new JMenuBar();
		JMenu game = new JMenu("Game");
		JMenuItem newGame = new JMenuItem("New Game");
		JMenuItem howToPlay = new JMenuItem("How to Play");
		JMenuItem undoMove = new JMenuItem("Undo Last Move");
		JMenuItem devMode = new JMenuItem("Developer Mode"); //will enable user to ignore game rules to test game functions - like win state/etc
		
		game.add(newGame);
		game.add(howToPlay);
		game.add(undoMove);
		game.add(devMode);
		menubar.add(game);
		display.setJMenuBar(menubar);
	}
	
	public StatusPanel getStatusPanel() {
		return statusPanel;
	}
	
	public CapturePanel getCapturePanel(int turn) {
		return turn == 0 ? capturePanel1 : capturePanel2;
	}
	
	public GamePanel getGamePanel() {
		return gamePanel;
	}
	
	public AttackPanel getAttackPanel() {
		return attackPanel;
	}
	
	//set theme/look and feel of the game
	public void setTheme(String type) {
		switch(type) {
		case "Default":
			theme = GameResources.getDefault();
		}//can add more
		
		statusPanel.setTheme(theme);
		capturePanel1.setTheme(theme);
		capturePanel2.setTheme(theme);
		gamePanel.setTheme(theme);
		attackPanel.setTheme(theme);
	}
}
