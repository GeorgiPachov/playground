package chessGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class StandardBoard extends Board {
	
	public King whiteKingTracker;
	public King blackKingTracker;

	public StandardBoard(int xSquares, int ySquares) {
		this.numXSquares = xSquares;
		this.numYSquares = ySquares;
		this.totalSquares = this.numXSquares * this.numYSquares;
		this.squaresList = new Square[this.numXSquares][this.numYSquares];
		populateBoardWithSquares();
		this.whiteKingTracker = null;
		this.blackKingTracker = null;
	}

	/**
	 * Method to populate our board with Squares.
	 * General pattern of white and black squares on the board.
	 */
	public void populateBoardWithSquares() {
		for (int i = 0; i < this.numXSquares; i++) {
			for (int j = 0; j < this.numYSquares; j++) {
				if (i % 2 == 0) {
					if (j % 2 == 0)
						squaresList[i][j] = new Square(false, TurnColor.black);
					else
						squaresList[i][j] = new Square(false, TurnColor.white);
				} 
				else {
					if (j % 2 == 0)
						squaresList[i][j] = new Square(false, TurnColor.white);
					else
						squaresList[i][j] = new Square(false, TurnColor.black);
				}
			}
		}
	}
	
	/**
	 * Method to populate our chess board with standard pieces.
	 */
	public void populateBoardWithPieces(boolean special) {
		if(special)
			setupSpecialPieces();
		else{
			setupKnights();
			setupBishops();
		}
		setupPawns();
		setupRooks();
		setupQueens();
		setupKings();
	}
	
	/**
	 * Method to setup Archbishop and Chancellor as special pieces in special game.
	 */
	public void setupSpecialPieces() {
		Archbishop whiteArchbishopOne = new Archbishop(2, 0, TurnColor.white, this);
		Archbishop whiteArchbishopTwo = new Archbishop(5, 0, TurnColor.white, this);
		Archbishop blackArchbishopOne = new Archbishop(2, 7, TurnColor.black, this);
		Archbishop blackArchbishopTwo = new Archbishop(5, 7, TurnColor.black, this);
		this.squaresList[2][0].isOccupied = true;
		this.squaresList[5][0].isOccupied = true;
		this.squaresList[2][0].occupyingPiece = whiteArchbishopOne;
		this.squaresList[5][0].occupyingPiece = whiteArchbishopTwo;
		this.squaresList[2][7].isOccupied = true;
		this.squaresList[5][7].isOccupied = true;
		this.squaresList[2][7].occupyingPiece = blackArchbishopOne;
		this.squaresList[5][7].occupyingPiece = blackArchbishopTwo;
		
		Chancellor whiteKnightOne = new Chancellor(1, 0, TurnColor.white, this);
		Chancellor whiteKnightTwo = new Chancellor(6, 0, TurnColor.white, this);
		Chancellor blackKnightOne = new Chancellor(1, 7, TurnColor.black, this);
		Chancellor blackKnightTwo = new Chancellor(6, 7, TurnColor.black, this);
		this.squaresList[1][0].isOccupied = true;
		this.squaresList[6][0].isOccupied = true;
		this.squaresList[1][0].occupyingPiece = whiteKnightOne;
		this.squaresList[6][0].occupyingPiece = whiteKnightTwo;
		this.squaresList[1][7].isOccupied = true;
		this.squaresList[6][7].isOccupied = true;
		this.squaresList[1][7].occupyingPiece = blackKnightOne;
		this.squaresList[6][7].occupyingPiece = blackKnightTwo;
		
	}

	/**
	 * Setup 8 black and 8 white pawns in their initial positions.
	 */
	public void setupPawns(){
		for(int i = 0; i < 8; i++){
			Pawn newWhitePawn = new Pawn(i, 1, TurnColor.white, this);
			Pawn newBlackPawn = new Pawn(i, 6, TurnColor.black, this);
			this.squaresList[i][1].isOccupied = true;
			this.squaresList[i][6].isOccupied = true;
			this.squaresList[i][1].occupyingPiece = newWhitePawn;
			this.squaresList[i][6].occupyingPiece = newBlackPawn;
			
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
		this.squaresList[0][0].isOccupied = true;
		this.squaresList[7][0].isOccupied = true;
		this.squaresList[0][0].occupyingPiece = whiteRookOne;
		this.squaresList[7][0].occupyingPiece = whiteRookTwo;
		this.squaresList[0][7].isOccupied = true;
		this.squaresList[7][7].isOccupied = true;
		this.squaresList[0][7].occupyingPiece = blackRookOne;
		this.squaresList[7][7].occupyingPiece = blackRookTwo;
		
	}
	
	/**
	 * Setup 2 black Bishops and 2 white Bishops in their initial positions.
	 */
	public void setupBishops(){
		Bishop whiteBishopOne = new Bishop(2, 0, TurnColor.white, this);
		Bishop whiteBishopTwo = new Bishop(5, 0, TurnColor.white, this);
		Bishop blackBishopOne = new Bishop(2, 7, TurnColor.black, this);
		Bishop blackBishopTwo = new Bishop(5, 7, TurnColor.black, this);
		this.squaresList[2][0].isOccupied = true;
		this.squaresList[5][0].isOccupied = true;
		this.squaresList[2][0].occupyingPiece = whiteBishopOne;
		this.squaresList[5][0].occupyingPiece = whiteBishopTwo;
		this.squaresList[2][7].isOccupied = true;
		this.squaresList[5][7].isOccupied = true;
		this.squaresList[2][7].occupyingPiece = blackBishopOne;
		this.squaresList[5][7].occupyingPiece = blackBishopTwo;
	}
	
	/**
	 * Setup 2 black Knights and 2 white Knights in their initial positions.
	 */
	public void setupKnights(){
		Knight whiteKnightOne = new Knight(1, 0, TurnColor.white, this);
		Knight whiteKnightTwo = new Knight(6, 0, TurnColor.white, this);
		Knight blackKnightOne = new Knight(1, 7, TurnColor.black, this);
		Knight blackKnightTwo = new Knight(6, 7, TurnColor.black, this);
		this.squaresList[1][0].isOccupied = true;
		this.squaresList[6][0].isOccupied = true;
		this.squaresList[1][0].occupyingPiece = whiteKnightOne;
		this.squaresList[6][0].occupyingPiece = whiteKnightTwo;
		this.squaresList[1][7].isOccupied = true;
		this.squaresList[6][7].isOccupied = true;
		this.squaresList[1][7].occupyingPiece = blackKnightOne;
		this.squaresList[6][7].occupyingPiece = blackKnightTwo;
	}
	
	/**
	 * Setup 2 queens white and black in their initial positions.
	 */	
	public void setupQueens(){
		Queen whiteQueen = new Queen(3, 0, TurnColor.white, this);
		Queen blackQueen = new Queen(3, 7, TurnColor.black, this);
		this.squaresList[3][0].isOccupied = true;
		this.squaresList[3][7].isOccupied = true;
		this.squaresList[3][0].occupyingPiece = whiteQueen;
		this.squaresList[3][7].occupyingPiece = blackQueen;
	}
	
	/**
	 * Setup 2 queens white and black in their initial positions.
	 */
	public void setupKings(){
		King whiteKing = new King(4, 0, TurnColor.white, this);
		King blackKing = new King(4, 7, TurnColor.black, this);
		this.squaresList[4][0].isOccupied = true;
		this.squaresList[4][7].isOccupied = true;
		this.squaresList[4][0].occupyingPiece = whiteKing;
		this.squaresList[4][7].occupyingPiece = blackKing;
		whiteKingTracker = whiteKing;
		blackKingTracker = blackKing;
	}
	
	/**
	 * Helper method to check if locations passed in are mapped on our generated board.
	 * @see Board#inBoardBounds(int, int)
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

	public List<Move> listPossibleMovesWhite() {
		List<Square> squares = Arrays.stream(squaresList).flatMap(s -> Arrays.stream(s))
				.filter(s1 -> s1.isOccupied)
				.filter(p -> p.occupyingPiece.turnColor.equals(TurnColor.white))
				.collect(Collectors.toList());

		List<Move> moves = squares.stream().map(s -> s.occupyingPiece).flatMap(p -> getAllowedMoves(p).stream()).collect(Collectors.toList());
		return moves;
	}

	private List<Move> getAllowedMoves(Piece p) {
		if (p instanceof King) {
			return getAllowedMoves((King) p);
		} else if (p instanceof Queen) {
			return getAllowedMoves((Queen) p);

		} else if (p instanceof Pawn) {
			return getAllowedMoves((Pawn) p);

		} else if (p instanceof Knight) {
			return getAllowedMoves((Knight) p);

		} else if (p instanceof Bishop) {
			return getAllowedMoves((Bishop) p);

		} else if (p instanceof Rook) {
			return getAllowedMoves((Rook) p);
		}

		return new ArrayList<>();
	}

	private List<Move> getAllowedMoves(King king) {
		return genericCheckMoves(king);
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

	private List<Move> getAllowedMoves(Queen queen) {
		return genericCheckMoves(queen);
	}

	private List<Move> getAllowedMoves(Pawn pawn) {
		return genericCheckMoves(pawn);
	}

	private List<Move> getAllowedMoves(Rook rook) {
		return genericCheckMoves(rook);
	}

	private List<Move> getAllowedMoves(Knight knight) {
		return genericCheckMoves(knight);
	}

	private List<Move> getAllowedMoves(Bishop bishop) {
		return genericCheckMoves(bishop);
	}

	public List<Move> listPossibleMovesBlack() {
		List<Square> squares = Arrays.stream(squaresList).flatMap(s -> Arrays.stream(s))
				.filter(s1 -> s1.isOccupied)
				.filter(p -> p.occupyingPiece.turnColor.equals(TurnColor.black))
				.collect(Collectors.toList());

		List<Move> moves = squares.stream().map(s -> s.occupyingPiece).flatMap(p -> getAllowedMoves(p).stream()).collect(Collectors.toList());
		return moves;
	}

	public List<Square> getBlackPieces() {
		return Arrays.stream(squaresList).flatMap(a -> Arrays.stream(a))
				.filter(s -> s.isOccupied)
				.filter(s -> s.occupyingPiece.turnColor == Board.TurnColor.black).collect(Collectors.toList());
	}

	public List<Square> getWhitePieces() {
		return Arrays.stream(squaresList).flatMap(a -> Arrays.stream(a))
				.filter(s -> s.isOccupied)
				.filter(s -> s.occupyingPiece.turnColor == Board.TurnColor.white).collect(Collectors.toList());

	}

	public List<Square> getPieces(TurnColor gameTurn) {
		switch (gameTurn) {
			case white:
				return getWhitePieces();
			case black:
				return getBlackPieces();
		}
		return null;
	}
}
