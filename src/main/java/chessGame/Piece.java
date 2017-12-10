package chessGame;

import chessControllers.TurnColor;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Superclass Piece since all chess pieces have common variables and methods to execute.
 * Defines a standard piece and it's features.
 * @author Pratik Naik
 */
public abstract class Piece {
	String nameOfPiece;
	public TurnColor turnColor;
	StandardBoard currentBoard;
	public int xLocation;
	public int yLocation;
	
	abstract boolean isValidSpecialMove(int newX, int newY);

	public Piece(int initX, int initY, TurnColor turnColor, StandardBoard board) {
		this.turnColor = turnColor;
		this.currentBoard = board;
		this.xLocation = initX;
		this.yLocation = initY;
	}

	public abstract void addAllowedMoves(List<Integer> moves);
}


