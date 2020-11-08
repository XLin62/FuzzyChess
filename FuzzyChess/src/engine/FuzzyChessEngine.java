package engine;

import java.awt.event.*;
import java.util.ArrayList;
import java.awt.Robot;

import javax.swing.SwingUtilities;

import gui.FuzzyChessDisplay;
import models.BoardPosition;
import models.ChessPiece;
import models.FuzzyChess;

public class FuzzyChessEngine{
	private FuzzyChess game;
	private FuzzyChessDisplay display;

	public void run() {
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
			return;
		}
			
		//check to see if turn has switched
		//switch to ai control if so
		/*if(game.getTurn() == 1) {
			getPlayer2Move();
		}*/
		
		//show win screen
		if(game.isGameOver()) {
			
		}
		
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
			
		}
		
		updateDisplay();
	}
	
	public void updateDisplay() {
		//status
		display.getStatusPanel().setTurnText(game.getTurn());
		display.getStatusPanel().setMoveText(game.getSubTurn());
		//board
		display.getGamePanel().updateBoard(game.getBoard().getBoardState(), game.getBoard().getBoardColors());
		//captures
		display.getCapturePanel1().update(game.getPlayer1Captures());
		display.getCapturePanel2().update(game.getPlayer2Captures());
		//attack
		char attackerID = game.getSelectedPiece() == null ? 'x' : game.getSelectedPiece().getid();
		char defenderID = game.getSelectedEnemyPiece() == null ? 'x' : game.getSelectedEnemyPiece().getid();
		display.getAttackPanel().update(attackerID, defenderID);
		if(game.getSelectedEnemyPiece() != null) {
			display.getAttackPanel().rollDice(game.getLastRoll());
		}
	}
	
	public void registerControls() {
		display.getGamePanel().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dealWithClick(e);
			}
		});
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
}
