import chessControllers.Game;
import chessControllers.MiniMaxStrategy;
import chessGame.Board;
import org.junit.Assert;
import org.junit.Test;

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

            Assert.assertArrayEquals(new int[] {5,2, 5, 0}, nextWhiteMove);
        }
    }

    @Test
    public void willNotMakeAStupidSacrifice(){

    }
}
