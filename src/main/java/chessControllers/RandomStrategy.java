package chessControllers;

import chessGame.Move;

import java.util.List;
import java.util.Random;

public class RandomStrategy implements PlayingStrategy {

    @Override
    public void playBlack(Game game) {
        List<Move> moves = game.gameBoard.listPossibleMovesBlack();
        int rnd = new Random().nextInt(moves.size());
        Move move = moves.get(rnd);
        game.executeMove(move);
    }

    @Override
    public void playWhite(Game game) {
        List<Move> moves = game.gameBoard.listPossibleMovesWhite();
        int rnd = new Random().nextInt(moves.size());
        Move move = moves.get(rnd);
        game.executeMove(move);
    }
}
