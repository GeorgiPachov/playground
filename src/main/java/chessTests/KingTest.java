//package chessTests;
//
//import chessControllers.TurnColor;
//import chessGame.King;
//import chessGame.Pawn;
//import chessGame.StandardBoard;
//import junit.framework.TestCase;
//
///**
// * Test cases for the Chancellor
// * @author Pratik Naik
// *
// */
//public class KingTest extends TestCase {
//
//	/**
//	 * Test valid horizontal move.
//	 */
//	public void testValidHorizontalKingMove(){
//		StandardBoard board = new StandardBoard();
//		King newKing = new King(4, 0, TurnColor.white, board);
//		assertTrue(board.canMove(4, 0, 5, 0));
//	}
//
//	/**
//	 * Test valid vertical move.
//	 */
//	public void testValidVerticalKingMove(){
//		StandardBoard board = new StandardBoard();
//		King newKing = new King(4, 0, TurnColor.white, board);
//		assertTrue(board.canMove(4, 0, 4, 1));
//	}
//
//	/**
//	 * Test valid Diagonal move
//	 */
//	public void testValidDiagonalKingMove(){
//		StandardBoard board = new StandardBoard();
//		King newKing = new King(4, 1, TurnColor.white, board);
//		assertTrue(board.canMove(4, 1, 3, 2));
//	}
//
//	/**
//	 * Test invalid King move.
//	 */
//	public void testInvalidKingMove(){
//		StandardBoard board = new StandardBoard();
//		King newKing = new King(3, 7, TurnColor.black, board);
//		assertFalse(board.canMove(3, 7, 3, 5));
//	}
//
//	/**
//	 * Test ally pice putting king in check
//	 */
//	public void testInvalidAllyPieceMove(){
//		StandardBoard board = new StandardBoard();
//		King newKing = new King(3, 7, TurnColor.black, board);
//		board.pieces[2][6] = StandardBoard.BLACK_PAWN;
//		assertFalse(board.canMove(3, 7, 2, 6));
//	}
//
//	/**
//	 * Test king capturing enemy piece.
//	 */
//	public void testValidEnemyPieceMove(){
//		StandardBoard board = new StandardBoard();
//		King newKing = new King(3, 7, TurnColor.black, board);
//		Pawn enemyPawn = new Pawn(2, 6, TurnColor.white, board);
//		assertTrue(newKing.canMove(2, 6));
//	}
//
//	/**
//	 * Test King putting itself in check
//	 */
//	public void testInvalidMoveToCheckLocation(){
//		StandardBoard board = new StandardBoard();
//		King newKing = new King(3, 7, TurnColor.black, board);
//		board.blackKingTracker = newKing;
//		Pawn enemyPawn = new Pawn(5, 5, TurnColor.white, board);
//		assertFalse(newKing.canMove(4, 6));
//	}
//
//	/**
//	 * Test if King displays checked status
//	 */
//	public void testKingInCheck(){
//		StandardBoard board = new StandardBoard();
//		King newKing = new King(3, 7, TurnColor.black, board);
//		Pawn enemyPawn = new Pawn(4, 6, TurnColor.white, board);
//		assertTrue(newKing.isKingInCheck(newKing));
//	}
//
//}
