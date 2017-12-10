package chessGame;

import chessControllers.MoveCommand;
import chessControllers.TurnColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static chessControllers.Game.DEBUG;

public class StandardBoard {
    public TurnColor gameTurn;
    public int pieces[][];
	public static final int WHITE_PAWN = 1;
    public static final int WHITE_ROOK = 2;
    public static final int WHITE_KNIGHT = 3;
    public static final int WHITE_BISHOP = 4;
    public static final int WHITE_QUEEN = 5;
    public static final int WHITE_KING = 6;
    public static final int[] WHITES = new int[] {
            WHITE_PAWN,
            WHITE_ROOK,
            WHITE_KNIGHT,
            WHITE_BISHOP,
            WHITE_QUEEN,
            WHITE_KING
    };

    public static final int BLACK_PAWN = 17;
    public static final int BLACK_ROOK = 18;
    public static final int BLACK_KNIGHT = 19;
    public static final int BLACK_BISHOP = 20;
    public static final int BLACK_QUEEN = 21;
    public static final int BLACK_KING = 22;
    public static final int[] BLACKS = new int[] {
            BLACK_PAWN,
            BLACK_ROOK,
            BLACK_KNIGHT,
            BLACK_BISHOP,
            BLACK_QUEEN,
            BLACK_KING
    };

	public StandardBoard() {
		this.pieces = new int[8][8];
        gameTurn = TurnColor.white;
        populateBoardWithSquares();
		populateBoardWithPieces();
	}

	public void populateBoardWithSquares() {
	    for (int i = 0; i < 8; i++) {
	        pieces[i] = new int[8];
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
	

	public void setupPawns(){
		for(int i = 0; i < 8; i++){
			this.pieces[i][1] = WHITE_PAWN;
			this.pieces[i][6] = BLACK_PAWN;
		}
	}
	
	public void setupRooks(){
		this.pieces[0][0] = WHITE_ROOK;
		this.pieces[7][0] = WHITE_ROOK;
		this.pieces[0][7] = BLACK_ROOK;
		this.pieces[7][7] = BLACK_ROOK;
	}
	
	public void setupBishops(){
		this.pieces[2][0] = WHITE_BISHOP;
		this.pieces[5][0] = WHITE_BISHOP;
		this.pieces[2][7] = BLACK_BISHOP;
		this.pieces[5][7] = BLACK_BISHOP;
	}
	
	public void setupKnights(){
		this.pieces[1][0] = WHITE_KNIGHT;
		this.pieces[6][0] = WHITE_KNIGHT;
		this.pieces[1][7] = BLACK_KNIGHT;
		this.pieces[6][7] = BLACK_KNIGHT;
	}
	
	public void setupQueens(){
		this.pieces[3][0] = WHITE_QUEEN;
		this.pieces[3][7] = BLACK_QUEEN;
	}
	
	public void setupKings(){
		this.pieces[4][0] = WHITE_KING;
		this.pieces[4][7] = BLACK_KING;
	}
	
	public boolean inBoardBounds(int newX, int newY){
		if(newX < 8 && newY < 8 && newX > -1 && newY > -1){
			return true;
		}
		else
			return false;
	}

	public int[] whiteKing = new int[]{4,0};
	public int[] blackKing = new int[]{4,7};

	public void populatePossibleMoves(TurnColor color, List<Integer> moves) {
	    List<int[]> pieces = getPieces(color);
        pieces.forEach(p -> addAllowedMoves(p, moves));
	}

    private void addAllowedMoves(int[] coordinates, List<Integer> moves) {
        int x = coordinates[0];
        int y = coordinates[1];

        switch (pieces[x][y]) {
            case WHITE_PAWN:
            case BLACK_PAWN:
                Pawn.addAllowedMoves(this, coordinates, moves);
                break;
            case WHITE_BISHOP:
            case BLACK_BISHOP:
                Bishop.addAllowedMoves(this, coordinates, moves);
                break;
            case WHITE_KNIGHT:
            case BLACK_KNIGHT:
                Knight.addAllowedMoves(this, coordinates, moves);
                break;
            case WHITE_ROOK:
            case BLACK_ROOK:
                Rook.addAllowedMoves(this, coordinates, moves);
                break;
            case WHITE_QUEEN:
            case BLACK_QUEEN:
                Queen.addAllowedMoves(this, coordinates, moves);
                break;
            case WHITE_KING:
            case BLACK_KING:
                King.addAllowedMoves(this, coordinates, moves);
                break;
        }
    }

    public List<int[]> getPieces(TurnColor gameTurn) {
	    switch (gameTurn) {
            case white:
                return findWhitePieces();
            case black:
                return findBlackPieces();
        }
        return null;
	}

    private List<int[]> findBlackPieces() {
        List<int[]> blacks = new ArrayList<>();
	    for (int i = 0; i < 8; i++) {
	        for (int j = 0; j < 8; j++) {
	            if (isBlack(pieces[i][j])) {
                    blacks.add(new int[] {i, j});
                }
            }
        }
        return blacks;
    }

    public boolean isBlack(int i) {
        return i > 16 && i < 32;
    }

    private List<int[]> findWhitePieces() {
        List<int[]> whites = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isWhite(pieces[i][j])) {
                    whites.add(new int[] {i, j});
                }
            }
        }
        return whites;

    }

    public boolean isWhite(int i) {
        return i > 0 && i < 16;
    }

    public int[] getKing(TurnColor gameTurn) {
        switch (gameTurn) {
            case white:
                return whiteKing;
            case black:
                return blackKing;
        }
        return null;
    }

    public boolean canMove(int oldX, int oldY, int newX, int newY){
        TurnColor myColor = getColor(oldX, oldY);
	    if(!inBoardBounds(newX, newY))
            return false;
        if(pieces[newX][newY]!=0 && getColor(newX, newY) == myColor) {
            return false;
        }
	    if (!isValidMove(oldX, oldY, newX, newY)) {
            return false;
        }
        if(kingBecomesEndangered(oldX, oldY, newX, newY))
            return false;
        return true;
    }

    private boolean isValidMove(int oldX, int oldY, int newX, int newY) {
        switch (pieces[oldX][oldY]){
            case WHITE_PAWN:
            case BLACK_PAWN:
                if (isValidPawnMove(oldX,oldY, newX,newY)) {
                    return true;
                }
                break;
            case WHITE_BISHOP:
            case BLACK_BISHOP:
                if (isValidBishopMove(oldX, oldY, newX, newY)) {
                    return true;
                }
                break;
            case WHITE_ROOK:
            case BLACK_ROOK:
                if (isValidRookMove(oldX, oldY, newX, newY)) {
                    return true;
                }
                break;
            case WHITE_KNIGHT:
            case BLACK_KNIGHT:
                if (isValidKnightMove(oldX, oldY, newX, newY)) {
                    return true;
                }
                break;
            case WHITE_QUEEN:
            case BLACK_QUEEN:
                if (isValidQueenMove(oldX, oldY, newX, newY)) {
                    return true;
                }
                break;
            case WHITE_KING:
            case BLACK_KING:
                if (isValidKingMove(oldX, oldY, newX, newY)) {
                    return true;
                }
                break;
        }
        return false;
    }

    private boolean isValidKingMove(int oldX, int oldY, int newX, int newY) {
        return King.isValidKingMove(this, oldX, oldY, newX, newY);
    }

    private boolean isValidQueenMove(int oldX, int oldY, int newX, int newY) {
        return Queen.isValidQueenMove(this, oldX, oldY, newX, newY);
    }

    private boolean isValidKnightMove(int oldX, int oldY, int newX, int newY) {
        return Knight.isValidKnightMove(this, oldX, oldY, newX, newY);
    }

    private boolean isValidRookMove(int oldX, int oldY, int newX, int newY) {
        return Rook.isValidRookMove(this, oldX, oldY, newX, newY);
    }

    private boolean isValidBishopMove(int oldX, int oldY, int newX, int newY) {
        return Bishop.isValidBishopMove(this, oldX, oldY, newX, newY);
    }

    private boolean isValidPawnMove(int oldX, int oldY, int newX, int newY) {
        boolean validSpecialMove = Pawn.isValidSpecialMove(this, oldX, oldY, newX, newY);
        if (DEBUG) {
            System.out.println(String.format("Move [%d, %d] -> [%d, %d] was demed %b", oldX, oldY, newX, newY, validSpecialMove));
        }
        return validSpecialMove;
    }

    public boolean isOfColor(int piece, TurnColor oppositeColor) {
        return Arrays.binarySearch(oppositeColor.getPieces(), piece) > -1;
    }

    public void flipTurn() {
        gameTurn = gameTurn.opposite();
    }

    private boolean kingBecomesEndangered(int oldX, int oldY, int newPieceX, int newPieceY) {
        TurnColor turnColor = getColor(oldX, oldY);

        int piece = pieces[newPieceX][newPieceY];
        MoveCommand command = new MoveCommand(this, new int[] { oldX, oldY, newPieceX, newPieceY});

        if(piece != 0){
            if(isOfColor(piece, turnColor.opposite())){
                command.execute();
                // check
                int[] myKing = getKing(turnColor);
                boolean kingIsInCheck = isKingInCheck(myKing);

                // revert move
                command.undo();
                return kingIsInCheck;
            } else {
                return false; //my piece is there :/
            }
        }
        else{
            command.execute();
            int[] myKing = getKing(turnColor);
            boolean kingIsInCheck = isKingInCheck(myKing);
            command.undo();
            return kingIsInCheck;
        }
    }

    public boolean isKingInCheck(int[] kingToCheck) {
        int kingX = kingToCheck[0];
        int kingY = kingToCheck[1];
        TurnColor kingColor = getColor(kingToCheck);
        List<int[]> oppositePieces = getPieces(kingColor.opposite());
        for (int[] coords: oppositePieces) {
            if (isValidMove(coords[0], coords[1], kingX, kingY)) {
                return true;
            }
        }
        return false;
    }

    public TurnColor getColor(int x, int y) {
        int figure = pieces[x][y];
        if (isWhite(figure)) {
            return TurnColor.white;
        } else if (isBlack(figure)) {
            return TurnColor.black;
        } else if (figure == 0) {
            throw new RuntimeException("Attempted to get color of empty square!");
        }
        return null;
    }

    private TurnColor getColor(int[] coordinates) {
        return getColor(coordinates[0], coordinates[1]);
    }

    public boolean isKingCheckmate(int[] kingToCheck){
        if(isKingInCheck(kingToCheck)) {
            TurnColor color = getColor(kingToCheck);
            System.out.println("King " + color + " is in check");
            List<Integer> moves = new ArrayList<>();
            populatePossibleMoves(color, moves);
            for(int i = 0; i < moves.size(); i+=4){
                int ox = moves.get(i);
                int oy = moves.get(i+1);
                int nx = moves.get(i+2);
                int ny = moves.get(i+3);
                MoveCommand command = new MoveCommand(this, new int[] {ox, oy, nx, ny});
                if(canMove(ox, oy, nx, ny)){ // maybe only check if valid move, without endangering the king?
                    command.execute();
                    // we could have moved the king...
                    kingToCheck = getKing(color);
                    boolean kingIsInCheck = isKingInCheck(kingToCheck);
                    command.undo();
                    if (!kingIsInCheck) {
                        return true;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public boolean isKing(int[] p) {
        switch (pieces[p[0]][p[1]]) {
            case WHITE_KING:
            case BLACK_KING:
                return true;
        }
        return false;
    }
}
