package chessControllers;

import chessGame.Board;

import java.util.Arrays;

public enum TurnColor {
    white, black;

    public TurnColor opposite(){
        if(this == white)
            return black;
        else
            return white;
    }

    public int[] getPieces() {
        switch (this) {
            case white:
                return Board.WHITES;
            case black:
                return Board.BLACKS;
        }
        return null;
    }

    public int getKingPtr() {
        switch (this) {
            case white:
                return Board.WHITE_KING;
            case black:
                return Board.BLACK_KING;
        }
        return -1;
    }

    public boolean isPiece(int number) {
        return Arrays.binarySearch(getPieces(), number) > -1;
    }
}