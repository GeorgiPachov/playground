package chessGame;

/**
 * Abstract superclass for designing a new Board.
 * @author Pratik Naik
 *
 */
public abstract class Board {

	/*
	 * Common variables in Chess game board. Defined to create new types of Boards.
	 */
	public int numXSquares;
	public int numYSquares;
	public int totalSquares;
	public Square squaresList[][];

	/**
	 * Abstact method to check the boundaries of our chess board.
	 * @param xLocation
	 * @param yLocation
	 * @return boolean true if in board bounds
	 */
	abstract boolean inBoardBounds(int xLocation, int yLocation);
	
	/**
	 * Enum for TurnColor values of black or white pieces and squares
	 * @author Pratik Naik
	 */
	public enum TurnColor {
		white, black;
		
		public TurnColor opposite(){
			if(this == white)
				return black;
			else
				return white;
		}
	}
}
