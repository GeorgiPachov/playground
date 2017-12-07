package chessControllers;

import chessGame.*;

import java.util.*;

public class MiniMaxStrategy implements PlayingStrategy {
    private static final int MAX_DEPTH = 3;
    private RandomStrategy backup = new RandomStrategy();
    private static short WHITE = 0;
    private static short BLACK = 1;
    private static short[][][] pawnPositionMap = new short[][][] {
            {
                    {100, 100, 100, 100, 100, 100, 100, 100}, // every pawn should try to make queen
                    {50, 50, 50, 50, 50, 50, 50, 50},
                    {10, 10, 20, 30, 30, 20, 10, 10},
                    {5, 5, 10, 25, 25, 10, 5, 5},
                    {0, 0, 0, 20, 20, 0, 0, 0},
                    {5, -5, -10, 0, 0, -10, -5, 5},
                    {5, 10, 10, -20, -20, 10, 10, 5}, // pawns should avoid starting on e2
                    {0, 0, 0, 0, 0, 0, 0, 0},
            },
            {
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {5, 10, 10, -20, -20, 10, 10, 5}, // pawns should avoid starting on e2
                    {5, -5, -10, 0, 0, -10, -5, 5},
                    {0, 0, 0, 20, 20, 0, 0, 0},
                    {5, 5, 10, 25, 25, 10, 5, 5},
                    {10, 10, 20, 30, 30, 20, 10, 10},
                    {50, 50, 50, 50, 50, 50, 50, 50},
                    {100, 100, 100, 100, 100, 100, 100, 100}, // every pawn should try to make queen
            }

    };

    private static short[][][] knightsPositionMap = new short[][][] {
            {
                    {-50,-40,-30,-30,-30,-30,-40,-50},
                    {-40,-20,  0,  0,  0,  0,-20,-40},
                    {-30,  0, 10, 15, 15, 10,  0,-30},
                    {-30,  5, 15, 20, 20, 15,  5,-30},
                    {-30,  0, 15, 20, 20, 15,  0,-30},
                    {-30,  5, 10, 15, 15, 10,  5,-30},
                    {-40,-20,  0,  5,  5,  0,-20,-40},
                    {-50,-40,-30,-30,-30,-30,-40,-50},
            },
            {
                    {-50,-40,-30,-30,-30,-30,-40,-50},
                    {-40,-20,  0,  5,  5,  0,-20,-40},
                    {-30,  5, 10, 15, 15, 10,  5,-30},
                    {-30,  0, 15, 20, 20, 15,  0,-30},
                    {-30,  5, 15, 20, 20, 15,  5,-30},
                    {-30,  0, 10, 15, 15, 10,  0,-30},
                    {-40,-20,  0,  0,  0,  0,-20,-40},
                    {-50,-40,-30,-30,-30,-30,-40,-50},

            }
    };

    private static short[][][] bishopsPositionMap = new short[][][] {
            {
                    {-20,-10,-10,-10,-10,-10,-10,-20},
                    {-10,  0,  0,  0,  0,  0,  0,-10},
                    {-10,  0,  5, 10, 10,  5,  0,-10},
                    {-10,  5,  5, 10, 10,  5,  5,-10},
                    {-10,  0, 10, 10, 10, 10,  0,-10},
                    {-10, 10, 10, 10, 10, 10, 10,-10},
                    {-10,  5,  0,  0,  0,  0,  5,-10},
                    {-20,-10,-10,-10,-10,-10,-10,-20},
            },
            {

                    {-20,-10,-10,-10,-10,-10,-10,-20},
                    {-10,  5,  0,  0,  0,  0,  5,-10},
                    {-10, 10, 10, 10, 10, 10, 10,-10},
                    {-10,  0, 10, 10, 10, 10,  0,-10},
                    {-10,  5,  5, 10, 10,  5,  5,-10},
                    {-10,  0,  5, 10, 10,  5,  0,-10},
                    {-10,  0,  0,  0,  0,  0,  0,-10},
                    {-20,-10,-10,-10,-10,-10,-10,-20},
            }
    };

    private static short[][][] rooksPositionMap = new short[][][] {
            {
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {5, 10, 10, 10, 10, 10, 10, 5},
                    {-5, 0, 0, 0, 0, 0, 0, -5},
                    {-5, 0, 0, 0, 0, 0, 0, -5},
                    {-5, 0, 0, 0, 0, 0, 0, -5},
                    {-5, 0, 0, 0, 0, 0, 0, -5},
                    {-5, 0, 0, 0, 0, 0, 0, -5},
                    {0, 0, 0, 5, 5, 0, 0, 0}
            },
            {

                    {0, 0, 0, 5, 5, 0, 0, 0},
                    {-5, 0, 0, 0, 0, 0, 0, -5},
                    {-5, 0, 0, 0, 0, 0, 0, -5},
                    {-5, 0, 0, 0, 0, 0, 0, -5},
                    {-5, 0, 0, 0, 0, 0, 0, -5},
                    {-5, 0, 0, 0, 0, 0, 0, -5},
                    {5, 10, 10, 10, 10, 10, 10, 5},
                    {0, 0, 0, 0, 0, 0, 0, 0},

            }
    };

    private static short[][][] queenPositionMap = new short[][][] {
            {
                    {-20,-10,-10, -5, -5,-10,-10,-20},
                    {-10,  0,  0,  0,  0,  0,  0,-10},
                    {-10,  0,  5,  5,  5,  5,  0,-10},
                    {-5,  0,  5,  5,  5,  5,  0, -5},
                    {0,  0,  5,  5,  5,  5,  0, -5},
                    {-10,  5,  5,  5,  5,  5,  0,-10},
                    {-10,  0,  5,  0,  0,  0,  0,-10},
                    {-20,-10,-10, -5, -5,-10,-10,-20}
            },
            {
                    {-20,-10,-10, -5, -5,-10,-10,-20},
                    {-10,  0,  5,  0,  0,  0,  0,-10},
                    {-10,  5,  5,  5,  5,  5,  0,-10},
                    {0,  0,  5,  5,  5,  5,  0, -5},
                    {-5,  0,  5,  5,  5,  5,  0, -5},
                    {-10,  0,  5,  5,  5,  5,  0,-10},
                    {-10,  0,  0,  0,  0,  0,  0,-10},
                    {-20,-10,-10, -5, -5,-10,-10,-20},
            }
    };

    private static short[][][] kingPositionMap = new short[][][] {
            {
                    {-30,-40,-40,-50,-50,-40,-40,-30},
                    {-30,-40,-40,-50,-50,-40,-40,-30},
                    {-30,-40,-40,-50,-50,-40,-40,-30},
                    {-30,-40,-40,-50,-50,-40,-40,-30},
                    {-20,-30,-30,-40,-40,-30,-30,-20},
                    {-10,-20,-20,-20,-20,-20,-20,-10},
                    {20, 20,  0,  0,  0,  0, 20, 20},
                    {20, 30, 10,  0,  0, 10, 30, 20}
            },
            {
                    {20, 30, 10,  0,  0, 10, 30, 20},
                    {20, 20,  0,  0,  0,  0, 20, 20},
                    {-10,-20,-20,-20,-20,-20,-20,-10},
                    {-20,-30,-30,-40,-40,-30,-30,-20},
                    {-30,-40,-40,-50,-50,-40,-40,-30},
                    {-30,-40,-40,-50,-50,-40,-40,-30},
                    {-30,-40,-40,-50,-50,-40,-40,-30},
                    {-30,-40,-40,-50,-50,-40,-40,-30}

            }
    };
    private Move lastChosenMove;

    // black
    public int maxi(Game game, int depth) {
        if (depth == 0) { //I am maxi
            return -countPiecesScore(game, game.gameTurn.opposite());
        }
        int max = Integer.MIN_VALUE;
        List<Move> moves = new ArrayList<>();
        if (game.gameTurn == Board.TurnColor.white) {
            moves = game.gameBoard.listPossibleMovesWhite();
        } else if (game.gameTurn == Board.TurnColor.black) {
            moves = game.gameBoard.listPossibleMovesBlack();
        }

        Collections.shuffle(moves);
        Move chosenMove = moves.iterator().next();
        for (Move potentialMove : moves) {
            game.preexecuteMove(potentialMove);
            int score = mini(game, depth - 1);
            game.undoMove();
            if (score > max) {
                max = score;
                chosenMove = potentialMove;
            }
        }

        this.lastChosenMove = chosenMove;
        return max;
    }

    private int negaMax(Game game, int depth ) {
        if ( depth == 0 ) {
            return -countPiecesScore(game, game.gameTurn);
        }
        int max = Integer.MIN_VALUE;
        List<Move> moves = null;
        switch (game.gameTurn) {
            case black:
                moves = game.gameBoard.listPossibleMovesBlack();
                break;
            case white:
                moves = game.gameBoard.listPossibleMovesWhite();
                break;
        }
        for (Move move : moves)  {
            game.executeMove(move);
            int score = -negaMax(game,depth - 1 );
            game.undoMove();
            if( score > max )
                max = score;
        }
        return max;
    }

    // white
    public int mini(Game game, int depth) {
        if (depth == 0) {
            return -countPiecesScore(game, game.gameTurn.opposite());
        }
        int min = Integer.MAX_VALUE;
        List<Move> moves = game.gameBoard.listPossibleMovesWhite();
        for (Move potentialMove : moves) {
            game.preexecuteMove(potentialMove);
            int score = maxi(game, depth - 1);
            game.undoMove();
            if (score < min) {
                min = score;
            }
        }
        return min;
    }


    @Override
    public Move playBlack(Game game) {
        List<Move> moves = game.gameBoard.listPossibleMovesBlack();
        Collections.shuffle(moves);
//        Move move = moves.stream().sorted((m1, m2) -> greedyEvaluation(m1, m2, game)).findFirst().get();
//        game.executeMove(move);
        maxi(game, MAX_DEPTH);
        Move chosenMove = lastChosenMove;
        game.executeMove(chosenMove);
        return chosenMove;
    }

    @Override
    public Move playWhite(Game game) {
        List<Move> moves = game.gameBoard.listPossibleMovesWhite();
        Collections.shuffle(moves);
//        Move move = moves.stream().sorted((m1, m2) -> greedyEvaluation(m1, m2, game)).findFirst().get();
//        game.executeMove(move);
        maxi(game, MAX_DEPTH);
        Move chosenMove = lastChosenMove;
        game.executeMove(chosenMove);
        return chosenMove;
//        List<Move> moves = game.gameBoard.listPossibleMovesWhite();
//        Move move = moves.stream().sorted((m1, m2) -> greedyEvaluation(m1, m2, game)).findFirst().get();
//
//        game.executeMove(move);
//        return move;
    }

    private int greedyEvaluation(Move move1, Move move2, Game game) {
        double score1 = estimateBoardScore(move1, game);
        double score2 = estimateBoardScore(move2, game);
        return (int)  (-1 * Math.signum(score1 - score2));
    }

    private double estimateBoardScore(Move move, Game game) {
        Board.TurnColor otherColor = game.gameTurn.opposite();
        long oppositePiecesScoreNow = countPiecesScore(game, otherColor);
        long teritorialCoverageNow = getTeritorialCoverage(game);
        long positionalBiasNow = getPositionalBias(game);
        double forwardScoreNow = getForwardScore(game);

        game.executeMove(move);

        long oppositePiecesScoreAfter = countPiecesScore(game, otherColor);
        long teritorialCoverageAfter = getTeritorialCoverage(game);
        long positionalBiasAfter = getPositionalBias(game);
        double forwardScoreAfter = getForwardScore(game);


        game.undoMove();

        long finalScore = 0;
        if (oppositePiecesScoreAfter < oppositePiecesScoreNow) {
            finalScore += (int) 1000 * (oppositePiecesScoreNow - oppositePiecesScoreAfter);
        } else if (oppositePiecesScoreAfter == oppositePiecesScoreNow) {
            finalScore += 25* (positionalBiasNow - positionalBiasAfter);
            if (teritorialCoverageAfter > teritorialCoverageNow) {
                finalScore += (int) 100 * (teritorialCoverageAfter - teritorialCoverageNow);
            } else {
                finalScore+= 100 *(forwardScoreAfter - forwardScoreNow);
            }
        }
        return finalScore;
    }

    private long getPositionalBias(Game game) {
        long positionalBias = 0;
        int colorPointer = -1;
        Collection<Square> squares = null;
        if (game.gameTurn.equals(Board.TurnColor.white)) {
            colorPointer = WHITE;
            squares = game.gameBoard.getWhitePieces();
        } else if (game.gameTurn.equals(Board.TurnColor.black)) {
            colorPointer = BLACK;
            squares = game.gameBoard.getBlackPieces();
        }
        for (Square square : squares){
            //FIXME rewrite pieces as pointers!!!
            if (square.occupyingPiece instanceof Pawn) {
                long score = pawnPositionMap[colorPointer][square.occupyingPiece.yLocation][square.occupyingPiece.xLocation];
                positionalBias += score;
            } else if (square.occupyingPiece instanceof Knight) {
                long score = knightsPositionMap[colorPointer][square.occupyingPiece.yLocation][square.occupyingPiece.xLocation];
                positionalBias += score;
            } else if (square.occupyingPiece instanceof Bishop) {
                long score = bishopsPositionMap[colorPointer][square.occupyingPiece.yLocation][square.occupyingPiece.xLocation];
                positionalBias += score;
            } else if (square.occupyingPiece instanceof Rook) {
                long score = rooksPositionMap[colorPointer][square.occupyingPiece.yLocation][square.occupyingPiece.xLocation];
                positionalBias += score;
            } else if (square.occupyingPiece instanceof Queen) {
                long score = queenPositionMap[colorPointer][square.occupyingPiece.yLocation][square.occupyingPiece.xLocation];
                positionalBias += score;
            } else if (square.occupyingPiece instanceof King) {
                long score = kingPositionMap[colorPointer][square.occupyingPiece.yLocation][square.occupyingPiece.xLocation];
                positionalBias += score;
            }
        };
        return positionalBias;
    }

    private Integer countPiecesScore(Game game, Board.TurnColor otherColor) {
        return Arrays.stream(game.gameBoard.squaresList).flatMap(a -> Arrays.stream(a))
                .filter(square -> square.isOccupied)
                .filter(square -> square.occupyingPiece.turnColor == otherColor)
                .map(square -> pieceScore(square.occupyingPiece)).reduce(Integer::sum).get();
    }

    private static int pieceScore(Piece occupyingPiece) {
        if (occupyingPiece instanceof King) {
            return 900;
        } else if (occupyingPiece instanceof Queen) {
            return 90;
        } else if (occupyingPiece instanceof Rook) {
            return 50;
        } else if (occupyingPiece instanceof Knight) {
            return 30;
        } else if (occupyingPiece instanceof Bishop) {
            return 30;
        } else if (occupyingPiece instanceof Pawn) {
            return 10;
        }
        return 0;
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


}
