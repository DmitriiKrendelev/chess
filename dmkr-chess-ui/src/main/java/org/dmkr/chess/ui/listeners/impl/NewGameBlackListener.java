package org.dmkr.chess.ui.listeners.impl;

import com.google.inject.Inject;
import lombok.SneakyThrows;
import org.dmkr.chess.api.model.Color;
import org.dmkr.chess.ui.Player;
import org.dmkr.chess.ui.Runner;
import org.dmkr.chess.ui.UIBoardJFrame;
import org.dmkr.chess.ui.listeners.AbstractPressAndTypedListener;


import static java.awt.event.KeyEvent.VK_B;
import static javax.swing.KeyStroke.getKeyStroke;
import static org.dmkr.chess.engine.board.BoardFactory.newInitialPositionBoard;

public class NewGameBlackListener extends AbstractPressAndTypedListener {
    private final Player player;
    private final UIBoardJFrame uiBoardJFrame;

    @Inject
    private NewGameBlackListener(Player player, UIBoardJFrame uiBoardJFrame) {
        super(CTRL, VK_B, "New... Black", getKeyStroke("ctrl B"));
        this.player = player;
        this.uiBoardJFrame = uiBoardJFrame;
    }

    @SneakyThrows
    @Override
    public void run() {
        uiBoardJFrame.close();

        Runner.run(player.withColor(Color.BLACK), newInitialPositionBoard());
    }
}
