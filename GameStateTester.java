import java.util.concurrent.ThreadLocalRandom;
import java.util.List;

public class GameStateTester {

	public static void main(String[] args) {
		System.out.println("TESTING 8x8 Array Representation:");
		// tests
		System.out.println("\nTESTING 32-Element Array Representation:");
		CheckersGameState initialState = ThirtyTwoElementArray.initialState();
		initialState.printState();
		
		
		System.out.println("\nTESTING 35-Element Representation:");
		CheckersGameState testState = ThirtyFiveElementArray.initialState(); // starting config of checkers game
		testState.printState();
		int numSampleMoves = 6;
		for (int i = 0; i < numSampleMoves; i++) {
			List<Move> moves = testState.actions();
			if (moves.size() == 0) break;
			int randomNum = ThreadLocalRandom.current().nextInt(0, moves.size()); // random integer
			Move move = moves.get(randomNum);
			System.out.println("Random move chosen: " + move + "\nBoard state after move:");
			testState = testState.result(move);
			testState.printState();
		}
		System.out.println("\nTESTING Sparse Representation:");
		CheckersGameState ts = SparseRepresentation.initialState();
		ts.printState();
		for (int i = 0; i < numSampleMoves; i++) {
			List<Move> moves = ts.actions();
			if (moves.size() == 0) break;
			int randomNum = ThreadLocalRandom.current().nextInt(0, moves.size()); // random integer
			Move move = moves.get(randomNum);
			System.out.println("Random move chosen: " + move + "\nBoard state after move:");
			ts = ts.result(move);
			ts.printState();
		}
	}

}
