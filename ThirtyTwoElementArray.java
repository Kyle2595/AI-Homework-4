import java.util.LinkedList;
import java.util.List;

// Kyle Robertson

public class ThirtyTwoElementArray implements CheckersGameState {

	private String _player;
	private String[] _board;

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
	}
}