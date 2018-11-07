package org.dmkr.chess.ui.listeners.impl;

import com.google.inject.Inject;
import lombok.SneakyThrows;
import org.dmkr.chess.api.model.Color;
import org.dmkr.chess.ui.Player;
import org.dmkr.chess.ui.Runner;
import org.dmkr.chess.ui.UIBoard;
import org.dmkr.chess.ui.listeners.AbstractPressAndTypedListener;


import static java.awt.event.KeyEvent.*;
import static javax.swing.KeyStroke.getKeyStroke;
import static org.dmkr.chess.engine.board.BoardFactory.newInitialPositionBoard;

public class NewGameWhiteListener extends AbstractPressAndTypedListener {
    private final Player player;
    private final UIBoard uiBoard;

    @Inject
    private NewGameWhiteListener(Player player, UIBoard uiBoard) {
        super(CTRL, VK_W, "New... White", getKeyStroke("ctrl W"));
        this.player = player;
        this.uiBoard = uiBoard;
    }

    @SneakyThrows
    @Override
    public void run() {
        uiBoard.close();

        Runner.run(player.withColor(Color.WHITE), newInitialPositionBoard());
    }
}
