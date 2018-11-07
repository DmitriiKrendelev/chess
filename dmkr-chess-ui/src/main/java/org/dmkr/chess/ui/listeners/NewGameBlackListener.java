package org.dmkr.chess.ui.listeners;

import com.google.inject.Inject;
import lombok.SneakyThrows;
import org.dmkr.chess.api.model.Color;
import org.dmkr.chess.ui.Player;
import org.dmkr.chess.ui.Runner;
import org.dmkr.chess.ui.UIBoard;


import static java.awt.event.KeyEvent.VK_B;
import static javax.swing.KeyStroke.getKeyStroke;
import static org.dmkr.chess.engine.board.BoardFactory.newInitialPositionBoard;

public class NewGameBlackListener extends AbstractPressAndTypedListener {
    private final Player player;
    private final UIBoard uiBoard;

    @Inject
    private NewGameBlackListener(Player player, UIBoard uiBoard) {
        super(CTRL, VK_B, "New... Black", getKeyStroke("ctrl B"));
        this.player = player;
        this.uiBoard = uiBoard;
    }

    @SneakyThrows
    @Override
    public void run() {
        uiBoard.close();

        Runner.run(player.withColor(Color.BLACK), newInitialPositionBoard());
    }
}
