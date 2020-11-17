package engine;

import java.awt.event.*;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import ai.FuzzyChessAgent;
import gui.FuzzyChessDisplay;
import models.BoardPosition;
import models.FuzzyChess;

//want to add timer to display as added win/lose condition
//also to work on ai

public class FuzzyChessEngine implements ActionListener{
	private FuzzyChess game;
	private FuzzyChessDisplay display;
	private Timer aiMoveTimer;
	private FuzzyChessAgent ai;
	private boolean inAnimation;
	private boolean aiTurn;
	
	public FuzzyChessEngine() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				game = new FuzzyChess();
				display = new FuzzyChessDisplay();
				updateDisplay();
				registerControls();
				registerAI();
				inAnimation = false;
			}
		});
	}
	
	public void getPlayerMove(BoardPosition move) {
		boolean moveMade = false;
		
		if(game.getSubTurn() >= game.getMaxSubTurns()) {
			return;
		}
		
		if(game.getSelectedPiece() == null) {
			game.selectPiece(move);
		}
		else{
			moveMade = game.makeMove(move);
			//did we select an enemy? - if so - show animation
			if(game.getSelectedEnemyPiece() != null) {
				startRollAnimation();
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
	
	//not sure if I want this to be a thread or naw
	public void startAITurn() {
		//first let ai look at state of the game and decide what it wants to do
		ai.evaluateTurn(game.copy(), FuzzyChessAgent.EASY_DIFFICULTY);
		//start timers - when they activate then do the moves calculated
		aiMoveTimer = new Timer(1000, this);
		aiMoveTimer.start();
	}
	
	public void startRollAnimation() {
		String rollsNeeded = "Rolls Needed To Capture (";
		int lastRoll = game.getLastRoll();
		int[] rolls = game.getSelectedPiece().getRolls(game.getSelectedEnemyPiece());
		for(int i = 0; i < rolls.length; i++) {
			if(i != rolls.length-1)
				rollsNeeded += rolls[i] + ", ";
			else
				rollsNeeded += rolls[i];
		}
		rollsNeeded += ")";
		display.getAttackPanel().update(game.getSelectedPiece().getid(), game.getSelectedEnemyPiece().getid());
		display.getAttackPanel().rollDice(lastRoll, rollsNeeded, game.getCaptureResult());
		inAnimation = true;	
	}
	
	public void endTurn() {
		game.endTurn();
		game.resetSelectedPieces();
		updateDisplay();
		
		if(game.getTurn() == 1) {
			aiTurn = true;
			startAITurn();
		}
		else {
			aiTurn = false;
			aiMoveTimer.stop();
		}
		
		
	}
	
	//called by dice roll animation thread when finished
	public void callbackUpdate() {
		inAnimation = false;
		game.endSubturn();
		updateDisplay();
		game.resetSelectedPieces();
	}
	
	public void updateDisplay() {
		//status
		display.getStatusPanel().setTurnText(game.getTurn());
		display.getStatusPanel().setMoveText(game.getSubTurn() >= game.getMaxSubTurns() ? "End Turn" : "" + (game.getSubTurn()+1));
		display.getStatusPanel().setButtonHighlight(game.getSubTurn() >= game.getMaxSubTurns());
		//board
		display.getGamePanel().updateBoard(game.getBoard().getBoardState(), game.getBoard().getBoardColors());
		//captures
		display.getCapturePanel1().update(game.getPlayer1Captures());
		display.getCapturePanel2().update(game.getPlayer2Captures());
		//attack
		char attackerID = game.getSelectedPiece() == null ? 'x' : game.getSelectedPiece().getid();
		char defenderID = game.getSelectedEnemyPiece() == null ? 'x' : game.getSelectedEnemyPiece().getid();
		display.getAttackPanel().update(attackerID, defenderID);
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
	
	public void registerAI() {
		ai = new FuzzyChessAgent(this);
	}
	
	public void dealWithClick(MouseEvent e) {
		if(!inAnimation || aiTurn) { //if we're in an animation or its the ai turn lock controls
			if(game.getTurn() == 0) {
				getPlayerMove(BoardPosition.convert(e.getX(), e.getY()));
			}
			/*else if(game.getTurn() == 1) {
				getPlayerMove(BoardPosition.convert(e.getX(), e.getY()));
			}*/
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
			game.toggleDevMode();
		}
		else if(e.getSource() == aiMoveTimer) {
			if(!inAnimation) {
				ai.makeMove();
			}
		}
		else if(e.getSource() == display.getStatusPanel().getEndTurnButton()) {
			if(!inAnimation && !aiTurn) {
				endTurn();
			}
		}
	}
	
	public FuzzyChess getGame() {
		return game;
	}
	
	public FuzzyChessDisplay getDisplay() {
		return display;
	}
}
