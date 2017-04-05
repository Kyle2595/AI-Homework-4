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
		ThirtyFiveElementArray newState = cloneMe();
		
		// test for invalid moves...
		if (x.using2DArray) return null; // this implementation uses a 1D array
		if (!x.playerMakingMove.equals(player)) return null; // player cannot make a move if it's not his turn
		int fromLocationIndex = x.fromLocation[0];
		int toLocationIndex = x.toLocation[0];
		if (player.equals(PLAYER1)) {
			// Black's move, so chip at 'fromLocationIndex' must be either 'b' or 'B'
			if (!(locations[fromLocationIndex] == 'b' || locations[fromLocationIndex] == 'B')) return null;
		} else {
			// Red's move, so chip at 'fromLocationIndex' must be either 'w' or 'W'
			if (!(locations[fromLocationIndex] == 'w' || locations[fromLocationIndex] == 'W')) return null;
		}
		
		// validated move, so update the newState to reflect changes
		char chipBeingMoved = newState.locations[fromLocationIndex];
		newState.locations[fromLocationIndex] = ' '; // old location is now empty
		newState.locations[toLocationIndex] = chipBeingMoved; // new location gets the chip
		// chars are ASCII-based, so subtracting 32 effectively capitalizes 'b' or 'w' if the chip is getting kinged
		if (x.movedChipBecomesKing) newState.locations[toLocationIndex] -= 32;
		// turn locations where chips were removed into blank locations
		for (Integer[] removedLocation : x.removedChips) {
			newState.locations[removedLocation[0]] = ' ';
		}
		// reverse who's move it is
		newState.player = newState.player.equals(PLAYER1) ? new String(PLAYER2) : new String(PLAYER1);
		return newState;
	}
	
	private ThirtyFiveElementArray cloneMe() {
		// returns a game state configured exactly like the current one, but a different object
		// that way the clone can be modified without modifying the original state (used in result() method)
		ThirtyFiveElementArray duplicate = new ThirtyFiveElementArray();
		for (int i = 0; i < locations.length; i++) {
			duplicate.locations[i] = locations[i];
		}
		duplicate.player = new String(player);
		return duplicate;
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

