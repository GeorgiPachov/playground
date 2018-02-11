package com.gpachov.chess.board;

import java.util.List;

import static com.gpachov.chess.board.Board.*;

public class Castling {
    public static void addCastlingIfApplicable(Board board, int[] coordinates, List<Integer> moves) {
        if (Constants.CASTLING_ENABLED) {
            TurnColor color = board.getColor(coordinates[0], coordinates[1]);
            if (color.equals(TurnColor.white)) {
                addWhiteCastlingIfApplicable(board, coordinates, moves);
            } else if (color.equals(TurnColor.black)) {
                addBlackCastlingIfApplicable(board, coordinates, moves);
            }
        }
    }

    private static void addBlackCastlingIfApplicable(Board board, int[] coordinates, List<Integer> moves) {
        addLeftBlackCastlingIfApplicable(board, coordinates, moves);
        addRightBlackCastlingIfApplicable(board, coordinates, moves);
    }


    private static void addRightBlackCastlingIfApplicable(Board board, int[] coordinates, List<Integer> moves) {
        boolean rightRookExists = board.pieces[7][7] == BLACK_ROOK;
        boolean blackKingIsAtRightPlace = board.pieces[4][7] == BLACK_KING;
        if (!rightRookExists || !blackKingIsAtRightPlace) {
            return;
        }

        boolean kingHasMoved = board.blackKingHasMoved;
        boolean rookLeftHasMoved = board.blackRRHasMoved;
        boolean noPiecesBetweenThem = true;
        for (int x = 5; x < 7; x++) {
            if (board.pieces[x][7] != 0) {
                noPiecesBetweenThem = false;
            }
        }

        TurnColor myColor = board.getColor(coordinates);
        TurnColor oppositionColor = myColor.opposite();


        boolean noFieldIsUnderSiege = true;
        for (int x = 4; x < 7; x++) {
            for (int[] c : board.getPieces(oppositionColor)) {
                if (board.isValidMove(c[0], c[1], x, 7)) {
                    noFieldIsUnderSiege = false;
                }
            }
        }

        boolean kingIsInCheckAfterMove = false;
        for (int[] c : board.getPieces(oppositionColor)) {
            if (board.isValidMove(c[0], c[1], 6, 7)) {
                kingIsInCheckAfterMove = true;
            }
        }

        boolean rightBlackCastlingAvailable = !kingIsInCheckAfterMove && noFieldIsUnderSiege && !kingHasMoved && !rookLeftHasMoved && noPiecesBetweenThem;
        if (rightBlackCastlingAvailable) {
            moves.add(4);
            moves.add(7);
            moves.add(6);
            moves.add(7);
            moves.add(0);
        }

    }

    private static void addLeftBlackCastlingIfApplicable(Board board, int[] coordinates, List<Integer> moves) {
        boolean leftRookExists = board.pieces[0][7] == BLACK_ROOK;
        boolean blackKingIsAtRightPlace = board.pieces[4][7] == BLACK_KING;
        if (!leftRookExists || blackKingIsAtRightPlace) {
            return;
        }
        // left castling
        boolean kingHasMoved = board.blackKingHasMoved;
        boolean rookLeftHasMoved = board.blackRLHasMoved;
        boolean noPiecesBetweenThem = true;
        for (int x = 1; x < 4; x++) {
            if (board.pieces[x][7] != 0) {
                noPiecesBetweenThem = false;
            }
        }

        TurnColor myColor = board.getColor(coordinates);
        TurnColor oppositionColor = myColor.opposite();


        boolean noFieldIsUnderSiege = true;
        for (int x = 2; x < 5; x++) {
            for (int[] c : board.getPieces(oppositionColor)) {
                if (board.isValidMove(c[0], c[1], x, 7)) {
                    noFieldIsUnderSiege = false;
                }
            }
        }

        boolean kingIsInCheckAfterMove = false;
        for (int[] c : board.getPieces(oppositionColor)) {
            if (board.isValidMove(c[0], c[1], 2, 7)) {
                kingIsInCheckAfterMove = true;
            }
        }

        boolean leftBlackCastlingAvailable = !kingIsInCheckAfterMove && noFieldIsUnderSiege && !kingHasMoved && !rookLeftHasMoved && noPiecesBetweenThem;
        if (leftBlackCastlingAvailable) {
            moves.add(4);
            moves.add(7);
            moves.add(2);
            moves.add(7);
            moves.add(0);
        }
    }


    private static void addWhiteCastlingIfApplicable(Board board, int[] coordinates, List<Integer> moves) {
        addLeftWhiteCastlingIfApplicable(board, coordinates, moves);
        addRightWhiteCastlingIfApplicable(board, coordinates, moves);
    }

    private static void addRightWhiteCastlingIfApplicable(Board board, int[] coordinates, List<Integer> moves) {
        boolean rightRookExists = board.pieces[7][0] == WHITE_ROOK;
        boolean whiteKingIsAtRightPlace = board.pieces[4][0] == WHITE_KING;
        if (!rightRookExists || !whiteKingIsAtRightPlace) {
            return;
        }

        boolean kingHasMoved = board.blackKingHasMoved;
        boolean rookLeftHasMoved = board.blackRRHasMoved;
        boolean noPiecesBetweenThem = true;
        for (int x = 5; x < 7; x++) {
            if (board.pieces[x][0] != 0) {
                noPiecesBetweenThem = false;
            }
        }

        TurnColor myColor = board.getColor(coordinates);
        TurnColor oppositionColor = myColor.opposite();


        boolean noFieldIsUnderSiege = true;
        for (int x = 4; x < 7; x++) {
            for (int[] c : board.getPieces(oppositionColor)) {
                if (board.isValidMove(c[0], c[1], x, 0)) {
                    noFieldIsUnderSiege = false;
                }
            }
        }

        boolean kingIsInCheckAfterMove = false;
        for (int[] c : board.getPieces(oppositionColor)) {
            if (board.isValidMove(c[0], c[1], 6, 0)) {
                kingIsInCheckAfterMove = true;
            }
        }

        boolean rightBlackCastlingAvailable = !kingIsInCheckAfterMove && noFieldIsUnderSiege && !kingHasMoved && !rookLeftHasMoved && noPiecesBetweenThem;
        if (rightBlackCastlingAvailable) {
            moves.add(4);
            moves.add(0);
            moves.add(6);
            moves.add(0);
            moves.add(0);
        }
    }

    private static void addLeftWhiteCastlingIfApplicable(Board board, int[] coordinates, List<Integer> moves) {
        boolean leftRookExists = board.pieces[0][0] == WHITE_ROOK;
        boolean whiteKingIsAtRightPlace = board.pieces[4][0] == WHITE_KING;
        if (!leftRookExists || !whiteKingIsAtRightPlace) {
            return;
        }


        boolean kingHasMoved = board.whiteKingHasMoved;
        boolean rookLeftHasMoved = board.whiteRLHasMoved;
        boolean noPiecesBetweenThem = true;
        for (int x = 1; x < 4; x++) {
            if (board.pieces[x][0] != 0) {
                noPiecesBetweenThem = false;
            }
        }

        TurnColor myColor = board.getColor(coordinates);
        TurnColor oppositionColor = myColor.opposite();


        boolean noFieldIsUnderSiege = true;
        for (int x = 2; x < 5; x++) {
            for (int[] c : board.getPieces(oppositionColor)) {
                if (board.isValidMove(c[0], c[1], x, 0)) {
                    noFieldIsUnderSiege = false;
                }
            }
        }

        boolean kingIsInCheckAfterMove = false;
        for (int[] c : board.getPieces(oppositionColor)) {
            if (board.isValidMove(c[0], c[1], 2, 0)) {
                kingIsInCheckAfterMove = true;
            }
        }

        boolean leftWhiteCastlingApplicable = !kingIsInCheckAfterMove && noFieldIsUnderSiege && !kingHasMoved && !rookLeftHasMoved && noPiecesBetweenThem;
        if (leftWhiteCastlingApplicable) {
            moves.add(4);
            moves.add(0);
            moves.add(2);
            moves.add(0);
            moves.add(0);
        }
    }

}
