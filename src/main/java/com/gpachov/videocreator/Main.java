package com.gpachov.videocreator;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {
    private static Path scriptPath = Paths.get("/home/aneline/Desktop/experiment/create-video.sh");

    public static void main(String[] args) throws IOException {
        
    }

    private static void doIt() {
        String text = "The Witcher short stories were first published in Fantastyka, a Polish science fiction and fantasy magazine, beginning in the mid-1980s. The first short story, \"Wiedźmin\" (\"The Witcher\") (1986), was written for a contest held by the magazine and won third place. The first four stories dealing with the witcher Geralt were originally featured in a 1990 short-story collection entitled Wiedźmin—now out of print—with \"Droga, z której się nie wraca\" (\"The Road With No Return\"), which is set in the world before the Witcher stories and features Geralt's mother to be.";
        String[] sentences = text.split("\\.|!|\\?");
        MaxentTagger tagger = new MaxentTagger("/home/aneline/IdeaProjects/video-creator/lib/english-bidirectional-distsim.tagger");
        for (String sentence : sentences) {
            String result = tagger.tagString(sentence);
            Arrays.stream(result.split("\\W")).forEach(word -> {
                String[] words = word.split("_");
                if (words.length > 0) {
                    String w = words[0];
                    String t = words[1];
                    System.out.println(w);
                    System.out.println(t);
                }
            });
//            System.out.println(result);
        }
    }
}
