package com.gpachov.chess;

import com.gpachov.chess.board.Constants;

public class Util {
    public static void logV(String s) {
        if (Constants.DEBUG_MOVE_SORTING) {
            System.out.println(s);
        }
    }

    public static void logS(String s) {
        if (Constants.DEBUG_SCORE_EVALUATION) {
            System.out.println(s);
        }
    }
}
