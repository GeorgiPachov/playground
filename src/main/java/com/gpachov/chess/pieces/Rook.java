package com.gpachov.chess.pieces;

import com.gpachov.chess.board.Board;

import java.util.List;
public class Rook  {

	public static int[] WHITE_ROOK_LEFT = new int[] {0, 0};
	public static int[] WHITE_ROOK_RIGHT = new int[] {7, 0};
	public static int[] BLACK_ROOK_LEFT = new int[] {0, 7};
	public static int[] BLACK_ROOK_RIGHT = new int[] {7, 7};

	public static boolean isValidRookMove(Board board, int oldX, int oldY, int newX, int newY) {
		int xDisplacement = newX - oldX;
		int yDisplacement = newY - oldY;
		if(startightLineMove(xDisplacement, yDisplacement)){
			// Total number of steps the piece has to take. Either x = 0 or y = 0.
			int steps = Math.max(Math.abs(xDisplacement), Math.abs(yDisplacement));
			int xDirection = xDisplacement/steps;
			int yDirection = yDisplacement/steps;
			// Check for obstacles in path of Rook.
			for(int i = 1; i < steps; i++){
				int squareToCheck = board.pieces[oldX+ i*xDirection][oldY+ i*yDirection];
				if(squareToCheck!=0)
					return false;
			}
			return true;
		}
		return false;
	}

	private static boolean startightLineMove(int xDisplacement, int yDisplacement) {
		// Vertical
		if(xDisplacement != 0 && yDisplacement == 0)
			return true;
			// Horizontal
		else if(xDisplacement == 0 && yDisplacement != 0)
			return true;
		else
			return false;
	}

	public static void addAllowedMoves(Board board, int[] coordinates, List<Integer> moves) {
		int xLocation = coordinates[0];
		int yLocation = coordinates[1];
		int[][] directions= new int[][]{
				{-1, 0},
				{1, 0},
				{0, -1},
				{0, 1}
		};
		for (int[] direction : directions) {
			int xStep = direction[0];
			int yStep = direction[1];

			int nx = xLocation + xStep;
			int ny = yLocation + yStep;
			while (board.canMove(xLocation, yLocation, nx, ny)) {
				moves.add(xLocation);
				moves.add(yLocation);
				moves.add(nx);
				moves.add(ny);
				moves.add(0);
				nx+=xStep;
				ny+=yStep;
			}
		}
	}
}
