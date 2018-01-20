package chessControllers;

import chessGame.*;

import java.util.*;

import static chessControllers.Constants.DEBUG;
import static chessControllers.TurnColor.black;
import static chessControllers.Util.logV;

public class MiniMaxStrategy implements PlayingStrategy {
    public static int MAX_DEPTH = 3;
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

    private int negaMax(Game game, int depth, float alpha, float beta, TurnColor turnColor) {
        int deepHash = 31 * turnColor.hashCode() + Arrays.deepHashCode(game.board.pieces);
        if (depth == 0) {
            return evaluateBoard(game, turnColor, deepHash);
        }

        if (negamaxCache.containsKey(deepHash)) {
            if (negamaxCache.get(deepHash).containsKey(depth)) {
                lastChosenMove = movesCache.get(deepHash).get(depth);
                return negamaxCache.get(deepHash).get(depth);
            }
        }


        int max = (Integer.MIN_VALUE/2);
        List<Integer> moves = new ArrayList<>();
//        game.board.populatePossibleMoves(game.board.gameTurn, moves);

        game.board.populatePossibleMoves(turnColor, moves);

        // dynamic depth reassignment - tomorrow
//        if (depth == maxDepth) {
//            if (moves.size() <= 10) {
//                depth = maxDepth = 7;
//            } else if (moves.size() < 20) {
//                depth = maxDepth = 6;
//            } else if (moves.size() < 30) {
//                depth = maxDepth = 5;
//            } else {
//                depth = maxDepth = 5;
//            }
//        }

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
        packedMoves.sort((c1, c2) -> cmp(game, c1, c2, turnColor));
        int maxBeamWidth = 100;
        if (packedMoves.size() > maxBeamWidth) {
            packedMoves = packedMoves.subList(0, maxBeamWidth);
        }

        int[] maxMove = null;
        if (packedMoves.size() == 0) {
            // we are either in mate or draw situation
            int[] playerKing = game.board.getKing(turnColor);
            if (game.board.isKingInCheck(playerKing)) {
                // checkmate
                int mateValue = game.board.getMultiplier(turnColor) * (Integer.MAX_VALUE / 2 - 10);
                return mateValue;
            } else {
                // stalemate
                int staleMateValue = game.board.getMultiplier(turnColor) * Integer.MAX_VALUE / 3;
                return staleMateValue;
            }

        }
        for (int[] move : packedMoves)  {
            game.preexecuteMove(move);
            int score = -1 * negaMax(game,depth - 1, -beta, -alpha, turnColor.opposite());
            if (MAX_DEPTH == depth && DEBUG) {
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

    private int cmp(Game game, int[] c1, int[] c2, TurnColor turnColor) {
        logV("Comparing: " + Arrays.toString(c1) + " against " + Arrays.toString(c2));
        game.preexecuteMove(c1);
        int h1 = 31* turnColor.hashCode() + Arrays.deepHashCode(game.board.pieces);
        int estimate1 = evaluateBoard(game, turnColor, h1);
        game.undoMove();

        game.preexecuteMove(c2);
        int h2 = 31* turnColor.hashCode() + Arrays.deepHashCode(game.board.pieces);
        int estimate2 = evaluateBoard(game, turnColor, h2);
        game.undoMove();

        logV("Estimated move " + Arrays.toString(c1) + " as " + estimate1);
        logV("Estimated move " + Arrays.toString(c2) + " as " + estimate2);

        return -1 * (estimate1 - estimate2);
    }

    private int evaluateBoard(Game game, TurnColor me, int hash) {
        if (gameHashes.containsKey(hash)) {
            return gameHashes.get(hash);
        } else {
            int o_mate = checkKingCheckMateScore(game, me.opposite());

            int m_mate = checkKingCheckMateScore(game, me);

            int m_pieces = countPiecesScore(game, me);
            int o_pieces = countPiecesScore(game, me.opposite());

            int m_pos = getPositionalBias(game, me);
            int o_pos = getPositionalBias(game, me.opposite());

//            int m_coveredScore = getCoveredPieces(game, me);
//            int o_coveredScore = getCoveredPieces(game, me.opposite());

            if (DEBUG) {
//            System.out.println("Pieces score for : " + game.board.gameTurn + (m_pieces - o_pieces));
//            System.out.println("Positional score for : " + game.board.gameTurn + (m_pos));

//            System.out.println("IsOponnentCheckMated Score : " + o_mate);
//            System.out.println("IsOpponentKingInCheck Score : " + o_check);
//            System.out.println("MyPieces score : " + m_pieces);
//            System.out.println("Opponenet Piece Score : " + o_pieces);
//            System.out.println("MyPositional Score : " + m_pos);


            }

            int finalScore = (int) (1*(o_mate - m_mate) +
                    +(m_pieces - o_pieces) + 1f * (m_pos + o_pos)) /*0*(m_coveredScore - o_coveredScore)*/;
            gameHashes.put(hash, finalScore);
            return finalScore;
        }

    }

    private int getCoveredPieces(Game game, TurnColor me) {
        List<int[]> pieces = game.board.getPieces(me);
        int coverScore = 0;
        for (int i = 0; i < pieces.size(); i++) {
            for (int j = 0; j < pieces.size(); j++) {
                if (j!=i) {
                    if (game.board.canMove(pieces.get(j)[0], pieces.get(j)[1], pieces.get(i)[0], pieces.get(i)[1])) {
                        coverScore+=PIECES_SCORE[game.board.pieces[pieces.get(i)[0]][pieces.get(i)[1]]];
                    }
                }
            }
        }
        return coverScore;
    }

    private int kingIsInCheck(Game game, TurnColor gameTurn) {
        int[] king = game.board.getKing(gameTurn);
        return (game.board.isKingInCheck(king)) ? 6 : 0;
    }

    private int checkKingCheckMateScore(Game game, TurnColor gameTurn) {
        int m_check = kingIsInCheck(game, gameTurn);
        if (m_check > 0) {
            int[] king = game.board.getKing(gameTurn);
            int kx = king[0];
            int ky = king[1];
            short possibleMoves = 0;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) {
                        continue;
                    }
                    if (game.board.canMove(kx, ky, kx + i, ky + j)) {
                        possibleMoves++;
                    }
                }
            }

            switch (possibleMoves) {
                case 0:
                    return 100_000;
                case 1:
                    return 15;
                case 2:
                    return 5;
                case 3:
                    return 1;
            }
            return m_check;
        }
        return 0;
    }


    @Override
    public int[] playBlack(Game game) {
        long start = System.currentTimeMillis();
        int[] chosenMove = findMoveBlack(game);
//        negaMax(game, MAX_DEPTH, Integer.MIN_VALUE/2, Integer.MAX_VALUE/2, black);
//        int[] chosenMove = lastChosenMove;
        game.executeMove(chosenMove);
        lastChosenMove = null;
        long end = System.currentTimeMillis();
        System.out.println("Thinking took " + (end-start) + "milliseconds");

        return chosenMove;
    }

    private int[] findMoveBlack(Game game) {
        negaMax(game, MAX_DEPTH, Integer.MIN_VALUE/2, Integer.MAX_VALUE/2, black);
        return lastChosenMove;
    }

    @Override
    public int[] playWhite(Game game) {
        int[] chosenMove = findMoveWhite(game);
//        negaMax(game, MAX_DEPTH, Integer.MIN_VALUE/2, Integer.MAX_VALUE/2, TurnColor.white);
//        int[] chosenMove = lastChosenMove;
        game.executeMove(chosenMove);
        lastChosenMove = null;
        return chosenMove;
    }

    private int[] findMoveWhite(Game game) {
        negaMax(game, MAX_DEPTH, Integer.MIN_VALUE/2, Integer.MAX_VALUE/2, TurnColor.white);
        return lastChosenMove;
    }

    private int getPositionalBias(Game game, TurnColor ofColor) {
        int positionalBias = 0;
        int colorPointer = -1;
        Collection<int[]> pieces = game.board.getPieces(ofColor);;
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
            switch (game.board.pieces[coords[0]][coords[1]]) {
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

    private Integer countPiecesScore(Game game, TurnColor ofColor) {
        return game.board.getPieces(ofColor).stream()
                .mapToInt(coords -> PIECES_SCORE[game.board.pieces[coords[0]][coords[1]]])
                .sum();
    }
}
