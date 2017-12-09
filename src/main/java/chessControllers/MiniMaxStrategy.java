package chessControllers;

import chessGame.*;

import java.util.*;
import java.util.stream.Collectors;

public class MiniMaxStrategy implements PlayingStrategy {
    private static final int MAX_DEPTH = 5;
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
    private int[] lastChosenMove;
    private int negaMax(Game game, int depth, float alpha, float beta) {
        long start = System.currentTimeMillis();
        if (depth == 0) {
            return estimateBoard(game);
        }
        int max = Integer.MIN_VALUE;
        List<Integer> moves = new ArrayList<>();
        game.gameBoard.populatePossibleMoves(game.gameTurn, moves);
        long e = System.currentTimeMillis();
        if (depth == MAX_DEPTH) {
            System.out.println("Move generation: " + (e - start));
        }

        List<int[]> packedMoves = new ArrayList<>();
        int[] c = new int[4];
        int counter = 0;
        for (int number: moves) {
            c[counter++] = number;
            if (counter == 4) {
                packedMoves.add(c);
                c = new int[4];
                counter = 0;
            }
        }
        packedMoves.sort((c1, c2) -> cmp(game, c1, c2));
        e = System.currentTimeMillis();
        if (depth == MAX_DEPTH) {
            System.out.println("Sorting:" + (e - start));
        }

        int[] maxMove = null;
        for (int[] move : packedMoves)  {
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
        e = System.currentTimeMillis();
        if (depth == MAX_DEPTH) {
            System.out.println("Negamax: " + (e - start));
        }
        this.lastChosenMove = maxMove;
        return max;
    }

    private int cmp(Game game, int[] c1, int[] c2) {
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
        Piece king = game.gameBoard.getPieces(gameTurn).stream().filter(p -> p instanceof King).collect(Collectors.toList()).get(0);
        switch (gameTurn) {
            case white:
                if (game.gameBoard.whiteKingTracker.isKingInCheck((King) king)) {
                    return 2000;
                }
            case black:
                if (game.gameBoard.blackKingTracker.isKingInCheck((King) king)) {
                    return 2000;
                }
            }
        return 0;

    }

    private int checkKingCheckMateScore(Game game, TurnColor gameTurn) {
        List<Piece> pieces = game.gameBoard.getPieces(gameTurn);
        Piece kingSquare = game.gameBoard.getKing(gameTurn);
        int kx = kingSquare.xLocation;
        int ky = kingSquare.yLocation;
        short possibleMoves = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i== 0 && j == 0) {
                    continue;
                }
                if (kingSquare.canMove(kx +i, ky+j)){
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
    public int[] playBlack(Game game) {
        long start = System.currentTimeMillis();
        negaMax(game, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE);
        int[] chosenMove = lastChosenMove;
        game.executeMove(chosenMove);
        long end = System.currentTimeMillis();
        System.out.println("Thinking took " + (end-start) + "milliseconds");
        return chosenMove;
    }

    @Override
    public int[] playWhite(Game game) {
        negaMax(game, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE);
        int[] chosenMove = lastChosenMove;
        game.executeMove(chosenMove);
        return chosenMove;
    }

    private int getPositionalBias(Game game, TurnColor ofColor) {
        int positionalBias = 0;
        int colorPointer = -1;
        Collection<Piece> pieces = game.gameBoard.getPieces(ofColor);;
        if (ofColor.equals(TurnColor.white)) {
            colorPointer = WHITE;
        } else if (ofColor.equals(TurnColor.black)) {
            colorPointer = BLACK;
        }
        for (Piece piece : pieces){
            //FIXME rewrite pieces as pointers!!!
            if (piece instanceof Pawn) {
                long score = pawnPositionMap[colorPointer][piece.yLocation][piece.xLocation];
                positionalBias += score;
            } else if (piece instanceof Knight) {
                long score = knightsPositionMap[colorPointer][piece.yLocation][piece.xLocation];
                positionalBias += score;
            } else if (piece instanceof Bishop) {
                long score = bishopsPositionMap[colorPointer][piece.yLocation][piece.xLocation];
                positionalBias += score;
            } else if (piece instanceof Rook) {
                long score = rooksPositionMap[colorPointer][piece.yLocation][piece.xLocation];
                positionalBias += score;
            } else if (piece instanceof Queen) {
                long score = queenPositionMap[colorPointer][piece.yLocation][piece.xLocation];
                positionalBias += score;
            } else if (piece instanceof King) {
                long score = kingPositionMap[colorPointer][piece.yLocation][piece.xLocation];
                positionalBias += score;
            }
        };
        return positionalBias;
    }

    private Integer countPiecesScore(Game game, TurnColor ofColor) {
        return game.gameBoard.getPieces(ofColor).stream()
                .map(MiniMaxStrategy::pieceScore)
                .reduce(Integer::sum).get();
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
}
