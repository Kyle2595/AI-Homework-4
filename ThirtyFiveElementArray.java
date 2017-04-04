import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;

// Trevor Phillips

public class ThirtyFiveElementArray implements CheckersGameState {

	// 35-element array
	// a 0 at index i indicates an empty position
	// a 1 at index i indicates a black piece
	// a 2 at index i indicates a red piece
	private int[] locations;
	private static List<Integer> invalidLocations = new LinkedList<Integer>(Arrays.asList(9, 18, 27));
	private String player;
	
	public ThirtyFiveElementArray() {
		// given size 36 so we can refer to indices as they are shown in diagrams (rather than starting at 0)
		locations = new int[36];
		player = "???";
	}
	
	// returns the starting configuration of any checkers game
	public static CheckersGameState initialState() {
		ThirtyFiveElementArray initial = new ThirtyFiveElementArray();
		initial.player = new String(PLAYER1);
		for (int i = 1; i <= 13; i++) {
			if (invalidLocations.contains(i)) continue;
			initial.locations[i] = 1; // set initial locations of black chips
		}
		for (int i = 23; i <= 35; i++) {
			if (invalidLocations.contains(i)) continue;
			initial.locations[i] = 2; // set initial locations of red chips
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
		System.out.println("printState() in ThirtyFiveElementArray class still needs to be implemented");
	}
}
