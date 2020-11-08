package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

import models.ChessPiece;

public class CapturePanel extends JPanel {
	private GameResources resources;
	private JLabel titleLabel;
	private ImageGrid grid;
	
	public CapturePanel(String title) {
		titleLabel = new JLabel(title);
		titleLabel.setHorizontalAlignment((int)JLabel.CENTER_ALIGNMENT);
		grid = new ImageGrid(8,2);
		setPreferredSize(new Dimension(160,760));
		setLayout(new BorderLayout());
		add(titleLabel, BorderLayout.NORTH);
		add(grid, BorderLayout.CENTER);
	}
	
	private class ImageGrid extends JPanel{
		private int SPACE_SIZE = 80;
		private int OFFSET = 5;
		private int rows;
		private int cols;
		private ArrayList<BufferedImage> images;
		
		public ImageGrid(int r, int c) {
			//setPreferredSize(new Dimension(WIDTH,HEIGHT));
			images = new ArrayList<BufferedImage>();
			rows = r;
			cols = c;
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBackground(resources.getBoardBackgroundColor());
			g.setColor(resources.getBoardBorderColor());
			int pieceIndex = 0;
			for(int i = 0; i < rows; i++) {
				for(int j = 0; j < cols; j++) {
					if(pieceIndex < images.size()) {
						g.drawImage(images.get(pieceIndex++), j * SPACE_SIZE+OFFSET, i * SPACE_SIZE+OFFSET,
								SPACE_SIZE-(OFFSET*2), SPACE_SIZE-(OFFSET*2), resources.getBoardColor(), this);
					}
					g.drawRect(j*SPACE_SIZE, i*SPACE_SIZE, SPACE_SIZE, SPACE_SIZE);
				}
			}
		}
		
		public void setImages(ArrayList<BufferedImage> imgs) {
			images = imgs;
		}
	}
	
	public void setTheme(GameResources t) {
		resources = t;
		updateFont();
		updateColors();
	}
	
	public void update(ArrayList<ChessPiece> pieces) {
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		for(ChessPiece piece : pieces) {
			images.add(resources.getChessSprite(piece.getid()));
		}
		grid.setImages(images);
		repaint();
	}
	
	public void updateFont() {
		titleLabel.setFont(resources.getFontStyle());
	}
	
	public void updateColors() {
		titleLabel.setForeground(resources.getForegroundColor());
		setBackground(resources.getBackgroundColor());
		setBorder(BorderFactory.createLineBorder(resources.getForegroundColor()));
	}
}
