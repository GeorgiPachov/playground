package chessGame;

import chessControllers.TurnColor;

import java.util.List;

/**
 * Subclass of a Piece specific to a Pawn. This handles all movements the pawn is capable
 * of making.
 * @author Pratik Naik
 */
public class Pawn {

	public static void addAllowedMoves(StandardBoard board, int[] coordinates, List<Integer> moves) {
		int[][] possibleMovesXY = null;
		int oldX = coordinates[0];
		int oldY = coordinates[1];
		boolean isWhite = board.isWhite(board.pieces[coordinates[0]][coordinates[1]]);
        boolean isBlack = board.isBlack(board.pieces[coordinates[0]][coordinates[1]]);

        if (isWhite) {
			possibleMovesXY = new int[][] {
					{oldX, oldY+1},
					{oldX, oldY+2},
					{oldX+1, oldY+1},
					{oldX-1, oldY+1}
			};
		} else if (isBlack) {
			possibleMovesXY = new int[][] {
					{oldX, oldY-1},
					{oldX, oldY-2},
					{oldX+1, oldY-1},
					{oldX-1, oldY-1}
			};
		}
		for (int[] possibleMove : possibleMovesXY) {
			if (board.canMove(oldX, oldY, possibleMove[0],possibleMove[1])) {
                moves.add(oldX);
				moves.add(oldY);
				moves.add(possibleMove[0]);
				moves.add(possibleMove[1]);
			}
		}
	}
	public static boolean isValidSpecialMove(StandardBoard board, int oldX, int oldY, int newX, int newY) {
		int xDisplacement = newX - oldX;
		if(isValidPawnMove(board, oldX, oldY, newX, newY)){
			int pieceToCheck = board.pieces[newX][newY];
			// If the pawn moves forward the square should not be occupied
			if(xDisplacement == 0){
				if(pieceToCheck!=0)
					return false;
				return true;
			}
			// If the pawn moves to capture the square should be occupied
			else{
				if(pieceToCheck!= 0)
					return true;
				return false;
			}
		}
		return false;
	}

	/**
	 * Helper method for Pawn specific move check. (First Move + Regular Move)
	 * @return boolean true if valid pawn move
	 */
	private static boolean isValidPawnMove(StandardBoard board, int oldX, int oldY, int newX, int newY) {
		int xDisplacement = newX - oldX;
		int yDisplacement = newY -oldY;
		boolean isBlack = board.isBlack(board.pieces[oldX][oldY]);
		boolean isWhite = board.isWhite(board.pieces[oldX][oldY]);

        // Two steps allowed in first move
		if((oldY== 6 && isBlack || (oldY == 1 && isWhite))) {
			return handlePawnFirstMove(board, oldX, oldY, newX, newY);
		}
		// Single steps allowed in future moves.
		else{
			return handleRegularPawnMove(isWhite, xDisplacement, yDisplacement);
		}
	}

	/**
	 * Helper method for regular move of Pawn. (Vertical + Capture)
	 * @param xDisplacement
	 * @param yDisplacement
	 * @return boolean true if valid regular pawn move
	 */
	private static boolean handleRegularPawnMove(boolean isWhite, int xDisplacement, int yDisplacement) {
	    if(isWhite){
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

	private static boolean handlePawnFirstMove(StandardBoard board, int oldX, int oldY, int newX, int newY) {
        int xDisplacement = newX - oldX;
        int yDisplacement = newY - oldY;
        boolean isWhite = board.isWhite(board.pieces[oldX][oldY]);
		// White pawns can only move upwards.
		if(isWhite){
			// Two step without capture.
			if((yDisplacement == 1 || yDisplacement == 2) && (xDisplacement == 0) && !isBlocked(board, oldX, oldY, newX, newY))
				return true;
			// One step plus capture.
			else if(yDisplacement == 1 && Math.abs(xDisplacement) == 1)
				return true;
			else			
				return false;
		}
		// Black pawns can only move downwards.
		else{
			if((yDisplacement == -1 || yDisplacement == -2) && (xDisplacement == 0) && !isBlocked(board, oldX, oldY, newX, yDisplacement))
				return true;
			else if(yDisplacement == -1 && Math.abs(xDisplacement) == 1)
				return true;
			else
				return false;
		}
	}

	private static boolean isBlocked(StandardBoard board, int oldX, int oldY, int newX, int newY) {
        int xDisplacement = newX - oldX;
        int yDisplacement = newY - oldY;
		if (yDisplacement == 1 || yDisplacement == -1) {
			if (board.pieces[oldX][oldY+ yDisplacement]!=0) {
				return true;
			}
		} else if (yDisplacement == 2 || yDisplacement == -2) {
			if (board.pieces[oldX][oldY+ yDisplacement/2]!=0) {
				return true;
			}
			if (board.pieces[oldX][oldY+ yDisplacement]!=0) {
				return true;
			}
		}
		return false;
	}
}
