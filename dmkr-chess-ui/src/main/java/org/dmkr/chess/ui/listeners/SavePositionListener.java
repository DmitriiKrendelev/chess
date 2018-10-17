package org.dmkr.chess.ui.listeners;

import com.google.inject.Inject;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.ui.Player;
import org.dmkr.chess.ui.saveandload.SaveAndLoadPositionManager;
import org.dmkr.chess.ui.saveandload.SavedPosition;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SavePositionListener extends AbstractPressAndTypedListener{
    private static final int S = 83;
    private final Player player;
    private final BoardEngine board;
    private final SaveAndLoadPositionManager saveAndLoadGameManager;

    @Inject
    private SavePositionListener(Player player, BoardEngine board, SaveAndLoadPositionManager saveAndLoadGameManager) {
        super(CTRL, S);
        this.player = player;
        this.board = board;
        this.saveAndLoadGameManager = saveAndLoadGameManager;
    }

    @Override
    public void run() throws IOException {
        try (
                final OutputStream os = new FileOutputStream(saveAndLoadGameManager.createSaveFile());
                final ObjectOutputStream oos = new ObjectOutputStream(os);
        ) {
            oos.writeObject(new SavedPosition(player.getColor(), board));
        }
    }
}
