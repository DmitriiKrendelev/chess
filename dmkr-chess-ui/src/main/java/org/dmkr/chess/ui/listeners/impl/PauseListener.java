package org.dmkr.chess.ui.listeners.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.dmkr.chess.api.Board;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.ui.listeners.AbstractPressAndTypedListener;


import static java.awt.event.KeyEvent.*;
import static javax.swing.KeyStroke.*;

public class PauseListener extends AbstractPressAndTypedListener {
    private final AsyncEngine<?extends BoardEngine> engine1;
    private final AsyncEngine<?extends BoardEngine> engine2;
    private final Board board;

    @Inject
    private PauseListener(BoardEngine board, @Named("engine1") AsyncEngine<BoardEngine> engine1, @Named("engine2") AsyncEngine<BoardEngine> engine2) {
        super(CTRL, VK_SPACE, "Pause", getKeyStroke(' '));
        this.board = board;
        this.engine1 = engine1;
        this.engine2 = engine2;
    }

    @Override
    public void run() {
        run(engine1);
        run(engine2);
    }

    private void run(AsyncEngine<?extends BoardEngine> engine) {
        if (!engine.isInProgress()) {
            return;
        }

        if (engine.isPaused()) {
            System.out.println("Resume");
            engine.resume();
        } else {
            System.out.println("Pause");
            engine.pause();
        }
    }

}
