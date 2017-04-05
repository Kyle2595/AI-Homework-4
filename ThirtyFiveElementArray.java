import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;

// Trevor Phillips

public class ThirtyFiveElementArray implements CheckersGameState {

	// 35-element array
	// a space (' ') at index i indicates a blank location
	// a b at index i indicates a black chip
	// a B at index i indicates a black king
	// a w at index i indicates a red chip
	// a W at index i indicates a red king
	private char[] locations;
	private static List<Integer> invalidLocations = new LinkedList<Integer>(Arrays.asList(9, 18, 27));
	private String player;
	
	public ThirtyFiveElementArray() {
		// given size 36 so we can refer to indices as they are shown in diagrams (rather than starting at 0)
		locations = new char[36];
		player = "???";
	}
	
	// returns the starting configuration of any checkers game
	public static CheckersGameState initialState() {
		ThirtyFiveElementArray initial = new ThirtyFiveElementArray();
		initial.player = new String(PLAYER1);
		for (int i = 1; i <= 13; i++) {
			if (invalidLocations.contains(i)) continue;
			initial.locations[i] = 'b'; // set initial locations of black chips
		}
		for (int i = 14; i <= 22; i++) {
			if (invalidLocations.contains(i)) continue;
			initial.locations[i] = ' '; // set empty locations
		}
		for (int i = 23; i <= 35; i++) {
			if (invalidLocations.contains(i)) continue;
			initial.locations[i] = 'w'; // set initial locations of red chips
		}
		return initial;
	}
	
	public String player() {
		return player;
	}
	
	public List<Move> actions() {
		List<Move> actions = new LinkedList<Move>();
		return actions;
	}
	
	public CheckersGameState result(Move x) {
		return null;
	}
	
	public void printState() {
		String boardRepresentation = "********************\n";
		int count = 0;
		boolean leadingBlank = true;
		for (int i = 1; i <= 35; i++) {
			if (invalidLocations.contains(i)) continue;
			if (count == 4) {
				boardRepresentation += "\n";
				leadingBlank = !leadingBlank;
				count = 0;
			}
			if (leadingBlank) boardRepresentation += "- " + locations[i] + " ";
			else boardRepresentation += locations[i] + " " + "- ";
			count++;
		}
		boardRepresentation += "\n";
		System.out.println(boardRepresentation + player + "'s move\n********************");
	}
}

