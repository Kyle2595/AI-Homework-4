import java.util.ArrayList;

public class Move {

	String playerMakingMove; // either 'Black' or 'White'
	boolean using2DArray; // true for '8 x 8 Array' and 'Sparse' representations
	int[] fromLocation; // the current location of the piece being moved
	int[] toLocation; // the location the piece being moved will end up after this move
	ArrayList<Integer[]> removedChips; // ArrayList of locations containing chips that will be removed by this move
	boolean movedChipBecomesKing; // true if, after this move, the chip being moved should be kinged
	
	public Move(String player, boolean twoD, int[] from, int[] to, ArrayList<Integer[]> removed, boolean kinged) {
		if (player.equals(CheckersGameState.PLAYER1) || player.equals(CheckersGameState.PLAYER2)){
			playerMakingMove = player;
		} else {
			playerMakingMove = "ERROR: Invalid player";
		}
		using2DArray = twoD;
		// if using 2D, 'fromLocation' and 'toLocation' have length 2 where the first number
		// is the row location and the second number is the column location
		// otherwise, 'fromLocation' and 'toLocation' have length 1 and have values of
		// either 1-32 or 1-35, depending on the game state representation
		fromLocation = from;
		toLocation = to;
		// each item in the 'removedChips' ArrayList is an int[] array with length either 2 or 1
		// will have length 2 if using 2D (first item contains row position and second item contains column position)
		// will have length 1 otherwise (array item will contain a number, either 1-32 or 1-35)
		removedChips = removed;
		if (removed == null) removedChips = new ArrayList<Integer[]>();
		movedChipBecomesKing = kinged;
	}
	
	// constructor used for 1D implementations
	public Move(String player, int from, int to, ArrayList<Integer> removed, boolean kinged) {
		playerMakingMove = player;
		using2DArray = false;
		fromLocation = new int[] { from };
		toLocation = new int[] { to };
		removedChips = new ArrayList<Integer[]>();
		if (removed != null) {
			for (int i : removed) removedChips.add(new Integer[] { i });
		}
		movedChipBecomesKing = kinged;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Move)) return false;
		Move other = (Move)o;
		
		if (!playerMakingMove.equals(other.playerMakingMove) || using2DArray != other.using2DArray) return false;
		if (movedChipBecomesKing != other.movedChipBecomesKing || removedChips.size() != other.removedChips.size()) return false;
		for (int i = 0; i < fromLocation.length; i++) {
			if (fromLocation[i] != other.fromLocation[i]) return false;
		}
		for (int i = 0; i < toLocation.length; i++) {
			if (toLocation[i] != other.toLocation[i]) return false;
		}
		for (int i = 0; i < removedChips.size(); i++) {
			Integer[] a1 = removedChips.get(i);
			Integer[] a2 = other.removedChips.get(i);
			if (a1.length != a2.length) return false;
			for (int j = 0; j < a1.length; j++) {
				if (a1[j] != a2[j]) return false;
			}
		}

		return true;
	}
	
	public String toString() {
		String str = "'" + playerMakingMove + "' is moving a piece from location ";
		if (using2DArray) {
			str += fromLocation[0] + "," + fromLocation[1] + " to location " + toLocation[0] + "," + toLocation[1];
		} else {
			str += fromLocation[0] + " to location " + toLocation[0];
		}
		if (movedChipBecomesKing) str += " (chip became king)";
		if (removedChips.size() == 0) return str;
		str += "\nChips were removed from the following location(s): ";
		for (Integer[] location : removedChips) {
			if (using2DArray) str += "(" + location[0] + "," + location[1] + "), ";
			else str += location[0] + ", ";
		}
		str = str.substring(0, str.length() - 2); // remove extra comma and space characters
		return str;
	}
	
}