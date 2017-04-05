import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// Kyle Robertson

public class ThirtyTwoElementArray implements CheckersGameState {

	private String _player;
	private String[] _board;
	private String[] locations;

	public void setBoard(String[] _board) {
		this._board = _board;
	}

	public ThirtyTwoElementArray() {
		this._board = new String[33];
		this._player = PLAYER1;
	}

	public ThirtyTwoElementArray(String player, String[] board) {
		this._player = player;
		this._board = board;
	}

	// The initial constructor
	public static CheckersGameState initialState() {
		ThirtyTwoElementArray initial = new ThirtyTwoElementArray();
		// Sets the initial board
		for(int i=1; i<=32; i++)
		{
			if(i < 13)
				initial._board[i] = "b";
			if(i >= 13 && i < 21)
				initial._board[i] = null;
			if(i >= 21)
				initial._board[i] = "w";
		}
		return initial;
	}

	// Returns 'Black' or 'White', the player with the current move.
	public String player() {
		return _player.equals(PLAYER1) ? PLAYER1 : PLAYER2;
	}

	public List<Move> actions() {

		// Returns the set of legal moves in a state
		List<Move> actionList = new LinkedList<Move>();
		List<Move> requiredMoves = new LinkedList<Move>();

		for (int i = 1; i <= 32; i++) {
			if (validLocation(i)) {
				String chip = locations[i];
				if (!isCurrentPlayersChip(chip)) continue;
				boolean canMoveForward = player().equals(PLAYER1) || isKing(chip); // forward = down the board
				boolean canMoveBackward = player().equals(PLAYER2) || isKing(chip); // backward = up the board
				Move forwardLeft;
				Move forwardRight;
				Move backwardLeft;
				Move backwardRight;
				Move jumpLeft;
				Move jumpRight;
				if (canMoveForward) {
					// Forward Left
					if((i >= 5 && i <= 8) ||(i >= 13 && i <= 16) || (i >= 21 && i <= 24))
					{
						forwardLeft = standardMove(i, i + 3);
					}
					else
					{
						forwardLeft = standardMove(i, i + 4);
					}
					// Forward Right
					if((i >= 5 && i <= 8) ||(i >= 13 && i <= 16) || (i >= 21 && i <= 24))
					{
						forwardRight = standardMove(i, i + 4);
					}
					else
					{
						forwardRight = standardMove(i, i + 5);
					}
					// Jump Left
					if((i >= 5 && i <= 8) || (i >= 13 && i <= 16) || (i >= 21 && i <= 24))
					{
						jumpLeft = jumpMove(i, i + 3, i + 7);
					}
					else
					{
						jumpLeft = jumpMove(i, i + 4, i + 7);
					}
					// Jump Right
					if((i >= 5 && i <= 8) || (i >= 13 && i <= 16) || (i >= 21 && i <= 24))
					{
						jumpRight = jumpMove(i, i + 4, i + 9);
					}
					else
					{
						jumpRight = jumpMove(i, i + 5, i + 9);
					}
					if (forwardLeft != null) actionList.add(forwardLeft);
					if (forwardRight != null) actionList.add(forwardRight);
					if (jumpLeft != null) {
						actionList.add(jumpLeft);
						requiredMoves.add(jumpLeft);
					}
					if (jumpRight != null) {
						actionList.add(jumpRight);
						requiredMoves.add(jumpRight);
					}
				}

				if (canMoveBackward) {
					// Backward Left
					if((i >= 5 && i <= 8) ||(i >= 13 && i <= 16) || (i >= 21 && i <= 24))
					{
						backwardLeft = standardMove(i, i - 5);
					}
					else
					{
						backwardLeft = standardMove(i, i - 4);
					}
					// Backward Right
					if((i >= 5 && i <= 8) ||(i >= 13 && i <= 16) || (i >= 21 && i <= 24))
					{
						backwardRight = standardMove(i, i - 4);
					}
					else
					{
						backwardRight = standardMove(i, i - 3);
					}
					// Jump Left
					if((i >= 5 && i <= 8) ||(i >= 13 && i <= 16) || (i >= 21 && i <= 24))
					{
						jumpLeft = jumpMove(i, i - 5, i - 9);
					}
					else
					{
						jumpLeft = jumpMove(i, i - 4, i - 9);
					}
					// Jump Right
					if((i >= 5 && i <= 8) ||(i >= 13 && i <= 16) || (i >= 21 && i <= 24))
					{
						jumpRight = jumpMove(i, i - 4, i - 7);
					}
					else
					{
						jumpRight = jumpMove(i, i - 3, i - 7);
					}
					if (backwardLeft != null) actionList.add(backwardLeft);
					if (backwardRight != null) actionList.add(backwardRight);
					if (jumpLeft != null) {
						actionList.add(jumpLeft);
						requiredMoves.add(jumpLeft);
					}
					if (jumpRight != null) {
						actionList.add(jumpRight);
						requiredMoves.add(jumpRight);
					}
				}
			}
		}
		return actionList;
	}

	public CheckersGameState result(Move x) {
		ThirtyTwoElementArray newState = cloneMe();

		// test for invalid moves...
		if (x.using2DArray) return null; // this implementation uses a 1D array
		if (!x.playerMakingMove.equals(player())) return null; // player cannot make a move if it's not his turn
		int fromLocationIndex = x.fromLocation[0];
		int toLocationIndex = x.toLocation[0];
		if (player().equals(PLAYER1)) {
			// Black's move, so chip at 'fromLocationIndex' must be either 'b' or 'B'
			if (!(locations[fromLocationIndex] == "b" || locations[fromLocationIndex] == "B")) return null;
		} 
		else {
			// Red's move, so chip at 'fromLocationIndex' must be either 'w' or 'W'
			if (!(locations[fromLocationIndex] == "w" || locations[fromLocationIndex] == "W")) return null;
		}

		// validated move, so update the newState to reflect changes
		String chipBeingMoved = newState.locations[fromLocationIndex];
		newState.locations[fromLocationIndex] = null; // old location is now empty
		newState.locations[toLocationIndex] = chipBeingMoved; // new location gets the chip
		// chars are ASCII-based, so subtracting 32 effectively capitalizes 'b' or 'w' if the chip is getting kinged
		if (x.movedChipBecomesKing)
			{
				char c = newState.locations[toLocationIndex].charAt(0);
				c -= 32;
				newState.locations[toLocationIndex] = String.valueOf(c);
			}
		// turn locations where chips were removed into blank locations
		for (Integer[] removedLocation : x.removedChips) {
			newState.locations[removedLocation[0]] = null;
		}
		// reverse who's move it is
		newState._player = newState._player.equals(PLAYER1) ? new String(PLAYER2) : new String(PLAYER1);
		return newState;
	}

	// Way more complicated than it probably needs to be
	public void printState() {
		System.out.println();

		int counter = 1;
		for(double i=1; i<=64; i++)
		{
			if(counter % 2 == 1)
			{
				if(i % 2 == 0)
				{
					System.out.print(this._board[(int) (i/2)]);
					System.out.print("\t");
				}
				else
				{
					System.out.print("-");
					System.out.print("\t");
				}
			}
			else if(counter % 2 == 0)
			{
				if(i % 2 == 1)
				{
					System.out.print(this._board[(int) ((i/2) + 0.5)]);
					System.out.print("\t");
				}
				else
				{
					System.out.print("-");
					System.out.print("\t");
				}
			}

			if(i == 8 || i == 16 || i == 24 || i == 32 || i == 40 || i == 48 || i == 56 || i == 64)
			{
				System.out.println();
				counter++;
			}
		}
		System.out.println();
		System.out.println(player() + "'s move");
	}

	// checks that the location is within bounds
	private static boolean validLocation(int loc) {
		if (loc < 1 || loc > 32) return false;
		return true;
	}

	// true if the chip character passed in belongs to the player who's move it is
	private boolean isCurrentPlayersChip(String chip) {
		// player 1's turn (black) and chip is black
		if (player().equals(PLAYER1) && (chip == "b" || chip == "B")) return true;
		// player 2's turn (red) and chip is white
		if (player().equals(PLAYER2) && (chip == "w" || chip == "W")) return true;
		return false;
	}

	// true if the chip character passed in belongs to the player who's move it ISN'T
	private boolean isOpponentPlayersChip(String chip) {
		// player 1's turn (black) and chip is red
		if (player().equals(PLAYER1) && (chip == "w" || chip == "W")) return true;
		// player 2's turn (red) and chip is black
		if (player().equals(PLAYER2) && (chip == "b" || chip == "B")) return true;
		return false;
	}

	// true if the chip character is a king
	private boolean isKing(String chip) {
		return chip == "B" || chip == "W";
	}

	private static boolean shouldKing(String player, int location) {
		// black player has reached the other end of the board and should be kinged
		if (player.equals(PLAYER1) && location >= 29 && location <= 32) return true;
		// red player has reached the other end of the board and should be kinged
		if (player.equals(PLAYER2) && location >= 1 && location <= 4) return true;
		return false;
	}

	// standard move involving no jumps (can be 1 step in any of the 4 diagonals)
	private Move standardMove(int start, int finish) {
		if (validLocation(finish) && locations[finish] == null) 
		{
			Move move = new Move(player(), false, new int[] { start }, new int[] { finish }, null, shouldKing(player(), finish));
			return move;
		}
		return null;
	}

	// move involving a jump over 1 of the opponent's pieces (in any of the 4 diagonals)
	private Move jumpMove(int start, int jumped, int finish) {
		if (validLocation(jumped) && validLocation(finish)) {
			if (isOpponentPlayersChip(locations[jumped]) && locations[finish] == null) {
				ArrayList<Integer[]> taken = new ArrayList<Integer[]>();
				taken.add(new Integer[] { jumped });
				Move move = new Move(player(), false, new int[] { start }, new int[] { finish }, taken, shouldKing(player(), finish));
				return move;
			}
		}
		return null;
	}

	private ThirtyTwoElementArray cloneMe() {
		// returns a game state configured exactly like the current one, but a different object
		// that way the clone can be modified without modifying the original state (used in result() method)
		ThirtyTwoElementArray duplicate = new ThirtyTwoElementArray();
		for (int i = 0; i < locations.length; i++) {
			duplicate.locations[i] = locations[i];
		}
		duplicate._player = new String(player());
		return duplicate;
	}
}