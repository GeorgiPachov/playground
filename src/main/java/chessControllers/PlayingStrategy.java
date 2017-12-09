package chessControllers;

public interface PlayingStrategy {
    int[] playBlack(Game game);
    int[] playWhite(Game game);
}
