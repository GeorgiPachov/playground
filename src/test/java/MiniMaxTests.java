import chessControllers.Game;
import chessControllers.MiniMaxStrategy;
import chessGame.Board;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MiniMaxTests {

    @Test
    public void testWillTakeFreeQueen() {
        for (int i = 1; i < 5; i++) {
            Game game = new Game();

            int[][] pieces = new int[8][8];
            pieces[0][0] = Board.BLACK_KING;
            pieces[6][6] = Board.WHITE_KING;

            pieces[5][2] = Board.WHITE_ROOK;
            pieces[5][0] = Board.BLACK_QUEEN;
            game.board = new Board(pieces);

            MiniMaxStrategy strategy = new MiniMaxStrategy();
            strategy.maxDepth = i;
            int[] nextWhiteMove = strategy.playWhite(game);

            Assert.assertArrayEquals(new int[] {5,2,5,0}, nextWhiteMove);
        }
    }

    @Test
    public void willNotMakeAQueenSacrifice(){
        for (int i = 2; i < 3; i++) {
            Game game = new Game();

            int[][] pieces = new int[8][8];
            pieces[0][0] = Board.BLACK_KING;
            pieces[1][0] = Board.BLACK_PAWN;
            pieces[0][1] = Board.BLACK_PAWN;
            pieces[1][1] = Board.BLACK_PAWN;

            pieces[6][6] = Board.WHITE_KING;

            pieces[5][2] = Board.WHITE_QUEEN;
            pieces[7][2] = Board.BLACK_ROOK;
            pieces[6][3] = Board.BLACK_BISHOP;
            game.board = new Board(pieces);

            MiniMaxStrategy strategy = new MiniMaxStrategy();
            strategy.maxDepth = i;
            int[] nextWhiteMove = strategy.playWhite(game);

            System.out.println(Arrays.toString(nextWhiteMove));
            if (Arrays.equals(nextWhiteMove, new int[] {5,2, 7,2})){
                Assert.fail("Attempted to take the rook, which is protected by a pawn!");
            }
        }
    }

    @Test
    public void willNotSacrificeBishopForPawn(){
        for (int i = 2; i < 6; i++) {
            Game game = new Game();

            int[][] pieces = new int[8][8];
            pieces[0][0] = Board.BLACK_KING;
            pieces[6][6] = Board.WHITE_KING;

            pieces[5][4] = Board.BLACK_PAWN;
            pieces[4][3] = Board.BLACK_PAWN;
            pieces[2][5] = Board.WHITE_BISHOP;
            game.board = new Board(pieces);

            MiniMaxStrategy strategy = new MiniMaxStrategy();
            strategy.maxDepth = i;
            int[] nextWhiteMove = strategy.playWhite(game);

            System.out.println(Arrays.toString(nextWhiteMove));
            if (Arrays.equals(nextWhiteMove, new int[] {2,5, 4,3})) {
                Assert.fail("Attempted to take the rook, which is protected by a pawn!");
            }
        }
    }

    @Test
    public void willAlwaysChooseAMove() {
        for (int i = 2; i < 6; i++) {
            Game game = new Game();

            int[][] pieces = new int[8][8];
            pieces[0][0] = Board.BLACK_KING;
            pieces[6][6] = Board.WHITE_KING;

            game.board = new Board(pieces);

            MiniMaxStrategy strategy = new MiniMaxStrategy();
            strategy.maxDepth = i;
            for (int j = 0; j < 100; j++) {
                int[] nextMove = null;
                if (j%2 == 0) {
                    nextMove = strategy.playWhite(game);
                    if (nextMove == null) {
                        Assert.fail("Next move was null!!!!");
                    }
                }
                if (j%2 == 1) {
                    nextMove = strategy.playBlack(game);
                    if (nextMove == null) {
                        Assert.fail("Next move was null!!!!");
                    }
                }
            }
        }
    }
}
