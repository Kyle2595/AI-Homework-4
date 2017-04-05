
public class Move {

	String playerMakingMove; // either 'black' or 'white'
	boolean using2DArray; // true for '8 x 8 Array' and 'Sparse' representations
	int[] fromLocation; // the current location of the piece being moved
	int[] toLocation; // the location the piece being moved will end up after this move
	
	public Move(String player, boolean twoD, int[] from, int[] to) {
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
	}
	
	public String toString() {
		String str = "'" + playerMakingMove + "' is moving a piece from location ";
		if (using2DArray) {
			str += fromLocation[0] + "," + fromLocation[1] + " to location " + toLocation[0] + "," + toLocation[1];
		} else {
			str += fromLocation[0] + " to location " + toLocation[0];
		}
		return str;
	}
	
}