package engine;

import java.awt.event.*;

import javax.swing.SwingUtilities;

import gui.FuzzyChessDisplay;
import models.BoardPosition;
import models.FuzzyChess;

public class FuzzyChessEngine implements ActionListener{
	private FuzzyChess game;
	private FuzzyChessDisplay display;
	
	public FuzzyChessEngine() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				game = new FuzzyChess();
				display = new FuzzyChessDisplay();
				updateDisplay();
				registerControls();
			}
		});
	}
	
	public void getPlayer1Move(BoardPosition move) {
		boolean moveMade = false;
		
		if(game.getSelectedPiece() == null) {
			game.selectPiece(move);
		}
		else{
			moveMade = game.makeMove(move);
			//did we select an enemy? - if so - show animation
			if(game.getSelectedEnemyPiece() != null) {
				
			}
			if(moveMade) {
				game.endSubturn();
			}
			updateDisplay();
			game.resetSelectedPieces();
			
			//show win screen
			if(game.isGameOver()) {
				System.out.println("Game Over");
				display.displayWinScreen();
			}
			return;
		}
			
		//check to see if turn has switched
		//switch to ai control if so
		/*if(game.getTurn() == 1) {
			getPlayer2Move();
		}*/
		updateDisplay();
	}
	
	public void getPlayer2Move(BoardPosition move) {
		boolean moveMade = false;
		
		if(game.getSelectedPiece() == null) {
			game.selectPiece(move);
		}
		else{
			moveMade = game.makeMove(move);
			//did we select an enemy? - if so - show animation
			if(game.getSelectedEnemyPiece() != null) {
				
			}
			if(moveMade) {
				game.endSubturn();
			}
			
			//show win screen
			if(game.isGameOver()) {
				System.out.println("Game Over");	
			}
			
			updateDisplay();
			game.resetSelectedPieces();
			return;
		}
			
		//check to see if turn has switched
		//switch to ai control if so
		/*if(game.getTurn() == 1) {
			getPlayer2Move();
		}*/
		
		//show win screen
		if(game.isGameOver()) {
			System.out.println("Game Over");
		}
		updateDisplay();
	}
	
	public void updateDisplay() {
		
		//status
		display.getStatusPanel().setTurnText(game.getTurn());
		display.getStatusPanel().setMoveText(game.getSubTurn());
		//if turn = players - disable button - otherwise enable
		//board
		display.getGamePanel().updateBoard(game.getBoard().getBoardState(), game.getBoard().getBoardColors());
		//captures
		display.getCapturePanel1().update(game.getPlayer1Captures());
		display.getCapturePanel2().update(game.getPlayer2Captures());
		//attack
		char attackerID = game.getSelectedPiece() == null ? 'x' : game.getSelectedPiece().getid();
		char defenderID = game.getSelectedEnemyPiece() == null ? 'x' : game.getSelectedEnemyPiece().getid();
		display.getAttackPanel().update(attackerID, defenderID, game.getCaptureResult());
		if(game.getSelectedEnemyPiece() != null) {
			display.getAttackPanel().rollDice(game.getLastRoll());
		}
		//menu
		display.getDevModeMenuItem().setSelected(game.isDevMode());
		//endgame
		if(game.isGameOver()) {
			display.displayWinScreen();
		}
	}
	
	public void registerControls() {
		display.getGamePanel().addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				dealWithClick(e);
			}
			
			public void mouseMoved(MouseEvent e) {
				dealWithMouseMovement(e);
			}
		});
		
		display.getNewGameMenuItem().addActionListener(this);
		display.getDevModeMenuItem().addActionListener(this);
		display.getHowToPlayMenuItem().addActionListener(this);
		display.getStatusPanel().getEndTurnButton().addActionListener(this);
	}
	
	public void dealWithClick(MouseEvent e) {
		//System.out.println(String.format("Actual: %d, %d\nBoardPosition: %s", e.getX(), e.getY(), BoardPosition.convert(e.getX(), e.getY()).toString()));
		if(game.getTurn() == 0) {
			getPlayer1Move(BoardPosition.convert(e.getX(), e.getY()));
		}
		else if(game.getTurn() == 1) {
			getPlayer2Move(BoardPosition.convert(e.getX(), e.getY()));
		}
	}
	
	public void dealWithMouseMovement(MouseEvent e) {
		display.getGamePanel().setToolTipText(BoardPosition.convert(e.getX(), e.getY()).toString());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == display.getNewGameMenuItem()) {
			game = new FuzzyChess();
			display.reset();
			updateDisplay();
		}
		else if(e.getSource() == display.getHowToPlayMenuItem()) {
			//display pdf somehow - or create own rule page
		}
		else if(e.getSource() == display.getDevModeMenuItem()) {
			game.toggleDevMode(); //need to make this a checkbox
		}
		else if(e.getSource() == display.getStatusPanel().getEndTurnButton()) {
			game.endTurn();
			game.resetSelectedPieces();
			updateDisplay();
		}
		
	}	
}
