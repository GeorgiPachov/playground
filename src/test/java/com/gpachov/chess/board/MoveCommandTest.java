package com.gpachov.chess.board;

import com.fluxchess.jcpi.commands.EngineNewGameCommand;
import com.gpachov.chess.ChessEngine;
import com.gpachov.chess.board.MoveCommand;
import com.gpachov.chess.board.Board;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.gpachov.chess.board.Board.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MoveCommandTest {

    @Test
    public void testMovePawnAndUndo() {
        int[][] pieces = new int[8][8];
        pieces[0][0] = Board.BLACK_KING;
        pieces[6][6] = Board.WHITE_KING;

        pieces[5][2] = WHITE_PAWN;
        pieces[5][0] = WHITE_PAWN;
        Board board = new Board(pieces);

        MoveCommand command = new MoveCommand(board, new int[] {5,2, 5, 3, 0});
        command.execute();
        assertEquals(pieces[5][2], 0);
        assertEquals(pieces[5][3], WHITE_PAWN);

        command.undo();
        assertEquals(pieces[5][2], WHITE_PAWN);
        assertEquals(pieces[5][3], 0);
    }

    @Test
    public void testTakePieceAndUndo() {
        int[][] pieces = new int[8][8];
        pieces[0][0] = Board.BLACK_KING;
        pieces[6][6] = Board.WHITE_KING;

        pieces[5][2] = BLACK_PAWN;
        pieces[5][0] = WHITE_ROOK;
        Board board = new Board(pieces);

        MoveCommand command = new MoveCommand(board, new int[] {5,0, 5, 2, 0});
        command.execute();
        assertEquals(pieces[5][0], 0);
        assertEquals(pieces[5][2], WHITE_ROOK);

        command.undo();
        assertEquals(pieces[5][2], BLACK_PAWN);
        assertEquals(pieces[5][0], WHITE_ROOK);
    }

    @Test
    public void testPromotionAndUndo() {
        int[][] pieces = new int[8][8];
        pieces[0][0] = Board.BLACK_KING;
        pieces[6][6] = Board.WHITE_KING;

        pieces[5][1] = BLACK_PAWN;
        Board board = new Board(pieces);

        MoveCommand command = new MoveCommand(board, new int[] {5,1, 5, 0, BLACK_QUEEN});
        command.execute();
        assertEquals(pieces[5][1], 0);
        assertEquals(pieces[5][0], BLACK_QUEEN);

        command.undo();
        assertEquals(pieces[5][1], BLACK_PAWN);
        assertEquals(pieces[5][0], 0);
    }

    @Test
    public void testTakePieceAndPromoted() {
        int[][] pieces = new int[8][8];
        pieces[0][0] = Board.BLACK_KING;
        pieces[6][6] = Board.WHITE_KING;

        pieces[5][1] = BLACK_PAWN;
        pieces[4][0] = WHITE_ROOK;

        Board board = new Board(pieces);

        MoveCommand command = new MoveCommand(board, new int[] {5,1, 4, 0, BLACK_QUEEN});
        command.execute();
        assertEquals(pieces[5][1], 0);
        assertEquals(pieces[4][0], BLACK_QUEEN);

        command.undo();
        assertEquals(pieces[5][1], BLACK_PAWN);
        assertEquals(pieces[4][0], WHITE_ROOK);
    }

    @Test
    public void castlingTest() throws FileNotFoundException {
        ChessEngine chessEngine = new ChessEngine();
        chessEngine.receive(new EngineNewGameCommand());
        Board board = chessEngine.board;
        int[][] moves = new int[][] {
                {4,1,4,3,0}, //w
//                {0,7,0,6,0}, //b
                {6,0,5,2,0}, //w
//                {0,6,0,5,0}, //b
                {5,0,4,1,0}, //w
//                {0,5,0,4,0}, //b
                {4,0,6,0,0}, //w

                {7,1, 7, 2, 0}
        };
        for (int[] move : moves) {
            System.out.println(board);
            System.out.println("---------------------");
            board.executeMove(move);
            System.out.println(board);
            System.out.println("---------------------");
            chessEngine.playingStrategy.playBlack(board);
        }
    }
}
