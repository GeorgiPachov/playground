package chessControllers;

import java.util.Stack;

import chessGame.*;

import static chessControllers.Constants.DEBUG;
import static chessControllers.Util.logV;

public class Game {
	public Board board;
	public boolean gameOver;
	Stack<MoveCommand> commandStack;
	
	public Game() {
		board = new Board();
		gameOver = false;
		commandStack = new Stack<>();
	}

	void preexecuteMove(int[] move) {
		int xDestination = move[2];
		int yDestination = move[3];
		int movingPiece = this.board.pieces[move[0]][move[1]];

		if (DEBUG) {
			log("Preexecuting move for " + toString(movingPiece) + " [" + move[0] + ", " + move[1] + "] to [" + xDestination + ", " + yDestination + "]");
		}
        MoveCommand newCommand = new MoveCommand(board, move);
        commandStack.add(newCommand);
        newCommand.execute();
        board.flipTurn();
	}

    private String toString(int movingPiece) {
        return String.valueOf(movingPiece);
    }

    public void executeMove(int[]  move) {
	    logV(board.toString());
        int xDestination = move[2];
        int yDestination = move[3];
		int movingPiece = this.board.pieces[move[0]][move[1]];
        TurnColor movingPieceColor = board.getColor(move[0], move[1]);

        if (DEBUG) {
		    log("Found piece " + movingPiece);
            log("Executing move for " + toString(movingPiece) + " [" + move[0] + ", " + move[1] + "] to [" + xDestination + ", " + yDestination + "]");
        }
        // if is promotion
        MoveCommand newCommand = new MoveCommand(board, move);
        commandStack.add(newCommand);
        newCommand.execute();
        logV(board.toString());

        int[] kingToCheck = board.getKing(movingPieceColor.opposite());
        boolean gameOver = board.isKingCheckmate(kingToCheck);
        board.flipTurn();

        if (gameOver) {
            stopGame();
         }

    }

    private void stopGame() {
		System.out.println("GAME ENDED!!!!" + board.gameTurn + " WON");
		this.gameOver = true;
	}

	private void log(String s) {
		System.err.println(s);
	}

	void undoMove() {
		if (!commandStack.isEmpty()) {
			MoveCommand move = commandStack.pop();
			move.undo();
			board.flipTurn();
		}
	}


}
