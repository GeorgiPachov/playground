package chessControllers;

import com.gpachov.videocreator.ChessEngine;
import org.apache.tools.ant.filters.StringInputStream;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
//            String inputFile = "/home/aneline/IdeaProjects/video-creator/src/main/resources/stdin";
//            System.setIn(new UCITestStream(new FileInputStream(inputFile)));
//            PlayingStrategy whiteStrategy = new GreedyStrategy();
//            PlayingStrategy blackStrategy = new GreedyStrategy();
//            Game game = Game.startNewGame();
//		    game.playSelf(whiteStrategy, blackStrategy);

            ChessEngine engine = new ChessEngine();
            engine.run();

//             exportUci();
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
