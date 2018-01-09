import chessControllers.Game;
import chessControllers.MiniMaxStrategy;
import chessControllers.TurnColor;
import chessGame.Board;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

public class CheckmateTests {

    @Test
    public void test1MoveCheckmate() {
        for (int i = 1; i < 5; i++) {
            Game game = new Game();

            int[][] pieces = new int[8][8];
            pieces[0][0] = Board.BLACK_KING;
            pieces[6][6] = Board.WHITE_KING;

            pieces[5][2] = Board.WHITE_ROOK;
            pieces[1][7] = Board.WHITE_ROOK;
            game.board = new Board(pieces);

            MiniMaxStrategy strategy = new MiniMaxStrategy();
            strategy.maxDepth = i;
            int[] move = strategy.playWhite(game);

            Assert.assertArrayEquals(move, new int[] {5,2,0,2});
            boolean checkMate = game.board.isKingCheckmate(new int[] {0, 0});
            Assert.assertTrue(checkMate);
        }
    }

    @Test
    public void test2MoveCheckmate() {
        for (int i = 3; i < 4; i++) {
            Game game = new Game();

            int[][] pieces = new int[8][8];
            pieces[0][1] = Board.BLACK_KING;
            pieces[6][6] = Board.WHITE_KING;

            pieces[5][2] = Board.WHITE_ROOK;
            pieces[6][7] = Board.WHITE_ROOK;
            game.board = new Board(pieces);

            MiniMaxStrategy strategy = new MiniMaxStrategy();
            strategy.maxDepth = i;
            int[] m1 = strategy.playWhite(game);
            System.out.println("Move 1 : " + Arrays.toString(m1));
            int[] m2 = strategy.playBlack(game);
            System.out.println("Move 2(black) : " + Arrays.toString(m2));
            int[] m3 = strategy.playWhite(game);
            System.out.println("Move 3 : " + Arrays.toString(m3));

            boolean checkMate = game.board.isKingCheckmate(game.board.getKing(TurnColor.black));
            Assert.assertTrue(checkMate);
        }
    }

    @Test
    public void testKingIsInCheck() {

        Game game = new Game();

        int[][] pieces = new int[8][8];
        pieces[0][0] = Board.BLACK_KING;
        pieces[6][6] = Board.WHITE_KING;

        pieces[3][0] = Board.WHITE_ROOK;
        game.board = new Board(pieces);

        boolean isKingIncheck = game.board.isKingInCheck(new int[]{0,0});
        Assert.assertTrue(isKingIncheck);
    }

    @Test
    public void testKingIsNotInCheck() {
        Game game = new Game();

        int[][] pieces = new int[8][8];
        pieces[0][4] = Board.BLACK_KING;
        pieces[6][6] = Board.WHITE_KING;

        pieces[3][0] = Board.WHITE_ROOK;
        game.board = new Board(pieces);

        boolean isKingIncheck = game.board.isKingInCheck(new int[]{0,4});
        Assert.assertFalse(isKingIncheck);
    }

    @Test
    public void testIsCheckmate() {
        Game game = new Game();

        int[][] pieces = new int[8][8];
        pieces[0][0] = Board.BLACK_KING;
        pieces[0][2] = Board.WHITE_KING;

        pieces[3][0] = Board.WHITE_ROOK;
        game.board = new Board(pieces);

        boolean isKingInCheckmate = game.board.isKingCheckmate(new int[]{0,0});
        Assert.assertTrue(isKingInCheckmate);
    }

    @Test
    public void testIsNotCheckmate() {
        Game game = new Game();

        int[][] pieces = new int[8][8];
        pieces[0][0] = Board.BLACK_KING;
        pieces[0][3] = Board.WHITE_KING;

        pieces[3][0] = Board.WHITE_ROOK;
        game.board = new Board(pieces);

        boolean isKingInCheckmate = game.board.isKingCheckmate(new int[]{0,0});
        Assert.assertFalse(isKingInCheckmate);
    }

}
