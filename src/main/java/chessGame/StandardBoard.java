package chessGame;

import chessControllers.TurnColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StandardBoard {
	public int numXSquares;
	public int numYSquares;
	public int totalSquares;
	public Piece pieces[][];
	
	public King whiteKingTracker;
	public King blackKingTracker;

	private List<Piece> blackPieces;
	private List<Piece> whitePieces;

	public StandardBoard(int xSquares, int ySquares) {
		this.numXSquares = xSquares;
		this.numYSquares = ySquares;
		this.totalSquares = this.numXSquares * this.numYSquares;
		this.pieces = new Piece[this.numXSquares][this.numYSquares];
		populateBoardWithSquares();
		this.whiteKingTracker = null;
		this.blackKingTracker = null;

       initCaches();
	}

	public void populateBoardWithSquares() {
	    for (int i = 0; i < 8; i++) {
	        pieces[i] = new Piece[8];
        }
	}
	
	public void populateBoardWithPieces() {
		setupKnights();
		setupBishops();
		setupPawns();
		setupRooks();
		setupQueens();
		setupKings();
	}
	

	/**
	 * Setup 8 black and 8 white pawns in their initial positions.
	 */
	public void setupPawns(){
		for(int i = 0; i < 8; i++){
			Pawn newWhitePawn = new Pawn(i, 1, TurnColor.white, this);
			Pawn newBlackPawn = new Pawn(i, 6, TurnColor.black, this);
			this.pieces[i][1] = newWhitePawn;
			this.pieces[i][6] = newBlackPawn;
		}
	}
	
	/**
	 * Setup 2 black rooks and 2 white rooks in their initial positions.
	 */
	public void setupRooks(){
		Rook whiteRookOne = new Rook(0, 0, TurnColor.white, this);
		Rook whiteRookTwo = new Rook(7, 0, TurnColor.white, this);
		Rook blackRookOne = new Rook(0, 7, TurnColor.black, this);
		Rook blackRookTwo = new Rook(7, 7, TurnColor.black, this);
		this.pieces[0][0] = whiteRookOne;
		this.pieces[7][0] = whiteRookTwo;
		this.pieces[0][7] = blackRookOne;
		this.pieces[7][7] = blackRookTwo;
	}
	
	/**
	 * Setup 2 black Bishops and 2 white Bishops in their initial positions.
	 */
	public void setupBishops(){
		Bishop whiteBishopOne = new Bishop(2, 0, TurnColor.white, this);
		Bishop whiteBishopTwo = new Bishop(5, 0, TurnColor.white, this);
		Bishop blackBishopOne = new Bishop(2, 7, TurnColor.black, this);
		Bishop blackBishopTwo = new Bishop(5, 7, TurnColor.black, this);
		this.pieces[2][0] = whiteBishopOne;
		this.pieces[5][0] = whiteBishopTwo;
		this.pieces[2][7] = blackBishopOne;
		this.pieces[5][7] = blackBishopTwo;
	}
	
	/**
	 * Setup 2 black Knights and 2 white Knights in their initial positions.
	 */
	public void setupKnights(){
		Knight whiteKnightOne = new Knight(1, 0, TurnColor.white, this);
		Knight whiteKnightTwo = new Knight(6, 0, TurnColor.white, this);
		Knight blackKnightOne = new Knight(1, 7, TurnColor.black, this);
		Knight blackKnightTwo = new Knight(6, 7, TurnColor.black, this);
		this.pieces[1][0] = whiteKnightOne;
		this.pieces[6][0] = whiteKnightTwo;
		this.pieces[1][7] = blackKnightOne;
		this.pieces[6][7] = blackKnightTwo;
	}
	
	/**
	 * Setup 2 queens white and black in their initial positions.
	 */	
	public void setupQueens(){
		Queen whiteQueen = new Queen(3, 0, TurnColor.white, this);
		Queen blackQueen = new Queen(3, 7, TurnColor.black, this);
		this.pieces[3][0] = whiteQueen;
		this.pieces[3][7] = blackQueen;
	}
	
	/**
	 * Setup 2 queens white and black in their initial positions.
	 */
	public void setupKings(){
		King whiteKing = new King(4, 0, TurnColor.white, this);
		King blackKing = new King(4, 7, TurnColor.black, this);
		this.pieces[4][0] = whiteKing;
		this.pieces[4][7] = blackKing;
		whiteKingTracker = whiteKing;
		blackKingTracker = blackKing;
	}
	
	/**
	 * Helper method to check if locations passed in are mapped on our generated board.
	 * @param newX
	 * @param newY
	 * @return boolean true if move is in board bounds
	 */
	public boolean inBoardBounds(int newX, int newY){
		if(newX < numXSquares && newY < numYSquares && newX > -1 && newY > -1){
			return true;
		}
		else
			return false;
	}

	public void populatePossibleMoves(TurnColor color, List<Integer> moves) {
	    List<Piece> squares = getPieces(color);
		squares.stream().forEach(p -> p.addAllowedMoves(moves));
	}

	private List<Move> genericCheckMoves(Piece piece) {
		List<Move> result = new ArrayList<>();
		for (int x = 0; x <= 8; x++) {
			for (int y = 0; y <= 8; y++) {
				if (piece.canMove(x, y)) {
					result.add(new Move(piece.xLocation, piece.yLocation, x, y));
				}
			}
		}
		return result;
	}

	public List<Piece> getPieces(TurnColor gameTurn) {
	    switch (gameTurn) {
            case white:
                return whitePieces;
            case black:
                return blackPieces;
        }
        return null;
	}

    public void initCaches() {
        whitePieces = findWhitePieces();
        blackPieces = findBlackPieces();
    }

    private List<Piece> findBlackPieces() {
        return Arrays.stream(pieces).flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .filter(p -> p.turnColor == TurnColor.black).collect(Collectors.toList());
    }

    private List<Piece> findWhitePieces() {
        return Arrays.stream(pieces).flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .filter(p -> p.turnColor == TurnColor.white).collect(Collectors.toList());
    }
}
