package com.gpachov.chess.ai;

import com.gpachov.chess.board.Board;
import com.gpachov.chess.board.TurnColor;
import com.gpachov.chess.Util;
import com.gpachov.chess.board.Constants;

import java.util.*;

import static com.gpachov.chess.board.TurnColor.black;

public class MiniMaxStrategy implements PlayingStrategy {
    public static int MAX_DEPTH = 5;
    private static short WHITE = 0;
    private static short BLACK = 1;
    private static short[] PIECES_SCORE = new short[32];

    static {
        PIECES_SCORE[Board.WHITE_PAWN] = 100;
        PIECES_SCORE[Board.BLACK_PAWN] = 100;
        PIECES_SCORE[Board.WHITE_KNIGHT] = 300;
        PIECES_SCORE[Board.BLACK_KNIGHT] = 300;
        PIECES_SCORE[Board.WHITE_BISHOP] = 300;
        PIECES_SCORE[Board.BLACK_BISHOP] = 300;
        PIECES_SCORE[Board.WHITE_ROOK] = 500;
        PIECES_SCORE[Board.BLACK_ROOK] = 500;
        PIECES_SCORE[Board.WHITE_QUEEN] = 900;
        PIECES_SCORE[Board.BLACK_QUEEN] = 900;
        PIECES_SCORE[Board.WHITE_KING] = 1100;
        PIECES_SCORE[Board.BLACK_KING] = 1100;
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
    private HashMap<Integer, Integer> gameHashes = new LinkedHashMap<Integer,Integer>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > 100_000_000;
        }
    };
    private HashMap<Integer, HashMap<Integer, int[]>> movesCache = new LinkedHashMap<Integer, HashMap<Integer, int[]>>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > 100_000_000;
        }
    };
    private HashMap<Integer, HashMap<Integer, Integer>> negamaxCache = new LinkedHashMap<Integer, HashMap<Integer, Integer>>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > 100_000_000;
        }
    };

    private int negaMax(Board board, int depth, float alpha, float beta, TurnColor turnColor) {
        int deepHash = 31 * turnColor.hashCode() + Arrays.deepHashCode(board.pieces);
        if (depth == 0) {
            return evaluateBoard(board, turnColor, deepHash);
        }

        if (negamaxCache.containsKey(deepHash)) {
            if (negamaxCache.get(deepHash).containsKey(depth)) {
                lastChosenMove = movesCache.get(deepHash).get(depth);
                return negamaxCache.get(deepHash).get(depth);
            }
        }


        int max = (Integer.MIN_VALUE/2);
        List<Integer> moves = new ArrayList<>();

        board.populatePossibleMoves(turnColor, moves);

        List<int[]> packedMoves = pack(moves);
        packedMoves.sort((c1, c2) -> cmp(board, c1, c2, turnColor));
        int maxBeamWidth = 100;
        if (packedMoves.size() > maxBeamWidth) {
            packedMoves = packedMoves.subList(0, maxBeamWidth);
        }

        int[] maxMove = null;
        if (packedMoves.size() == 0) {
            // we are either in mate or draw situation
            int[] playerKing = board.getKing(turnColor);
            if (board.isKingInCheck(playerKing)) {
                // checkmate
                int mateValue = board.getMultiplier(turnColor) * (Integer.MIN_VALUE / 2 - 10);
                return mateValue;
            } else {
                // stalemate
                int staleMateValue = 0;
                return staleMateValue;
            }

        }
        for (int[] move : packedMoves)  {
            board.preexecuteMove(move);
            int score = -1 * negaMax(board,depth - 1, -beta, -alpha, turnColor.opposite());
            if (MAX_DEPTH == depth && Constants.DEBUG) {
                System.out.println(String.format("Move: [%d,%d,%d,%d] evaluated as %d", move[0], move[1], move[2], move[3], score));
            }
            board.undoMove();
            if( score > max ) {
                max = score;
                maxMove = move;
            }
            alpha = Math.max(max, alpha);
            if (alpha > beta) {
                this.lastChosenMove = maxMove;

                // update caches
                negamaxCache.putIfAbsent(deepHash, new HashMap<>());
                negamaxCache.get(deepHash).put(depth,max);

                movesCache.putIfAbsent(deepHash, new HashMap<>());
                movesCache.get(deepHash).put(depth, lastChosenMove);

                return max;
            }
        }
        this.lastChosenMove = maxMove;

        // update caches
//        negamaxCache.putIfAbsent(deepHash, new HashMap<>());
//        negamaxCache.get(deepHash).put(depth,max);
//
//        movesCache.putIfAbsent(deepHash, new HashMap<>());
//        movesCache.get(deepHash).put(depth, lastChosenMove);

        return max;
    }

    private List<int[]> pack(List<Integer> moves) {
        List<int[]> packedMoves = new ArrayList<>();
        int[] c = new int[5];
        int counter = 0;
        for (int number: moves) {
            counter++;
            if (counter == 5) {
                boolean isChessPiece = black.isPiece(number) || TurnColor.white.isPiece(number);
                if (isChessPiece) {
                    c[counter-1] = number;
                } else {
                    c[counter-1] = 0;
                }
                packedMoves.add(c);
                c = new int[5];
                counter = 0;
            } else {
                c[counter-1] = number;
            }
        }
        return packedMoves;
    }

    private int cmp(Board board, int[] c1, int[] c2, TurnColor turnColor) {
        Util.logV("Comparing: " + Arrays.toString(c1) + " against " + Arrays.toString(c2));
        board.preexecuteMove(c1);
        int h1 = 31* turnColor.hashCode() + Arrays.deepHashCode(board.pieces);
        int estimate1 = evaluateBoard(board, turnColor, h1);
        board.undoMove();

        board.preexecuteMove(c2);
        int h2 = 31* turnColor.hashCode() + Arrays.deepHashCode(board.pieces);
        int estimate2 = evaluateBoard(board, turnColor, h2);
        board.undoMove();

        Util.logV("Estimated move " + Arrays.toString(c1) + " as " + estimate1);
        Util.logV("Estimated move " + Arrays.toString(c2) + " as " + estimate2);

        return -1 * (estimate1 - estimate2);
    }

    private int evaluateBoard(Board board, TurnColor me, int hash) {
        if (gameHashes.containsKey(hash)) {
            return gameHashes.get(hash);
        } else {
            int m_mate = checkKingCheckMateScore(board, me.opposite());

            int o_mate = checkKingCheckMateScore(board, me);

            int m_pieces = countPiecesScore(board, me);
            int o_pieces = countPiecesScore(board, me.opposite());

            int m_pos = getPositionalBias(board, me);
            int o_pos = getPositionalBias(board, me.opposite());

            int m_coveredScore = getCoveredPieces(board, me);
            int o_coveredScore = getCoveredPieces(board, me.opposite());

            if (Constants.DEBUG) {
//            System.out.println("Pieces score for : " + game.board.gameTurn + (m_pieces - o_pieces));
//            System.out.println("Positional score for : " + game.board.gameTurn + (m_pos));

//            System.out.println("IsOponnentCheckMated Score : " + o_mate);
//            System.out.println("IsOpponentKingInCheck Score : " + o_check);
//            System.out.println("MyPieces score : " + m_pieces);
//            System.out.println("Opponenet Piece Score : " + o_pieces);
//            System.out.println("MyPositional Score : " + m_pos);


            }

            int finalScore = (int) ((int) (1*(m_mate - o_mate) +
                                         + 1* (m_pieces - o_pieces) +
                                        0.25f * (m_pos + o_pos)) +
                                        0.01f *(m_coveredScore - o_coveredScore));

            int coveredScore = (int) (0.01f *(m_coveredScore - o_coveredScore));
            Util.logS("Covered score: " + coveredScore);
            gameHashes.put(hash, finalScore);
            return finalScore;
        }

    }

    private int getCoveredPieces(Board board, TurnColor me) {
        List<int[]> pieces = board.getPieces(me);
        int coverScore = 0;
        for (int i = 0; i < pieces.size(); i++) {
            int[] targetPiece = pieces.get(i);
            for (int j = 0; j < pieces.size(); j++) {
                if (j!=i) {
                    int[] movingPiece = pieces.get(j);
                    if (board.isValidMove(movingPiece[0], movingPiece[1], targetPiece[0], targetPiece[1])) {
                        int targetFigure = board.pieces[targetPiece[0]][targetPiece[1]];
                        coverScore+=PIECES_SCORE[targetFigure];
                    }
                }
            }
        }
        return coverScore;
    }

    private int kingIsInCheck(Board board, TurnColor gameTurn) {
        int[] king = board.getKing(gameTurn);
        return (board.isKingInCheck(king)) ? 6 : 0;
    }

    private int checkKingCheckMateScore(Board board, TurnColor gameTurn) {
        int m_check = kingIsInCheck(board, gameTurn);
        if (m_check > 0) {
            int[] king = board.getKing(gameTurn);
            int kx = king[0];
            int ky = king[1];
            short possibleMoves = 0;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) {
                        continue;
                    }
                    if (board.canMove(kx, ky, kx + i, ky + j)) {
                        possibleMoves++;
                    }
                }
            }

            switch (possibleMoves) {
                case 0:
                    return 100_000;
                default:
                    return 0;
            }
        }
        return 0;
    }


    @Override
    public int[] playBlack(Board board) {
        long start = System.currentTimeMillis();
        int[] chosenMove = findMoveBlack(board);
//        negaMax(game, MAX_DEPTH, Integer.MIN_VALUE/2, Integer.MAX_VALUE/2, black);
//        int[] chosenMove = lastChosenMove;
        board.executeMove(chosenMove);
        lastChosenMove = null;
        long end = System.currentTimeMillis();
        System.out.println("Thinking took " + (end-start) + "milliseconds");

        return chosenMove;
    }

    private int[] findMoveBlack(Board board) {
        negaMax(board, MAX_DEPTH, Integer.MIN_VALUE/2, Integer.MAX_VALUE/2, black);
        return lastChosenMove;
    }

    @Override
    public int[] playWhite(Board board) {
        int[] chosenMove = findMoveWhite(board);
//        negaMax(game, MAX_DEPTH, Integer.MIN_VALUE/2, Integer.MAX_VALUE/2, TurnColor.white);
//        int[] chosenMove = lastChosenMove;
        board.executeMove(chosenMove);
        lastChosenMove = null;
        return chosenMove;
    }

    private int[] findMoveWhite(Board board) {
        negaMax(board, MAX_DEPTH, Integer.MIN_VALUE/2, Integer.MAX_VALUE/2, TurnColor.white);
        return lastChosenMove;
    }

    private int getPositionalBias(Board board, TurnColor ofColor) {
        int positionalBias = 0;
        int colorPointer = -1;
        Collection<int[]> pieces = board.getPieces(ofColor);;
        if (ofColor.equals(TurnColor.white)) {
            colorPointer = WHITE;
        } else if (ofColor.equals(black)) {
            colorPointer = BLACK;
        }
        for (int[] coords : pieces){
            int x = coords[0];
            int y = coords[1];
            long score = 0;
            //FIXME rewrite pieces as pointers!!!
            switch (board.pieces[coords[0]][coords[1]]) {
                case Board.WHITE_PAWN:
                case Board.BLACK_PAWN:
                    score = pawnPositionMap[colorPointer][y][x];
                    positionalBias += score;
                    break;
                case Board.WHITE_BISHOP:
                case Board.BLACK_BISHOP:
                    score = bishopsPositionMap[colorPointer][y][x];
                    positionalBias += score;
                    break;
                case Board.WHITE_KNIGHT:
                case Board.BLACK_KNIGHT:
                    score = knightsPositionMap[colorPointer][y][x];
                    positionalBias += score;
                    break;
                case Board.WHITE_QUEEN:
                case Board.BLACK_QUEEN:
                    score = queenPositionMap[colorPointer][y][x];
                    positionalBias += score;
                    break;
                case Board.WHITE_ROOK:
                case Board.BLACK_ROOK:
                    score = rooksPositionMap[colorPointer][y][x];
                    positionalBias += score;
                    break;
                case Board.WHITE_KING:
                case Board.BLACK_KING:
                    score = kingPositionMap[colorPointer][y][x];
                    positionalBias += score;
                    break;
            }
        };
        return positionalBias;
    }

    private Integer countPiecesScore(Board board, TurnColor ofColor) {
        return board.getPieces(ofColor).stream()
                .mapToInt(coords -> PIECES_SCORE[board.pieces[coords[0]][coords[1]]])
                .sum();
    }
}
