package chessControllers;

import chessGame.*;
import com.gpachov.videocreator.ChessEngine;

import java.util.*;

import static chessControllers.Game.DEBUG;

public class MiniMaxStrategy implements PlayingStrategy {
    public static int maxDepth = 3;
    private static short WHITE = 0;
    private static short BLACK = 1;
    private static short[] PIECES_SCORE = new short[32];

    static {
        PIECES_SCORE[StandardBoard.WHITE_PAWN] = 10;
        PIECES_SCORE[StandardBoard.BLACK_PAWN] = 10;
        PIECES_SCORE[StandardBoard.WHITE_KNIGHT] = 30;
        PIECES_SCORE[StandardBoard.BLACK_KNIGHT] = 30;
        PIECES_SCORE[StandardBoard.WHITE_BISHOP] = 30;
        PIECES_SCORE[StandardBoard.BLACK_BISHOP] = 30;
        PIECES_SCORE[StandardBoard.WHITE_ROOK] = 50;
        PIECES_SCORE[StandardBoard.BLACK_ROOK] = 50;
        PIECES_SCORE[StandardBoard.WHITE_QUEEN] = 90;
        PIECES_SCORE[StandardBoard.BLACK_QUEEN] = 90;
        PIECES_SCORE[StandardBoard.WHITE_KING] = 900;
        PIECES_SCORE[StandardBoard.BLACK_KING] = 900;
    }

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
    private int negaMax(Game game, int depth, float alpha, float beta, TurnColor turnColor) {
        long start = System.currentTimeMillis();
        if (depth == 0 || game.gameOver) {
            int colorMultiplier = 0;
            switch (turnColor) {
                case white:
                    colorMultiplier = 1;
                    break;
                case black:
                    colorMultiplier = -1;
                    break;
            }
            return colorMultiplier*estimateBoard(game, turnColor);
        }
        int max = (Integer.MIN_VALUE/2);
        List<Integer> moves = new ArrayList<>();
        game.gameBoard.populatePossibleMoves(game.gameBoard.gameTurn, moves);
        long e = System.currentTimeMillis();
        if (depth == maxDepth && DEBUG) {
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
        packedMoves.sort((c1, c2) -> cmp(game, c1, c2, turnColor));
        e = System.currentTimeMillis();
        if (depth == maxDepth && DEBUG) {
            System.out.println("Sorting:" + (e - start));
        }

        int[] maxMove = null;
        if (packedMoves.size() == 0) {
            // we are either in mate or draw situation
            int[] playerKing = game.gameBoard.getKing(turnColor);
            if (game.gameBoard.isKingInCheck(playerKing)) {
                // checkmate
                return game.gameBoard.getMultiplier(turnColor) * (Integer.MAX_VALUE - 10);
            } else {
                return game.gameBoard.getMultiplier(turnColor) * Integer.MAX_VALUE/2;
            }

        }
        for (int[] move : packedMoves)  {
            game.preexecuteMove(move);
            int score = -1 * negaMax(game,depth - 1, -beta, -alpha, turnColor.opposite());
            if (maxDepth == depth && DEBUG) {
                System.out.println(String.format("Move: [%d,%d,%d,%d] evaluated as %d", move[0], move[1], move[2], move[3], score));
            }
            game.undoMove();
            if( score > max ) {
                max = score;
                maxMove = move;
            }
            alpha = Math.max(max, alpha);
            if (alpha > beta) {
                this.lastChosenMove = maxMove;
                return max;
            }
        }
        e = System.currentTimeMillis();
        if (depth == maxDepth && DEBUG) {
            System.out.println("Negamax: " + (e - start));
            System.out.println("Choosing move" + Arrays.toString(lastChosenMove));
        }
        this.lastChosenMove = maxMove;
        return max;
    }

    private int cmp(Game game, int[] c1, int[] c2, TurnColor turnColor) {
        game.preexecuteMove(c1);
        int estimate1 = estimateBoard(game, turnColor);
        game.undoMove();

        game.preexecuteMove(c2);
        int estimate2 = estimateBoard(game, turnColor);
        game.undoMove();

        return -1 * (estimate1 - estimate2);
    }

    private int estimateBoard(Game game, TurnColor turnColor) {
        int isOpponentKingCheckMated = checkKingCheckMateScore(game, turnColor);
        int isOpponentKingInCheck = checkKingCheckStatus(game, turnColor);

        int myPiecesScore = countPiecesScore(game, turnColor);
        int opponentPieceScore = countPiecesScore(game, turnColor.opposite());

        int myPositionalScore = getPositionalBias(game, turnColor);
        if (DEBUG) {
//            System.out.println("Pieces score for : " + game.gameBoard.gameTurn + (myPiecesScore - opponentPieceScore));
//            System.out.println("Positional score for : " + game.gameBoard.gameTurn + (myPositionalScore));

//            System.out.println("IsOponnentCheckMated Score : " + isOpponentKingCheckMated);
//            System.out.println("IsOpponentKingInCheck Score : " + isOpponentKingInCheck);
//            System.out.println("MyPieces score : " + myPiecesScore);
//            System.out.println("Opponenet Piece Score : " + opponentPieceScore);
//            System.out.println("MyPositional Score : " + myPositionalScore);


        }

        int finalScore = isOpponentKingCheckMated + isOpponentKingInCheck
                + 100 * (myPiecesScore - opponentPieceScore)
                + (myPositionalScore);
        return finalScore; //opening book simulation
    }

    private int checkKingCheckStatus(Game game, TurnColor gameTurn) {
        int[] king = game.gameBoard.getKing(gameTurn);
        if (game.gameBoard.isKingInCheck(king)) {
            return 2000;
        }
        return 0;

    }

    private int checkKingCheckMateScore(Game game, TurnColor gameTurn) {
        int[] king = game.gameBoard.getKing(gameTurn);
        int kx = king[0];
        int ky = king[1];
        short possibleMoves = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i== 0 && j == 0) {
                    continue;
                }
                if (game.gameBoard.canMove(kx, ky,kx +i, ky+j)){
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
        negaMax(game, maxDepth, Integer.MIN_VALUE/2, Integer.MAX_VALUE/2, TurnColor.black);
        int[] chosenMove = lastChosenMove;
        game.executeMove(chosenMove);
        long end = System.currentTimeMillis();
        System.out.println("Thinking took " + (end-start) + "milliseconds");
        return chosenMove;
    }

    @Override
    public int[] playWhite(Game game) {
        negaMax(game, maxDepth, Integer.MIN_VALUE/2, Integer.MAX_VALUE/2, TurnColor.white);
        int[] chosenMove = lastChosenMove;
        game.executeMove(chosenMove);
        return chosenMove;
    }

    private int getPositionalBias(Game game, TurnColor ofColor) {
        int positionalBias = 0;
        int colorPointer = -1;
        Collection<int[]> pieces = game.gameBoard.getPieces(ofColor);;
        if (ofColor.equals(TurnColor.white)) {
            colorPointer = WHITE;
        } else if (ofColor.equals(TurnColor.black)) {
            colorPointer = BLACK;
        }
        for (int[] coords : pieces){
            int x = coords[0];
            int y = coords[1];
            long score = 0;
            //FIXME rewrite pieces as pointers!!!
            switch (game.gameBoard.pieces[coords[0]][coords[1]]) {
                case StandardBoard.WHITE_PAWN:
                case StandardBoard.BLACK_PAWN:
                    score = pawnPositionMap[colorPointer][y][x];
                    positionalBias += score;
                    break;
                case StandardBoard.WHITE_BISHOP:
                case StandardBoard.BLACK_BISHOP:
                    score = bishopsPositionMap[colorPointer][y][x];
                    positionalBias += score;
                    break;
                case StandardBoard.WHITE_KNIGHT:
                case StandardBoard.BLACK_KNIGHT:
                    score = knightsPositionMap[colorPointer][y][x];
                    positionalBias += score;
                    break;
                case StandardBoard.WHITE_QUEEN:
                case StandardBoard.BLACK_QUEEN:
                    score = queenPositionMap[colorPointer][y][x];
                    positionalBias += score;
                    break;
                case StandardBoard.WHITE_ROOK:
                case StandardBoard.BLACK_ROOK:
                    score = rooksPositionMap[colorPointer][y][x];
                    positionalBias += score;
                    break;
                case StandardBoard.WHITE_KING:
                case StandardBoard.BLACK_KING:
                    score = kingPositionMap[colorPointer][y][x];
                    positionalBias += score;
                    break;
            }
        };
        return positionalBias;
    }

    private Integer countPiecesScore(Game game, TurnColor ofColor) {
        return game.gameBoard.getPieces(ofColor).stream()
                .mapToInt(coords -> PIECES_SCORE[game.gameBoard.pieces[coords[0]][coords[1]]])
                .sum();
    }

    public static void main(String[] args) {
        int test = Integer.MAX_VALUE - 10;
        System.out.println(-1 * test);
    }
}
