package ai;

import java.util.ArrayList;

import engine.FuzzyChessEngine;
import gui.FuzzyChessDisplay;
import models.*;

public class FuzzyChessAgent {
	public static final int EASY_DIFFICULTY = 0;
	public static final int MED_DIFFICULTY = 1;
	public static final int HARD_DIFFICULTY = 2;
	//visualization of game to lookahead
	private FuzzyChess environment;
	
	//actual game to manipulate
	private FuzzyChessEngine engine;
	
	//moves created by evaluating the current state of the game
	ArrayList<BoardPosition> moves;
	int currentMoveIndex;
	
	//maybe build build heuristic which evaluates place you may move to in 3x3 grid
	//if the score there is very high then this means you're surrounded by allies
	//if the score there is very low then this means you're surrounded by enemies
	
	
	/*values of pieces (lowest to highest) pawn, knight, rook, queen, bishop, king
	* pawns obviously not valued very high
	* knight next lowest b/c while its range is high and it has the ability to snipe pawns
	* it cant charge and kill a king or queen (0% chance) and has low chance to take out others
	* rooks very powerful due to range ability and can assassinate leaders easily
	* knights also very powerful due to charge ability
	* bishop and kings highest priority b/c leader */
	private int[][] blkPieceValues = {{20, 40, 50, 70, 100, 1000},
								      {-20, -40, -50, -70, -100, -1000}};
	
	//piece position multipliers - probably will change around in the future
	//pawns really just need to move 1 space up so the back pieces can move
	private int[][] blkPawnBoardPositionValues = {{0,0,0,0,0,0,0,0},
												  {0,1,1,0,0,1,1,0},
												  {5,3,2,5,5,2,3,5},
												  {1,1,1,1,1,1,1,1},
												  {1,1,1,1,1,1,1,1},
												  {2,2,2,2,2,2,2,2},
												  {2,2,2,2,2,2,2,2},
												  {0,0,0,0,0,0,0,0}};
	//bishops need to stay back to block king
	private int[][] blkBishopBoardPositionValues = {{0,0,1,0,0,1,0,0},
			                                        {0,2,2,3,3,2,2,0},
												    {1,2,2,3,3,2,2,1},
												    {1,1,1,1,1,1,1,1},
												    {1,1,1,1,1,1,1,1},
												    {1,1,1,1,1,1,1,1},
												    {1,1,1,1,1,1,1,1},
												    {1,1,1,1,1,1,1,1}};
	//knights are stronger in the middle - they can reach every position then
	private int[][] blkKnightBoardPositionValues = {{1,1,1,1,1,1,1,1},
												    {1,1,1,1,1,1,1,1},
												    {1,1,2,2,2,2,1,1},
												    {1,1,2,},
												    {},
												    {},
											   	    {},
												    {}};
	//rooks are strong in the middle for offence and in the corners for defence
	private int[][] blkRookBoardPositionValues = {{},
												  {},
												  {},
												  {},
												  {},
												  {},
												  {},
												  {}};
	//queens are strong in the middle - they can move almost anywhere
	private int[][] blkQueenBoardPositionValues = {{},
												   {},
												   {},
												   {},
												   {},
												   {},
												   {},
											  	   {}};
	//kings need to stay back and get to corner
	private int[][] blkKingBoardPositionValues = {{},
												  {},
												  {},
												  {},
												  {},
												  {},
												  {},
												  {}};
	
	//white piece position multipliers
	private int[][] whtPawnBoardPositionValues;
	private int[][] whtBishopBoardPositionValues;
	private int[][] whtKnightBoardPositionValues;
	private int[][] whtRookBoardPositionValues;
	private int[][] whtQueenBoardPositionValues;
	private int[][] whtKingBoardPositionValues;
	
	//will need to get values for white also - because minimax
	//we will get to this - for now lets try to do just 1 turn - no lookaheads

	public FuzzyChessAgent(FuzzyChessEngine engine) {
		whtPawnBoardPositionValues = reverse(blkPawnBoardPositionValues);
		whtBishopBoardPositionValues = reverse(blkPawnBoardPositionValues);
		whtKnightBoardPositionValues = reverse(blkKnightBoardPositionValues);
		whtRookBoardPositionValues = reverse(blkRookBoardPositionValues);
		whtQueenBoardPositionValues = reverse(blkQueenBoardPositionValues);
		whtKingBoardPositionValues = reverse(blkKingBoardPositionValues);
		
		this.engine = engine;
		moves = new ArrayList<BoardPosition>();
		currentMoveIndex = -1;
	}

	//actuators to manipulate the state of the game and the display
	public void makeMove() {
		++currentMoveIndex;
		if(engine.getGame().getSubTurn() >= engine.getGame().getMaxSubTurns()) {
			engine.endTurn();
		}
		else {
			System.out.println("Clicking location" + moves.get(currentMoveIndex));
			if(currentMoveIndex % 2 == 0) {
				engine.getGame().selectPiece(moves.get(currentMoveIndex));
			}
			else {
				engine.getGame().makeMove(moves.get(currentMoveIndex));
				if(engine.getGame().getSelectedEnemyPiece() != null) {
					engine.startRollAnimation();
					return;
				}
				engine.getGame().endSubturn();
				engine.updateDisplay();
				engine.getGame().resetSelectedPieces();
				return;
			}
			engine.updateDisplay();
		}		
	}
	
	//evaluates the current state of the game and figures out
	//a list of moves it will make in the turn - afterwards is called
	//multiple times to iterate through its movements
	public void evaluateTurn(FuzzyChess game, int difficulty) {
		environment = game;
		currentMoveIndex = -1;
		switch(difficulty) {
		case 0:
			moves = makeRandomMoves();
			break;
		case 1:
		case 2:
		default:
			moves = makeRandomMoves();
		}
	}
	
	private ArrayList<BoardPosition> makeRandomMoves(){
		ArrayList<BoardPosition> randomMoves = new ArrayList<BoardPosition>();
		ArrayList<BoardPosition> availablePieces;
		ArrayList<BoardPosition> availableMoves;
		BoardPosition selectedPosition;
		BoardPosition moveToPosition;
		while(environment.getSubTurn() < environment.getMaxSubTurns()) {
			availablePieces = environment.getCurrentCorp().getActiveMemberPositions();
			while(true) {
				selectedPosition = availablePieces.get((int)(Math.random() * 100) % availablePieces.size());
				environment.selectPiece(selectedPosition);
				//need to make sure it can actually move/capture
				System.out.println("Selected Piece " + environment.getSelectedPiece());
				availableMoves = environment.getAllMoves();
				if(availableMoves.size() > 0) break;
				environment.resetSelectedPieces();
			}

			System.out.println(availableMoves);
			moveToPosition = availableMoves.get((int)(Math.random() * 100) % availableMoves.size());
			if(environment.makeMove(moveToPosition)) {
				environment.endSubturn();
			};
			randomMoves.add(selectedPosition);
			randomMoves.add(moveToPosition);
		}
		System.out.println("Moves to be made...");
		System.out.println(randomMoves);
		return randomMoves;
	}
	
	private int[][] reverse(int[][] arr){
		int[][] reversed = new int[arr.length][arr[0].length];
		for(int i = 0; i < arr.length; i++) {
			reversed[i] = arr[arr.length-i-1];
		}		
		return reversed;
	}
	
}
