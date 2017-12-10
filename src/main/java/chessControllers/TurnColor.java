package chessControllers;

import chessGame.StandardBoard;

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
                return StandardBoard.WHITES;
            case black:
                return StandardBoard.BLACKS;
        }
        return null;
    }

    public int getKingPtr() {
        switch (this) {
            case white:
                return StandardBoard.WHITE_KING;
            case black:
                return StandardBoard.BLACK_KING;
        }
        return -1;
    }
}