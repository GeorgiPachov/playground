package chessGame;

import chessGame.Board.TurnColor;

/**
 * This class represents a square on the chess board.
 * @author Pratik Naik 
 */
public class Square {

	/**
	 * Common variables belonging to a chess board square
	 */
	// Square is occupied or not
	public boolean isOccupied;
	// Assign 0 as white turnColor and 1 as black turnColor
	public TurnColor turnColor;
	// Square objects keep track of which piece occupies that square.
	public Piece occupyingPiece;

	/**
	 * Constructor to initialize chess board Squares
	 * @param isOccupied
	 * @param turnColor
	 */
	public Square(boolean isOccupied, TurnColor turnColor) {
		this.isOccupied = isOccupied;
		this.turnColor = turnColor;
		this.occupyingPiece = null;
	}
}
