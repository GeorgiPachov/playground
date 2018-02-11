package com.gpachov.chess.board;

import static com.gpachov.chess.board.Board.*;

public class MoveCommand {

	private final Board board;
	private final TurnColor movingColor;
	private int promoted = 0;

	private int xDestination;
	private int yDestination;
	private int xOrigin;
	private int yOrigin;
	private int enemyRemoved;


	private int movingPiece;

	public MoveCommand(Board board, int[] move){
		this.board = board;
		this.xOrigin = move[0];
		this.yOrigin = move[1];
		this.xDestination = move[2];
		this.yDestination = move[3];
		this.promoted = move[4];
		this.movingPiece = board.pieces[xOrigin][yOrigin];
		this.movingColor = board.getColor(xOrigin, yOrigin);
	}


	private boolean revertEnPassantIfApplicable(TurnColor color) {
		switch (color) {
			case white:
				if (isWhiteEnPassant()) {
					// execute move
					int pieceToUndo = board.pieces[xDestination][yDestination];
					board.pieces[xOrigin][yOrigin] = pieceToUndo;
					board.pieces[xDestination][yDestination] = 0; // most of the time = 0
					board.pieces[xDestination][yDestination- 1] = enemyRemoved; // most of the time = 0
					return true;
				}
				break;
			case black:
				if (isBlackEnPassant()) {
					int pieceToUndo = board.pieces[xDestination][yDestination];
					board.pieces[xOrigin][yOrigin] = pieceToUndo;
					board.pieces[xDestination][yDestination] = 0; // most of the time = 0
					board.pieces[xDestination][yDestination + 1] = enemyRemoved; // most of the time = 0
					return true;
				}
				break;
		}
		return false;
	}


	public void execute() {
        // prepare caches
        int[] king = board.getKing(movingColor);

		boolean isEnPassant = handleEnPassantIfApplicable(movingColor);
		if (!isEnPassant) {
			boolean isCastling = handleCastlingIfApplicable(movingColor);
			if (!isCastling) {
				if (isPromotion(movingColor)) {
					board.pieces[xOrigin][yOrigin] = 0;
					this.enemyRemoved = board.pieces[xDestination][yDestination];
					board.pieces[xDestination][yDestination] = promoted;
				} else {
					// execute move
					int pieceToMove = board.pieces[xOrigin][yOrigin];
					board.pieces[xOrigin][yOrigin] = 0;
					this.enemyRemoved = board.pieces[xDestination][yDestination];
					board.pieces[xDestination][yDestination] = pieceToMove;
				}
			}
		}

		if (enemyRemoved == BLACK_KING || enemyRemoved == WHITE_KING) {
			throw new RuntimeException("Captured king! Should not be possible!");
		}

		// update caches
        if (xOrigin == king[0] && yOrigin == king[1]) {
            king[0] = xDestination;
            king[1] = yDestination;
        }

        // update pieces pieces cache
        this.board.whitePieces = board.findWhitePieces();
		this.board.blackPieces = board.findBlackPieces();
	}

	private boolean handleEnPassantIfApplicable(TurnColor turnColor) {
		switch (turnColor) {
			case white:
				if (isWhiteEnPassant()) {
					int pieceToMove = board.pieces[xOrigin][yOrigin]; //pawn
					board.pieces[xOrigin][yOrigin] = 0;
					this.enemyRemoved = board.pieces[xDestination][yDestination - 1]; // black pawn BEHIND the white one
					board.pieces[xDestination][yDestination - 1] = 0; //remove opposition pawn!
					board.pieces[xDestination][yDestination] = pieceToMove;
					return true;
				}
				break;
				// can check if new move is behind the other pawn, but not needed - this should be enough to identify en passant;
			case black:
				if (isBlackEnPassant()) {
					int pieceToMove = board.pieces[xOrigin][yOrigin]; //pawn
					board.pieces[xOrigin][yOrigin] = 0;
					this.enemyRemoved = board.pieces[xDestination][yDestination + 1]; // black pawn BEHIND the white one
					board.pieces[xDestination][yDestination + 1] = 0; //remove opposition pawn!
					board.pieces[xDestination][yDestination] = pieceToMove;
					return true;
				}
				break;
		}
		return false;
	}

	private boolean revertCastlingIfApplicable(TurnColor color) {
		switch (color) {
			case white:
				int k = Board.WHITE_KING;
				int r = Board.WHITE_ROOK;
				if (isShortWhiteCastling()) {
					board.pieces[4][0] = k;
					board.pieces[6][0] = 0;
					board.pieces[7][0] = r;
					board.pieces[5][0] = 0;
					return true;
				} else if (isLongWhiteCastling()) {
					board.pieces[4][0] = k;
					board.pieces[2][0] = 0;
					board.pieces[0][0] = r;
					board.pieces[3][0] = 0;
					return true;
				}
				break;
			case black:
				k = Board.BLACK_KING;
				r = Board.BLACK_ROOK;

				if (isShortBlackCastling()) {
					board.pieces[4][7] = k;
					board.pieces[6][7] = 0;
					board.pieces[7][7] = r;
					board.pieces[5][7] = 0;
					return true;
				} else if (isLongBlackCastling()) {
					board.pieces[4][7] = k;
					board.pieces[2][7] = 0;
					board.pieces[0][7] = r;
					board.pieces[3][7] = 0;
					return true;
				}
				break;
		}
		return false;
	}


	public void undo() {
		// update caches
		int[] king = board.getKing(movingColor);

		boolean wasEnPassant = revertEnPassantIfApplicable(movingColor);
		if (!wasEnPassant) {
			boolean wasCastling = revertCastlingIfApplicable(movingColor);
			if (!wasCastling) {
				if (isPromotion(movingColor)) {
					switch (movingColor) {
						case white:
							board.pieces[xOrigin][yOrigin] = WHITE_PAWN;
							break;
						case black:
							board.pieces[xOrigin][yOrigin] = BLACK_PAWN;
							break;
					}
					board.pieces[xDestination][yDestination] = enemyRemoved;
				} else {
					// execute move
					int pieceToUndo = board.pieces[xDestination][yDestination];
					board.pieces[xOrigin][yOrigin] = pieceToUndo;
					board.pieces[xDestination][yDestination] = enemyRemoved; // most of the time = 0
				}
			}
		}
		// update caches
		if (xDestination == king[0] && yDestination == king[1]) {
			king[0] = xOrigin;
			king[1] = yOrigin;
		}

        // update pieces pieces cache
        this.board.whitePieces = board.findWhitePieces();
        this.board.blackPieces = board.findBlackPieces();
	}

	private boolean handleCastlingIfApplicable(TurnColor turnColor) {
		switch (turnColor) {
			case white:
				int k = Board.WHITE_KING;
				int r = Board.WHITE_ROOK;
				if (isShortWhiteCastling()) {
					board.pieces[4][0] = 0;
					board.pieces[6][0] = k;

					board.pieces[7][0] = 0;
					board.pieces[5][0] = r;
					return true;
				} else if (isLongWhiteCastling()) {
					board.pieces[4][0] = 0;
					board.pieces[2][0] = k;

					board.pieces[0][0] = 0;
					board.pieces[3][0] = r;
					return true;
				}
				break;
			case black:
				k = Board.BLACK_KING;
				r = Board.BLACK_ROOK;
				if (isShortBlackCastling()) {
					board.pieces[4][7] = 0;
					board.pieces[6][7] = k;

					board.pieces[7][7] = 0;
					board.pieces[5][7] = r;
					return true;

				} else if (isLongBlackCastling()) {
					board.pieces[4][7] = 0;
					board.pieces[2][7] = k;

					board.pieces[0][7] = 0;
					board.pieces[3][7] = r;
					return true;

				}
				break;
		}
		return false;
	}

	private boolean isLongBlackCastling() {
		//e8c8
		return movingPiece == BLACK_KING && xOrigin == 4 && yOrigin == 7 && xDestination == 2 && yDestination == 7;
	}

	private boolean isShortBlackCastling() {
		//e8g8
		return movingPiece == BLACK_KING && xOrigin == 4 && yOrigin == 7 && xDestination == 6 && yDestination == 7;
	}

	private boolean isLongWhiteCastling() {
		//e1c1eval
		return movingPiece == WHITE_KING && xOrigin == 4 && yOrigin == 0 && xDestination == 2 && yDestination == 0;
	}

	private boolean isShortWhiteCastling() {
		//e1g1
		return movingPiece == WHITE_KING && xOrigin == 4 && yOrigin == 0 && xDestination == 6 && yDestination == 0;
	}

	private boolean isPromotion(TurnColor turnColor) {
        return promoted != 0;
    }

    private boolean isWhiteEnPassant() {
		boolean isPawnMove = movingPiece == Board.WHITE_PAWN;
		boolean isY4 = yOrigin == 4;
		boolean movingSideways = Math.abs(xOrigin - xDestination) == 1;
		boolean noEnemyTaken = board.pieces[xDestination][yDestination] == 0;
		return isPawnMove && isY4 && movingSideways && noEnemyTaken;
	}


	private boolean isBlackEnPassant() {
		boolean isPawnMove = movingPiece == Board.BLACK_PAWN;
		boolean isY3 = yOrigin == 3;
		boolean movingSideways = Math.abs(xOrigin - xDestination) == 1;
		boolean noEnemyTaken = board.pieces[xDestination][yDestination] == 0;
		return isPawnMove && isY3 && movingSideways && noEnemyTaken;
	}


	public int getXDestination() {
		return xDestination;
	}

	public int getYDestination() {
		return yDestination;
	}

	public int getXOrigin() {
		return xOrigin;
	}

	public int getYOrigin() {
		return yOrigin;
	}

	public int getMovingPiece() {
		return movingPiece;
	}
}
