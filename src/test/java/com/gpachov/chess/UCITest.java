package com.gpachov.chess;

import com.gpachov.chess.board.Constants;
import org.junit.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UCITest {

    private static InputStream in;

    @BeforeClass
    public static void setup() {
        in = System.in;
    }
    @Test
    public void gameOne() throws FileNotFoundException {
//      testCase(() -> {});
    }

    @Test
    public void handleShortCastling() throws FileNotFoundException {
        Runnable doNothing = () -> {};
        testCase("/home/aneline/IdeaProjects/video-creator/src/test/resources/short_castling.txt", doNothing);
    }

    private void testCase(String path, Runnable callback) throws FileNotFoundException {
        UCITestStream testStream = new UCITestStream(new FileInputStream(new File(path)), callback);
        System.setIn(testStream);
        ChessEngine engine = new NonQuittingChessEngine();
        engine.run();
    }

    @Test
    public void handleLongCastling() {

    }

    @Test
    public void promotionWhite() {

    }

    @Test
    public void promotionBlack() {

    }

    @Test
    public void testException() throws FileNotFoundException {
        Constants.RECREATE_BOARD_ON_MOVE = true;
        testCase("/home/aneline/IdeaProjects/video-creator/src/test/resources/exception.txt", () -> {});
    }


    @Test
    public void testException2() throws FileNotFoundException {
        Constants.RECREATE_BOARD_ON_MOVE = true;
        testCase("/home/aneline/IdeaProjects/video-creator/src/test/resources/exception2.txt", () -> {});
    }

    @AfterClass
    public static void restore() {
        System.setIn(in);
    }


}
