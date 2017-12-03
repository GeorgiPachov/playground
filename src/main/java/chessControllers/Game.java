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
	JFrame window;
	public JPanel gamePanel;
	JPanel sidePanel;
	JLabel whiteLabel;
	JLabel blackLabel;
	JButton undoButton;
	JButton restartButton;
	Piece movingPiece;
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
	 * Method to setup initial display of the Board. Sets up the gamePanel and sidePanel in the
	 * game's main frame.
	 */
	public void setupDisplay(){
		window = new JFrame("Super Mega Death Chess");
        gamePanel = initializeGamePanel(gameBoard); 
        Container contentPanel = window.getContentPane();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.LINE_AXIS));
        sidePanel = initializeSidePanel();
        contentPanel.add(gamePanel);
        contentPanel.add(sidePanel);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.validate();
        window.pack();

	}
	
	/**
	 * Helper method to initialize a JPanel for the game.
	 * @param gameBoard
	 * @return A JPanel instance of the game Board window.
	 */
	private JPanel initializeGamePanel(StandardBoard gameBoard) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		float squareSize = Math.min(width, height)/9f;
        GameDisplay gameDisplay = new GameDisplay(gameBoard, (int) squareSize);
        gameDisplay.setPreferredSize(new Dimension(width,height));
        gameDisplay.setLayout(new BorderLayout());
        return gameDisplay;
    }
	
	/**
	 * Helper method to initialize a side JPanel for the game. 
	 * Contains :
	 *  - Player Name and Scores.
	 *  - Indicators of whose turn it is.
	 *  - Undo, Restart, and Forfeit button.
	 * @return a JPanel with all the above components
	 */
	private JPanel initializeSidePanel(){
		JPanel sideDisplay = new JPanel();
		undoButton = new JButton("Undo Move");
		restartButton = new JButton("Restart Game");
		setupButtonListeners();
		whiteLabel = new JLabel("WHITE PLAYER : ");
		whiteLabel.setForeground(Color.BLUE);
		blackLabel = new JLabel("BLACK PLAYER : ");
		sideDisplay.setLayout(new BoxLayout(sideDisplay, BoxLayout.PAGE_AXIS));
		sideDisplay.add(whiteLabel);
		sideDisplay.add(blackLabel);
		sideDisplay.add(undoButton);
		sideDisplay.add(restartButton);
		return sideDisplay;
	}
	
	/**
	 * Helper method to setup the button listeners for Undo, Restart and Forfeit buttons.
	 */
	private void setupButtonListeners() {
		undoButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				undoMove();
			}
		});
		restartButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				restartGame();
			}
		});
	}

	/**
	 * TaggerMain method to control and update results of Mouse Actions.
	 * It implements a Mouse Adapter to analyze moves made on the chess board.
	 * Mouse Pressed : 
	 * - Calculates Origin x and y of the move
	 * - Get reference to moving piece.
	 * Mouse Released : 
	 * - Calculate Destination x and y of the move
	 * - Check for valid gameTurn and valid move of the moving piece.
	 * - Make the move and add it to the move commandStack
	 * - Change the gameTurn and update it visually on the sideDisplay.
	 * - Check King in check or Checkmate using helper method.
	 * - If illegal move display error message.
	 */
	public void mouseActions(){
		gamePanel.addMouseListener(new MouseAdapter(){
			
			public void mousePressed(MouseEvent me){
//				int xOrigin = me.getX();
//				int yOrigin = me.getY();
//				xOrigin = xOrigin/squareSize;
//				yOrigin = yOrigin/squareSize;
//				yOrigin = 7 - yOrigin;
//				movingPiece = gameBoard.squaresList[xOrigin][yOrigin].occupyingPiece;
			}
			
			public void mouseReleased(MouseEvent me) {
//			    int xDestination = me.getX();
//				int yDestination = me.getY();
//				xDestination = xDestination/squareSize;
//				yDestination = yDestination/squareSize;
//				yDestination = 7 - yDestination;
//                executeMove(xDestination, yDestination);
			}
		});
	}

    public void executeMove(Move move) {
		int xDestination = move.getNewX();
		int yDestination = move.getNewY();
		this.movingPiece = this.gameBoard.squaresList[move.oldX][move.oldY].occupyingPiece;

		log("Executing move for " + movingPiece.turnColor + " [" + movingPiece.xLocation + ", " + movingPiece.yLocation+ "] to [" + xDestination + ", " + yDestination + "]");
        if(movingPiece.turnColor == gameTurn && movingPiece.canMove(xDestination, yDestination)){
            Piece enemyPiece = null;
            if(gameBoard.squaresList[xDestination][yDestination].isOccupied)
                enemyPiece = gameBoard.squaresList[xDestination][yDestination].occupyingPiece;
            MoveCommand newCommand = new MoveCommand(movingPiece, enemyPiece, xDestination, yDestination);
            commandStack.add(newCommand);
            newCommand.execute();
            if(movingPiece.turnColor.equals(Board.TurnColor.white)){
                gameTurn = gameTurn.opposite();
                blackLabel.setForeground(Color.BLUE);
                whiteLabel.setForeground(Color.BLACK);
                checkKingStatus(gameBoard.blackKingTracker);
             }
             else{
                 gameTurn = gameTurn.opposite();
                 whiteLabel.setForeground(Color.BLUE);
                 blackLabel.setForeground(Color.BLACK);
                 checkKingStatus(gameBoard.whiteKingTracker);
             }

        }
        else{
            messageBox("This is an Illegal Move!", "Illegal move message");
        }
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
	protected void checkKingStatus(King kingToCheck) {
		if(kingToCheck.turnColor == Board.TurnColor.white){
		}
		else{
		}
		if(kingToCheck.isKingInCheck(kingToCheck)) {
			if(kingToCheck.isKingCheckmate(kingToCheck)) {
				messageBox(" ,Your King is in Checkmate\nYou Lost\nPlease Click Restart to Play again", "GAME OVER!!");
				gameOver = true;
				return;
			}
		}
	}
	
	/**
	 * Undo button calls this method. This pops the last command off the command stack 
	 * and executes it's reverse using the undo helper method in our MoveCommand class.
	 * It also switches the game turn accordingly.
	 */
	public void undoMove(){
		if(!commandStack.isEmpty()){
			MoveCommand move = commandStack.pop();
			move.undo();
			if(gameTurn == Board.TurnColor.white){
				blackLabel.setForeground(Color.BLUE);
				whiteLabel.setForeground(Color.BLACK);
			}
			else{
				whiteLabel.setForeground(Color.BLUE);
			 	blackLabel.setForeground(Color.BLACK);
			}
			gameTurn = gameTurn.opposite();
		}
	}
	
	/**
	 * Restart button calls this method which checks if your opponent wants to restart as well 
	 * and if so it starts a new game.
	 */
	private void restartGame(){
        startNewGame();
    }

	public void playSelf(PlayingStrategy whiteStrategy, PlayingStrategy blackStrategy) {
	    Game game = this;
		new Thread(() -> {
            while (!game.gameOver) {
                if (game.gameTurn == Board.TurnColor.white) {
                	whiteStrategy.playWhite(game);
                    game.gameTurn = Board.TurnColor.black;
                } else {
					blackStrategy.playBlack(game);
					game.gameTurn = Board.TurnColor.white;
                }
				try {
                	game.gamePanel.repaint();
					Thread.sleep(250L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
        }).start();
	}

    /**
	 * Helper method to start off a new game.
	 * Called when the players want to restart or when new players join in initially.
	 */
	public static Game startNewGame() {
		Game newGame = new Game();
		newGame.gameInit(false);
		newGame.setupDisplay();
		newGame.mouseActions();
        return newGame;
	}


	/**
	 * Helper method to display a warning message of illegal moves, check and checkmate!
	 * @param message
	 * @param title
	 */
	public static void messageBox(String message, String title)
    {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }
}
