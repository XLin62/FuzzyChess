package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

public class CapturePanel extends JPanel {
	private GameResources theme;
	private JLabel titleLabel;
	private JPanel piecesPanel;
	private ArrayList<JLabel> capturedPieceLabels;
	
	public CapturePanel(String title) {
		titleLabel = new JLabel(title);
		titleLabel.setHorizontalAlignment((int)JLabel.CENTER_ALIGNMENT);
		piecesPanel = new JPanel();

		
		setPreferredSize(new Dimension(160,760));
		setLayout(new BorderLayout());
		add(titleLabel, BorderLayout.NORTH);
		add(piecesPanel, BorderLayout.CENTER);
	}
	
	public void setTheme(GameResources t) {
		theme = t;
		updateFont();
		updateColors();
	}
	
	public void updateFont() {
		titleLabel.setFont(theme.getFontStyle());
	}
	
	public void updateColors() {
		titleLabel.setForeground(theme.getForegroundColor());
		setBackground(theme.getBackgroundColor());
		piecesPanel.setBackground(theme.getBackgroundColor());
		setBorder(BorderFactory.createLineBorder(theme.getForegroundColor()));
	}
}
