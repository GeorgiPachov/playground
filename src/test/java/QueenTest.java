import chessGame.Board;
import junit.framework.TestCase;

import static chessGame.Board.BLACK_KING;
import static chessGame.Board.BLACK_QUEEN;
import static chessGame.Board.BLACK_ROOK;

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

		Board board = new Board();
        board.pieces = new int[8][8];
        board.pieces[0][0] = BLACK_KING;
        board.pieces[3][0] = BLACK_QUEEN;
		assertTrue(board.canMove(3, 0,7, 0));
	}

	/**
	 * Test valid queen horizontal move
	 */
	public void testValidHorizontalQueenMove(){
		Board board = new Board();
        board.pieces = new int[8][8];
        board.pieces[0][0] = BLACK_KING;
        board.pieces[3][0] = BLACK_QUEEN;

        assertTrue(board.canMove(3, 0, 5, 0));
	}

	/**
	 * Test valid queen diagonal move.
	 */
	public void testValidDiagonalQueenMove(){
        Board board = new Board();
        board.pieces = new int[8][8];
        board.pieces[0][0] = BLACK_KING;
        board.pieces[3][0] = BLACK_QUEEN;
		assertTrue(board.canMove(3, 0, 6, 3));
	}

	/**
	 * Test blocking piece queen move.
	 */
	public void testBlockingPieceQueenMove(){
        Board board = new Board();
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
        Board board = new Board();
        board.pieces = new int[8][8];
        board.pieces[0][0] = BLACK_KING;
        board.pieces[1][2] = BLACK_QUEEN;
		assertFalse(board.canMove(1, 2, 2, 4));
	}

}
