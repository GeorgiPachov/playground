package chessTests;

import chessControllers.TurnColor;
import chessGame.King;
import chessGame.Pawn;
import chessGame.StandardBoard;
import junit.framework.TestCase;

import static chessGame.StandardBoard.BLACK_KING;
import static chessGame.StandardBoard.BLACK_PAWN;
import static chessGame.StandardBoard.WHITE_KING;

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
		StandardBoard board = new StandardBoard();
		board.pieces[4][0] = WHITE_KING;
        board.pieces[5][0] = 0; // remove from board
        assertTrue(board.canMove(4, 0, 5, 0));
	}

	/**
	 * Test valid vertical move.
	 */
	public void testValidVerticalKingMove(){
		StandardBoard board = new StandardBoard();
		board.pieces[4][1] = 0;
        board.pieces[4][0] = WHITE_KING;
        assertTrue(board.canMove(4, 0, 4, 1));
	}

	/**
	 * Test valid Diagonal move
	 */
	public void testValidDiagonalKingMove(){
		StandardBoard board = new StandardBoard();
        board.pieces[4][1] = WHITE_KING;
		assertTrue(board.canMove(4, 1, 3, 2));
	}

	/**
	 * Test invalid King move.
	 */
	public void testInvalidKingMove(){
		StandardBoard board = new StandardBoard();
        board.pieces[3][7] = WHITE_KING;
        assertFalse(board.canMove(3, 7, 3, 5));
	}

	/**
	 * Test ally pice putting king in check
	 */
	public void testInvalidAllyPieceMove(){
		StandardBoard board = new StandardBoard();
		board.pieces[3][7] = WHITE_KING;
		board.pieces[2][6] = StandardBoard.WHITE_PAWN;
		assertFalse(board.canMove(3, 7, 2, 6));
	}

	/**
	 * Test king capturing enemy piece.
	 */
	public void testValidEnemyPieceMove(){
		StandardBoard board = new StandardBoard();
		board.pieces[3][7] = WHITE_KING;
		board.pieces[2][6] = StandardBoard.BLACK_PAWN;
		assertTrue(board.canMove(3, 7, 2, 6));
	}

	/**
	 * Test King putting itself in check
	 */
	public void testInvalidMoveToCheckLocation(){
		StandardBoard board = new StandardBoard();
		board.pieces = new int[8][8];
		board.pieces[3][7] = BLACK_KING;
		board.pieces[5][5] = StandardBoard.WHITE_PAWN;
		assertFalse(board.canMove(3, 7, 4, 6));
	}

	/**
	 * Test if King displays checked status
	 */
	public void testKingInCheck(){
		StandardBoard board = new StandardBoard();
        board.pieces[3][0] = WHITE_KING;
        board.pieces[4][1] = BLACK_PAWN;
		assertTrue(board.isKingInCheck(new int[] {3,0}));
	}

}
