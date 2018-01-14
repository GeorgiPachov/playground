import chessControllers.Game;
import chessControllers.MiniMaxStrategy;
import chessControllers.MoveCommand;
import chessGame.Board;
import org.junit.Assert;
import org.junit.Test;

import static chessGame.Board.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MoveCommandTest {

    @Test
    public void testMovePawnAndUndo() {
        Game game = new Game();

        int[][] pieces = new int[8][8];
        pieces[0][0] = Board.BLACK_KING;
        pieces[6][6] = Board.WHITE_KING;

        pieces[5][2] = Board.WHITE_PAWN;
        pieces[5][0] = Board.WHITE_PAWN;
        game.board = new Board(pieces);

        MoveCommand command = new MoveCommand(game.board, new int[] {5,2, 5, 3, 0});
        command.execute();
        assertEquals(pieces[5][2], 0);
        assertEquals(pieces[5][3], WHITE_PAWN);

        command.undo();
        assertEquals(pieces[5][2], WHITE_PAWN);
        assertEquals(pieces[5][3], 0);
    }

    @Test
    public void testTakePieceAndUndo() {
        Game game = new Game();

        int[][] pieces = new int[8][8];
        pieces[0][0] = Board.BLACK_KING;
        pieces[6][6] = Board.WHITE_KING;

        pieces[5][2] = Board.BLACK_PAWN;
        pieces[5][0] = Board.WHITE_ROOK;
        game.board = new Board(pieces);

        MoveCommand command = new MoveCommand(game.board, new int[] {5,0, 5, 2, 0});
        command.execute();
        assertEquals(pieces[5][0], 0);
        assertEquals(pieces[5][2], WHITE_ROOK);

        command.undo();
        assertEquals(pieces[5][2], BLACK_PAWN);
        assertEquals(pieces[5][0], WHITE_ROOK);
    }

    @Test
    public void testPromotionAndUndo() {
        Game game = new Game();

        int[][] pieces = new int[8][8];
        pieces[0][0] = Board.BLACK_KING;
        pieces[6][6] = Board.WHITE_KING;

        pieces[5][1] = Board.BLACK_PAWN;
        game.board = new Board(pieces);

        MoveCommand command = new MoveCommand(game.board, new int[] {5,1, 5, 0, BLACK_QUEEN});
        command.execute();
        assertEquals(pieces[5][1], 0);
        assertEquals(pieces[5][0], BLACK_QUEEN);

        command.undo();
        assertEquals(pieces[5][1], BLACK_PAWN);
        assertEquals(pieces[5][0], 0);
    }

    @Test
    public void testTakePieceAndPromoted() {
        Game game = new Game();

        int[][] pieces = new int[8][8];
        pieces[0][0] = Board.BLACK_KING;
        pieces[6][6] = Board.WHITE_KING;

        pieces[5][1] = Board.BLACK_PAWN;
        pieces[4][0] = Board.WHITE_ROOK;

        game.board = new Board(pieces);

        MoveCommand command = new MoveCommand(game.board, new int[] {5,1, 4, 0, BLACK_QUEEN});
        command.execute();
        assertEquals(pieces[5][1], 0);
        assertEquals(pieces[4][0], BLACK_QUEEN);

        command.undo();
        assertEquals(pieces[5][1], BLACK_PAWN);
        assertEquals(pieces[4][0], WHITE_ROOK);
    }
}
