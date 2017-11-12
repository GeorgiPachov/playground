package chessControllers;

import chessGame.Board;

/**
 * Class reprensenting a Chess Player.
 * @author Pratik Naik
 *
 */
public class Player {
	
	/**
	 * Global variables of a chess Player
	 * - Name
	 * - TurnColor being played.
	 * - Score of the player in game.
	 */
	public String playerName;
	Board.TurnColor playerTurnColor;
	public int playerScore;

	/**
	 * Constructor to add a new player to the game.
	 * @param playerName
	 * @param playerTurnColor
	 */
	public Player(String playerName, Board.TurnColor playerTurnColor) {
		this.playerName = playerName;
		this.playerTurnColor = playerTurnColor;
		this.playerScore = 0;
		
	}
}
