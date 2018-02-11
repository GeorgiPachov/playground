package com.gpachov.chess;

import java.io.FileNotFoundException;

public class NonQuittingChessEngine extends ChessEngine {
    public NonQuittingChessEngine() throws FileNotFoundException {

    }

    @Override
    protected void quit() {
        // do nothing
    }
}
