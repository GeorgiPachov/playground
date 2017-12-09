package chessGame;

import chessControllers.TurnColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Subclass of a Piece specific to a Queen. This handles all movements the queen is capable
 * of making.
 * @author Pratik Naik
 */
public class Queen extends Piece {

	/**
	 * Queen constructor initializes name of piece to Queen. Every other parameter is
	 * initialized by superclass.
	 * @param initX
	 * @param initY
	 * @param turnColor
	 * @param board
	 */
	public Queen(int initX, int initY, TurnColor turnColor, StandardBoard board) {
		super(initX, initY, turnColor, board);
		this.nameOfPiece = "queen";
	}

	@Override
	public void addAllowedMoves(List<Integer> moves) {
		int[][] possibleDirections = new int[][] {
				// bishop
				{-1, -1},
				{-1, 1},
				{1, -1},
				{1, 1},

				// rook
				{-1, 0},
				{1, 0},
				{0, -1},
				{0, 1}
		};

		for (int[] direction: possibleDirections) {
			int xStep = direction[0];
			int yStep = direction[1];

			int nx = xLocation + xStep;
			int ny = yLocation + yStep;
			while (canMove(nx, ny)) {
				moves.add(xLocation);
				moves.add(yLocation);
				moves.add(nx);
				moves.add(ny);
				nx+=xStep;
				ny+=yStep;
			}
		}
	}

	/**
	 * Queen specific implementation of abstract method.
	 * @see Piece#isValidSpecialMove(int, int)
	 * @param newX
	 * @param newY
	 * @return boolean true if valid special move
	 */
	@Override
	boolean isValidSpecialMove(int newX, int newY) {
		int xDisplacement = newX - xLocation;
		int yDisplacement = newY - yLocation;
		if(isValidQueenMove(xDisplacement, yDisplacement)){
			int steps = Math.max(Math.abs(xDisplacement), Math.abs(yDisplacement));
			int xDirection = xDisplacement/steps;
			int yDirection = yDisplacement/steps;
			// Check for obstacles in path of Queen.
			for(int i = 1; i < steps; i++){
				Piece pieceToCheck = currentBoard.pieces[xLocation + i*xDirection][yLocation + i*yDirection];
				if(pieceToCheck!=null)
					return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * Helper method for Queen specific moves (Diagonal + Vertical + Horizontal)
	 * @param xDisplacement
	 * @param yDisplacement
	 * @return boolean true if valid queen move
	 */
	private boolean isValidQueenMove(int xDisplacement, int yDisplacement) {
		// Diagonal movement.
		if((Math.abs(xDisplacement) == Math.abs(yDisplacement)) && xDisplacement != 0)
			return true;
		else{
			// Horizontal movement
			if(xDisplacement != 0 && yDisplacement == 0)
				return true;
			// Vertical movement
			else if(xDisplacement == 0 && yDisplacement != 0)
				return true;
			else
				return false;
		}
	}

}
