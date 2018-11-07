package org.dmkr.chess.ui.listeners.impl;

import com.google.inject.Inject;
import lombok.SneakyThrows;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.ui.Player;
import org.dmkr.chess.ui.listeners.AbstractPressAndTypedListener;
import org.dmkr.chess.ui.saveandload.SaveAndLoadPositionManager;
import org.dmkr.chess.ui.saveandload.SavedPosition;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import static java.awt.event.KeyEvent.VK_S;
import static javax.swing.KeyStroke.getKeyStroke;

public class SavePositionListener extends AbstractPressAndTypedListener {
    private final Player player;
    private final BoardEngine board;
    private final SaveAndLoadPositionManager saveAndLoadGameManager;

    @Inject
    private SavePositionListener(Player player, BoardEngine board, SaveAndLoadPositionManager saveAndLoadGameManager) {
        super(CTRL, VK_S, "Save", getKeyStroke("ctrl S"));
        this.player = player;
        this.board = board;
        this.saveAndLoadGameManager = saveAndLoadGameManager;
    }

    @SneakyThrows
    @Override
    public void run() {
        try (
                final OutputStream os = new FileOutputStream(saveAndLoadGameManager.createSaveFile());
                final ObjectOutputStream oos = new ObjectOutputStream(os);
        ) {
            oos.writeObject(new SavedPosition(player.getColor(), board));
        }
    }
}
