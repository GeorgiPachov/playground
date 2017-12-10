package chessTests;

import chessControllers.TurnColor;
import chessGame.Pawn;
import chessGame.StandardBoard;
import junit.framework.TestCase;

import static chessGame.StandardBoard.BLACK_KING;
import static chessGame.StandardBoard.BLACK_PAWN;
import static chessGame.StandardBoard.WHITE_PAWN;

/**
 * Test cases for the Pawn
 * @author Pratik Naik
 *
 */
public class PawnTest extends TestCase {

	/**
	 * Test valid white pawn move.
	 */
	public void testValidWhitePawnMove(){
		StandardBoard board = new StandardBoard();
		assertTrue(board.canMove(0, 1, 0, 2));
	}

	/**
	 * Test valid black pawn move.
	 */
	public void testValidBlackPawnMove(){
		StandardBoard board = new StandardBoard();
		board.pieces = new int[8][8];
        board.pieces[0][1] = BLACK_PAWN;
        board.pieces[0][5] = BLACK_KING;
        assertTrue(board.canMove(0, 1 , 0, 0));
	}

	/**
	 * Test invalid pawn move.
	 */
	public void testInvalidPawnMove(){
		StandardBoard board = new StandardBoard();
        board.pieces = new int[8][8];
        board.pieces[1][4] = BLACK_PAWN;

        assertFalse(board.canMove(1,4, 1, 6));
	}

	/**
	 * Test valid enemy piece move/capture.
	 */
	public void testValidEnemyPieceMove(){
		StandardBoard board = new StandardBoard();
        board.pieces[2][2] = WHITE_PAWN;
        board.pieces[3][3] = BLACK_PAWN;

        assertTrue(board.canMove(2, 2, 3, 3));
	}

	/**
	 * Test valid First white pawn move.
	 */
	public void testValidFirstPawnMove(){
		StandardBoard board = new StandardBoard();
        board.pieces[0][1] = WHITE_PAWN;
		assertTrue(board.canMove(0,1, 0, 3));
	}

	/**
	 * Test valid First black pawn move.
	 */
	public void testValidFirstBlackPawnMove(){
		StandardBoard board = new StandardBoard();
        board.pieces[3][6] = BLACK_PAWN;
        assertTrue(board.canMove(3, 6,3, 4));
	}

	/**
	 * Test valid black pawn first move.
	 */
	public void testValidFirstPawnEnemyMove(){
		StandardBoard board = new StandardBoard();
        board.pieces[0][1] = WHITE_PAWN;
        board.pieces[1][2] = BLACK_PAWN;
		assertTrue(board.canMove(0, 1, 1, 2));
	}

}
