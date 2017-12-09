package chessTests;

import chessControllers.TurnColor;
import chessGame.Queen;
import chessGame.Rook;
import chessGame.StandardBoard;
import junit.framework.TestCase;

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
		Queen newQueen = new Queen(3, 0, TurnColor.black, board);
		assertTrue(newQueen.canMove(7, 0));
	}
	
	/**
	 * Test valid queen horizontal move
	 */
	public void testValidHorizontalQueenMove(){
		StandardBoard board = new StandardBoard();
		Queen newQueen = new Queen(3, 0, TurnColor.black, board);
		assertTrue(newQueen.canMove(5, 0));
	}
	
	/**
	 * Test valid queen diagonal move.
	 */
	public void testValidDiagonalQueenMove(){
		StandardBoard board = new StandardBoard();
		Queen newQueen = new Queen(3, 0, TurnColor.black, board);
		assertTrue(newQueen.canMove(6, 3));
	}
	
	/**
	 * Test blocking piece queen move.
	 */
	public void testBlockingPieceQueenMove(){
		StandardBoard board = new StandardBoard();
		Queen newQueen = new Queen(3, 0, TurnColor.black, board);
		Rook newRook = new Rook(3, 1, TurnColor.white, board);
		assertFalse(newQueen.canMove(3, 3));
	}
	
	/**
	 * TYest invalid queen move.
	 */
	public void testInvalidQueenMove(){
		StandardBoard board = new StandardBoard();
		Queen newQueen = new Queen(1, 2, TurnColor.black, board);
		assertFalse(newQueen.canMove(2, 4));
	}

}
