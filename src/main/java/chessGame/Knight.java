package chessGame;

import java.util.Arrays;
import java.util.List;
public class Knight {
	public static boolean isValidKnightMove(Board board, int oldX, int oldY, int newX, int newY) {
		int xDisplacement = newX - oldX;
		int yDisplacement = newY - oldY;
		// No need to check for obstacles since knight can hop over pieces
		if(isKnightMove(xDisplacement, yDisplacement))
			return true;
		else
			return false;
	}

	private static boolean isKnightMove(int xDisplacement, int yDisplacement) {
		if(Math.abs(xDisplacement) == 2 && Math.abs(yDisplacement) == 1)
			return true;
		else if(Math.abs(xDisplacement) == 1 && Math.abs(yDisplacement) == 2)
			return true;
		else
			return false;
	}

    public static void addAllowedMoves(Board board, int[] coordinates, List<Integer> moves) {
	    int xLocation = coordinates[0];
	    int yLocation = coordinates[1];
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
                .filter(move -> board.canMove(xLocation, yLocation, move[0], move[1]))
                .forEach(m -> {
                    moves.add(xLocation);
                    moves.add(yLocation);
                    moves.add(m[0]);
                    moves.add(m[1]);
                    moves.add(0);
                });
    }
}
