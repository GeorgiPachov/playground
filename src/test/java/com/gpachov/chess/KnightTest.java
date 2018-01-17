package chessTests;

import chessControllers.TurnColor;
import chessGame.Knight;
import chessGame.Pawn;
import chessGame.Board;
import junit.framework.TestCase;
import org.junit.Test;

import static chessGame.Board.*;

/**
 * Test cases for the Knight
 * @author Pratik Naik
 *
 */
public class KnightTest extends TestCase {

	/**
	 * Test valid vertical Knight move
	 */
	@Test
	public void testValidVerticalKnightMove(){

        Board board = new Board();
        board.pieces = new int[8][8];
        board.pieces[0][0] = BLACK_KING;
        board.pieces[6][6] = WHITE_KING;
        board.pieces[4][4] = BLACK_KNIGHT;
        board.initCaches();
		assertTrue(board.canMove(4, 4, 5, 6));
        assertTrue(board.canMove(4, 4, 3, 6));

        assertTrue(board.canMove(4, 4, 2, 5));
        assertTrue(board.canMove(4, 4, 6, 5));

        assertTrue(board.canMove(4, 4, 2, 3));
        assertTrue(board.canMove(4, 4, 6, 3));

        assertTrue(board.canMove(4, 4, 3, 2));
        assertTrue(board.canMove(4, 4, 5, 2));



    }

//	/**
//	 * Test valid horizontal Knight move
//	 */
//	public void testValidHorizontalKnightMove(){
//		Board board = new Board();
//		Knight newKnight = new Knight(1, 0, TurnColor.white, board);
//		assertTrue(newKnight.canMove(3, 1));
//	}
//
//	/**
//	 * Test invalid Knight move.
//	 */
//	public void testInvalidKnightMove(){
//		Board board = new Board();
//		Knight newKnight = new Knight(6, 2, TurnColor.white, board);
//		assertFalse(newKnight.canMove(5, 1));
//	}
//
//	/**
//	 * Test valid Hopping over blocking piece move.
//	 */
//	public void testBlockingPieceMove(){
//		Board board = new Board();
//		Knight newKnight = new Knight(1, 0, TurnColor.white, board);
//		Pawn blockingPawn = new Pawn(1, 1, TurnColor.white, board);
//		assertTrue(newKnight.canMove(2, 2));
//	}
//
//	/**
//	 * Test invalid ally at destination
//	 */
//	public void testInvalidAllyBlockingPieceMove(){
//		Board board = new Board();
//		Knight newKnight = new Knight(1, 0, TurnColor.white, board);
//		Pawn blockingPawn = new Pawn(2, 2, TurnColor.white, board);
//		assertFalse(newKnight.canMove(2, 2));
//	}

}
