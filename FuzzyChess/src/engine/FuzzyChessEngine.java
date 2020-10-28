package engine;

import java.awt.event.*;

import gui.FuzzyChessDisplay;
import models.FuzzyChess;

public class FuzzyChessEngine implements Runnable, ActionListener{
	private FuzzyChess game;
	private FuzzyChessDisplay display;
	private MouseAdapter boardControl;

	
	public FuzzyChessEngine() {
		game = new FuzzyChess();
		display = new FuzzyChessDisplay();
	}
	
	public void run() {
		
	}
	
	public void getPlayer0Move() {
		
	}
	
	public void getPlayer1Move() {
		
	}
	
	public void updateDisplay() {
		
	}
	
	public void initBoardControl() {
		boardControl = new MouseAdapter() {
			public void onMouseClick(MouseEvent e) {
				
			}
		};
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
