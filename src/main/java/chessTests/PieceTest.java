package chessTests;

import chessControllers.TurnColor;
import chessGame.*;
import junit.framework.TestCase;

/**
 * General tests for all Pieces.
 * @author Pratik Naik
 *
 */
public class PieceTest extends TestCase {
	
	/**
	 * Testing the capture function and checking final location of pieces.
	 */
	public void testEnemyCapture() {
		StandardBoard board = new StandardBoard(8,8);
		Pawn whitePawn = new Pawn(1, 1, TurnColor.white, board);
		Pawn blackPawn = new Pawn(2, 2, TurnColor.black, board);
		assertTrue(whitePawn.canMove(2, 2));
		whitePawn.executeCaptureOrMove(2,2);
		assertTrue(whitePawn.xLocation == 2 && whitePawn.yLocation == 2);
	}
	
	/**
	 * Testing out of bounds invalid move.
	 */
	public void testInvalidPositiveBoundsMove() {
		StandardBoard board = new StandardBoard(8,8);
		Pawn whitePawn = new Pawn(7, 7, TurnColor.white, board);
		assertFalse(whitePawn.canMove(8, 8));
	}
	
	/**
	 * Invalid negative out of bounds move.
	 */
	public void testInvalidNegativeBoundsMove() {
		StandardBoard board = new StandardBoard(8,8);
		Pawn whitePawn = new Pawn(0, 1, TurnColor.white, board);
		assertFalse(whitePawn.canMove(0, -1));
	}
	
	/**
	 * Testing the king in danger state on the baord for given king.
	 */
	public void testKingInDanger(){
		StandardBoard board = new StandardBoard(8,8);
		King newKing = new King(3, 7, TurnColor.black, board);
		board.blackKingTracker = newKing;
		Pawn allyPawn = new Pawn(5, 5, TurnColor.black, board);
		Bishop enemyBishop = new Bishop(7, 3, TurnColor.white, board);
		assertFalse(allyPawn.canMove(5, 4));
	}
	
	/**
	 * Test to check he fastest checkmate state in the game.
	 */
	public void testCheckmate(){
		StandardBoard board = new StandardBoard(8,8);
		board.populateBoardWithPieces();
		Piece wpawn1 = board.pieces[5][1];
		wpawn1.executeCaptureOrMove(5, 2);
		assertTrue(wpawn1.xLocation == 5 && wpawn1.yLocation == 2);
		Piece bpawn1 = board.pieces[4][6];
		bpawn1.executeCaptureOrMove(4, 4);
		assertTrue(bpawn1.xLocation == 4 && bpawn1.yLocation == 4);
		Piece wpawn2 = board.pieces[6][1];
		wpawn2.executeCaptureOrMove(6, 3);
		assertTrue(wpawn2.xLocation == 6 && wpawn2.yLocation == 3);
		Piece bqueen = board.pieces[3][7];
		bqueen.executeCaptureOrMove(7, 3);
		assertTrue(bqueen.xLocation == 7 && bqueen.yLocation == 3);
		King wking = (King) board.pieces[4][0];
		assertTrue(wking.isKingInCheck(wking));
		assertTrue(wking.isKingCheckmate(wking));
	}
}
