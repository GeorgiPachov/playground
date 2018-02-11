package com.gpachov.chess.ai;

import com.gpachov.chess.Util;
import com.gpachov.chess.ai.MiniMaxStrategy;
import com.gpachov.chess.board.Board;
import com.gpachov.chess.board.Constants;
import com.gpachov.chess.board.TurnColor;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.gpachov.chess.board.Board.BLACK_PAWN;
import static com.gpachov.chess.board.Board.WHITE_PAWN;

public class MiniMaxTests {

    @Test
    public void testWillTakeFreeQueen() {
        int[][] pieces = new int[8][8];
        pieces[0][0] = Board.BLACK_KING;
        pieces[6][6] = Board.WHITE_KING;

        pieces[5][2] = Board.WHITE_ROOK;
        pieces[5][0] = Board.BLACK_QUEEN;
        Board board = new Board(pieces);

        MiniMaxStrategy strategy = new MiniMaxStrategy();
        int[] nextWhiteMove = strategy.playWhite(board);

        Assert.assertArrayEquals(new int[] {5,2,5,0,0}, nextWhiteMove);
    }

    @Test
    public void willNotMakeAQueenSacrifice(){
        for (int i = 2; i < 3; i++) {
            int[][] pieces = new int[8][8];
            pieces[0][0] = Board.BLACK_KING;
            pieces[1][0] = Board.BLACK_PAWN;
            pieces[0][1] = Board.BLACK_PAWN;
            pieces[1][1] = Board.BLACK_PAWN;

            pieces[6][6] = Board.WHITE_KING;

            pieces[5][2] = Board.WHITE_QUEEN;
            pieces[7][2] = Board.BLACK_ROOK;
            pieces[6][3] = Board.BLACK_BISHOP;
            Board board = new Board(pieces);

            MiniMaxStrategy strategy = new MiniMaxStrategy();
//            strategy.MAX_DEPTH = i;
            int[] nextWhiteMove = strategy.playWhite(board);

            System.out.println(Arrays.toString(nextWhiteMove));
            if (Arrays.equals(nextWhiteMove, new int[] {5,2, 7,2, 0})){
                Assert.fail("Attempted to take the rook, which is protected by a pawn!");
            }
        }
    }

    @Test
    public void willNotSacrificeBishopForPawn(){
        for (int i = 2; i < 6; i++) {
            int[][] pieces = new int[8][8];
            pieces[0][0] = Board.BLACK_KING;
            pieces[6][6] = Board.WHITE_KING;

            pieces[5][4] = Board.BLACK_PAWN;
            pieces[4][3] = Board.BLACK_PAWN;
            pieces[2][5] = Board.WHITE_BISHOP;
            Board board = new Board(pieces);

            MiniMaxStrategy strategy = new MiniMaxStrategy();
            strategy.MAX_DEPTH = i;
            int[] nextWhiteMove = strategy.playWhite(board);

            System.out.println(Arrays.toString(nextWhiteMove));
            if (Arrays.equals(nextWhiteMove, new int[] {2,5, 4,3, 0})) {
                Assert.fail("Attempted to take the rook, which is protected by a pawn!");
            }
        }
    }

    @Test
    public void blackWillTakeEnPassant(){
        Board board = Board.testBoard();
        board.pieces[5][3] = BLACK_PAWN;
        board.pieces[4][1] = WHITE_PAWN;

        board.executeMove(new int[]{4,1, 4,3, 0});
        int[] move = new MiniMaxStrategy().playBlack(board);
        System.out.println(Arrays.toString(move));
        Assert.assertArrayEquals(new int[] {5, 3, 4, 2, 0}, move);
    }

    @Test
    public void whiteWillTakeEnPassant(){
        Board board = Board.testBoard();
        board.pieces[1][6] = BLACK_PAWN;
        board.pieces[0][4] = WHITE_PAWN;

        board.executeMove(new int[]{1,6, 1, 4, 0});
        int[] move = new MiniMaxStrategy().playWhite(board);
        Assert.assertArrayEquals(new int[] {0, 4, 1, 5, 0}, move);
    }

    @Test
    public void willAlwaysChooseAMove() {
        for (int i = 2; i < 6; i++) {
            int[][] pieces = new int[8][8];
            pieces[0][0] = Board.BLACK_KING;
            pieces[6][6] = Board.WHITE_KING;

            Board board = new Board(pieces);

            MiniMaxStrategy strategy = new MiniMaxStrategy();
            strategy.MAX_DEPTH = i;
            for (int j = 0; j < 100; j++) {
                if (board.metadata.gameOver) {
                    break;
                }
                int[] nextMove = null;
                if (j%2 == 0) {
                    nextMove = strategy.playWhite(board);
                    if (nextMove == null) {
                        Assert.fail("Next move was null!!!!");
                    }
                }
                if (board.metadata.gameOver) {
                    break;
                }
                if (j%2 == 1) {
                    nextMove = strategy.playBlack(board);
                    if (nextMove == null) {
                        Assert.fail("Next move was null!!!!");
                    }
                }
            }
        }
    }

    @Test
    @Ignore
    public void perftTest() {
        Constants.BEAM_WIDTH = Integer.MAX_VALUE;
        for (int i = 0; i < 6; i++) {
            Board board = new Board();
            long score = perft(board, i, TurnColor.white);
            System.out.println("Expected: " + getExpected(i) + ", actual" + score);
            Assert.assertEquals(getExpected(i), score);
        }
    }

    private long getExpected(int level) {
        int[] expectations = new int[] {
                1, 20, 400, 8902, 197281, 4865609
        };
        return expectations[level];
    }

    private long perft(Board board, int depth, TurnColor color) {
        if (depth == 0) {
            return 1;
        }
        long nodes = 0;
        List<Integer> possibleMoves = new ArrayList<>();
        board.populatePossibleMoves(color, possibleMoves);
        List<int[]> moves = Util.pack(possibleMoves);
        for (int[] move: moves) {
            board.executeMove(move);
            nodes += perft(board, depth-1, color.opposite());
            board.undoMove();
        }
        return nodes;
    }
}
