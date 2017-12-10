package chessControllers;

import java.util.Stack;

import chessGame.*;
public class Game {

	public static final boolean DEBUG = false;
	StandardBoard gameBoard;
	boolean gameOver;
	Stack<MoveCommand> commandStack;
	
	public Game() {
		gameBoard = new StandardBoard();
		gameOver = false;
		commandStack = new Stack<>();
	}

	void preexecuteMove(int[] move) {
		int xDestination = move[2];
		int yDestination = move[3];
		int movingPiece = this.gameBoard.pieces[move[0]][move[1]];

		if (DEBUG) {
			log("Preexecuting move for " + toString(movingPiece) + " [" + move[0] + ", " + move[1] + "] to [" + xDestination + ", " + yDestination + "]");
		}
        MoveCommand newCommand = new MoveCommand(gameBoard, move);
        commandStack.add(newCommand);
        newCommand.execute();
        gameBoard.flipTurn();
	}

    private String toString(int movingPiece) {
        return String.valueOf(movingPiece);
    }

    public void executeMove(int[]  move) {
	    printBoard();
        int xDestination = move[2];
        int yDestination = move[3];
		int movingPiece = this.gameBoard.pieces[move[0]][move[1]];
        TurnColor movingPieceColor = gameBoard.getColor(move[0], move[1]);

        if (DEBUG) {
		    log("Found piece " + movingPiece);
            log("Executing move for " + toString(movingPiece) + " [" + move[0] + ", " + move[1] + "] to [" + xDestination + ", " + yDestination + "]");
        }
        MoveCommand newCommand = new MoveCommand(gameBoard, move);
        commandStack.add(newCommand);
        newCommand.execute();
        printBoard();


        int[] kingToCheck = gameBoard.getKing(movingPieceColor.opposite());
        boolean gameOver = gameBoard.isKingCheckmate(kingToCheck);
        gameBoard.flipTurn();

        if (gameOver) {
            stopGame();
         }

    }

    private void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(gameBoard.pieces[j][i] + " ");
            }
            System.out.println();
        }
    }


    private void stopGame() {
		System.out.println("GAME ENDED!!!!" + gameBoard.gameTurn + " WON");
		this.gameOver = true;
	}

	private void log(String s) {
		System.err.println(s);
	}

	void undoMove() {
		if (!commandStack.isEmpty()) {
			MoveCommand move = commandStack.pop();
			move.undo();
			gameBoard.flipTurn();
		}
	}


}
