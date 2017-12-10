//package chessTests;
//
//import chessControllers.TurnColor;
//import chessGame.Knight;
//import chessGame.Pawn;
//import chessGame.StandardBoard;
//import junit.framework.TestCase;
//
///**
// * Test cases for the Knight
// * @author Pratik Naik
// *
// */
//public class KnightTest extends TestCase {
//
//	/**
//	 * Test valid vertical Knight move
//	 */
//	public void testValidVerticalKnightMove(){
//		StandardBoard board = new StandardBoard();
//		Knight newKnight = new Knight(1, 0, TurnColor.white, board);
//		assertTrue(newKnight.canMove(2, 2));
//	}
//
//	/**
//	 * Test valid horizontal Knight move
//	 */
//	public void testValidHorizontalKnightMove(){
//		StandardBoard board = new StandardBoard();
//		Knight newKnight = new Knight(1, 0, TurnColor.white, board);
//		assertTrue(newKnight.canMove(3, 1));
//	}
//
//	/**
//	 * Test invalid Knight move.
//	 */
//	public void testInvalidKnightMove(){
//		StandardBoard board = new StandardBoard();
//		Knight newKnight = new Knight(6, 2, TurnColor.white, board);
//		assertFalse(newKnight.canMove(5, 1));
//	}
//
//	/**
//	 * Test valid Hopping over blocking piece move.
//	 */
//	public void testBlockingPieceMove(){
//		StandardBoard board = new StandardBoard();
//		Knight newKnight = new Knight(1, 0, TurnColor.white, board);
//		Pawn blockingPawn = new Pawn(1, 1, TurnColor.white, board);
//		assertTrue(newKnight.canMove(2, 2));
//	}
//
//	/**
//	 * Test invalid ally at destination
//	 */
//	public void testInvalidAllyBlockingPieceMove(){
//		StandardBoard board = new StandardBoard();
//		Knight newKnight = new Knight(1, 0, TurnColor.white, board);
//		Pawn blockingPawn = new Pawn(2, 2, TurnColor.white, board);
//		assertFalse(newKnight.canMove(2, 2));
//	}
//
//}
