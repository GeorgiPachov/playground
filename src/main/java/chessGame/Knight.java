package chessGame;


import chessControllers.TurnColor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Subclass of a Piece specific to a Knight. This handles all movements the knight is capable
 * of making.
 * @author Pratik Naik
 */
public class Knight extends Piece {

	/**
	 * Knight constructor initializes name of piece to Knight. Every other parameter is
	 * initialized by superclass
	 * @param initX
	 * @param initY
	 * @param turnColor
	 * @param board
	 */
	public Knight(int initX, int initY, TurnColor turnColor, StandardBoard board) {
		super(initX, initY, turnColor, board);
		this.nameOfPiece = "knight";
	}

	public void addAllowedMoves(List<Integer> moves) {
		int[][] possibleMoves = new int[][] {
				{-2, 1},
				{-2, -1},
				{-1, 2},
				{-1, -2},
				{1, 2},
				{1, -2},
				{2, 1},
				{2, -1}
		};
		Arrays.stream(possibleMoves)
				.filter(move -> canMove(move[0], move[1]))
				.forEach(m -> {
					moves.add(xLocation);
					moves.add(yLocation);
					moves.add(m[0]);
					moves.add(m[1]);
				});
	}

	/**
	 * Knight specific implementation of abstract method.
	 * @see Piece#isValidSpecialMove(int, int)
	 * @param newX
	 * @param newY
	 * @return boolean true if valid special move
	 */
	@Override
	boolean isValidSpecialMove(int newX, int newY) {
		int xDisplacement = newX - xLocation;
		int yDisplacement = newY - yLocation;
		// No need to check for obstacles since knight can hop over pieces
		if(isValidKnightMove(xDisplacement, yDisplacement))
			return true;
		else
			return false;
	}
	
	/**
	 * Helper method for Knight specific move check 
	 * (2 displacement in either x or y and 1 displacement in the other)
	 * @param xDisplacement
	 * @param yDisplacement
	 * @return boolean true if valid Knight move
	 */
	public static boolean isValidKnightMove(int xDisplacement, int yDisplacement) {
		if(Math.abs(xDisplacement) == 2 && Math.abs(yDisplacement) == 1)
			return true;
		else if(Math.abs(xDisplacement) == 1 && Math.abs(yDisplacement) == 2)
			return true;
		else
			return false;
	}

}
