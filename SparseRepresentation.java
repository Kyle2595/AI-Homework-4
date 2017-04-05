import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


//Rahul Jevisetty
//Added onto code by Trevor Phillips
//Sparse Representation using 35 element array

public class SparseRepresentation implements CheckersGameState{

	List<SparsePiece<Character,Integer>> pieceList = new ArrayList<SparsePiece<Character,Integer>>(); //arrayList used to represent pieces
	//pieces are represented as a tuple -- (type of piece, location of piece)
	private static List<Integer> invalidLocations = new LinkedList<Integer>(Arrays.asList(9, 18, 27));  //locations where a piece cannot exist in a 35 element array representation
	private String player = new String(PLAYER1);;

	public static CheckersGameState initialState() {
		SparseRepresentation initial = new SparseRepresentation();
		initial.player = new String(PLAYER1);
		for (int i = 1; i <= 13; i++) {
			if (validLocation(i)) {
				initial.pieceList.add(new SparsePiece<Character, Integer>('b',i)); // set initial locations of black chips
			}
		}
		
		for (int i = 23; i <= 35; i++) {
			if (validLocation(i)) {
				initial.pieceList.add(new SparsePiece<Character, Integer>('w',i));; // set initial locations of red chips
			}
		}
		
		return initial;
	}
	
	private static boolean validLocation(int loc) {
		if (loc < 1 || loc > 35) return false;
		if (invalidLocations.contains(loc)) return false;
		return true;
	}
	
	private boolean isCurrentPlayersChip(char chip) {
		// player 1's turn (black) and chip is black
		if (player.equals(PLAYER1) && (chip == 'b' || chip == 'B')) return true;
		// player 2's turn (red) and chip is red
		if (player.equals(PLAYER2) && (chip == 'w' || chip == 'W')) return true;
		return false;
	}
	
	private boolean isOpponentPlayersChip(char chip) {
		// player 1's turn (black) and chip is red
		if (player.equals(PLAYER1) && (chip == 'w' || chip == 'W')) return true;
		// player 2's turn (red) and chip is black
		if (player.equals(PLAYER2) && (chip == 'b' || chip == 'B')) return true;
		return false;
	}
	
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
	
	private SparseRepresentation cloneMe() {
		// returns a game state configured exactly like the current one, but a different object
		// that way the clone can be modified without modifying the original state (used in result() method)
		SparseRepresentation duplicate = new SparseRepresentation();
		for (int i = 0; i < pieceList.size(); i++) {
			duplicate.pieceList.add(pieceList.get(i));
		}
		duplicate.player = new String(player);
		return duplicate;
	}

	@Override
	public String player() {
		return player;
	}

	
	@Override
	public List<Move> actions() {
		List<Move> actions = new LinkedList<Move>();
		List<Move> requiredMoves = new LinkedList<Move>();
		for (int i = 1; i <= 35; i++) {
			if (validLocation(i)) {
				char chip = getPieceAt(i);
				if (!isCurrentPlayersChip(chip)) continue;
				boolean canMoveForward = player.equals(PLAYER1) || isKing(chip); // forward = down the board
				boolean canMoveBackward = player.equals(PLAYER2) || isKing(chip); // backward = up the board
				if (canMoveForward) {
					Move forwardLeft = standardMove(i, i + 4);
					Move forwardRight = standardMove(i, i + 5);
					Move jumpLeft = jumpMove(i, i + 4, i + 8);
					Move jumpRight = jumpMove(i, i + 5, i + 10);
					if (forwardLeft != null) actions.add(forwardLeft);
					if (forwardRight != null) actions.add(forwardRight);
					if (jumpLeft != null) {
						actions.add(jumpLeft);
						requiredMoves.add(jumpLeft);
					}
					if (jumpRight != null) {
						actions.add(jumpRight);
						requiredMoves.add(jumpRight);
					}
				}
				if (canMoveBackward) {
					Move backwardLeft = standardMove(i, i - 5);
					Move backwardRight = standardMove(i, i - 4);
					Move jumpLeft = jumpMove(i, i - 5, i - 10);
					Move jumpRight = jumpMove(i, i - 4, i - 8);
					if (backwardLeft != null) actions.add(backwardLeft);
					if (backwardRight != null) actions.add(backwardRight);
					if (jumpLeft != null) {
						actions.add(jumpLeft);
						requiredMoves.add(jumpLeft);
					}
					if (jumpRight != null) {
						actions.add(jumpRight);
						requiredMoves.add(jumpRight);
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
	
	private Move standardMove(int start, int finish) {
		if (validLocation(finish) && getPieceAt(finish) == ' ') {
			Move move = new Move(player, false, new int[] { start }, new int[] { finish }, null, shouldKing(player, finish));
			return move;
		}
		return null;
	}
	
	private Move jumpMove(int start, int jumped, int finish) {
		
		if (validLocation(jumped) && validLocation(finish)) {
			if (isOpponentPlayersChip(getPieceAt(jumped)) && getPieceAt(finish) == ' ') {
				ArrayList<Integer[]> taken = new ArrayList<Integer[]>();
				taken.add(new Integer[] { jumped });
				Move move = new Move(player, false, new int[] { start }, new int[] { finish }, taken, shouldKing(player, finish));
				return move;
			}
		}
		return null;
	}
	
	public char getPieceAt(int location) {
		char c = ' ';
    	for(int i = 0; i < pieceList.size(); i++) {
    		if (pieceList.get(i).getLocation() == location) {
    			c = pieceList.get(i).getType();
    		}
    	}
    	return c;
    }
	
	public void removePieceAt(int location) {
    	for(int i = 0; i < pieceList.size(); i++) {
    		if (pieceList.get(i).getLocation() == location) {
    			pieceList.remove(i);
    		}
    	}
    }

	@Override
	public CheckersGameState result(Move x) {
		SparseRepresentation newState = cloneMe();
		if (x.using2DArray) return null; // this implementation uses a 1D array
		if (!x.playerMakingMove.equals(player)) return null; // player cannot make a move if it's not his turn
		int fromLocationIndex = x.fromLocation[0];
		int toLocationIndex = x.toLocation[0];
		if (player.equals(PLAYER1)) {
			// Black's move, so chip at 'fromLocationIndex' must be either 'b' or 'B'
			if (!(getPieceAt(fromLocationIndex) == 'b' || getPieceAt(fromLocationIndex) == 'B')) return null;
		} else {
			// Red's move, so chip at 'fromLocationIndex' must be either 'w' or 'W'
			if (!(getPieceAt(fromLocationIndex) == 'w' || getPieceAt(fromLocationIndex) == 'W')) return null;
		}
		// validated move, so update the newState to reflect changes
		char chipBeingMoved = newState.getPieceAt(fromLocationIndex);
		newState.removePieceAt(fromLocationIndex); // old piece-location pair is removed 
		newState.pieceList.add(new SparsePiece<Character,Integer>(chipBeingMoved,toLocationIndex)); // new piece-location pair added to list
		// chars are ASCII-based, so subtracting 32 effectively capitalizes 'b' or 'w' if the chip is getting kinged
		if (x.movedChipBecomesKing) {
			newState.removePieceAt(toLocationIndex);
			newState.pieceList.add(new SparsePiece<Character,Integer>(chipBeingMoved-=32,toLocationIndex));
		}
		// remove piece-location pairs from list where pieces were removed
		for (Integer[] removedLocation : x.removedChips) {
			newState.removePieceAt(removedLocation[0]);
		}
		// reverse who's move it is
		newState.player = newState.player.equals(PLAYER1) ? new String(PLAYER2) : new String(PLAYER1);
		return newState;
	}

	@Override
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
			if (leadingBlank) boardRepresentation += "- " + getPieceAt(i) + " ";
			else boardRepresentation += getPieceAt(i) + " - ";
			count++;
		}
		boardRepresentation += "\n";
		System.out.println(boardRepresentation + player + "'s move\n********************");
	}
	
	
}
