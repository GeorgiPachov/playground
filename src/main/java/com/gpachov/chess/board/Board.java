package com.gpachov.chess.board;

import com.google.common.annotations.VisibleForTesting;
import com.gpachov.chess.pieces.Queen;
import com.gpachov.chess.pieces.Rook;
import com.gpachov.chess.pieces.Bishop;
import com.gpachov.chess.pieces.King;
import com.gpachov.chess.pieces.Knight;
import com.gpachov.chess.pieces.Pawn;
import com.gpachov.chess.Util;

import java.util.*;

import static com.gpachov.chess.Util.logV;

public class Board {

    public Metadata metadata;

    public static class Metadata {
        public Metadata() {
            this.gameTurn = TurnColor.white;
        }

        public Metadata(Metadata old) {
            this.whiteKingHasMoved = old.whiteKingHasMoved;
            this.blackKingHasMoved = old.blackKingHasMoved;
            this.whiteRLHasMoved = old.whiteRLHasMoved;
            this.whiteRRHasMoved = old.whiteRRHasMoved;
            this.blackRLHasMoved = old.blackRLHasMoved;
            this.blackRRHasMoved = old.blackRRHasMoved;

            this.lastExecutedCommand = old.lastExecutedCommand;
            this.gameOver = old.gameOver;
            this.gameTurn = old.gameTurn;
        }

        public boolean whiteKingHasMoved = false;
        public boolean blackKingHasMoved = false;

        public boolean whiteRLHasMoved = false;
        public boolean whiteRRHasMoved = false;
        public boolean blackRLHasMoved = false;
        public boolean blackRRHasMoved = false;
        public MoveCommand lastExecutedCommand;
        public boolean gameOver;
        public TurnColor gameTurn;

    }
    public Stack<MoveCommand> commandStack;
    public int pieces[][];


    public List<int[]> blackPieces = new ArrayList<>();
    public List<int[]> whitePieces = new ArrayList<>();
    public int[] whiteKing = new int[]{4,0};
    public int[] blackKing = new int[]{4,7};


	public Board() {
        this.pieces = new int[8][8];
        this.metadata = new Metadata();
        metadata.gameTurn = TurnColor.white;
        populateBoardWithSquares();
        populateBoardWithPieces();

        metadata.gameOver = false;
        commandStack = new Stack<>();

        initCaches();
    }

    public Board(int[][] board) {
        this.pieces = board;
        metadata = new Metadata();
        metadata.gameTurn = TurnColor.white;

        metadata.gameOver = false;
        commandStack = new Stack<>();

        initCaches();
    }

    public static Board testBoard() {
	    int pieces[][] = new int[8][8];
	    pieces[0][0] = WHITE_KING;
	    pieces[7][7] = BLACK_KING;
	    return new Board(pieces);
    }

    public void initCaches() {
        whitePieces = findWhitePieces();
        blackPieces = findBlackPieces();
        this.whiteKing = whitePieces.stream().filter(this::isKing).findFirst().get();
        this.blackKing = blackPieces.stream().filter(this::isKing).findFirst().get();
    }

    public void preexecuteMove(int[] move) {
        int xDestination = move[2];
        int yDestination = move[3];
        int movingPiece = pieces[move[0]][move[1]];

        if (Constants.DEBUG) {
            log("Preexecuting move for " + toString(movingPiece) + " [" + move[0] + ", " + move[1] + "] to [" + xDestination + ", " + yDestination + "]");
        }
        MoveCommand newCommand = new MoveCommand(this, move);
        commandStack.add(newCommand);
        newCommand.execute();
        flipTurn();
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

	public void populatePossibleMoves(TurnColor color, List<Integer> moves) {
	    Collection<int[]> pieces = new ArrayList(getPieces(color));
        pieces.forEach(p -> addAllowedMoves(p, moves));
	}

    public @VisibleForTesting void addAllowedMoves(int[] coordinates, List<Integer> moves) {
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
                //XXX disable caches
//                return findWhitePieces();
                return whitePieces;
            case black:
                //XXX disable caches
//                return findBlackPieces();
                return blackPieces;
        }
        return null;
	}

    public List<int[]> findBlackPieces() {
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
        return i > 16 && i <= 22;
    }

    public List<int[]> findWhitePieces() {
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
        return i > 10 && i <= 16;
    }

    public int[] getKing(TurnColor gameTurn) {
        switch (gameTurn) {
            case white:
                return whiteKing;
            case black:
                return blackKing;
        }
//        Optional<int[]> king = getPieces(gameTurn).stream().filter(this::isKing).findAny();
//        if (!king.isPresent()){
//            throw new RuntimeException("Missing king!");
//        }
//        return king.get();
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

    public boolean isValidMove(int oldX, int oldY, int newX, int newY) {
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
        if (Constants.DEBUG) {
            System.out.println(String.format("Move [%d, %d] -> [%d, %d] was demed %b", oldX, oldY, newX, newY, validSpecialMove));
        }
        return validSpecialMove;
    }

    public boolean isOfColor(int piece, TurnColor oppositeColor) {
        return Arrays.binarySearch(oppositeColor.getPieces(), piece) > -1;
    }

    public void flipTurn() {
        metadata.gameTurn = metadata.gameTurn.opposite();
    }

    private boolean kingBecomesEndangered(int oldX, int oldY, int newPieceX, int newPieceY) {
        TurnColor turnColor = getColor(oldX, oldY);
        MoveCommand command = new MoveCommand(this, new int[] { oldX, oldY, newPieceX, newPieceY, 0});

        command.execute();
        int[] myKing = getKing(turnColor);
        boolean kingIsInCheck = isKingInCheck(myKing);
        command.undo();
        return kingIsInCheck;
    }

    public boolean isKingInCheck(int[] kingToCheck) {
        int kingX = kingToCheck[0];
        int kingY = kingToCheck[1];
        TurnColor kingColor = getColor(kingToCheck);
        Collection<int[]> oppositePieces = getPieces(kingColor.opposite());
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

    public TurnColor getColor(int[] coordinates) {
        return getColor(coordinates[0], coordinates[1]);
    }

    public boolean isKingCheckmate(int[] kingToCheck){
        if(isKingInCheck(kingToCheck)) {
            TurnColor color = getColor(kingToCheck);
            List<Integer> moves = new ArrayList<>();
            populatePossibleMoves(color, moves);
            if (moves.size() == 0) {
                return true;
            }
            return false;
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

    public int getMultiplier(TurnColor turnColor) {
        switch (turnColor){
            case white:
                return 1;
            case black:
                return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            builder.append(7-i + " ");
            for (int j = 0; j < 8; j++) {
                builder.append(mapToLetters(pieces[j][7-i]) + " ");
            }
            builder.append("\n");
        }

        builder.append("  ");
        for (int j = 0; j < 8; j++) {
            builder.append(j + " " + " ");
        }
        return builder.toString();
    }

    private String mapToLetters(int i) {
        switch (i) {
            case WHITE_KING:
                return "wk";
            case WHITE_ROOK:
                return "wr";
            case WHITE_PAWN:
                return "wp";
            case WHITE_BISHOP:
                return "wb";
            case WHITE_KNIGHT:
                return "wn";
            case WHITE_QUEEN:
                return "wq";

            case BLACK_KING:
                return "bk";
            case BLACK_QUEEN:
                return "bq";
            case BLACK_ROOK:
                return "br";
            case BLACK_BISHOP:
                return "bb";
            case BLACK_KNIGHT:
                return "bn";
            case BLACK_PAWN:
                return "bp";
        }
        return "__";
    }


    private void log(String s) {
        System.err.println(s);
    }

    private String toString(int movingPiece) {
        return String.valueOf(movingPiece);
    }

    public void executeMove(int[]  move) {
        logV(toString());
        int xDestination = move[2];
        int yDestination = move[3];
        int movingPiece = pieces[move[0]][move[1]];
        TurnColor movingPieceColor = getColor(move[0], move[1]);

        if (Constants.DEBUG) {
            log("Found piece " + movingPiece);
            log("Executing move for " + toString(movingPiece) + " [" + move[0] + ", " + move[1] + "] to [" + xDestination + ", " + yDestination + "]");
        }
        // if is promotion
        MoveCommand newCommand = new MoveCommand(this, move);
        commandStack.add(newCommand);
        newCommand.execute();

        updateCastlingCaches(newCommand);

        logV(toString());

        int[] kingToCheck = this.getKing(movingPieceColor.opposite());
        boolean gameOver = this.isKingCheckmate(kingToCheck);
        this.flipTurn();

        if (gameOver) {
            stopGame();
        }

    }

    private void updateCastlingCaches(MoveCommand command) {
        // king castling caches
        if (command.getMovingPiece() == WHITE_KING) {
            metadata.whiteKingHasMoved = true;
        }
        else if (command.getMovingPiece() == BLACK_KING) {
            metadata.blackKingHasMoved = true;
        }

        // rook castling caches
        else if (command.getMovingPiece() == WHITE_ROOK && command.getXOrigin() == Rook.WHITE_ROOK_LEFT[0] && command.getYOrigin() == Rook.WHITE_ROOK_LEFT[1]) {
            metadata.whiteRLHasMoved = true;
        } else if (command.getMovingPiece() == WHITE_ROOK && command.getXOrigin() == Rook.WHITE_ROOK_RIGHT[0] && command.getYOrigin() == Rook.WHITE_ROOK_RIGHT[1]) {
            metadata.whiteRRHasMoved = true;
        } else if (command.getMovingPiece() == BLACK_ROOK && command.getXOrigin() == Rook.BLACK_ROOK_LEFT[0] && command.getYOrigin() == Rook.BLACK_ROOK_LEFT[1]) {
            metadata.blackRLHasMoved = true;
        } else if (command.getMovingPiece() == BLACK_ROOK && command.getXOrigin() == Rook.BLACK_ROOK_RIGHT[0] && command.getYOrigin() == Rook.BLACK_ROOK_RIGHT[1]) {
            metadata.blackRRHasMoved = true;
        }


    }

    private void stopGame() {
        logV("GAME ENDED!!!!" + this.metadata.gameTurn + " WON");
        this.metadata.gameOver = true;
    }

    public void undoMove() {
        if (!commandStack.isEmpty()) {
            MoveCommand move = commandStack.pop();
            move.undo();
            this.flipTurn();
        }
    }



    ///board constants
    public static final int WHITE_PAWN = 11;
    public static final int WHITE_ROOK = 12;
    public static final int WHITE_KNIGHT = 13;
    public static final int WHITE_BISHOP = 14;
    public static final int WHITE_QUEEN = 15;
    public static final int WHITE_KING = 16;
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

}
