package chessGame;

import chessControllers.TurnColor;

import java.util.List;

/**
 * Superclass Piece since all chess pieces have common variables and methods to execute.
 * Defines a standard piece and it's features.
 * @author Pratik Naik
 */
public abstract class Piece {
	String nameOfPiece;
	public TurnColor turnColor;
	Board currentBoard;
	public int xLocation;
	public int yLocation;
	
	abstract boolean isValidSpecialMove(int newX, int newY);

	public Piece(int initX, int initY, TurnColor turnColor, Board board) {
		this.turnColor = turnColor;
		this.currentBoard = board;
		this.xLocation = initX;
		this.yLocation = initY;
	}

	public abstract void addAllowedMoves(List<Integer> moves);
}


