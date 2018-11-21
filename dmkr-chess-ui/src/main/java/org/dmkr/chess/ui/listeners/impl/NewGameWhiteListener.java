package org.dmkr.chess.ui.listeners.impl;

import com.google.inject.Inject;
import lombok.SneakyThrows;
import org.dmkr.chess.api.model.Color;
import org.dmkr.chess.ui.Player;
import org.dmkr.chess.ui.Runner;
import org.dmkr.chess.ui.UIBoardJFrame;
import org.dmkr.chess.ui.listeners.AbstractPressAndTypedListener;


import static java.awt.event.KeyEvent.*;
import static javax.swing.KeyStroke.getKeyStroke;
import static org.dmkr.chess.engine.board.BoardFactory.newInitialPositionBoard;

public class NewGameWhiteListener extends AbstractPressAndTypedListener {
    private final Player player;
    private final UIBoardJFrame uiBoardJFrame;

    @Inject
    private NewGameWhiteListener(Player player, UIBoardJFrame uiBoardJFrame) {
        super(CTRL, VK_W, "New... White", getKeyStroke("ctrl W"));
        this.player = player;
        this.uiBoardJFrame = uiBoardJFrame;
    }

    @SneakyThrows
    @Override
    public void run() {
        uiBoardJFrame.close();

        Runner.run(player.withColor(Color.WHITE), newInitialPositionBoard());
    }
}
