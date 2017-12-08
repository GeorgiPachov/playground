package chessControllers;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


import chessGame.*;
import chessViews.GameDisplay;

/**
 * Game class to setup a complete Chess Game. Will move to Controllers once implemented properly.
 * @author Pratik Naik
 *
 */
public class Game {

	public static final boolean DEBUG = false;
	/**
	 * Global variables of the game 
	 * - Players
	 * - Game board, turn, type, board square size.
	 * - References of gamepanel, sidepanels, buttons, score labels.
	 * - Reference to piece being moved.
	 * - Moves stack.
	 */
	Board.TurnColor gameTurn;
	StandardBoard gameBoard;
	boolean gameOver;
	int squareSize;
	public JPanel gamePanel;
	Stack<MoveCommand> commandStack;
	
	/**
	 * Method to initialize gameBoard, populate it with pieces according to gameType 
	 * and start a new move stack.
	 * @param gameType 
	 */
	public void gameInit(boolean gameType) {
		gameBoard = new StandardBoard(8,8);
		gameBoard.populateBoardWithPieces(gameType);
		gameTurn = Board.TurnColor.white;
		gameOver = false;
		squareSize = 80;
		commandStack = new Stack();
	}

	/**
	 * Helper method to start off a new game.
	 * Called when the players want to restart or when new players join in initially.
	 */
	public static Game startNewGame() {
		Game newGame = new Game();
		newGame.gameInit(false);
		return newGame;
	}

	public void preexecuteMove(Move move) {
		int xDestination = move.getNewX();
		int yDestination = move.getNewY();
		Piece movingPiece = this.gameBoard.squaresList[move.oldX][move.oldY].occupyingPiece;

		if (DEBUG) {
			log("Preexecuting move for " + movingPiece.turnColor + " [" + movingPiece.xLocation + ", " + movingPiece.yLocation + "] to [" + xDestination + ", " + yDestination + "]");
		}
		if(movingPiece.turnColor == gameTurn && movingPiece.canMove(xDestination, yDestination)) {
			Piece enemyPiece = null;
			if (gameBoard.squaresList[xDestination][yDestination].isOccupied)
				enemyPiece = gameBoard.squaresList[xDestination][yDestination].occupyingPiece;
			MoveCommand newCommand = new MoveCommand(movingPiece, enemyPiece, xDestination, yDestination);
			commandStack.add(newCommand);
			newCommand.execute();
		}
		gameTurn = gameTurn.opposite();

	}
	
    public void executeMove(Move move) {
		int xDestination = move.getNewX();
		int yDestination = move.getNewY();
		Piece movingPiece = this.gameBoard.squaresList[move.oldX][move.oldY].occupyingPiece;

		if (DEBUG) {
			log("Executing move for " + movingPiece.turnColor + " [" + movingPiece.xLocation + ", " + movingPiece.yLocation + "] to [" + xDestination + ", " + yDestination + "]");
		}
        if(movingPiece.turnColor == gameTurn && movingPiece.canMove(xDestination, yDestination)){
            Piece enemyPiece = null;
            if(gameBoard.squaresList[xDestination][yDestination].isOccupied)
                enemyPiece = gameBoard.squaresList[xDestination][yDestination].occupyingPiece;
            MoveCommand newCommand = new MoveCommand(movingPiece, enemyPiece, xDestination, yDestination);
            commandStack.add(newCommand);
            newCommand.execute();
            boolean gameOver = false;
            if(movingPiece.turnColor.equals(Board.TurnColor.white)){
                gameTurn = gameTurn.opposite();
                gameOver = checkKingStatus(gameBoard.blackKingTracker);
             }
             else{
                 gameTurn = gameTurn.opposite();
                 gameOver = checkKingStatus(gameBoard.whiteKingTracker);
             }
             if (gameOver) {
            	stopGame();
			 }

        }
        else{
            log("This is an Illegal Move!" + "Illegal move message");
        }
    }

	private void stopGame() {
		System.out.println("GAME ENDED!!!!" + gameTurn + " WON");
		this.gameOver = true;
		gamePanel.repaint();
	}

	public void log(String s) {
		System.err.println(s);
	}

	/**
	 * Helper method to check if the passed king is in check.
	 * It calls the respective check and checkmate helper methods in our library 
	 * and displays appropriate messages and updates the score.
	 * @param kingToCheck
	 */
	protected boolean checkKingStatus(King kingToCheck) {
		if(kingToCheck.isKingInCheck(kingToCheck)) {
			if(kingToCheck.isKingCheckmate(kingToCheck)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Undo button calls this method. This pops the last command off the command stack 
	 * and executes it's reverse using the undo helper method in our MoveCommand class.
	 * It also switches the game turn accordingly.
	 */
	public void undoMove() {
		if (!commandStack.isEmpty()) {
			MoveCommand move = commandStack.pop();
			move.undo();
			if (gameTurn == Board.TurnColor.white) {
				blackLabel.setForeground(Color.BLUE);
				whiteLabel.setForeground(Color.BLACK);
			} else {
				whiteLabel.setForeground(Color.BLUE);
				blackLabel.setForeground(Color.BLACK);
			}
			gameTurn = gameTurn.opposite();
		}
	}
}
