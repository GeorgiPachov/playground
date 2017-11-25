package chessControllers;

import chessGame.Board;
import chessGame.Move;
import chessGame.Square;

import java.util.Arrays;
import java.util.List;

public class GreedyStrategy implements PlayingStrategy {
    private RandomStrategy backup = new RandomStrategy();
    @Override
    public void playBlack(Game game) {
        List<Move> moves = game.gameBoard.listPossibleMovesBlack();
        Move move = moves.stream().sorted((m1, m2) -> greedyEvaluation(m1, m2, game)).findFirst().get();
        game.executeMove(move);
    }

    private int greedyEvaluation(Move move1, Move move2, Game game) {
        double score1 = estimateAggresiveScore(move1, game);
        double score2 = estimateAggresiveScore(move2, game);
        return (int) Math.signum(score1 - score2);
    }

    private double estimateAggresiveScore(Move move, Game game) {
        Board.TurnColor otherColor = game.gameTurn.opposite();
        long oppositeRemainingPiecesNow = Arrays.stream(game.gameBoard.squaresList).flatMap(a -> Arrays.stream(a))
                .filter(square -> square.isOccupied)
                .filter(square -> square.occupyingPiece.turnColor == otherColor).count();
        long teritorialCoverageNow = getTeritorialCoverage(game);
        double forwardScoreNow = getForwardScore(game);

        game.executeMove(move);

        long oppositeRemainingPiecesAfter = Arrays.stream(game.gameBoard.squaresList).flatMap(a -> Arrays.stream(a))
                .filter(square -> square.isOccupied)
                .filter(square -> square.occupyingPiece.turnColor == otherColor).count();
        long teritorialCoverageAfter = getTeritorialCoverage(game);
        double forwardScoreAfter = getForwardScore(game);


        game.undoMove();

        if (oppositeRemainingPiecesAfter < oppositeRemainingPiecesNow) {
            return (int) 1000 * (16 - oppositeRemainingPiecesAfter);
        } else if (oppositeRemainingPiecesAfter == oppositeRemainingPiecesNow) {
            if (teritorialCoverageAfter > teritorialCoverageNow) {
                return (int) 100 * (teritorialCoverageAfter - teritorialCoverageNow);
            } else {
                return 10 *(forwardScoreAfter - forwardScoreNow);
            }
        }
        return -1;
    }

    private int getForwardScore(Game game) {
        switch (game.gameTurn) {
            case white:
                return game.gameBoard.getWhitePieces().stream().mapToInt(p -> p.occupyingPiece.yLocation).reduce(Integer::sum).getAsInt();
            case black:
                return game.gameBoard.getBlackPieces().stream().mapToInt(p -> 8 - p.occupyingPiece.yLocation).reduce(Integer::sum).getAsInt();
        }
        return 0;
    }


    private long getTeritorialCoverage(Game game) {
        int threatened = 0;
        for (int y = 0; y < game.gameBoard.squaresList.length; y++) {
            for (int x = 0; x < game.gameBoard.squaresList[0].length; x++) {
                if (isThreatened(x, y, game)) {
                    threatened++;
                }
            }
        }
        return threatened;
    }

    private boolean isThreatened(int x, int y, Game game) {
        switch (game.gameTurn) {
            case white:
                return game.gameBoard.listPossibleMovesWhite().stream().anyMatch(move -> move.newX == x && move.newY == y);
            case black:
                return game.gameBoard.listPossibleMovesBlack().stream().anyMatch(move -> move.newX == x && move.newY == y);

        }
        return false;
    }

    @Override
    public void playWhite(Game game) {
        List<Move> moves = game.gameBoard.listPossibleMovesWhite();
        Move move = moves.stream().sorted((m1, m2) -> greedyEvaluation(m1, m2, game)).findFirst().get();

        game.executeMove(move);
    }
}
