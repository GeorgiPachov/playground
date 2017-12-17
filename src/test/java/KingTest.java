import chessGame.Board;
import junit.framework.TestCase;

import static chessGame.Board.BLACK_KING;
import static chessGame.Board.BLACK_PAWN;
import static chessGame.Board.WHITE_KING;

/**
 * Test cases for the Chancellor
 * @author Pratik Naik
 *
 */
public class KingTest extends TestCase {



	/**
	 * Test valid horizontal move.
	 */
	public void testValidHorizontalKingMove(){
		Board board = new Board();
		board.pieces[4][0] = WHITE_KING;
        board.pieces[5][0] = 0; // remove from board
        assertTrue(board.canMove(4, 0, 5, 0));
	}

	/**
	 * Test valid vertical move.
	 */
	public void testValidVerticalKingMove(){
		Board board = new Board();
		board.pieces[4][1] = 0;
        board.pieces[4][0] = WHITE_KING;
        assertTrue(board.canMove(4, 0, 4, 1));
	}

	/**
	 * Test valid Diagonal move
	 */
	public void testValidDiagonalKingMove(){
		Board board = new Board();
        board.pieces[4][1] = WHITE_KING;
		assertTrue(board.canMove(4, 1, 3, 2));
	}

	/**
	 * Test invalid King move.
	 */
	public void testInvalidKingMove(){
		Board board = new Board();
        board.pieces[3][7] = WHITE_KING;
        assertFalse(board.canMove(3, 7, 3, 5));
	}

	/**
	 * Test ally pice putting king in check
	 */
	public void testInvalidAllyPieceMove(){
		Board board = new Board();
		board.pieces[3][7] = WHITE_KING;
		board.pieces[2][6] = Board.WHITE_PAWN;
		assertFalse(board.canMove(3, 7, 2, 6));
	}

	/**
	 * Test king capturing enemy piece.
	 */
	public void testValidEnemyPieceMove(){
		Board board = new Board();
		board.pieces[3][7] = WHITE_KING;
		board.pieces[2][6] = Board.BLACK_PAWN;
		assertTrue(board.canMove(3, 7, 2, 6));
	}

	/**
	 * Test King putting itself in check
	 */
	public void testInvalidMoveToCheckLocation(){
		Board board = new Board();
		board.pieces = new int[8][8];
		board.pieces[3][7] = BLACK_KING;
		board.pieces[5][5] = Board.WHITE_PAWN;
		assertFalse(board.canMove(3, 7, 4, 6));
	}

	/**
	 * Test if King displays checked status
	 */
	public void testKingInCheck(){
		Board board = new Board();
        board.pieces[3][0] = WHITE_KING;
        board.pieces[4][1] = BLACK_PAWN;
		assertTrue(board.isKingInCheck(new int[] {3,0}));
	}

}
