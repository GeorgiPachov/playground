package chessControllers;

import chessGame.Move;

public interface PlayingStrategy {
    Move playBlack(Game game);
    Move playWhite(Game game);
}
