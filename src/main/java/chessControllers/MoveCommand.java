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
	
	public void undo(){
		int pieceToUndo = board.pieces[xDestination][yDestination];
		board.pieces[xOrigin][yOrigin] = pieceToUndo;
		board.pieces[xDestination][yDestination] = enemyRemoved; // most of the time = 0
	}
	
	public void execute(){
		int pieceToMove = board.pieces[xOrigin][yOrigin];
		board.pieces[xOrigin][yOrigin] = 0;
		this.enemyRemoved = board.pieces[xDestination][yDestination];
		board.pieces[xDestination][yDestination] = pieceToMove;
	}
}
