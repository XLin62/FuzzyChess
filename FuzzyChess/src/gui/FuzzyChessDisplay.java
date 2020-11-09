package gui;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import fireworks.FireworksPanel;

public class FuzzyChessDisplay {
	private GameResources theme;
	private JFrame display;
	private StatusPanel statusPanel;
	private CapturePanel capturePanel1;
	private CapturePanel capturePanel2;
	private GamePanel gamePanel;
	private AttackPanel attackPanel;
	private FireworksPanel winScreen;
	
	
	private JMenuItem newGameMenuItem;
	private JMenuItem howToPlayMenuItem;
	private JCheckBoxMenuItem devModeMenuItem;
	private JMenuItem defaultStyleMenuItem;
	private JMenuItem style2MenuItem;

	
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
		JMenu styles = new JMenu("Styles");
		newGameMenuItem = new JMenuItem("New Game");
		howToPlayMenuItem = new JMenuItem("How to Play");
		devModeMenuItem = new JCheckBoxMenuItem("Developer Mode"); //will enable user to ignore game rules to test game functions - like win state/etc
		defaultStyleMenuItem = new JMenuItem("Default");
		
		
		game.add(newGameMenuItem);
		game.add(howToPlayMenuItem);
		game.add(styles);
		styles.add(defaultStyleMenuItem);
		game.add(devModeMenuItem);
		menubar.add(game);
		display.setJMenuBar(menubar);
	}
	
	public void displayWinScreen() {
		if(gamePanel != null) {
			display.getContentPane().remove(gamePanel);
			winScreen = new FireworksPanel();
			display.add(winScreen, BorderLayout.CENTER);
			statusPanel.getEndTurnButton().setEnabled(false);
		}
	}
	
	public void reset() {
		if(winScreen != null) {
			display.getContentPane().remove(winScreen);
			display.getContentPane().add(gamePanel);
			statusPanel.getEndTurnButton().setEnabled(true);
			winScreen = null;
		}
	}
	
	public StatusPanel getStatusPanel() {
		return statusPanel;
	}
	
	public CapturePanel getCapturePanel1() {
		return capturePanel1;
	}
	
	public CapturePanel getCapturePanel2() {
		return capturePanel2;
	}
	
	public GamePanel getGamePanel() {
		return gamePanel;
	}
	
	public AttackPanel getAttackPanel() {
		return attackPanel;
	}
	
	public JMenuItem getNewGameMenuItem() {
		return newGameMenuItem;
	}
	
	public JMenuItem getHowToPlayMenuItem() {
		return howToPlayMenuItem;
	}

	public JMenuItem getDevModeMenuItem() {
		return devModeMenuItem;
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
