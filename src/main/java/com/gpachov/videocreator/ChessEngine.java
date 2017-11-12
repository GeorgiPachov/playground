package com.gpachov.videocreator;

import com.fluxchess.jcpi.AbstractEngine;
import com.fluxchess.jcpi.commands.*;

public class ChessEngine extends AbstractEngine {
    @Override
    protected void quit() {
        System.exit(0);
    }

    @Override
    public void receive(EngineInitializeRequestCommand command) {
        command.accept(this);
    }

    @Override
    public void receive(EngineSetOptionCommand command) {
        command.accept(this);
    }

    @Override
    public void receive(EngineDebugCommand command) {
        command.accept(this);
    }

    @Override
    public void receive(EngineReadyRequestCommand command) {
        command.accept(this);
    }

    @Override
    public void receive(EngineNewGameCommand command) {
        command.accept(this);
    }

    @Override
    public void receive(EngineAnalyzeCommand command) {
        command.accept(this);
    }

    @Override
    public void receive(EngineStartCalculatingCommand command) {
        command.accept(this);
    }

    @Override
    public void receive(EngineStopCalculatingCommand command) {
        command.accept(this);
    }

    @Override
    public void receive(EnginePonderHitCommand command) {
        command.accept(this);
    }
}
