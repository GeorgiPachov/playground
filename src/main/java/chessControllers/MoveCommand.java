package chessControllers;
import chessGame.StandardBoard;
public class MoveCommand {

	private final StandardBoard board;
	int xDestination;
	int yDestination;
	int xOrigin;
	int yOrigin;
	private int enemyRemoved;

	public MoveCommand(StandardBoard board, int[] move){
		this.board = board;
		this.xOrigin = move[0];
		this.yOrigin = move[1];
		this.xDestination = move[2];
		this.yDestination = move[3];
	}
	
	public void undo() {
        // update caches
        TurnColor color = board.getColor(xDestination, yDestination);
        int[] king = board.getKing(color);


        // execute move
		int pieceToUndo = board.pieces[xDestination][yDestination];
		board.pieces[xOrigin][yOrigin] = pieceToUndo;
		board.pieces[xDestination][yDestination] = enemyRemoved; // most of the time = 0

        if (xDestination == king[0] && yDestination == king[1]) {
            king[0] = xOrigin;
            king[1] = yOrigin;
        }

	}
	
	public void execute() {
        // update caches
        TurnColor color = board.getColor(xOrigin, yOrigin);
        int[] king = board.getKing(color);


        // execute move
        int pieceToMove = board.pieces[xOrigin][yOrigin];
		board.pieces[xOrigin][yOrigin] = 0;
		this.enemyRemoved = board.pieces[xDestination][yDestination];
		board.pieces[xDestination][yDestination] = pieceToMove;

        if (xOrigin == king[0] && yOrigin == king[1]) {
            king[0] = xDestination;
            king[1] = yDestination;
        }


	}
}
