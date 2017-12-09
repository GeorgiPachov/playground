package chessGame;

import chessControllers.TurnColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Subclass of a Piece specific to a King. This handles all movements the king is capable
 * of making.
 * @author Pratik Naik
 *
 */
public class King extends Piece {

	/**
	 * King constructor initializes name of piece to King. Every other parameter is
	 * initialized by superclass.
	 * @param initX
	 * @param initY
	 * @param turnColor
	 * @param board
	 */
	public King(int initX, int initY, TurnColor turnColor, StandardBoard board) {
		super(initX, initY, turnColor, board);
		this.nameOfPiece = "king";
	}
	
	/**
	 * King specific implementation of abstract method.
	 * @see chessGame.Piece#isValidSpecialMove(int, int)
	 * @param newX
	 * @param newY
	 * @return boolean true if valid special move
	 */
	@Override
	boolean isValidSpecialMove(int newX, int newY) {
		int xDisplacement = newX - xLocation;
		int yDisplacement = newY - yLocation;
		// No need to check for obstacles since it's a single step move.
		if(isValidKingMove(xDisplacement, yDisplacement))
			return true;
		else
			return false;
	}

	/**
	 * Helper method for King specific move check (One step in all directions)
	 * @param xDisplacement
	 * @param yDisplacement
	 * @return boolean true if valid King move
	 */
	private boolean isValidKingMove(int xDisplacement, int yDisplacement) {
		// Diagonal
		if(Math.abs(xDisplacement) == 1 && Math.abs(yDisplacement) == 1)
			return true;
		// Horizontal
		else if(Math.abs(xDisplacement) == 1 && Math.abs(yDisplacement) == 0)
			return true;
		// Vertical
		else if(Math.abs(xDisplacement) == 0 && Math.abs(yDisplacement) == 1)
			return true;
		else
			return false;
	}

	public void addAllowedMoves(List<Integer> moves) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0) {
					continue;
				}
				if (canMove(xLocation + i, yLocation + j)){
					moves.add(xLocation);
					moves.add(yLocation);
					moves.add(xLocation+i);
					moves.add(yLocation+j);
				}
			}
		}
	}
}
