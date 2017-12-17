package chessControllers;
import chessGame.Board;

import java.util.Arrays;

public class MoveCommand {

	private final Board board;
	int xDestination;
	int yDestination;
	int xOrigin;
	int yOrigin;
	private int enemyRemoved;

	public MoveCommand(Board board, int[] move){
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
		int[] coords = {xDestination, yDestination};
		switch (color) {
			case black:
				board.blackPieces.remove(Arrays.hashCode(coords));
				break;
			case white:
				board.whitePieces.remove(Arrays.hashCode(coords));
				break;
		}


        // execute move
		int pieceToUndo = board.pieces[xDestination][yDestination];
		board.pieces[xOrigin][yOrigin] = pieceToUndo;
		board.pieces[xDestination][yDestination] = enemyRemoved; // most of the time = 0

		// update caches
        if (xDestination == king[0] && yDestination == king[1]) {
            king[0] = xOrigin;
            king[1] = yOrigin;
        }

        // restore enemy to opponent pieces's cache
		if (enemyRemoved!=0) {
			int[] enemyPieceCoordiantes = {xDestination, yDestination};
			switch (color) {
				case white:
					board.blackPieces.put(Arrays.hashCode(enemyPieceCoordiantes), enemyPieceCoordiantes);
					break;
				case black:
					board.whitePieces.put(Arrays.hashCode(enemyPieceCoordiantes), enemyPieceCoordiantes);
					break;
			}
		}

		int[] newCoordinates = {xOrigin, yOrigin};
		switch (color) {
			case black:
				board.blackPieces.put(Arrays.hashCode(newCoordinates), newCoordinates);
				break;
			case white:
				board.whitePieces.put(Arrays.hashCode(newCoordinates), newCoordinates);
				break;
		}

	}
	
	public void execute() {
        // prepare caches
        TurnColor color = board.getColor(xOrigin, yOrigin);
        int[] king = board.getKing(color);
		switch (color) {
			case black:
				board.blackPieces.remove(Arrays.hashCode(new int[] {xOrigin, yOrigin}));
				break;
			case white:
				board.whitePieces.remove(Arrays.hashCode(new int[] {xOrigin, yOrigin}));
				break;
		}

        // execute move
        int pieceToMove = board.pieces[xOrigin][yOrigin];
		board.pieces[xOrigin][yOrigin] = 0;
		this.enemyRemoved = board.pieces[xDestination][yDestination];
		board.pieces[xDestination][yDestination] = pieceToMove;

		// update caches
        if (xOrigin == king[0] && yOrigin == king[1]) {
            king[0] = xDestination;
            king[1] = yDestination;
        }

        // remove enemy from opposite pieces cache
		if (enemyRemoved!=0) {
			switch (color) {
				case white:
					board.blackPieces.remove(Arrays.hashCode(new int[] {xDestination, yDestination}));
					break;
				case black:
					board.whitePieces.remove(Arrays.hashCode(new int[] {xDestination, yDestination}));
					break;
			}
		}

		int[] coordinates = {xDestination, yDestination};
		switch (color) {
			case black:
				board.blackPieces.put(Arrays.hashCode(coordinates), coordinates);
				break;
			case white:
				board.whitePieces.put(Arrays.hashCode(coordinates), coordinates);
				break;
		}


	}
}
