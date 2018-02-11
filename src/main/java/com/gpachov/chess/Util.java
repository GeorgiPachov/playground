package com.gpachov.chess;

import com.gpachov.chess.board.Constants;
import com.gpachov.chess.board.TurnColor;

import java.util.ArrayList;
import java.util.List;

import static com.gpachov.chess.board.TurnColor.black;

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

    public static List<int[]> pack(List<Integer> moves) {
        List<int[]> packedMoves = new ArrayList<>();
        int[] c = new int[5];
        int counter = 0;
        for (int number : moves) {
            counter++;
            if (counter == 5) {
                boolean isChessPiece = black.isPiece(number) || TurnColor.white.isPiece(number);
                if (isChessPiece) {
                    c[counter - 1] = number;
                } else {
                    c[counter - 1] = 0;
                }
                packedMoves.add(c);
                c = new int[5];
                counter = 0;
            } else {
                c[counter - 1] = number;
            }
        }
        return packedMoves;
    }
}
