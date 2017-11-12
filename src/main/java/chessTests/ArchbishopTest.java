package chessTests;

import chessGame.Archbishop;
import chessGame.Pawn;
import chessGame.StandardBoard;
import chessGame.Board.TurnColor;
import junit.framework.TestCase;

/**
 * Tests for the Archbishop Piece.
 * @author Pratik Naik
 *
 */
public class ArchbishopTest extends TestCase {
	
	/**
	 * Test valid Archbishop move
	 */
	public void testValidArchBishopMove() {
		StandardBoard board = new StandardBoard(8,8);
		Archbishop testBishop = new Archbishop(5, 3, TurnColor.black, board);
		assertTrue(testBishop.canMove(1, 7));	
	}
		
	/**
	 * Test Invalid Arch Bishop Move
	 */
	public void testInvalidArchBishopMove(){
		StandardBoard board = new StandardBoard(8,8);
		Archbishop testBishop = new Archbishop(5, 3, TurnColor.white, board);
		assertFalse(testBishop.canMove(4, 6));
	}
	
	/**
	 * Test valid Arch Knight move.
	 */	
	public void testValidArchKnightMove() {
		StandardBoard board = new StandardBoard(8,8);
		Archbishop testKnight = new Archbishop(1, 0, TurnColor.black, board);
		assertTrue(testKnight.canMove(2, 2));	
	}
	
	/**
	 * Test invalid Arch Knight move
	 */
	public void testInvalidArchKnightMove(){
		StandardBoard board = new StandardBoard(8,8);
		Archbishop testKnight = new Archbishop(6, 2, TurnColor.white, board);
		assertFalse(testKnight.canMove(6, 1));
	}
	
	/**
	 * Test valid blocking piece move. Knight skipping over.
	 */
	public void testValidBlockingPieceMove(){
		StandardBoard board = new StandardBoard(8,8);
		Archbishop testKnight = new Archbishop(1, 0, TurnColor.white, board);
		Pawn blockingPawn = new Pawn(1, 1, TurnColor.white, board);
		assertTrue(testKnight.canMove(2, 2));
	}
	
	/**
	 * Test invalid blocking piece move. Bishop getting stopped.
	 */
	public void testInvalidBlockingPieceMove(){
		StandardBoard board = new StandardBoard(8,8);
		Archbishop testBishop = new Archbishop(1, 0, TurnColor.white, board);
		Pawn blockingPawn = new Pawn(2, 1, TurnColor.white, board);
		assertFalse(testBishop.canMove(3, 2));
	}

}
