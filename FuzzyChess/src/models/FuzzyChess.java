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
	private Corp p1_lbishop_corp;
	private Corp p1_rbishop_corp;
	private Corp p1_king_corp;
	private Corp p2_lbishop_corp;
	private Corp p2_rbishop_corp;
	private Corp p2_king_corp;
	private ArrayList<ChessPiece> p1_captures;
	private ArrayList<ChessPiece> p2_captures;
	private GameBoard board;
	private int turn;
	private int subturn;
	private boolean gameOver;

	private ChessPiece selectedPiece;
	private ArrayList<BoardPosition> possibleMoves;
	private ArrayList<BoardPosition> possibleCaptures;
	
	//todo for captures
	private Random dice;

	public FuzzyChess() {
		gameOver = false;
		board = new GameBoard();
		initCorps();
		p1_captures = new ArrayList<ChessPiece>();
		p2_captures = new ArrayList<ChessPiece>();
		turn = 0;
		subturn = 0;
		selectedPiece = null;
		board.updateBoardColors(getCurrentCorp().getMemberPositions(), null, null);
	}

	public void quitGame() {
		gameOver = true;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	//corps grab pieces from the board
	private void initCorps() {
		char[][] gameBoard = board.getBoardState();
		p1_king_corp = new Corp(null);
		p2_king_corp = new Corp(null);
		p1_lbishop_corp = new Corp(p1_king_corp);
		p2_lbishop_corp = new Corp(p2_king_corp);
		p1_rbishop_corp = new Corp(p1_king_corp);
		p2_rbishop_corp = new Corp(p2_king_corp);

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
	}

	public void resetGame() {
		gameOver = false;
		board.resetBoardColors();
		board.resetBoardState();
		p1_lbishop_corp.removeAll();
		p1_rbishop_corp.removeAll();
		p1_king_corp.removeAll();
		p2_lbishop_corp.removeAll();
		p2_rbishop_corp.removeAll();
		p2_king_corp.removeAll();
		initCorps();
		p1_captures = new ArrayList<ChessPiece>();
		p2_captures = new ArrayList<ChessPiece>();
		turn = 0;
		subturn = 0;
	}

	/* as of now - corps go in order */
	public Corp getCurrentCorp() {
		if (turn == 0) {
			if (subturn == 0)
				return p1_lbishop_corp;
			else if (subturn == 1)
				return p1_rbishop_corp;
			else if (subturn == 2)
				return p1_king_corp;
		} else if (turn == 1) {
			if (subturn == 0)
				return p2_lbishop_corp;
			else if (subturn == 1)
				return p2_rbishop_corp;
			else if (subturn == 2)
				return p2_king_corp;
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
	
	public boolean makeMove(BoardPosition newPosition) {
		// check to make sure there is a selected piece
		if(selectedPiece != null) {
			BoardPosition oldPosition = new BoardPosition(selectedPiece.getPosition().getX(), selectedPiece.getPosition().getY());
			if(possibleMoves.contains(newPosition)) {
				selectedPiece.setPosition(newPosition);
				movePiece(oldPosition, newPosition);
				return true;
			}
		}
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

	//todo - add meaningful comment
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
		if(selectedPiece.getid() == 'n' || selectedPiece.getid() == 'N') {
			//do knight stuff
		}
		else if(selectedPiece.getid() == 'r' || selectedPiece.getid() == 'R') {
			//do rook stuff
		}
		else {
			for(BoardPosition p : selectedPiece.getActions()) {
				//check if this position contains an enemy
			}
		}
		
		return null;
	}

	private void movePiece(BoardPosition oldPosition, BoardPosition newPosition) {
		board.updateBoardState(oldPosition, newPosition);
	}	
	
	//update
	private boolean capturePiece(BoardPosition attacker, BoardPosition defender) {
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

	public GameBoard getBoard() {
		return board;
	}

	public int getTurn() {
		return turn;
	}

	public int getSubTurn() {
		return subturn;
	}

	public ArrayList<ChessPiece> getPlayer1Captures() {
		return p1_captures;
	}

	public ArrayList<ChessPiece> getPlayer2Captures() {
		return p2_captures;
	}
}
