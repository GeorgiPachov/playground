import chessControllers.UCITestStream;
import com.gpachov.videocreator.ChessEngine;
import org.apache.tools.ant.filters.StringInputStream;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class UCITest {
    @Test
    public void gameOne() throws FileNotFoundException {
        System.setIn(new UCITestStream(new FileInputStream(new File("/home/aneline/IdeaProjects/video-creator/src/test/resources/promotion.txt"))));
        ChessEngine engine = new ChessEngine();
        engine.run();

    }

    @Test
    public void promotionWhite() {

    }

    @Test
    public void promotionBlack() {

    }
}
