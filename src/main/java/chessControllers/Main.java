package chessControllers;

import com.gpachov.videocreator.ChessEngine;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        runEngine();
    }

    private static void runEngine() throws FileNotFoundException {
        ChessEngine engine = new ChessEngine();
        engine.run();
    }
}

