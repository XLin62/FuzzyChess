package models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


/* FuzzyChess
 * Author: Marcus Kruzel
 * Version: 1.0
 * Last Updated Date: 10/17/20
 * Contains main logic for game
 */
public class FuzzyChess {
	//Commanding forces for each player
	//0 == king, 1 == l_bishop, 2 == r_bishop
	private ArrayList<Corp> p1_corps;
	private ArrayList<Corp> p2_corps;
	private Corp currentEnemyCorp;

	private ArrayList<ChessPiece> p1_captures;
	private ArrayList<ChessPiece> p2_captures;
	private GameBoard board;
	private int turn;
	private int subturn;
	private boolean gameOver;

	private ChessPiece selectedPiece;
	private ChessPiece selectedEnemy;
	private ArrayList<BoardPosition> possibleMoves;
	private ArrayList<BoardPosition> possibleCaptures;

	//use for capture panel display
	private Random dice;
	private boolean diceOffset;
	private int lastRoll;
	private String captureResult;

	public FuzzyChess() {
		gameOver = false;
		board = new GameBoard();
		initCorps();
		currentEnemyCorp = null;
		p1_captures = new ArrayList<ChessPiece>();
		p2_captures = new ArrayList<ChessPiece>();
		turn = 0;
		subturn = 0;
		selectedPiece = null;
		selectedEnemy = null;
		dice = new Random();
		dice.setSeed((long)Math.random() * 100000);
		board.updateBoardColors(getCurrentCorp().getMemberPositions(), null, null);
	}

	public void quitGame() {
		gameOver = true;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	//corps grab pieces from the board
	//this is ugly code too - but w/e
	private void initCorps() {
		char[][] gameBoard = board.getBoardState();
		Corp p1_king_corp = new Corp(null);
		Corp p2_king_corp = new Corp(null);
		Corp p1_lbishop_corp = new Corp(p1_king_corp);
		Corp p2_lbishop_corp = new Corp(p2_king_corp);
		Corp p1_rbishop_corp = new Corp(p1_king_corp);
		Corp p2_rbishop_corp = new Corp(p2_king_corp);
		p1_corps = new ArrayList<Corp>();
		p2_corps = new ArrayList<Corp>();

		for (int i = 0; i < gameBoard.length; i++) {
			for (int j = 0; j < gameBoard[0].length; j++) {
				char id = gameBoard[i][j];
				BoardPosition curPosition = new BoardPosition(j, i);
				
				// kings corp
				if (j >= 3 && j < 5) {
					if (id == 'p') {
						ChessPiece piece = new ChessPiece(curPosition, id, ChessPiece.DOWN);
						p2_king_corp.addMember(piece);
					} else if (id == 'P') {
						ChessPiece piece = new ChessPiece(curPosition, id, ChessPiece.UP);
						p1_king_corp.addMember(piece);
					}
				}
				if (id == 'k' || id == 'q' || id == 'r') {
					ChessPiece piece = new ChessPiece(curPosition, id, ChessPiece.DOWN);
					p2_king_corp.addMember(piece);
				}
				if (id == 'K' || id == 'Q' || id == 'R') {
					ChessPiece piece = new ChessPiece(curPosition, id, ChessPiece.UP);
					p1_king_corp.addMember(piece);
				}
				
				// left bishop corp
				if (j < 3) {
					if (id == 'p' || id == 'b' || id == 'n') {
						ChessPiece piece = new ChessPiece(curPosition, id, ChessPiece.DOWN);
						p2_lbishop_corp.addMember(piece);
					}
					if (id == 'P' || id == 'B' || id == 'N') {
						ChessPiece piece = new ChessPiece(curPosition, id, ChessPiece.UP);
						p1_lbishop_corp.addMember(piece);
					}
				}

				// right bishop corp
				if (j >= 5) {
					if (id == 'p' || id == 'b' || id == 'n') {
						ChessPiece piece = new ChessPiece(curPosition, id, ChessPiece.DOWN);
						p2_rbishop_corp.addMember(piece);
					} else if (id == 'P' || id == 'B' || id == 'N') {
						ChessPiece piece = new ChessPiece(curPosition, id, ChessPiece.UP);
						p1_rbishop_corp.addMember(piece);
					}
				}
			}
		}
		p1_corps.add(p1_king_corp);
		p1_corps.add(p1_lbishop_corp);
		p1_corps.add(p1_rbishop_corp);
		p2_corps.add(p2_king_corp);
		p2_corps.add(p2_lbishop_corp);
		p2_corps.add(p2_rbishop_corp);
	}
	
	/* as of now - corps go in order */
	public Corp getCurrentCorp() {
		if (turn == 0) {
			return p1_corps.get(subturn);
		} else if (turn == 1) {
			return p2_corps.get(subturn);
		}
		return null;
	}

	/*select for movement/capturing - update board colors when selected */
	public boolean selectPiece(BoardPosition selectedPosition) {
		if(board.isInBounds(selectedPosition)) {
			selectedPiece = getCurrentCorp().getMemberAt(selectedPosition);			
			if(selectedPiece != null) {
				possibleMoves = getMoves();
				possibleCaptures = getCaptures();
				board.updateBoardColors(getCurrentCorp().getMemberPositions(), possibleMoves, possibleCaptures);
				return true;
			}
		}
		return false;
	}
	
	
	/*check all opposing corps member locations and return if
	 * the selected position contains an enemy piece
	 */
	private boolean selectEnemyPiece(BoardPosition selectedPosition) {
		ArrayList<Corp> enemyCorps = new ArrayList<Corp>();
		if(turn == 0)
			enemyCorps.addAll(p2_corps);
		else
			enemyCorps.addAll(p1_corps);
		
		for(Corp enemyCorp : enemyCorps) {
			if(!enemyCorp.isActive()) continue;
			//save ref to enemyCorp to remove
			currentEnemyCorp = enemyCorp;
			selectedEnemy = enemyCorp.getMemberAt(selectedPosition);
			if(selectedEnemy != null) return true;
		}
		return false;
	}
	
	public boolean makeMove(BoardPosition newPosition) {
		// check to make sure there is a selected piece
		if(selectedPiece != null) {
			BoardPosition oldPosition = new BoardPosition(selectedPiece.getPosition().getX(), selectedPiece.getPosition().getY());
			if(possibleMoves.contains(newPosition)) {
				movePiece(oldPosition, newPosition);
				return true;
			}
			if(possibleCaptures.contains(newPosition)) {
				selectEnemyPiece(newPosition);
				if(capturePiece()) {
					movePiece(oldPosition, newPosition);
					return true;
				}
			}
		}
		//need to reset board incase move not possible
		return false;
	}

	//for pathfinding use
	private class GameNode{
		public GameBoard state;
		public GameNode parent;
		public ChessPiece currentPiece;
		public int depth;
		
		public GameNode(GameBoard state, ChessPiece currentPiece, GameNode parent) {
			this.state = state;
			this.currentPiece = currentPiece;
			this.parent = parent;
			depth = parent == null ? 0 : parent.depth + 1;
		}
		
		@Override
		public int hashCode() {
			int result = state.hashCode();
			result = 31 * result + currentPiece.hashCode();
			result = 31 * result + Integer.hashCode(depth);
			return result;
		}
		
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof GameNode)) 
				return false;
			GameNode other = (GameNode)o;
			return (state.equals(other.state) && currentPiece.equals(other.currentPiece)); 
		}
	}
	
	//creates a new gamenode for pathfinding algorithm
	private GameNode getNode(GameNode parent, BoardPosition action) {
		if(parent == null || action == null) {
			return new GameNode(board.copy(), selectedPiece.copy(), null);
		}
		
		GameBoard newState = parent.state.copy();
		ChessPiece piece = parent.currentPiece.copy();
		
		if(newState.isInBounds(action) && !newState.isOccupied(action)){
			newState.updateBoardState(piece.getPosition(), action);
			piece.setPosition(action);
			return new GameNode(newState, piece, parent);
		}		
		return null;
	}

	//todo - add meaningful comments
	//also - update to include available captures as well
	private ArrayList<BoardPosition> getMoves() {
		GameNode root = new GameNode(board.copy(), selectedPiece.copy(), null);
		Queue<GameNode> frontier = new LinkedList<GameNode>();
		frontier.add(root);
		HashSet<BoardPosition> explored = new HashSet<BoardPosition>();
		int maxDepth = root.currentPiece.getMoveCount();

		while(!frontier.isEmpty()) {
			GameNode curNode = frontier.remove();
			if(curNode.depth > maxDepth) break;   
			if(!curNode.state.equals(root.state)) //dont want to count start position
				explored.add(curNode.currentPiece.getPosition());
			for(int i = 0; i < curNode.currentPiece.getActions().size(); i++) {
				BoardPosition curAction = curNode.currentPiece.getActions().get(i);
				GameNode child = getNode(curNode, curAction);
				if((child != null) && !(frontier.contains(child)) && !(explored.contains(curAction))) {
					frontier.add(child);
				}
			}
		}
		return new ArrayList<BoardPosition>(explored);
	}
	
	private ArrayList<BoardPosition> getCaptures(){
		ArrayList<BoardPosition> possibleCaptures = new ArrayList<BoardPosition>();
		ArrayList<Corp> enemyCorps = turn == 0 ? p2_corps : p1_corps;
		if(selectedPiece.getid() == 'n' || selectedPiece.getid() == 'N') {
			for(BoardPosition move : possibleMoves) {
				//more than one space away - set flag
				if(!selectedPiece.getActions().contains(move)) {
					diceOffset = true;
				}
				
			}
			//do knight stuff
			//make sure to make a dice offset if captures are more than 1 space away
		}
		else if(selectedPiece.getid() == 'r' || selectedPiece.getid() == 'R') {
			//do rook stuff
		}
		else {
			for(BoardPosition p : selectedPiece.getActions()) {
				for(Corp enemyCorp : enemyCorps) {
					if(enemyCorp.getMemberAt(p) != null) {
						possibleCaptures.add(p);
					}
				}
			}
		}
		
		return possibleCaptures;
	}

	private void movePiece(BoardPosition oldPosition, BoardPosition newPosition) {
		selectedPiece.setPosition(newPosition);
		board.updateBoardState(oldPosition, newPosition);
	}	
	
	//update
	private boolean capturePiece() {
		int[] neededRolls = selectedPiece.getRolls(selectedEnemy);
		lastRoll = Math.abs((dice.nextInt() % 6) + 1);
		System.out.println(lastRoll);	
		for(int x = 0; x < neededRolls.length; x++) {
			if(neededRolls[x] == lastRoll) {
				captureResult = "Won";
				currentEnemyCorp.removeMember(selectedEnemy);
				if(turn == 0)
					p1_captures.add(selectedEnemy);
				else 
					p2_captures.add(selectedEnemy);
				if(selectedEnemy.getid() == 'k' || selectedEnemy.getid() == 'K') {
					gameOver = true;
				}
				return true;
			}				
		}
		captureResult = "Lost";
		return false;
	}

	public void endTurn() {
		turn = ++turn % 2;
		subturn = 0;		
		board.resetBoardColors();
		board.updateBoardColors(getCurrentCorp().getMemberPositions(), null, null);
		System.out.println("End Turn");
	}

	public void endSubturn() {
		if(++subturn == 3) {
			endTurn();
		}
		else{
			board.resetBoardColors();
			board.updateBoardColors(getCurrentCorp().getMemberPositions(), null, null);
		}
		System.out.println("End Subturn");
	}
	
	public ChessPiece getSelectedPiece() {
		return selectedPiece;
	}
	
	public ChessPiece getSelectedEnemyPiece() {
		return selectedEnemy;
	}

	public GameBoard getBoard() {
		return board;
	}
	
	public int getLastRoll() {
		return lastRoll;
	}

	public int getTurn() {
		return turn;
	}

	public int getSubTurn() {
		return subturn;
	}
	
	public String getCaptureResult() {
		return captureResult;
	}

	public ArrayList<ChessPiece> getPlayer1Captures() {
		return p1_captures;
	}

	public ArrayList<ChessPiece> getPlayer2Captures() {
		return p2_captures;
	}
}
