import java.util.ArrayList;
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
			if (validLocation(i)) {
				initial.locations[i] = 'b'; // set initial locations of black chips
			}
		}
		for (int i = 14; i <= 22; i++) {
			if (validLocation(i)) {
				initial.locations[i] = ' '; // set empty locations
			}
		}
		for (int i = 23; i <= 35; i++) {
			if (validLocation(i)) {
				initial.locations[i] = 'w'; // set initial locations of red chips
			}
		}
		return initial;
	}
	
	public String player() {
		return player;
	}
	
	public List<Move> actions() {
		List<Move> actions = new LinkedList<Move>();
		List<Move> requiredMoves = new LinkedList<Move>();
		for (int i = 1; i <= 35; i++) {
			if (validLocation(i)) {
				char chip = locations[i];
				if (!isCurrentPlayersChip(chip)) continue;
				boolean canMoveForward = player.equals(PLAYER1) || isKing(chip); // forward = down the board
				boolean canMoveBackward = player.equals(PLAYER2) || isKing(chip); // backward = up the board
				if (canMoveForward) {
					Move forwardLeft = standardMove(i, i + 4);
					Move forwardRight = standardMove(i, i + 5);
					if (forwardLeft != null) actions.add(forwardLeft);
					if (forwardRight != null) actions.add(forwardRight);
				}
				if (canMoveBackward) {
					Move backwardLeft = standardMove(i, i - 5);
					Move backwardRight = standardMove(i, i - 4);
					if (backwardLeft != null) actions.add(backwardLeft);
					if (backwardRight != null) actions.add(backwardRight);
				}
				LinkedList<Move> jumps = jumpMoves(i, canMoveForward, canMoveBackward);
				if (jumps.size() > 0) {
					actions.addAll(jumps);
					requiredMoves.addAll(jumps);
				}
			}
		}
		if (requiredMoves.size() > 0) {
			actions.clear();
			// if one or more jumps are possible, one of them must be made
			// a standard non-jumping move cannot be made in this case
			for (Move action : requiredMoves) actions.add(action);
		}
		return actions;
	}
	
	// standard move involving no jumps (can be 1 step in any of the 4 diagonals)
	private Move standardMove(int start, int finish) {
		if (validLocation(finish) && locations[finish] == ' ') {
			Move move = new Move(player, false, new int[] { start }, new int[] { finish }, null, shouldKing(player, finish));
			return move;
		}
		return null;
	}
	
	private int[][] getRelativeJumps(int location) {
		int[][] relativeJumps = new int[][] {
				// start,   removed chip, finish location
				{ location, location + 4, location + 8 },
				{ location, location + 5, location + 10 },
				{ location, location - 4, location - 8 },
				{ location, location - 5, location - 10 }
		};
		return relativeJumps;
	}
	
	private LinkedList<Move> jumpMoves(int location, boolean canMoveForward, boolean canMoveBackward) {
		// WARNING: Need to account for the situation where after a jump move, a chip is crowned and can now move backwards
		int initialLoc = location;
		LinkedList<Move> jumps = new LinkedList<Move>();
		
		// test jumping over 1 chip in the 4 diagonal directions
		for (int[] jump : getRelativeJumps(location)) {
			// test if this jump is actually possible
			if (canJump(jump[0], jump[1], jump[2], canMoveForward, canMoveBackward)) {
				// if yes, we remove the chip at location[1]
				ArrayList<Integer[]> removed = new ArrayList<Integer[]>();
				removed.add(new Integer[] { jump[1] });
				// create the jump move
				Move m = new Move(player, false, new int[] { jump[0] }, new int[] { jump[2] }, 
						removed, shouldKing(player, jump[2]));
				jumps.add(m);
			}
		}
		if (jumps.size() == 0) return jumps; // no jumps possible
		
		boolean secondaryJumpsPossible = true;
		while (secondaryJumpsPossible) {
			boolean newJumpAdded = false;
			for (int i = 0; i < jumps.size(); i++) {
				Move m = jumps.get(i);
				int chipLocationAfterLastJump = m.toLocation[0];
				boolean secondaryJumpAdded = false;
				for (int[] jump : getRelativeJumps(chipLocationAfterLastJump)) {
					if (m.chipRemovedAtLocation(jump[1])) continue; // cannot jump over a chip that's already been removed
					if (canJump(jump[0], jump[1], jump[2], canMoveForward, canMoveBackward)) {
						// if yes, we create a new, updated move
						ArrayList<Integer[]> removed = new ArrayList<Integer[]>();
						for (Integer[] c : m.removedChips) removed.add(c);
						removed.add(new Integer[] { jump[1] }); // remove the chip at location[1]
						Move m2 = new Move(player, false, new int[] { initialLoc }, new int[] { jump[2] },
								removed, shouldKing(player, jump[2]));
						jumps.add(m2);
						secondaryJumpAdded = true;
						newJumpAdded = true;
					}
				}
				if (secondaryJumpAdded) jumps.remove(i--);
			}
			if (!newJumpAdded) secondaryJumpsPossible = false;
		}
		
		return jumps;
	}
	
	/*
	// move involving a jump over 1 of the opponent's pieces (in any of the 4 diagonals)
	private Move jumpMove(int start, int jumped, int finish) {
		if (validLocation(jumped) && validLocation(finish)) {
			if (isOpponentPlayersChip(locations[jumped]) && locations[finish] == ' ') {
				ArrayList<Integer[]> taken = new ArrayList<Integer[]>();
				taken.add(new Integer[] { jumped });
				Move move = new Move(player, false, new int[] { start }, new int[] { finish }, taken, shouldKing(player, finish));
				return move;
			}
		}
		return null;
	}
	*/
	/*
	private ArrayList<Move> testForChaining(ThirtyFiveElementArray initial, ArrayList<Move> combos, Move jump) {
		ThirtyFiveElementArray temp = initial.cloneMe();
		temp = (ThirtyFiveElementArray)temp.result(jump);
		temp.player = new String(initial.player); // keep the same player; testing whether this player can jump again
		
		ArrayList<Move> moves = new ArrayList<Move>();
		int i = jump.toLocation[0];
		char chip = temp.locations[i];
		boolean canMoveForward = temp.player.equals(PLAYER1) || isKing(chip); // forward = down the board
		boolean canMoveBackward = temp.player.equals(PLAYER2) || isKing(chip); // backward = up the board
		if (canMoveForward) {
			Move jumpLeft = temp.jumpMove(i, i + 4, i + 8);
			Move jumpRight = temp.jumpMove(i, i + 5, i + 10);
			if (jumpLeft != null) moves.add(jumpLeft);
			if (jumpRight != null) moves.add(jumpRight);
		}
		if (canMoveBackward) {
			Move jumpLeft = temp.jumpMove(i, i - 5, i - 10);
			Move jumpRight = temp.jumpMove(i, i - 4, i - 8);
			if (jumpLeft != null) moves.add(jumpLeft);
			if (jumpRight != null) moves.add(jumpRight);
		}
		if (moves.isEmpty()) combos.add(jump); // chaining is not possible
		
		return combos;
	}
	*/
	// checks that the location is within bounds
	private static boolean validLocation(int loc) {
		if (loc < 1 || loc > 35) return false;
		if (invalidLocations.contains(loc)) return false;
		return true;
	}
	
	// true if the chip character passed in belongs to the player who's move it is
	private boolean isCurrentPlayersChip(char chip) {
		// player 1's turn (black) and chip is black
		if (player.equals(PLAYER1) && (chip == 'b' || chip == 'B')) return true;
		// player 2's turn (red) and chip is red
		if (player.equals(PLAYER2) && (chip == 'w' || chip == 'W')) return true;
		return false;
	}
	
	// true if the chip character passed in belongs to the player who's move it ISN'T
	private boolean isOpponentPlayersChip(char chip) {
		// player 1's turn (black) and chip is red
		if (player.equals(PLAYER1) && (chip == 'w' || chip == 'W')) return true;
		// player 2's turn (red) and chip is black
		if (player.equals(PLAYER2) && (chip == 'b' || chip == 'B')) return true;
		return false;
	}
	
	// true if the chip character is a king
	private boolean isKing(char chip) {
		return chip == 'B' || chip == 'W';
	}
	
	private static boolean shouldKing(String player, int location) {
		// black player has reached the other end of the board and should be kinged
		if (player.equals(PLAYER1) && location >= 32 && location <= 35) return true;
		// red player has reached the other end of the board and should be kinged
		if (player.equals(PLAYER2) && location >= 1 && location <= 4) return true;
		return false;
	}
	
	private boolean canJump(int start, int jumped, int finish, boolean canMoveForward, boolean canMoveBackward) {
		if (!canMoveForward && (jumped > start || finish > start)) return false;
		if (!canMoveBackward && (jumped < start || finish < start)) return false;
		if (!validLocation(start) || !validLocation(jumped) || !validLocation(finish)) return false;
		return isCurrentPlayersChip(locations[start]) && isOpponentPlayersChip(locations[jumped]) && locations[finish] == ' ';
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
			else boardRepresentation += locations[i] + " - ";
			count++;
		}
		boardRepresentation += "\n";
		System.out.println(boardRepresentation + player + "'s move\n********************");
	}
}

