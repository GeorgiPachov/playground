package chessTests;

import chessControllers.TurnColor;
import chessGame.Queen;
import chessGame.Rook;
import chessGame.StandardBoard;
import junit.framework.TestCase;

import static chessGame.StandardBoard.BLACK_KING;
import static chessGame.StandardBoard.BLACK_QUEEN;
import static chessGame.StandardBoard.BLACK_ROOK;

/**
 * Test cases for the Queen
 * @author Pratik Naik
 *
 */
public class QueenTest extends TestCase {

	/**
	 * Test valid vertical queen move.
	 */
	public void testValidVerticalQueenMove(){

		StandardBoard board = new StandardBoard();
        board.pieces = new int[8][8];
        board.pieces[0][0] = BLACK_KING;
        board.pieces[3][0] = BLACK_QUEEN;
		assertTrue(board.canMove(3, 0,7, 0));
	}

	/**
	 * Test valid queen horizontal move
	 */
	public void testValidHorizontalQueenMove(){
		StandardBoard board = new StandardBoard();
        board.pieces = new int[8][8];
        board.pieces[0][0] = BLACK_KING;
        board.pieces[3][0] = BLACK_QUEEN;

        assertTrue(board.canMove(3, 0, 5, 0));
	}

	/**
	 * Test valid queen diagonal move.
	 */
	public void testValidDiagonalQueenMove(){
        StandardBoard board = new StandardBoard();
        board.pieces = new int[8][8];
        board.pieces[0][0] = BLACK_KING;
        board.pieces[3][0] = BLACK_QUEEN;
		assertTrue(board.canMove(3, 0, 6, 3));
	}

	/**
	 * Test blocking piece queen move.
	 */
	public void testBlockingPieceQueenMove(){
        StandardBoard board = new StandardBoard();
        board.pieces = new int[8][8];
        board.pieces[3][0] = BLACK_QUEEN;
        board.pieces[0][0] = BLACK_KING;
        board.pieces[3][1] = BLACK_ROOK;
		assertFalse(board.canMove(3, 0, 3, 3));
	}

	/**
	 * TYest invalid queen move.
	 */
	public void testInvalidQueenMove(){
        StandardBoard board = new StandardBoard();
        board.pieces = new int[8][8];
        board.pieces[0][0] = BLACK_KING;
        board.pieces[1][2] = BLACK_QUEEN;
		assertFalse(board.canMove(1, 2, 2, 4));
	}

}
