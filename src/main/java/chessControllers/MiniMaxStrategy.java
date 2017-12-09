package chessControllers;

import chessGame.*;

import java.util.*;
import java.util.stream.Collectors;

public class MiniMaxStrategy implements PlayingStrategy {
    private static final int MAX_DEPTH = 3;
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
    private int negaMax(Game game, int depth, float alpha, float beta) {
        if (depth == 0) {
            long s = System.currentTimeMillis();
            int estimation = estimateBoard(game);
            long e = System.currentTimeMillis();
//            System.out.println("Pure estimation took " + (e - s) + " milliseconds");
            return estimation;
        }
        int max = Integer.MIN_VALUE;
        List<Move> moves = null;
        long s = System.currentTimeMillis();

        switch (game.gameTurn) {
            case black:
                moves = game.gameBoard.listPossibleMovesBlack();
                break;
            case white:
                moves = game.gameBoard.listPossibleMovesWhite();
                break;
        }
        long e = System.currentTimeMillis();
        if (depth ==MAX_DEPTH) {
            System.out.println("Move generation estimation took " + (e - s) + " milliseconds");
        }
        s = System.currentTimeMillis();
        moves.sort((c1, c2) -> cmp(game, c1, c2));
        e = System.currentTimeMillis();
        if (depth == MAX_DEPTH) {
            System.out.println("Sorting took " + (e - s) + " milliseconds");
        }

        Move maxMove = null;
        for (Move move : moves)  {
            game.preexecuteMove(move);
            int score = -negaMax(game,depth - 1, -beta, -alpha);
            game.undoMove();
            if( score > max ) {
                max = score;
                maxMove = move;
            }
            alpha = Math.max(max, alpha);
            if (alpha > beta) {
                return score;
            }
        }
        this.lastChosenMove = maxMove;
        return max;
    }

    private int cmp(Game game, Move c1, Move c2) {
        game.preexecuteMove(c1);
        int estimate1 = estimateBoard(game);
        game.undoMove();

        game.preexecuteMove(c2);
        int estimate2 = estimateBoard(game);
        game.undoMove();

        return -1 * (estimate1 - estimate2);
    }

    private int estimateBoard(Game game) {
        int isOpponentKingCheckMated = checkKingCheckMateScore(game, game.gameTurn);
        int isOpponentKingInCheck = checkKingCheckStatus(game, game.gameTurn);

        int myPiecesScore = countPiecesScore(game, game.gameTurn);
        int opponentPieceScore = countPiecesScore(game, game.gameTurn.opposite());

        int myPositionalScore = getPositionalBias(game, game.gameTurn);
        if (Game.DEBUG) {
            System.out.println("Pieces score for : " + game.gameTurn + (myPiecesScore - opponentPieceScore));
            System.out.println("Positional score for : " + game.gameTurn + (myPositionalScore));
        }

        int finalScore = isOpponentKingCheckMated + isOpponentKingInCheck
                + 100 * (myPiecesScore - opponentPieceScore)
                + (myPositionalScore);
        return finalScore; //opening book simulation
    }

    private int checkKingCheckStatus(Game game, TurnColor gameTurn) {
        Square kingSquare = game.gameBoard.getPieces(gameTurn).stream().filter(s -> s.occupyingPiece instanceof King).collect(Collectors.toList()).get(0);
        switch (gameTurn) {
            case white:
                if (game.gameBoard.whiteKingTracker.isKingInCheck((King) kingSquare.occupyingPiece)) {
                    return 2000;
                }
            case black:
                if (game.gameBoard.blackKingTracker.isKingInCheck((King) kingSquare.occupyingPiece)) {
                    return 2000;
                }
            }
        return 0;

    }

    private int checkKingCheckMateScore(Game game, TurnColor gameTurn) {
        List<Square> squares = game.gameBoard.getPieces(gameTurn);
        Square kingSquare = squares.stream().filter(s -> s.occupyingPiece instanceof King).collect(Collectors.toList()).get(0);
        int kx = kingSquare.occupyingPiece.xLocation;
        int ky = kingSquare.occupyingPiece.yLocation;
        short possibleMoves = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i== 0 && j == 0) {
                    continue;
                }
                if (kingSquare.occupyingPiece.canMove(kx +i, ky+j)){
                    possibleMoves++;
                }
            }
        }

        switch (possibleMoves) {
            case 0:
                return Integer.MAX_VALUE/2;
            case 1:
                return 500;
            case 2:
                return 250;
            case 3:
                return 150;
        }
        return 0;
    }


    @Override
    public Move playBlack(Game game) {
        long start = System.currentTimeMillis();
        negaMax(game, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE);
        Move chosenMove = lastChosenMove;
        game.executeMove(chosenMove);
        long end = System.currentTimeMillis();
        System.out.println("Thinking took " + (end-start) + "milliseconds");
        return chosenMove;
    }

    @Override
    public Move playWhite(Game game) {
        negaMax(game, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE);
        Move chosenMove = lastChosenMove;
        game.executeMove(chosenMove);
        return chosenMove;
    }

//    private int greedyEvaluation(Move move1, Move move2, Game game) {
//        double score1 = estimateBoardScore(move1, game);
//        double score2 = estimateBoardScore(move2, game);
//        return (int)  (-1 * Math.signum(score1 - score2));
//    }

//    private double estimateBoardScore(Move move, Game game) {
//        TurnColor otherColor = game.gameTurn.opposite();
//        long oppositePiecesScoreNow = countPiecesScore(game, otherColor);
//        long teritorialCoverageNow = getTeritorialCoverage(game);
//        long positionalBiasNow = getPositionalBias(game);
//        double forwardScoreNow = getForwardScore(game);
//
//        game.executeMove(move);
//
//        long oppositePiecesScoreAfter = countPiecesScore(game, otherColor);
//        long teritorialCoverageAfter = getTeritorialCoverage(game);
//        long positionalBiasAfter = getPositionalBias(game);
//        double forwardScoreAfter = getForwardScore(game);
//
//
//        game.undoMove();
//
//        long finalScore = 0;
//        if (oppositePiecesScoreAfter < oppositePiecesScoreNow) {
//            finalScore += (int) 1000 * (oppositePiecesScoreNow - oppositePiecesScoreAfter);
//        } else if (oppositePiecesScoreAfter == oppositePiecesScoreNow) {
//            finalScore += 25* (positionalBiasNow - positionalBiasAfter);
//            if (teritorialCoverageAfter > teritorialCoverageNow) {
//                finalScore += (int) 100 * (teritorialCoverageAfter - teritorialCoverageNow);
//            } else {
//                finalScore+= 100 *(forwardScoreAfter - forwardScoreNow);
//            }
//        }
//        return finalScore;
//    }

    private int getPositionalBias(Game game, TurnColor ofColor) {
        int positionalBias = 0;
        int colorPointer = -1;
        Collection<Square> squares = null;
        if (ofColor.equals(TurnColor.white)) {
            colorPointer = WHITE;
            squares = game.gameBoard.getWhitePieces();
        } else if (ofColor.equals(TurnColor.black)) {
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

    private Integer countPiecesScore(Game game, TurnColor ofColor) {
        return Arrays.stream(game.gameBoard.squaresList).flatMap(a -> Arrays.stream(a))
                .filter(square -> square.isOccupied)
                .filter(square -> square.occupyingPiece.turnColor == ofColor)
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
