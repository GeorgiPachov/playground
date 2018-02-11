package com.gpachov.chess.ai;

import com.gpachov.chess.board.Board;

public interface PlayingStrategy {
    int[] playBlack(Board board);
    int[] playWhite(Board board);
}
