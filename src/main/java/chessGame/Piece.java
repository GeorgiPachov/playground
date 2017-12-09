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

	public boolean canMove(int newX, int newY){
		if(!currentBoard.inBoardBounds(newX, newY))
			return false;
		if(!isValidSpecialMove(newX, newY))
			return false;
		if(!isEnemyPieceAtDestination(newX, newY))
			return false;
		if(isKingInDanger(newX, newY))
			return false;
		return true;
	}
	
	private boolean isEnemyPieceAtDestination(int newX, int newY){
		Piece piece = currentBoard.pieces[newX][newY];
		if(piece != null){
			return isEnemyPiece(this.turnColor, piece);
		}
		return true;
	}
	
	public void executeCaptureOrMove(int newX, int newY){
		movePiece(this, newX, newY);
	}
	
	public boolean isKingInCheck(King kingToCheck) {
		int kingXLocation = kingToCheck.xLocation;
		int kingYLocation = kingToCheck.yLocation;
		TurnColor turnColorToCheck = kingToCheck.turnColor;
		// Iterates through the squares on the board and checks if enemy pieces can attack king.
		for(int i = 0; i < currentBoard.pieces.length; i++){
			for(int j = 0; j < currentBoard.pieces[0].length; j++){
				Piece piece = currentBoard.pieces[i][j];
				if(piece != null){
					if(isEnemyPiece(turnColorToCheck, piece)){
						if(piece.isValidSpecialMove(kingXLocation, kingYLocation))
							return true;
					}
				}
			}
		}
		return false;
		
	}
	
	private boolean isKingInDanger(int newPieceX, int newPieceY) {
		int oldPieceX = this.xLocation;
		int oldPieceY = this.yLocation;
		King kingToCheck;
		Piece piece = currentBoard.pieces[newPieceX][newPieceY];
		if(piece != null){
			if(isEnemyPieceAtDestination(newPieceX, newPieceY)){
				Piece enemyPiece = piece;
				if(this.turnColor.equals(TurnColor.white)){
					if(currentBoard.whiteKingTracker == null)
						return false;
					kingToCheck = currentBoard.whiteKingTracker;
				}
				else{
					if(currentBoard.blackKingTracker == null)
						return false;
					kingToCheck = currentBoard.blackKingTracker;
				}
				movePiece(this, newPieceX, newPieceY);
				if(isKingInCheck(kingToCheck)){
					movePiece(this, oldPieceX, oldPieceY);
					movePiece(enemyPiece, newPieceX, newPieceY);
					return true;
				}
				movePiece(this, oldPieceX, oldPieceY);
				movePiece(enemyPiece, newPieceX, newPieceY);
			}
		}
		else{
			if(this.turnColor.equals(TurnColor.white)){
				if(currentBoard.whiteKingTracker == null)
					return false;
				kingToCheck = currentBoard.whiteKingTracker;
			}
			else{
				if(currentBoard.blackKingTracker == null)
					return false;
				kingToCheck = currentBoard.blackKingTracker;
			}
			movePiece(this, newPieceX, newPieceY);
			if(isKingInCheck(kingToCheck)){
				movePiece(this, oldPieceX, oldPieceY);
				return true;
			}
			movePiece(this, oldPieceX, oldPieceY);
		}
		return false;
	}
	
	/**
	 * Helper method to move piece into given location.
	 * Called in 2 cases:
	 * - When the move is valid and needs to be executed.
	 * - To check if making this move would put ally king in danger.
	 * @param pieceToMove
	 * @param newPieceX
	 * @param newPieceY
	 */
	private void movePiece(Piece pieceToMove, int newPieceX, int newPieceY){
		currentBoard.pieces[pieceToMove.xLocation][pieceToMove.yLocation] = null;
		currentBoard.pieces[newPieceX][newPieceY] = pieceToMove;
		pieceToMove.xLocation = newPieceX;
		pieceToMove.yLocation = newPieceY;
	}

	/**
	 * Helper method comparing colors to determine ally or enemy.
	 * @param turnColorToCheck
	 * @param occupyingPiece
	 * @return boolean true if piece is your enemy.
	 */
	private boolean isEnemyPiece(TurnColor turnColorToCheck, Piece occupyingPiece) {
		if(turnColorToCheck.equals(occupyingPiece.turnColor))
			return false;
		else
			return true;
	}
	
	/**
	 * Method to draw the pieces on the board. This method also determines piece asset to draw.
	 * Takes in graphics which we are using and square size of the board.
	 * @param graphic
	 * @param squareSize
	 * @param y 
	 * @param x 
	 * 
	 */
	public void drawPiece(Graphics graphic, int squareSize, int x, int y){
		if(this.turnColor.equals(TurnColor.black)){
			String name = this.nameOfPiece.concat(".png");
			String imagePath = "assets/black_";
			String imageName = imagePath.concat(name);
			drawPieceHelper(graphic, squareSize, imageName, x, y);
		}
		else{
			String name = this.nameOfPiece.concat(".png");
			String imagePath = "assets/white_";
			String imageName = imagePath.concat(name);
			drawPieceHelper(graphic, squareSize, imageName, x, y);
		}
	}

	/**
	 * A helper method to draw the piece in the proper coordinates on the board.
	 * @param graphic
	 * @param squareSize
	 * @param imageName
	 * @param y 
	 * @param x 
	 */
	private void drawPieceHelper(Graphics graphic, int squareSize, String imageName, int x, int y) {
		File imageFile = new File(imageName);
		BufferedImage image = null;
		try {
			image = ImageIO.read(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int imageHeight = image.getHeight();
		int imageWidth = image.getWidth();
		int heightPadding = (squareSize - imageHeight)/2;
		int widthPadding = (squareSize - imageWidth)/2;
		graphic.drawImage(image, (squareSize*x) + widthPadding, ((7-y)*squareSize) + heightPadding, imageWidth, imageHeight, null);
	}
	
	/**
	 * Helper method to check if the king is in checkmate!
	 * @param kingToCheck
	 * @return boolean true of king passed in is in checkmate
	 */
	public boolean isKingCheckmate(King kingToCheck){
		if(!isKingInCheck(kingToCheck))
			return false;
		TurnColor turnColorToCheck = kingToCheck.turnColor;
		for(int i = 0; i < currentBoard.pieces.length; i++){
			for(int j = 0; j < currentBoard.pieces[0].length; j++){
				Piece pieceToCheck = currentBoard.pieces[i][j];
				if(pieceToCheck != null){
					if(!isEnemyPiece(turnColorToCheck, pieceToCheck)){
						if(!checkmateHelper(pieceToCheck, kingToCheck))
							 return false;
					}
				}	
			}
		}
		return true;
	}

	/**
	 * Helper method to iterate through the pieces to check if any move can break the check.
	 * @param allyPiece
	 * @param kingToCheck
	 * @return boolean true if the king is well and truly in checkmate.
	 */
	private boolean checkmateHelper(Piece allyPiece, King kingToCheck) {
		int oldPieceX = allyPiece.xLocation;
		int oldPieceY = allyPiece.yLocation;
		for(int i = 0; i < currentBoard.pieces.length; i++){
			for(int j = 0; j < currentBoard.pieces[0].length; j++){
				Piece pieceToCheck = currentBoard.pieces[i][j];
				if(isEnemyPieceAtDestination(i,j)){
					if(allyPiece.isValidSpecialMove(i, j)){
						if(pieceToCheck != null){
							movePiece(allyPiece, i, j);
							if(!isKingInCheck(kingToCheck)){
								movePiece(allyPiece, oldPieceX, oldPieceY);
								movePiece(pieceToCheck, i, j);
								return false;
							}
							movePiece(allyPiece, oldPieceX, oldPieceY);
							movePiece(pieceToCheck, i, j);
						}
						else{
							movePiece(allyPiece, i, j);
							if(!isKingInCheck(kingToCheck)){
								movePiece(allyPiece, oldPieceX, oldPieceY);
								return false;
							}
							movePiece(allyPiece, oldPieceX, oldPieceY);
						}
					}
				}
			}
		}
		return true;
	}

	public abstract void addAllowedMoves(List<Integer> moves);
}


