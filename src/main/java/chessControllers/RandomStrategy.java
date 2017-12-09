package chessControllers;

import chessGame.Move;

import java.util.List;
import java.util.Random;

public class RandomStrategy implements PlayingStrategy {

    @Override
    public Move playBlack(Game game) {
        List<Move> moves = game.gameBoard.populatePossibleMoves(game.gameTurn);
        int rnd = new Random().nextInt(moves.size());
        Move move = moves.get(rnd);
        game.executeMove(move);
        return move;
    }

    @Override
    public Move playWhite(Game game) {
        List<Move> moves = game.gameBoard.populatePossibleMoves(game.gameTurn);
        int rnd = new Random().nextInt(moves.size());
        Move move = moves.get(rnd);
        game.executeMove(move);
        return move;
    }
}
