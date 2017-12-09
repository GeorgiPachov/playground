package chessGame;

import chessControllers.TurnColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Subclass of a Piece specific to a Bishop. This handles all movements the bishop is capable
 * of making.
 * @author Pratik Naik
 */
public class Bishop extends Piece {
	
	/**
	 * Bishop constructor initializes name of piece to Bishop. Every other parameter is
	 * initialized by superclass.
	 * @param initX
	 * @param initY
	 * @param turnColor
	 * @param board
	 */
	public Bishop(int initX, int initY, TurnColor turnColor, StandardBoard board) {
		super(initX, initY, turnColor, board);
		this.nameOfPiece = "bishop";
	}

	@Override
	public void addAllowedMoves(List<Integer> moves) {
		int[] steps = new int[] {-1, 1};
		for (int xStep: steps) {
			for (int yStep: steps) {
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
	}

	/**
	 * Bishop specific implementation of abstract method.
	 * @see Piece#isValidSpecialMove(int, int)
	 * @param newX
	 * @param newY
	 * @return boolean true if valid special move
	 */
	@Override
	boolean isValidSpecialMove(int newX, int newY) {
		int xDisplacement = newX - xLocation;
		int yDisplacement = newY - yLocation;
		if(isValidBishopMove(xDisplacement, yDisplacement)){
			// Total number of steps the piece has to take
			int steps = Math.abs(xDisplacement);
			int xDirection = xDisplacement/steps;
			int yDirection = yDisplacement/steps;
			// Check for obstacles in path of Bishop.
			for(int i = 1; i < steps; i++){
				Piece pieceToCheck = currentBoard.pieces[xLocation + i*xDirection][yLocation + i*yDirection];
				if(pieceToCheck != null)
					return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Helper method for Bishop specific move check (Diagonals)
	 * @param xDisplacement
	 * @param yDisplacement
	 * @return boolean 
	 */
	public static boolean isValidBishopMove(int xDisplacement, int yDisplacement) {
		if((Math.abs(xDisplacement) == Math.abs(yDisplacement)) && xDisplacement != 0)
			return true;
		return false;
	}

}
