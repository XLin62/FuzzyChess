package engine;

import java.awt.event.*;

import javax.swing.SwingUtilities;

import gui.FuzzyChessDisplay;
import models.BoardPosition;
import models.FuzzyChess;

public class FuzzyChessEngine implements ActionListener{
	private FuzzyChess game;
	private FuzzyChessDisplay display;
	private boolean inAnimation;
	
	public FuzzyChessEngine() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				game = new FuzzyChess();
				display = new FuzzyChessDisplay();
				updateDisplay();
				registerControls();
				inAnimation = false;
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
				display.getAttackPanel().update(game.getSelectedPiece().getid(), game.getSelectedEnemyPiece().getid(), game.getCaptureResult());
				display.getAttackPanel().rollDice(game.getLastRoll());
				inAnimation = true;
				return;
			}
			if(moveMade) {
				game.endSubturn();
			}
			updateDisplay();
			game.resetSelectedPieces();
			return;
		}
		updateDisplay();
		//game.resetSelectedPieces();
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
				display.getAttackPanel().update(game.getSelectedPiece().getid(), game.getSelectedEnemyPiece().getid(), game.getCaptureResult());
				display.getAttackPanel().rollDice(game.getLastRoll());
				inAnimation = true;
				return;
			}
			if(moveMade) {
				game.endSubturn();
			}
			updateDisplay();
			game.resetSelectedPieces();
			return;
		}
		updateDisplay();
	}
	
	//called by dice roll animation thread when finished
	public void callbackUpdate() {
		inAnimation = false;
		game.endSubturn();
		updateDisplay();
		game.resetSelectedPieces();
		
		//show win screen
		if(game.isGameOver()) {
			System.out.println("Game Over");
			display.displayWinScreen();
		}
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
		//menu
		display.getDevModeMenuItem().setSelected(game.isDevMode());
		//endgame
		if(game.isGameOver()) {
			display.displayWinScreen();
		}
	}
	
	public void registerControls() {
		display.getAttackPanel().setCallBackRef(this);
		display.getGamePanel().addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				dealWithClick(e);
			}
		});
		
		display.getNewGameMenuItem().addActionListener(this);
		display.getDevModeMenuItem().addActionListener(this);
		display.getHowToPlayMenuItem().addActionListener(this);
		display.getStatusPanel().getEndTurnButton().addActionListener(this);
	}
	
	public void dealWithClick(MouseEvent e) {
		if(!inAnimation) { //if we're in an animation lock controls
			if(game.getTurn() == 0) {
				getPlayer1Move(BoardPosition.convert(e.getX(), e.getY()));
			}
			else if(game.getTurn() == 1) {
				getPlayer2Move(BoardPosition.convert(e.getX(), e.getY()));
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == display.getNewGameMenuItem()) {
			game = new FuzzyChess();
			display.reset();
			updateDisplay();
		}
		else if(e.getSource() == display.getHowToPlayMenuItem()) {
			display.displayHelpScreen();
		}
		else if(e.getSource() == display.getDevModeMenuItem()) {
			game.toggleDevMode(); //need to make this a checkbox
		}
		else if(e.getSource() == display.getStatusPanel().getEndTurnButton()) {
			if(!inAnimation) {
				game.endTurn();
				game.resetSelectedPieces();
				updateDisplay();
			}
		}
		
	}	
}
