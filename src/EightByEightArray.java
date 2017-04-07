import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

// Samah Saeed
// Adaptation from ThirtyFiveElementArray (Trevor Phillips)

public class EightByEightArray implements CheckersGameState
{
	// 8x8 array representation of board
	// unoccupied squares are null
	// b - black chip, w - white chip
	// B - black king, W - white king

	private String player;
	private String[][] board;
	public static List<int[]> invalidSquares;

	public EightByEightArray()
	{
		player = PLAYER1;	// starts w/ Black by default
		board = new String[8][8];
		invalidSquares = new ArrayList<int[]>();
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				if ((i % 2) == 0 && (j % 2) == 0)
					invalidSquares.add(new int[] {i,j});
				if ((i % 2) == 1 && (j % 2) == 1)
					invalidSquares.add(new int[] {i,j});
			}
		}
	}

	public static CheckersGameState initialState() {
		EightByEightArray initial = new EightByEightArray();
		initial.player = new String(PLAYER1);
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				if (validSquare(new int[] {i,j})) {
					initial.board[i][j] = "b"; // set initial locations of black chips
				}
			}
		}

		for (int i = 5; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				if (validSquare(new int[] {i,j})) {
					initial.board[i][j] = "w"; // set initial locations of white chips
				}
			}
		}
		return initial;
	}

	@Override
	public String player()
	{
		return player;
	}

	private boolean canMoveForward(String chip) { return player.equals(PLAYER1) || isKing(chip); }
	private boolean canMoveBackward(String chip) { return player.equals(PLAYER2) || isKing(chip); }

	@Override
	public List<Move> actions()
	{
		List<Move> actions = new LinkedList<Move>();
		List<Move> requiredMoves = new LinkedList<Move>();
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				if (validSquare(new int[] {i,j}))
				{
					String chip = board[i][j];
					if (!isCurrentPlayersChip(chip)) continue;
					if (canMoveForward(chip)) {
						Move forwardLeft = standardMove(new int[] {i,j}, new int[] {i+1,j-1});
						Move forwardRight = standardMove(new int[] {i,j}, new int[] {i+1,j+1});
						if (forwardLeft != null) actions.add(forwardLeft);
						if (forwardRight != null) actions.add(forwardRight);
					}

					if (canMoveBackward(chip)) {
						Move backwardLeft = standardMove(new int[] {i,j}, new int[] {i-1,j-1});
						Move backwardRight = standardMove(new int[] {i,j}, new int[] {i-1,j+1});
						if (backwardLeft != null) actions.add(backwardLeft);
						if (backwardRight != null) actions.add(backwardRight);
					}
					LinkedList<Move> jumps = jumpMoves(new int[] {i,j});
					if (jumps.size() > 0) {
						actions.addAll(jumps);
						requiredMoves.addAll(jumps);
					}
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


	@Override
	public CheckersGameState result(Move x)
	{
		EightByEightArray newState = cloneMe();

		// test for invalid moves...
		if (!x.playerMakingMove.equals(player)) return null; // player cannot make a move if it's not his turn
		int[] fromLocation = x.fromLocation;
		int[] toLocation = x.toLocation;
		if (player.equals(PLAYER1)) {
			// Black's move, so chip at 'fromLocationIndex' must be either 'b' or 'B'
			if (!(board[fromLocation[0]][fromLocation[1]].equals("b") || board[fromLocation[0]][fromLocation[1]].equals("B"))) return null;
		} else {
			// White's move, so chip at 'fromLocationIndex' must be either 'w' or 'W'
			if (!(board[fromLocation[0]][fromLocation[1]].equals("w") || board[fromLocation[0]][fromLocation[1]].equals("W"))) return null;
		}

		// validated move, so update the newState to reflect changes
		String chipBeingMoved = newState.board[fromLocation[0]][fromLocation[1]];
		newState.board[fromLocation[0]][fromLocation[1]] = null; // old location is now empty
		newState.board[toLocation[0]][toLocation[1]] = chipBeingMoved; // new location gets the chip
		// chars are ASCII-based, so subtracting 32 effectively capitalizes 'b' or 'w' if the chip is getting kinged
		if (x.movedChipBecomesKing) 
			newState.board[toLocation[0]][toLocation[1]] = newState.board[toLocation[0]][toLocation[1]].toUpperCase();
		// turn locations where chips were removed into blank locations
		for (Integer[] removedLocation : x.removedChips) {
			newState.board[removedLocation[0]][removedLocation[1]] = null;
		}
		// reverse who's move it is
		newState.player = newState.player.equals(PLAYER1) ? new String(PLAYER2) : new String(PLAYER1);
		return newState;
	}

	// standard move involving no jumps (can be 1 step in any of the 4 diagonals)
	private Move standardMove(int[] start, int[] finish) {
		if (validSquare(finish) && board[finish[0]][finish[1]] == null) {
			Move move = new Move(player, true, start, finish, null, shouldKing(player, finish));
			return move;
		}
		return null;
	}

	private EightByEightArray cloneMe() {
		// returns a game state configured exactly like the current one, but a different object
		// that way the clone can be modified without modifying the original state (used in result() method)
		EightByEightArray duplicate = new EightByEightArray();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				duplicate.board[i][j] = board[i][j];
			}
		}
		duplicate.player = new String(player);
		return duplicate;
	}

	private LinkedList<Move> simpleJumps(int[] location) {
		int[][][] relativeJumps = new int[][][] {
			// start,   removed chip, 							  finish location
			{ location, new int[] {location[0]+1, location[1]-1}, new int[] {location[0]+2, location[1]-2} },
			{ location, new int[] {location[0]+1, location[1]+1}, new int[] {location[0]+2, location[1]+2} },
			{ location, new int[] {location[0]-1, location[1]+1}, new int[] {location[0]-2, location[1]+2} },
			{ location, new int[] {location[0]-1, location[1]-1}, new int[] {location[0]-2, location[1]-2} }
		};

		LinkedList<Move> jumps = new LinkedList<Move>();
		// test jumping over 1 chip in the 4 diagonal directions
		for (int[][] jump : relativeJumps) {
			// test if this jump is actually possible
			if (canJump(jump[0], jump[1], jump[2])) {
				// if yes, we remove the chip at location[1]
				Integer[] _jump = new Integer[] {jump[1][0], jump[1][1]};
				ArrayList<Integer[]> removed = new ArrayList<Integer[]>();
				removed.add(_jump);
				// create the jump move
				Move m = new Move(player, true, jump[0], jump[2], removed, shouldKing(player, jump[2]));
				jumps.add(m);
			}
		}
		return jumps;
	}

	private Move traceback(SearchNode node) {
		// simple properties to determine about this sequence of jump moves
		int[] endingLocation = node.fromParentToCurrent.toLocation;
		String player = node.current.player();
		EightByEightArray endState = (EightByEightArray)node.current;
		String chipAtEndState = endState.board[node.fromParentToCurrent.toLocation[0]][node.fromParentToCurrent.toLocation[1]];
		boolean kinged = chipAtEndState.equals("B") || chipAtEndState.equals("W");
		// trace back to find the original chip location and all removed chips
		int[] startingLocation = node.fromParentToCurrent.fromLocation;
		ArrayList<Integer[]> removed = new ArrayList<Integer[]>();
		for (Integer[] removedLoc : node.fromParentToCurrent.removedChips) {
			removed.add(removedLoc);
		}
		SearchNode temp = node.parent;
		while (temp != null && temp.fromParentToCurrent != null) {
			startingLocation = temp.fromParentToCurrent.fromLocation;
			for (Integer[] removedLoc : temp.fromParentToCurrent.removedChips) {
				removed.add(removedLoc);
			}
			temp = temp.parent;
		}
		return new Move(player, true, startingLocation, endingLocation, removed, kinged);
	}

	// move involving a jump over 1 of the opponent's pieces (in any of the 4 diagonals)
	private LinkedList<Move> jumpMoves(int[] location) {
		// DFS to determine all possible sequences of jump moves from a chip's given location
		LinkedList<Move> jumps = new LinkedList<Move>();

		SearchNode root = new SearchNode(null, this, null, true);
		ArrayList<SearchNode> stack = new ArrayList<SearchNode>();
		LinkedList<Move> initialJumps = simpleJumps(location);
		for (Move jump : initialJumps) {
			EightByEightArray result = (EightByEightArray)this.result(jump);
			result.player = new String(player); // do not reverse the player in this special case
			SearchNode node = new SearchNode(root, result, jump, false);
			stack.add(node);
		}
		while (!stack.isEmpty()) {
			SearchNode v = stack.remove(0);
			if (!v.visited) {
				v.visited = true;
				EightByEightArray currentState = (EightByEightArray)v.current;
				int[] newLocationOfJumpingChip = v.fromParentToCurrent.toLocation;
				LinkedList<Move> adjacencies = currentState.simpleJumps(newLocationOfJumpingChip);
				if (adjacencies.size() == 0) {
					// the last jump resulted in a terminal state allowing no more jumps
					jumps.add(traceback(v));
				} else {
					// add nodes for the DFS
					for (Move jump : adjacencies) {
						EightByEightArray result = (EightByEightArray)currentState.result(jump);
						result.player = new String(currentState.player); // do not reverse the player
						SearchNode node = new SearchNode(v, result, jump, false);
						stack.add(0, node);
					}
				}
			}
		}
		return jumps;
	}

	// true if the chip character passed in belongs to the player who's move it is
	private boolean isCurrentPlayersChip(String chip) {
		// player 1's turn (black) and chip is black
		if (chip == null) return false;
		if (player.equals(PLAYER1) && (chip.equals("b") || chip.equals("B"))) return true;
		// player 2's turn (white) and chip is white
		if (player.equals(PLAYER2) && (chip.equals("w") || chip.equals("W"))) return true;
		return false;
	}

	// true if the chip character passed in belongs to the player whose move it ISN'T
	private boolean isOpponentPlayersChip(String chip) {
		// player 1's turn (black) and chip is white
		if (chip == null) return false;
		if (player.equals(PLAYER1) && (chip.equals("w") || chip.equals("W"))) return true;
		// player 2's turn (white) and chip is black
		if (player.equals(PLAYER2) && (chip.equals("b") || chip.equals("B"))) return true;
		return false;
	}

	// true if the chip character is a king
	private boolean isKing(String chip) {
		return chip.equals("B") || chip.equals("W");
	}

	private static boolean shouldKing(String player, int[] sq) {
		// black player has reached the other end of the board and should be kinged
		if (player.equals(PLAYER1) && (Arrays.equals(sq, (new int[] {7,0})) || 
				Arrays.equals(sq, (new int[] {7,2})) || Arrays.equals(sq, (new int[] {7,4})) ||
				Arrays.equals(sq, (new int[] {7,6})))) return true;
		// white player has reached the other end of the board and should be kinged
		if (player.equals(PLAYER2) && (Arrays.equals(sq, (new int[] {0,1})) || 
				Arrays.equals(sq, (new int[] {0,3})) || Arrays.equals(sq, (new int[] {0,5})) ||
				Arrays.equals(sq, (new int[] {0,7})))) return true;
		return false;
	}

	private boolean canJump(int[] start, int[] jumped, int[] finish) {
		if (!validSquare(start) || !validSquare(jumped) || !validSquare(finish)) return false;
		if (!canMoveForward(board[start[0]][start[1]]) && (jumped[0] > start[0] || finish[0] > start[0])) return false;
		if (!canMoveBackward(board[start[0]][start[1]]) && (jumped[0] < start[0] || finish[0] < start[0])) return false;
		return isCurrentPlayersChip(board[start[0]][start[1]]) && isOpponentPlayersChip(board[jumped[0]][jumped[1]]) 
				&& (board[finish[0]][finish[1]] == null);
	}

	private static boolean validSquare(int[] sq) {
		if (sq[0] < 0 || sq[1] < 0 || sq[0] > 7 || sq[1] > 7) return false;
		if (isInList(invalidSquares, sq)) return false;
		return true;
	}

	private static boolean isInList(List<int[]> list, int[] candidate)
	{
		for (int[] item : list)
			if (Arrays.equals(item, candidate))
				return true;
		return false;
	}

	@Override
	public void printState()
	{
		System.out.println("************");
		for (int i = 0; i < 8; i ++)
		{
			for (int j = 0; j < 8; j++)
			{
				if (board[i][j] == null)
					System.out.print("-");
				else
					System.out.print(board[i][j]);
			}
			System.out.println();
		}
		System.out.println();
		System.out.println(player + "'s move");
		System.out.println("************");
	}

}
