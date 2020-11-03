package engine;

import java.util.ArrayList;
import java.util.Scanner;

import ai.SimpleChessAgent;
import gui.FuzzyChessConsoleDisplay;
import models.BoardPosition;
import models.FuzzyChess;

/*Acts as an interface to user through the console to the game models*/
public class FuzzyChessConsoleEngine {
	private Scanner inputScanner;
	private FuzzyChess game;
	private SimpleChessAgent ai;
	
	public FuzzyChessConsoleEngine() {
		inputScanner = new Scanner(System.in);
		game = new FuzzyChess();
	}
	
	public void run() {
		System.out.println("FUZZY CHESS");
		System.out.println("enter o at piece selection for options...");
		while(!game.isGameOver()) {
			FuzzyChessConsoleDisplay.displayTurn(game.getTurn(), game.getSubTurn(), game.getBoard().toString());
			
			if(game.getTurn() == 0) {
				getPlayer1Move();
			}
			
			else if(game.getTurn() == 1) {
				//ai turn - currently just ends turn
				getPlayer2Move();
			}
		}
		
		System.out.println("Game Over!");
	}
	
	public void getPlayer1Move() {
		while(true) {
			System.out.println("Select a piece");
			System.out.print(">>>");
			String input = inputScanner.nextLine();
			
			if(input.equals("e")) {
				game.endTurn();
				break;
			}
			else if(input.contentEquals("q")) {
				game.quitGame();
				break;
			}
			else if(input.contentEquals("c")) {
				FuzzyChessConsoleDisplay.displayCapturedPieces(game.getPlayer1Captures().toString(), game.getPlayer2Captures().toString());
			}
			else if(input.contentEquals("o")) {
				FuzzyChessConsoleDisplay.displayOptions();
			}
			else if(input.contentEquals("r")) {
				FuzzyChessConsoleDisplay.displayRules();
			}
			else{
				try{
					String[] inputs = input.replace(" ", "").split(",");
					int x = Integer.parseInt(inputs[0]);
					int y = Integer.parseInt(inputs[1]);
					BoardPosition selectedPosition = new BoardPosition(x, y);
					if(game.selectPiece(selectedPosition)) {
						System.out.println(String.format("Selected %s", game.getSelectedPiece()));
						FuzzyChessConsoleDisplay.displayTurn(game.getTurn(), game.getSubTurn(), game.getBoard().toString());
						
						//Select place to move to
						System.out.println("Select place to move to.");
						input = inputScanner.nextLine();
						inputs = input.replace(" ",  "").split(",");
						x = Integer.parseInt(inputs[0]);
						y = Integer.parseInt(inputs[1]);
						selectedPosition = new BoardPosition(x, y);
						if(game.makeMove(selectedPosition)) {
							game.endSubturn();
						}
						else {
							System.out.println("\nInvalid Move\n");
						}
						//move or capture
						
						break;
					}
					else {
						System.out.println("\nNo piece selected\n");
					}
				} catch(NumberFormatException e) {
					System.out.println("\nInvalid Input\n");
				}
			}
			inputScanner.reset();
		}
		
		//wait for user input
		//check input
		//update models accordingly
	}
	
	public void getPlayer2Move() {
		//moves < 3 or turn has ended
		game.endTurn();
	}
}
