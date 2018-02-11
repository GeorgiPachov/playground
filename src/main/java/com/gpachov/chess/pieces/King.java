package com.gpachov.chess.pieces;

import com.gpachov.chess.board.Board;
import com.gpachov.chess.board.Castling;

import java.util.List;

public class King {

	private static boolean isKingMove(int xDisplacement, int yDisplacement) {
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

	public static boolean isValidKingMove(Board board, int oldX, int oldY, int newX, int newY) {
		int xDisplacement = newX - oldX;
		int yDisplacement = newY - oldY;
		// No need to check for obstacles since it's a single step move.
		if(isKingMove(xDisplacement, yDisplacement))
			return true;
		else
			return false;
	}

	public static void addAllowedMoves(Board board, int[] coordinates, List<Integer> moves) {
		int xLocation = coordinates[0];
		int yLocation = coordinates[1];
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0) {
					continue;
				}
				if (board.canMove(xLocation, yLocation,xLocation + i, yLocation + j)){
					moves.add(xLocation);
					moves.add(yLocation);
					moves.add(xLocation+i);
					moves.add(yLocation+j);
					moves.add(0);
				}
			}
		}

		Castling.addCastlingIfApplicable(board, coordinates, moves);
	}

}
