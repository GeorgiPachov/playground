import chessControllers.Game;
import chessControllers.MiniMaxStrategy;
import chessGame.StandardBoard;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class CheckmateTests {

    private StandardBoard board;
    private Game game;

    @Before
    public void before() {
        this.game = new Game();
        this.board = new StandardBoard();
        this.board.pieces = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.board.pieces[i][j] = 0;
            }
        }

        game.gameBoard = this.board;
    }

    @Test
    public void test1MoveCheckmate() {
        for (int i = 1; i < 5; i++) {
            before();
            board.pieces[0][0] = StandardBoard.BLACK_KING;
            board.pieces[6][6] = StandardBoard.WHITE_KING;

            board.pieces[5][2] = StandardBoard.WHITE_ROOK;
            board.pieces[1][7] = StandardBoard.WHITE_ROOK;

            game.gameBoard.whitePieces.clear();
            game.gameBoard.blackPieces.clear();

            game.gameBoard.findWhitePieces().forEach(whitePiece -> {
                game.gameBoard.whitePieces.put(Arrays.hashCode(whitePiece), whitePiece);
            });

            game.gameBoard.findBlackPieces().forEach(blackPiece -> {
                game.gameBoard.blackPieces.put(Arrays.hashCode(blackPiece), blackPiece);
            });
            board.blackKing = new int[]{0, 0};
            board.whiteKing = new int[]{6, 6};

            MiniMaxStrategy strategy = new MiniMaxStrategy();
            strategy.maxDepth = i;
            strategy.playWhite(game);

            boolean checkMate = game.gameOver;
            Assert.assertTrue(checkMate);
        }
    }

    @Test
    public void test2MoveCheckmate() {

        board.pieces[0][1] = StandardBoard.BLACK_KING;
        board.pieces[6][6] = StandardBoard.WHITE_KING;

        board.pieces[5][2] = StandardBoard.WHITE_ROOK;
        board.pieces[6][7] = StandardBoard.WHITE_ROOK;

        game.gameBoard.whitePieces.clear();
        game.gameBoard.blackPieces.clear();

        game.gameBoard.findWhitePieces().forEach(whitePiece -> {
            game.gameBoard.whitePieces.put(Arrays.hashCode(whitePiece), whitePiece);
        });

        game.gameBoard.findBlackPieces().forEach(blackPiece -> {
            game.gameBoard.blackPieces.put(Arrays.hashCode(blackPiece), blackPiece);
        });
        board.blackKing = new int[] {0, 1};
        board.whiteKing = new int[] {6, 6};
    }
}
