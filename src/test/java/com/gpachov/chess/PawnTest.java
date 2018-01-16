package com.gpachov.chess;

import chessGame.Board;
import junit.framework.TestCase;

import static chessGame.Board.*;

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
		Board board = new Board();
		assertTrue(board.canMove(0, 1, 0, 2));
	}

	/**
	 * Test valid black pawn move.
	 */
	public void testValidBlackPawnMove(){
		Board board = new Board();
		board.pieces = new int[8][8];
        board.pieces[0][1] = BLACK_PAWN;
        board.pieces[0][5] = BLACK_KING;
		board.pieces[3][3] = WHITE_KING;
        board.initCaches();
        assertTrue(board.canMove(0, 1 , 0, 0));
	}

	/**
	 * Test invalid pawn move.
	 */
	public void testInvalidPawnMove(){
		Board board = new Board();
        board.pieces = new int[8][8];
        board.pieces[1][4] = BLACK_PAWN;

        assertFalse(board.canMove(1,4, 1, 6));
	}

	/**
	 * Test valid enemy piece move/capture.
	 */
	public void testValidEnemyPieceMove(){
		Board board = new Board();
        board.pieces[2][2] = WHITE_PAWN;
        board.pieces[3][3] = BLACK_PAWN;

        assertTrue(board.canMove(2, 2, 3, 3));
	}

	/**
	 * Test valid First white pawn move.
	 */
	public void testValidFirstPawnMove(){
		Board board = new Board();
        board.pieces[0][1] = WHITE_PAWN;
		assertTrue(board.canMove(0,1, 0, 3));
	}

	/**
	 * Test valid First black pawn move.
	 */
	public void testValidFirstBlackPawnMove(){
		Board board = new Board();
        board.pieces[3][6] = BLACK_PAWN;
        assertTrue(board.canMove(3, 6,3, 4));
	}

	/**
	 * Test valid black pawn first move.
	 */
	public void testValidFirstPawnEnemyMove(){
		Board board = new Board();
        board.pieces[0][1] = WHITE_PAWN;
        board.pieces[1][2] = BLACK_PAWN;
		assertTrue(board.canMove(0, 1, 1, 2));
	}

}
