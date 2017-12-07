package chessControllers;

import com.gpachov.videocreator.ChessEngine;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        runSelf();

//        runEngine();

//             exportUci();
        }

    private static void runSelf() throws FileNotFoundException {
        PlayingStrategy whiteStrategy = new MiniMaxStrategy();
        PlayingStrategy blackStrategy = new MiniMaxStrategy();
        Game game = Game.startNewGame();
        game.playSelf(whiteStrategy, blackStrategy);
    }

    private static void runEngine() throws FileNotFoundException {
        ChessEngine engine = new ChessEngine();
        engine.run();
    }

    private static void exportUci() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = "";
        FileOutputStream fileOutputStream = new FileOutputStream("/tmp/out.txt");
        while ((line = reader.readLine()) != null) {
//            System.out.println(line);
            fileOutputStream.write(line.getBytes());
            fileOutputStream.flush();
        }
    }
}
