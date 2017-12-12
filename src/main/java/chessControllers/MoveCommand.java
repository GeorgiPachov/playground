package chessControllers;
import chessGame.StandardBoard;

import java.util.Arrays;

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
        TurnColor undoingPieceColor = board.getColor(xDestination, yDestination);
        int[] king = board.getKing(undoingPieceColor);
		int[] coords = {xDestination, yDestination};
		switch (undoingPieceColor) {
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

		// restore check cache
		int[] oppositeKing = board.getKing(undoingPieceColor.opposite());
		boolean isInCheck = board.isValidMove(xOrigin,yOrigin,oppositeKing[0], oppositeKing[1]);
		switch (undoingPieceColor.opposite()) {
			case black:
				board.blackKingInCheck = isInCheck;
				break;
			case white:
				board.whiteKingInCheck = isInCheck;
				break;
		}

		// restore king cache
        if (xDestination == king[0] && yDestination == king[1]) {
            king[0] = xOrigin;
            king[1] = yOrigin;
        }

        // restore enemy to opponent pieces's cache
		if (enemyRemoved!=0) {
			int[] enemyPieceCoordiantes = {xDestination, yDestination};
			switch (undoingPieceColor) {
				case white:
					board.blackPieces.put(Arrays.hashCode(enemyPieceCoordiantes), enemyPieceCoordiantes);
					break;
				case black:
					board.whitePieces.put(Arrays.hashCode(enemyPieceCoordiantes), enemyPieceCoordiantes);
					break;
			}
		}

		// restore coordinates cache
		int[] newCoordinates = {xOrigin, yOrigin};
		switch (undoingPieceColor) {
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
        TurnColor movingPieceColor = board.getColor(xOrigin, yOrigin);
        int[] king = board.getKing(movingPieceColor);
		switch (movingPieceColor) {
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

		// update is in check cache
		int[] oppositeKing = board.getKing(movingPieceColor.opposite());
		boolean isInCheck = board.isValidMove(xDestination,yDestination,oppositeKing[0], oppositeKing[1]);
		switch (movingPieceColor.opposite()) {
			case black:
				board.blackKingInCheck = isInCheck;
				break;
			case white:
				board.whiteKingInCheck = isInCheck;
				break;
		}

		// update king cache
        if (xOrigin == king[0] && yOrigin == king[1]) {
            king[0] = xDestination;
            king[1] = yDestination;
        }

        // remove enemy from opposite pieces cache
		if (enemyRemoved!=0) {
			switch (movingPieceColor) {
				case white:
					board.blackPieces.remove(Arrays.hashCode(new int[] {xDestination, yDestination}));
					break;
				case black:
					board.whitePieces.remove(Arrays.hashCode(new int[] {xDestination, yDestination}));
					break;
			}
		}

		// update figures cache
		int[] coordinates = {xDestination, yDestination};
		switch (movingPieceColor) {
			case black:
				board.blackPieces.put(Arrays.hashCode(coordinates), coordinates);
				break;
			case white:
				board.whitePieces.put(Arrays.hashCode(coordinates), coordinates);
				break;
		}


	}
}
