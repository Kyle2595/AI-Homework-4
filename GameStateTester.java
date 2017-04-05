import java.util.concurrent.ThreadLocalRandom;
import java.util.List;

public class GameStateTester {

	public static void main(String[] args) {
		
		int numSampleMoves = 6; // Number of moves to make for each test
		
		System.out.println("TESTING 8x8 Array Representation:");
		// tests
		
		System.out.println("\nTESTING 32-Element Array Representation:");
		CheckersGameState testState32 = ThirtyTwoElementArray.initialState();
		testState32.printState();
		for (int i = 0; i < numSampleMoves; i++) {
			List<Move> moves32 = testState32.actions();
			if (moves32.size() == 0) break;
			int randomNum32 = ThreadLocalRandom.current().nextInt(0, moves32.size()); // random integer
			Move move = moves32.get(randomNum32);
			System.out.println("Random move chosen: " + move + "\nBoard state after move:");
			testState32 = testState32.result(move);
			testState32.printState();
		}
		
		System.out.println("\nTESTING 35-Element Representation:");
		CheckersGameState testState = ThirtyFiveElementArray.initialState(); // starting config of checkers game
		testState.printState();
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
			List<Move> mvs = ts.actions();
			if (mvs.size() == 0) break;
			int randomNum = ThreadLocalRandom.current().nextInt(0, mvs.size()); // random integer
			Move mv = mvs.get(randomNum);
			System.out.println("Random move chosen: " + mv + "\nBoard state after move:");
			ts = ts.result(mv);
			ts.printState();
		}
		
	}
}
