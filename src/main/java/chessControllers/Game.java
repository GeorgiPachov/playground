package chessControllers;

import java.util.Stack;

import chessGame.*;
public class Game {

	public static final boolean DEBUG = false;
	TurnColor gameTurn;
	StandardBoard gameBoard;
	boolean gameOver;
	Stack<MoveCommand> commandStack;
	
	public Game() {
		gameBoard = new StandardBoard();
		gameBoard.populateBoardWithPieces();
		gameTurn = TurnColor.white;
		gameOver = false;
		commandStack = new Stack<>();
	}

	void preexecuteMove(int[] move) {
		int xDestination = move[2];
		int yDestination = move[3];
		Piece movingPiece = this.gameBoard.pieces[move[0]][move[1]];

		if (DEBUG) {
			log("Preexecuting move for " + movingPiece.turnColor + " [" + movingPiece.xLocation + ", " + movingPiece.yLocation + "] to [" + xDestination + ", " + yDestination + "]");
		}
		if(movingPiece.turnColor == gameTurn) {
			Piece enemyPiece = null;
			if (gameBoard.pieces[xDestination][yDestination]!= null)
				enemyPiece = gameBoard.pieces[xDestination][yDestination];
			MoveCommand newCommand = new MoveCommand(movingPiece, enemyPiece, xDestination, yDestination);
			commandStack.add(newCommand);
			newCommand.execute();
            gameBoard.initCaches();
        }
		gameTurn = gameTurn.opposite();

	}
	
    public void executeMove(int[]  move) {
        int xDestination = move[2];
        int yDestination = move[3];
		Piece movingPiece = this.gameBoard.pieces[move[0]][move[1]];

		if (DEBUG) {
			log("Executing move for " + movingPiece.turnColor + " [" + movingPiece.xLocation + ", " + movingPiece.yLocation + "] to [" + xDestination + ", " + yDestination + "]");
		}
        if(movingPiece.turnColor == gameTurn && movingPiece.canMove(xDestination, yDestination)){
            Piece enemyPiece = null;
            if(gameBoard.pieces[xDestination][yDestination]!= null)
                enemyPiece = gameBoard.pieces[xDestination][yDestination];
            MoveCommand newCommand = new MoveCommand(movingPiece, enemyPiece, xDestination, yDestination);
            commandStack.add(newCommand);
            newCommand.execute();
            gameBoard.initCaches();

            boolean gameOver = false;
            if(movingPiece.turnColor.equals(TurnColor.white)){
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
	}

	public void log(String s) {
		System.err.println(s);
	}

	protected boolean checkKingStatus(King kingToCheck) {
		if(kingToCheck.isKingInCheck(kingToCheck)) {
			if(kingToCheck.isKingCheckmate(kingToCheck)) {
				return true;
			}
		}
		return false;
	}
	public void undoMove() {
		if (!commandStack.isEmpty()) {
			MoveCommand move = commandStack.pop();
			move.undo();
			gameTurn = gameTurn.opposite();
		}
	}
}
