import java.util.LinkedList;
import java.util.List;

// Kyle Robertson

public class ThirtyTwoElementArray implements CheckersGameState {

	private String _player;
	private static String[] _board;

	public static String[] getBoard() {
		return _board;
	}

	public void setBoard(String[] _board) {
		this._board = _board;
	}

	public ThirtyTwoElementArray(String player, String[] board) {
		this._player = player;
		this._board = board;
	}

	// Returns 'black' or 'white', the player with the current move.
	public String player() {
		return _player.equals(PLAYER1) ? PLAYER1 : PLAYER2;
	}

	public List<Move> actions() {
		
		// Returns the set of legal moves in a state
		List<Move> actionList = new LinkedList<Move>();

		if (!_player.equals(PLAYER1) && !_player.equals(PLAYER2)) return null;
		
		// Pulled from other project, needs to be modified before implemented
		
//		if(getBoard()[1] != null)
//		{
//		}
//		if(getBoard()[2] != null)
//		{
//		}
//		if(getBoard()[3] != null)
//		{
//		}
//		if(getBoard()[4] != null)
//		{
//		}
//		if(getBoard()[5] != null)
//		{
//		}
//		if(getBoard()[6] != null)
//		{
//		}
//		if(getBoard()[7] != null)
//		{
//		}
//		if(getBoard()[8] != null)
//		{
//		}
//		if(getBoard()[9] != null)
//		{
//		}
//		if(getBoard()[10] != null)
//		{
//		}
//		if(getBoard()[11] != null)
//		{
//		}
//		if(getBoard()[12] != null)
//		{
//		}
//		if(getBoard()[13] != null)
//		{
//		}
//		if(getBoard()[14] != null)
//		{
//		}
//		if(getBoard()[15] != null)
//		{
//		}
//		if(getBoard()[16] != null)
//		{
//		}
//		if(getBoard()[17] != null)
//		{
//		}
//		if(getBoard()[18] != null)
//		{
//		}
//		if(getBoard()[19] != null)
//		{
//		}
//		if(getBoard()[20] != null)
//		{
//		}
//		if(getBoard()[21] != null)
//		{
//		}
//		if(getBoard()[22] != null)
//		{
//		}
//		if(getBoard()[23] != null)
//		{
//		}
//		if(getBoard()[24] != null)
//		{
//		}
//		if(getBoard()[25] != null)
//		{
//		}
//		if(getBoard()[26] != null)
//		{
//		}
//		if(getBoard()[27] != null)
//		{
//		}
//		if(getBoard()[28] != null)
//		{
//		}
//		if(getBoard()[29] != null)
//		{
//		}
//		if(getBoard()[30] != null)
//		{
//		}
//		if(getBoard()[31] != null)
//		{
//		}
//		if(getBoard()[32] != null)
//		{
//		}
		
		return actionList;
	}

	public CheckersGameState result(Move x) {
		// TODO Auto-generated method stub
		return null;
	}

	public void printState() {
		// TODO Auto-generated method stub

	}
}
