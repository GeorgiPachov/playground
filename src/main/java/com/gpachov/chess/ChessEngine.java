package com.gpachov.chess;

import com.google.common.annotations.VisibleForTesting;
import com.gpachov.chess.ai.MiniMaxStrategy;
import com.gpachov.chess.ai.PlayingStrategy;
import com.gpachov.chess.board.Board;
import com.gpachov.chess.board.Constants;
import com.gpachov.chess.board.TurnColor;
import com.fluxchess.jcpi.AbstractEngine;
import com.fluxchess.jcpi.commands.*;
import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.models.IllegalNotationException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ChessEngine extends AbstractEngine {
    public @VisibleForTesting Board board;
    int commandNumber = 0;
    private FileOutputStream uciCommandLog = new FileOutputStream("/tmp/uci.log");
    private int[] lastMove;
    public @VisibleForTesting PlayingStrategy playingStrategy = new MiniMaxStrategy();
    private TurnColor uciTurn;

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
        newGame();
    }

    private void newGame() {
        this.board = new Board();
    }

    @Override
    public void receive(EngineAnalyzeCommand command) {
        log(command);
        analyze(command);
    }

    private void analyze(EngineAnalyzeCommand command) {
        this.uciTurn = calculateTurn(command);

        if (Constants.RECREATE_BOARD_ON_MOVE) {
            newGame();
            command.moves.forEach(this::applyMove);
        }

        else if (command.moves.size() > 0) {
            GenericMove genericMove = command.moves.get(command.moves.size() - 1);
            applyMove(genericMove);
        }
    }

    private void applyMove(GenericMove genericMove) {
        char fromLetter = genericMove.from.file.toChar();
        char fromRank = genericMove.from.rank.toChar();

        //'a' = 97, 'b' = 98
        // conversion should make a = 0, b = 1, c = 2, etc...

        char toLetter = genericMove.to.file.toChar();
        char toRank = genericMove.to.rank.toChar();

        int fromXIndex = fromLetter - 97;
        int fromYIndex = fromRank - 49; //f:[1-8] => [0-7]; f(x) = x-1;

        int toXIndex = toLetter - 97;
        int toYIndex = toRank - 49;


        if (genericMove.promotion != null && genericMove.promotion.isLegalPromotion()) {
            this.lastMove = new int[]{fromXIndex, fromYIndex, toXIndex, toYIndex, toMyNotation(genericMove.promotion.toCharAlgebraic(), board.metadata.gameTurn)};
        } else {
            this.lastMove = new int[]{fromXIndex, fromYIndex, toXIndex, toYIndex, 0};
        }
        Util.logV("<<<" + Arrays.toString(lastMove));
        board.executeMove(lastMove);
    }

    private TurnColor calculateTurn(EngineAnalyzeCommand command) {
        if (command.moves.size() % 2 == 1) {
            return TurnColor.black;
        } else if (command.moves.size() % 2 == 0) {
            return TurnColor.white;
        }
        return null;
    }

    private int toMyNotation(char c, TurnColor gameTurn) {
        c = (c + "").toLowerCase().charAt(0);
        switch (gameTurn) {
            case black:
                switch (c) {
                    case 'q':
                        return Board.BLACK_QUEEN;
                    case 'n':
                        return Board.BLACK_KNIGHT;
                    case 'r':
                        return Board.BLACK_ROOK;
                    case 'b':
                        return Board.BLACK_BISHOP;
                }
                break;
            case white:
                switch (c) {
                    case 'q':
                        return Board.WHITE_QUEEN;
                    case 'n':
                        return Board.WHITE_KNIGHT;
                    case 'r':
                        return Board.WHITE_ROOK;
                    case 'b':
                        return Board.WHITE_BISHOP;
                }
        }
        return 0;
    }

    @Override
    public void receive(EngineStartCalculatingCommand command) {
        log(command);
        move();
    }

    private void move() {
        int[] move = null;
        switch (uciTurn) {
            case black:
                move = playingStrategy.playBlack(board);
                break;
            case white:
                move = playingStrategy.playWhite(board);
                break;
        }
        GenericMove bestMove = toGenericMove(move);
        ProtocolBestMoveCommand bestMoveCommand = new ProtocolBestMoveCommand(bestMove, null);
        System.out.println(board.toString());

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

        boolean isPromotion = blackMove.length == 5;

        String moveString = String.valueOf((char) fromXLetter) + String.valueOf((char) fromYRank)
                + String.valueOf((char) toXLetter) + String.valueOf((char) toYRank) + (isPromotion ? toUciNotation(blackMove[4]) : "");
        try {
            return new GenericMove(moveString);
        } catch (IllegalNotationException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate good notation" + moveString);
        }

    }

    private char toUciNotation(int i) {
        switch (i) {
            case Board.WHITE_QUEEN:
            case Board.BLACK_QUEEN:
                return 'q';
            case Board.BLACK_ROOK:
            case Board.WHITE_ROOK:
                return 'r';
            case Board.WHITE_BISHOP:
            case Board.BLACK_BISHOP:
                return 'b';
            case Board.BLACK_KNIGHT:
            case Board.WHITE_KNIGHT:
                return 'n';
        }
        return 0;
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
