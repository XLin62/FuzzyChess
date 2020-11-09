package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusPanel extends JPanel {
	private static final long serialVersionUID = -3288518908343717443L;
	private JLabel turnLabel;
	private JLabel moveLabel;
	private JButton endTurnButton;
	private GameResources resources;
	//private JLabel lastMoveLabel;
	
	public StatusPanel() {
		turnLabel = new JLabel("White's Turn");
		moveLabel = new JLabel("Move: 1");
		endTurnButton = new JButton("End Turn");
		//lastMoveLabel = new JLabel("LastMove"); //idk yet
		
		setLayout(new FlowLayout());
		add(turnLabel);
		add(moveLabel);
		add(endTurnButton);

		turnLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
		moveLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
		setPreferredSize(new Dimension(1080,60));
	}
	
	public void setTheme(GameResources t) {
		resources = t;
		updateFont();
		updateColors();
	}
	
	private void updateFont() {
		turnLabel.setFont(resources.getFontStyle());
		moveLabel.setFont(resources.getFontStyle());
		endTurnButton.setFont(resources.getFontStyle());
	}
	
	private void updateColors() {
		setBackground(resources.getBackgroundColor());
		endTurnButton.setBackground(resources.getBackgroundColor());
		endTurnButton.setForeground(resources.getForegroundColor());
		turnLabel.setForeground(resources.getForegroundColor());
		moveLabel.setForeground(resources.getForegroundColor());
		setBorder(BorderFactory.createLineBorder(resources.getBoardBorderColor()));
	}
	
	public void setTurnText(int turn) {
		String turnText = turn == 0 ? "White's Turn" : "Black's Turn";
		turnLabel.setText(turnText);
	}
	
	public void setMoveText(int move) {
		moveLabel.setText(String.format("Move: %d", move+1));
	}
	
	public JButton getEndTurnButton() {
		return endTurnButton;
	}
}
