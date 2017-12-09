package chessGame;

import chessControllers.TurnColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Subclass of a Piece specific to a Pawn. This handles all movements the pawn is capable
 * of making.
 * @author Pratik Naik
 */
public class Pawn extends Piece {

	/**
	 * Pawn constructor initializes name of piece to pawn and sets firstMove. Every other parameter 
	 * is initialized by superclass.
	 * @param initX
	 * @param initY
	 * @param turnColor
	 * @param board
	 */
	public Pawn(int initX, int initY, TurnColor turnColor, StandardBoard board) {
		super(initX, initY, turnColor, board);
		this.nameOfPiece = "pawn";
	}

	@Override
	public List<Move> getAllowedMoves() {
		List<Move> allowedMoves = new ArrayList<>();
		int[][] possibleMovesXY = null;
		if (turnColor == TurnColor.white) {
			possibleMovesXY = new int[][] {
					{xLocation, yLocation+1},
					{xLocation, yLocation+2},
					{xLocation+1, yLocation+1},
					{xLocation-1, yLocation+1}
			};
		} else if (turnColor == TurnColor.black) {
			possibleMovesXY = new int[][] {
					{xLocation, yLocation-1},
					{xLocation, yLocation-2},
					{xLocation+1, yLocation-1},
					{xLocation-1, yLocation-1}
			};
		}
		for (int[] possibleMove : possibleMovesXY) {
			if (canMove(possibleMove[0],possibleMove[1])) {
				allowedMoves.add(new Move(xLocation, yLocation, possibleMove[0], possibleMove[1]));
			}
		}
		return allowedMoves;
	}

	/**
	 * Pawn specific implementation of abstract method.
	 * @see chessGame.Piece#isValidSpecialMove(int, int)
	 * @param newX
	 * @param newY
	 * @return boolean true if valid special move
	 */
	@Override
	boolean isValidSpecialMove(int newX, int newY) {
		int xDisplacement = newX - xLocation;
		int yDisplacement = newY - yLocation;
		if(isValidPawnMove(xDisplacement, yDisplacement)){
			Square squareToCheck = currentBoard.squaresList[xLocation + xDisplacement][yLocation + yDisplacement];
			// If the pawn moves forward the square should not be occupied
			if(xDisplacement == 0){
				if(squareToCheck.isOccupied)
					return false;
				return true;
			}
			// If the pawn moves to capture the square should be occupied
			else{
				if(squareToCheck.isOccupied)
					return true;
				return false;
			}
		}
		return false;
	}

	/**
	 * Helper method for Pawn specific move check. (First Move + Regular Move)
	 * @param xDisplacement
	 * @param yDisplacement
	 * @return boolean true if valid pawn move
	 */
	private boolean isValidPawnMove(int xDisplacement, int yDisplacement) {
		// Two steps allowed in first move
		if((this.yLocation == 6 && this.turnColor.equals(TurnColor.black)) || (this.yLocation == 1 && this.turnColor.equals(TurnColor.white))){
			return handlePawnFirstMove(xDisplacement, yDisplacement);
		}
		// Single steps allowed in future moves.
		else{
			return handleRegularPawnMove(xDisplacement, yDisplacement);
		}
	}

	/**
	 * Helper method for regular move of Pawn. (Vertical + Capture)
	 * @param xDisplacement
	 * @param yDisplacement
	 * @return boolean true if valid regular pawn move
	 */
	private boolean handleRegularPawnMove(int xDisplacement, int yDisplacement) {
		if(turnColor.equals(TurnColor.white)){
			// White capture or move upwards.
			if(yDisplacement == 1 && (xDisplacement == 0 || Math.abs(xDisplacement) == 1))
				return true;
			else
				return false;
		}
		else{
			// Black capture or move downwards.
			if(yDisplacement == -1 && (xDisplacement == 0 || Math.abs(xDisplacement) == 1))
				return true;
			else
				return false;
		}
	}

	/**
	 * Helper method for first move of Pawn. Two steps allowed.
	 * @param xDisplacement
	 * @param yDisplacement
	 * @return boolean true if valid first pawn move
	 */
	private boolean handlePawnFirstMove(int xDisplacement, int yDisplacement) {
		// White pawns can only move upwards.
		if(turnColor.equals(TurnColor.white)){
			// Two step without capture.
			if((yDisplacement == 1 || yDisplacement == 2) && (xDisplacement == 0) && !isBlocked(yDisplacement))
				return true;
			// One step plus capture.
			else if(yDisplacement == 1 && Math.abs(xDisplacement) == 1)
				return true;
			else			
				return false;
		}
		// Black pawns can only move downwards.
		else{
			if((yDisplacement == -1 || yDisplacement == -2) && (xDisplacement == 0) && !isBlocked(yDisplacement))
				return true;
			else if(yDisplacement == -1 && Math.abs(xDisplacement) == 1)
				return true;
			else
				return false;
		}
	}

	private boolean isBlocked(int yDisplacement) {
		if (yDisplacement == 1 || yDisplacement == -1) {
			if (currentBoard.squaresList[xLocation][yLocation + yDisplacement].isOccupied) {
				return true;
			}
		} else if (yDisplacement == 2 || yDisplacement == -2) {
			if (currentBoard.squaresList[xLocation][yLocation + yDisplacement/2].isOccupied) {
				return true;
			}
			if (currentBoard.squaresList[xLocation][yLocation + yDisplacement].isOccupied) {
				return true;
			}
		}
		return false;
	}
}
