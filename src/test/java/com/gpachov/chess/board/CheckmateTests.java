package com.gpachov.chess.board;

import com.gpachov.chess.ai.MiniMaxStrategy;
import com.gpachov.chess.board.TurnColor;
import com.gpachov.chess.board.Board;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class CheckmateTests {

    @Test
    public void test1MoveCheckmate() {
        for (int i = 1; i < 5; i++) {
            int[][] pieces = new int[8][8];
            pieces[0][0] = Board.BLACK_KING;
            pieces[6][6] = Board.WHITE_KING;

            pieces[5][2] = Board.WHITE_ROOK;
            pieces[1][7] = Board.WHITE_ROOK;
            Board board = new Board(pieces);

            MiniMaxStrategy strategy = new MiniMaxStrategy();
            strategy.MAX_DEPTH = i;
            int[] move = strategy.playWhite(board);

            assertArrayEquals(move, new int[] {5,2,0,2, 0});
            boolean checkMate = board.isKingCheckmate(new int[] {0, 0});
            Assert.assertTrue(checkMate);
        }
    }

    @Test
    public void test2MoveCheckmate() {
        for (int i = 3; i < 4; i++) {

            int[][] pieces = new int[8][8];
            pieces[0][1] = Board.BLACK_KING;
            pieces[6][6] = Board.WHITE_KING;

            pieces[5][2] = Board.WHITE_ROOK;
            pieces[6][7] = Board.WHITE_ROOK;
            Board board = new Board(pieces);

            MiniMaxStrategy strategy = new MiniMaxStrategy();
//            strategy.MAX_DEPTH = i;
            int[] m1 = strategy.playWhite(board);
            assertArrayEquals(m1, new int[] {6, 7, 1, 7, 0});
            System.out.println("Move 1 : " + Arrays.toString(m1));
            int[] m2 = strategy.playBlack(board);
            assertArrayEquals(m2, new int[] {0, 1, 0, 0, 0});
            System.out.println("Move 2(black) : " + Arrays.toString(m2));
            int[] m3 = strategy.playWhite(board);
            System.out.println("Move 3 : " + Arrays.toString(m3));

            boolean checkMate = board.isKingCheckmate(board.getKing(TurnColor.black));
            Assert.assertTrue(checkMate);
        }
    }

    @Test
    public void test9MoveCheckmate() {
        int[][] pieces = new int[8][8];
        pieces[3][4] = Board.BLACK_KING;
        pieces[7][7] = Board.WHITE_KING;

        pieces[1][5] = Board.WHITE_ROOK;
        pieces[6][7] = Board.WHITE_ROOK;
        Board board = new Board(pieces);

        MiniMaxStrategy strategy = new MiniMaxStrategy();
        for (int j = 0; j < 50; j++) {
            if (j % 2 == 0) {
                strategy.playWhite(board);
            } else {
                strategy.playBlack(board);
            }
        }
        boolean checkMate = board.isKingCheckmate(board.getKing(TurnColor.black));
        Assert.assertTrue(checkMate);
    }


    @Test
    public void testKingIsInCheck() {
        int[][] pieces = new int[8][8];
        pieces[0][0] = Board.BLACK_KING;
        pieces[6][6] = Board.WHITE_KING;

        pieces[3][0] = Board.WHITE_ROOK;
        Board board = new Board(pieces);

        boolean isKingIncheck = board.isKingInCheck(new int[]{0,0});
        Assert.assertTrue(isKingIncheck);
    }

    @Test
    public void testKingIsNotInCheck() {
        int[][] pieces = new int[8][8];
        pieces[0][4] = Board.BLACK_KING;
        pieces[6][6] = Board.WHITE_KING;

        pieces[3][0] = Board.WHITE_ROOK;
        Board board = new Board(pieces);

        boolean isKingIncheck = board.isKingInCheck(new int[]{0,4});
        Assert.assertFalse(isKingIncheck);
    }

    @Test
    public void testIsCheckmate() {
        int[][] pieces = new int[8][8];
        pieces[0][0] = Board.BLACK_KING;
        pieces[0][2] = Board.WHITE_KING;

        pieces[3][0] = Board.WHITE_ROOK;
        Board board = new Board(pieces);

        boolean isKingInCheckmate = board.isKingCheckmate(new int[]{0,0});
        Assert.assertTrue(isKingInCheckmate);
    }

    @Test
    public void testIsNotCheckmate() {
        int[][] pieces = new int[8][8];
        pieces[0][0] = Board.BLACK_KING;
        pieces[0][3] = Board.WHITE_KING;

        pieces[3][0] = Board.WHITE_ROOK;
        Board board = new Board(pieces);

        boolean isKingInCheckmate = board.isKingCheckmate(new int[]{0,0});
        Assert.assertFalse(isKingInCheckmate);
    }

    public void testIsInCheckByRook() {
        int[][] pieces = new int[8][8];
        pieces[0][0] = Board.BLACK_KING;
        pieces[0][1] = Board.BLACK_BISHOP;
        pieces[1][0] = Board.BLACK_ROOK;

    }

    @Test
    public void testCanProtectSelf() {
        int[][] pieces = new int[8][8];
        pieces[0][0] = Board.BLACK_KING;
        pieces[0][1] = Board.BLACK_BISHOP;
        pieces[1][0] = Board.BLACK_ROOK;


        pieces[7][7] = Board.WHITE_KING;
        pieces[2][2] = Board.WHITE_BISHOP;
        Board board = new Board(pieces);

        boolean isKingInCheckmate = board.isKingCheckmate(new int[]{0,0});
        int[] move = new MiniMaxStrategy().playBlack(board);
        Assert.assertNotNull(move);
        Assert.assertFalse(isKingInCheckmate);
    }

}
