package chessGame;
import java.util.List;

public class Bishop {
	public static boolean isValidBishopMove(Board board, int oldX, int oldY, int newX, int newY) {
		int xDisplacement = newX - oldX;
		int yDisplacement = newY - oldY;
		if(diagonalMove(xDisplacement, yDisplacement)){
			// Total number of steps the piece has to take
			int steps = Math.abs(xDisplacement);
			int xDirection = xDisplacement/steps;
			int yDirection = yDisplacement/steps;
			// Check for obstacles in path of Bishop.
			for(int i = 1; i < steps; i++){
				int pieceToCheck = board.pieces[oldX+ i*xDirection][oldY + i*yDirection];
				if(pieceToCheck != 0)
					return false;
			}
			return true;
		}
		return false;
	}

	private static boolean diagonalMove(int xDisplacement, int yDisplacement) {
		if((Math.abs(xDisplacement) == Math.abs(yDisplacement)) && xDisplacement != 0)
			return true;
		return false;
	}

	public static void addAllowedMoves(Board board, int[] coordinates, List<Integer> moves) {
		int xLocation = coordinates[0];
		int yLocation = coordinates[1];
		int[] steps = new int[] {-1, 1};
		for (int xStep: steps) {
			for (int yStep: steps) {
				int nx = xLocation + xStep;
				int ny = yLocation + yStep;
				while (board.canMove(xLocation, yLocation, nx, ny)) {
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
}
