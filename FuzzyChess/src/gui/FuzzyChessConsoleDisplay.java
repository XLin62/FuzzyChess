package gui;

//acts as somewhat of a logic testing suite
public class FuzzyChessConsoleDisplay {
	public static void displayTurn(int turn, int subturn, String board) {
		System.out.print(String.format("Player %d's Turn:", turn+1));
		System.out.println(String.format("\t\tMove %d: ", subturn+1));		
		System.out.println(board);
	}
	
	public static void displayRules() {
		
	}
	
	public static void displayCapturedPieces(String p1_captures, String p2_captures) {
		System.out.println("Player 1 Captures:");
		System.out.println(p1_captures);
		System.out.println("Player 2 Captures:");
		System.out.println(p2_captures);
	}
	
	public static void displayOptions() {
		System.out.println("Options:\ne: endturn\nl: show game log\nq: quit game\nc: show captured peices\nr: rules\nx, y: select tile");
	}
}
