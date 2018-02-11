package com.gpachov.chess.pieces;

import com.gpachov.chess.board.Board;
import com.gpachov.chess.board.Constants;
import com.gpachov.chess.board.MoveCommand;

import java.util.List;

public class Pawn {
	public static void addAllowedMoves(Board board, int[] coordinates, List<Integer> moves) {
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
				boolean isPromotion = (isWhite && possibleMove[1] == 7) || (isBlack && possibleMove[1] == 0);
				if (isPromotion) {
					if (isWhite && possibleMove[1] == 7) {
						// add promotion to queen
						moves.add(oldX);
						moves.add(oldY);
						moves.add(possibleMove[0]);
						moves.add(possibleMove[1]);
						moves.add(Board.WHITE_QUEEN);

						// add knight
						moves.add(oldX);
						moves.add(oldY);
						moves.add(possibleMove[0]);
						moves.add(possibleMove[1]);
						moves.add(Board.WHITE_KNIGHT);

						// add rook

						// add bishop
					} else if (isBlack && possibleMove[1] == 0) {
						// add promotion to queen
						moves.add(oldX);
						moves.add(oldY);
						moves.add(possibleMove[0]);
						moves.add(possibleMove[1]);
						moves.add(Board.BLACK_QUEEN);

						// add knight
						moves.add(oldX);
						moves.add(oldY);
						moves.add(possibleMove[0]);
						moves.add(possibleMove[1]);
						moves.add(Board.BLACK_KNIGHT);

						// add rook

						// add bishop
					}
				} else {
					moves.add(oldX);
					moves.add(oldY);
					moves.add(possibleMove[0]);
					moves.add(possibleMove[1]);
					moves.add(0);
				}
			}
		}

		addEnPassantIfApplicable(board, coordinates, moves);
	}

    private static void addEnPassantIfApplicable(Board board, int[] coordinates, List<Integer> moves) {
		if (Constants.EN_PASSANT_ENABLED) {
			boolean isWhite = board.isWhite(board.pieces[coordinates[0]][coordinates[1]]);
			boolean isBlack = board.isBlack(board.pieces[coordinates[0]][coordinates[1]]);

			// do nothing on first move;
			if (board.commandStack.isEmpty()) {
				return;
			}

			if (isWhite) {
				boolean isOnProperY = coordinates[1] == 4;
				MoveCommand prevMove = board.commandStack.peek();
				boolean prevMoveIsPawn = prevMove.getMovingPiece() == Board.BLACK_PAWN;
				boolean prevMoveXIsNextToMyPawn = Math.abs(prevMove.getXOrigin() - coordinates[0]) == 1 && Math.abs(prevMove.getXDestination() - coordinates[0]) == 1;
				boolean prevMoveIsTwoUpFront = prevMove.getYOrigin() == 6 && prevMove.getYDestination() == 4;

				boolean isWhiteEnPassanApplicable = isOnProperY && prevMoveIsPawn && prevMoveXIsNextToMyPawn && prevMoveIsTwoUpFront;
				if (isWhiteEnPassanApplicable) {
					moves.add(coordinates[0]);
					moves.add(coordinates[1]);
					moves.add(prevMove.getXDestination());
					moves.add(5); //it's fixed
					moves.add(0); //no promotion
				}
			} else if (isBlack) {
				boolean isOnProperY = coordinates[1] == 3;
				MoveCommand prevMove = board.commandStack.peek();
				boolean prevMoveIsPawn = prevMove.getMovingPiece() == Board.WHITE_PAWN;
				boolean prevMoveXIsNextToMyPawn = Math.abs(prevMove.getXOrigin() - coordinates[0]) == 1 && Math.abs(prevMove.getXDestination() - coordinates[0]) == 1;
				boolean prevMoveIsTwoUpFront = prevMove.getYOrigin() == 1 && prevMove.getYDestination() == 3;
				boolean isBlackEnPassanApplicable = isOnProperY && prevMoveIsPawn && prevMoveXIsNextToMyPawn && prevMoveIsTwoUpFront;

				if (isBlackEnPassanApplicable) {
					moves.add(coordinates[0]);
					moves.add(coordinates[1]);
					moves.add(prevMove.getXDestination());
					moves.add(2); //fixed
					moves.add(0); //no promotion
				}
			}
		}
    }

		// is on the appropriate field (line)
		// last opponent move was the pawn 2 upfront

	public static boolean isValidSpecialMove(Board board, int oldX, int oldY, int newX, int newY) {
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
	private static boolean isValidPawnMove(Board board, int oldX, int oldY, int newX, int newY) {
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

	private static boolean handlePawnFirstMove(Board board, int oldX, int oldY, int newX, int newY) {
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
			if((yDisplacement == -1 || yDisplacement == -2) && (xDisplacement == 0) && !isBlocked(board, oldX, oldY, newX, newY))
				return true;
			else if(yDisplacement == -1 && Math.abs(xDisplacement) == 1)
				return true;
			else
				return false;
		}
	}

	private static boolean isBlocked(Board board, int oldX, int oldY, int newX, int newY) {
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
