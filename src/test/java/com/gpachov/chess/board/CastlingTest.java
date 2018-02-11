package com.gpachov.chess.board;

import com.gpachov.chess.board.Board;
import com.gpachov.chess.board.Castling;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.gpachov.chess.board.Board.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CastlingTest {

    @Test
    public void testWhiteLeftCastling() {
        // test forward case
        Board board = testCaseWL();
        int[][] pieces = board.pieces;
        List<Integer> moves = new ArrayList<>();
        int[] kingCoordinates = {4, 0};
        Castling.addCastlingIfApplicable(board, kingCoordinates, moves);
        assertEquals(5, moves.size());
        moves.clear();


        // negative tests

        // test piece between
        pieces[2][0] = WHITE_BISHOP;
        Castling.addCastlingIfApplicable(board, kingCoordinates, moves);
        assertEquals(0, moves.size());
        pieces[2][0] = 0;
        moves.clear();

        // test king has moved
        board.whiteKingHasMoved = true;
        Castling.addCastlingIfApplicable(board, kingCoordinates, moves);
        assertEquals(0, moves.size());
        moves.clear();
        board.whiteKingHasMoved = false;

        // test rook has moved
        board.whiteRLHasMoved = true;
        Castling.addCastlingIfApplicable(board, kingCoordinates, moves);
        assertEquals(0, moves.size());
        moves.clear();
        board.whiteRLHasMoved = false;

        // test king is in check
        board.pieces[4][3] = BLACK_ROOK;
        Castling.addCastlingIfApplicable(board, kingCoordinates, moves);
        assertEquals(0, moves.size());
        board.pieces[4][3] = 0;
        moves.clear();


        // test contested field
        board.pieces[2][3] = BLACK_ROOK;
        Castling.addCastlingIfApplicable(board, kingCoordinates, moves);
        assertEquals(0, moves.size());
        board.pieces[2][3] = 0;
        moves.clear();

        // test king in check after swap
        board.pieces[4][2] = BLACK_BISHOP;
        Castling.addCastlingIfApplicable(board, kingCoordinates, moves);
        assertEquals(0, moves.size());
        board.pieces[4][2] = 0;
        moves.clear();;
    }

    private Board testCaseWL() {
        int[][] pieces = new int[8][8];
        pieces[0][0] = WHITE_ROOK;
        pieces[4][0] = WHITE_KING;
        pieces[7][7] = BLACK_KING;
        Board board = new Board(pieces);
        return board;
    }

    @Test
    public void testWhiteRightCastling() {
        int[][] pieces = new int[8][8];
        pieces[7][0] = WHITE_ROOK;
        pieces[4][0] = WHITE_KING;
        pieces[7][7] = BLACK_KING;
        Board board = new Board(pieces);

        List<Integer> moves = new ArrayList<>();
        Castling.addCastlingIfApplicable(board, new int[] {4, 0}, moves);
        assertTrue(moves.size() == 5);
    }

    @Test
    public void testBlackLeftCastling() {
        int[][] pieces = new int[8][8];
        pieces[0][0] = WHITE_ROOK;
        pieces[4][0] = WHITE_KING;
        pieces[7][7] = BLACK_KING;
        Board board = new Board(pieces);

        List<Integer> moves = new ArrayList<>();
        Castling.addCastlingIfApplicable(board, new int[] {4, 0}, moves);
        assertTrue(moves.size() == 5);
    }

    @Test
    public void testBlackRightCastling() {
        int[][] pieces = new int[8][8];
        pieces[0][7] = BLACK_ROOK;
        pieces[4][0] = WHITE_KING;
        pieces[4][7] = BLACK_KING;
        Board board = new Board(pieces);

        List<Integer> moves = new ArrayList<>();
        Castling.addCastlingIfApplicable(board, new int[] {4, 7}, moves);
        assertEquals(5, moves.size());
    }
}
