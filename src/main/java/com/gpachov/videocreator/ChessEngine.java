package com.gpachov.videocreator;

import chessControllers.Game;
import chessControllers.MiniMaxStrategy;
import chessControllers.PlayingStrategy;
import chessGame.Move;
import com.fluxchess.jcpi.AbstractEngine;
import com.fluxchess.jcpi.commands.*;
import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.models.IllegalNotationException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChessEngine extends AbstractEngine {
    private Game game;
    int commandNumber = 0;
    private FileOutputStream uciCommandLog = new FileOutputStream("/tmp/uci.log");
    private int[] lastMove;
    private PlayingStrategy playingStrategy = new MiniMaxStrategy();

    public ChessEngine() throws FileNotFoundException {
    }

    @Override
    protected void quit() {
        System.exit(0);
    }

    @Override
    public void receive(EngineInitializeRequestCommand command) {
        log(command);
        respond("id Georgi");
        respond("uciok");
    }


    private void log(IEngineCommand command) {
        String message = commandNumber + "=>" + command.getClass();
        try {
            uciCommandLog.write(message.getBytes());
            uciCommandLog.write("\n".getBytes());
            uciCommandLog.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        commandNumber++;

    }

    @Override
    public void receive(EngineSetOptionCommand command) {
        log(command);
    }

    @Override
    public void receive(EngineDebugCommand command) {
        log(command);
    }

    @Override
    public void receive(EngineReadyRequestCommand command) {
        log(command);
        respond("readyok");
    }

    @Override
    public void receive(EngineNewGameCommand command) {
        log(command);
        this.game = Game.startNewGame();
    }

    @Override
    public void receive(EngineAnalyzeCommand command) {
        log(command);
        GenericMove genericMove = command.moves.get(command.moves.size() - 1);
        char fromLetter = genericMove.from.file.toChar();
        char fromRank = genericMove.from.rank.toChar();

        //'a' = 97, 'b' = 98
        // conversion should make a = 0, b = 1, c =2, etc...

        char toLetter = genericMove.to.file.toChar();
        char toRank = genericMove.to.rank.toChar();

        int fromXIndex = fromLetter - 97;
        int fromYIndex = fromRank - 49; //f:[1-8] => [0-7]; f(x) = x-1;

        int toXIndex = toLetter - 97;
        int toYIndex = toRank - 49;

        this.lastMove = new int[]{fromXIndex, fromYIndex, toXIndex, toYIndex};
        System.out.println("<<<" + lastMove.toString());

        game.executeMove(lastMove);

    }

    @Override
    public void receive(EngineStartCalculatingCommand command) {
        log(command);
        int[] blackMove = playingStrategy.playBlack(game);
        GenericMove bestMove = toGenericMove(blackMove);
        ProtocolBestMoveCommand bestMoveCommand = new ProtocolBestMoveCommand(bestMove, bestMove);
        super.getProtocol().send(bestMoveCommand);
    }

    private GenericMove toGenericMove(int[] blackMove) {
//X = Letter
        // Y = Rank
//        int fromXIndex = fromLetter - 97;
//        int fromYIndex = fromRank - 49; //f:[1-8] => [0-7]; f(x) = x-1;
//
//        int toXIndex = toLetter - 97;
//        int toYIndex = toRank - 49;
        int fromX = blackMove[0];
        int fromY = blackMove[1];

        int toX = blackMove[2];
        int toY = blackMove[3];

        int fromXLetter = 97 + fromX;
        int fromYRank = 49 + fromY;

        int toXLetter = 97 + toX;
        int toYRank = 49 + toY;

        String moveString = String.valueOf((char) fromXLetter) + String.valueOf((char) fromYRank)
                + String.valueOf((char) toXLetter) + String.valueOf((char) toYRank);
        try {
            return new GenericMove(moveString);
        } catch (IllegalNotationException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate good notation" + moveString);
        }

    }

    @Override
    public void receive(EngineStopCalculatingCommand command) {
        log(command);
    }

    @Override
    public void receive(EnginePonderHitCommand command) {
        log(command);
    }

    private void respond(String response) {
        System.out.println(response);
    }



}
