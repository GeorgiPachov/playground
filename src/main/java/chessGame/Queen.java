package chessGame;
import java.util.List;
public class Queen {
	private static boolean isQueenMove(int xDisplacement, int yDisplacement) {
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

	public static boolean isValidQueenMove(StandardBoard board, int oldX, int oldY, int newX, int newY) {
		int xDisplacement = newX - oldX;
		int yDisplacement = newY - oldY;
		if(isQueenMove(xDisplacement, yDisplacement)){
			int steps = Math.max(Math.abs(xDisplacement), Math.abs(yDisplacement));
			int xDirection = xDisplacement/steps;
			int yDirection = yDisplacement/steps;
			// Check for obstacles in path of Queen.
			for(int i = 1; i < steps; i++){
				int pieceToCheck = board.pieces[oldX + i*xDirection][oldY + i*yDirection];
				if(pieceToCheck!=0)
					return false;
			}
			return true;
		}
		return false;
	}

	public static void addAllowedMoves(StandardBoard board, int[] coordinates, List<Integer> moves) {
		int xLocation = coordinates[0];
		int yLocation = coordinates[1];
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
