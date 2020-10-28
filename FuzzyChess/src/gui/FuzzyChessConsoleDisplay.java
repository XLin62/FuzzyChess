package gui;

//acts as somewhat of a logic testing suite
public class FuzzyChessConsoleDisplay {
	public static void displayTurn(int turn, int subturn, String board) {
		System.out.println(String.format("\nPlayer %d's turn", turn+1));
		System.out.println(String.format("Move %d: ", subturn+1));
		System.out.println("Board State:\n" + board);
	}
	
	public static void displayCurrentCorp(String members, String leader) {
		System.out.println("Current Corp Leader:\n" + leader);

		String[] memberStr = members.split("\\),");
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < memberStr.length; i++) {
			if(i % 3 == 0 && i != 0) sb.append("\n");
			if(i == memberStr.length-1)
				sb.append(memberStr[i] + ')');
			else
				sb.append(memberStr[i] + "),");
			
		}
		System.out.println("Current Corp Members:\n" + sb);
	}
	
	public static void displayRules() {
		
	}
	
	public static void displayCapturedPieces() {
		
	}
	
	public static void displayOptions() {
		System.out.println("Options:\ne: endturn\nl: show game log\nq: quit game\nc: show captured peices\nr: rules\nx, y: select tile");
	}
}
