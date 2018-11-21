package org.dmkr.chess.ui.listeners.impl;

import com.google.inject.Inject;
import lombok.SneakyThrows;
import org.dmkr.chess.ui.Player;
import org.dmkr.chess.ui.Runner;
import org.dmkr.chess.ui.UIBoardJFrame;
import org.dmkr.chess.ui.listeners.AbstractPressAndTypedListener;
import org.dmkr.chess.ui.saveandload.LoadFileChooser;
import org.dmkr.chess.ui.saveandload.SaveAndLoadPositionManager;
import org.dmkr.chess.ui.saveandload.SavedPosition;

import javax.swing.*;
import java.io.*;

import static java.awt.event.KeyEvent.VK_L;
import static javax.swing.KeyStroke.getKeyStroke;

public class LoadPositionListener extends AbstractPressAndTypedListener {
    private final SaveAndLoadPositionManager saveAndLoadPositionManager;
    private final LoadFileChooser fileChooser;
    private final UIBoardJFrame uiBoardJFrame;
    private final Player player;

    @Inject
    private LoadPositionListener(
            SaveAndLoadPositionManager saveAndLoadPositionManager,
            LoadFileChooser fileChooser,
            UIBoardJFrame uiBoardJFrame,
            Player player) {
        super(CTRL, VK_L, "Load", getKeyStroke("ctrl L"));
        this.saveAndLoadPositionManager = saveAndLoadPositionManager;
        this.fileChooser = fileChooser;
        this.uiBoardJFrame = uiBoardJFrame;
        this.player = player;
    }

    @SneakyThrows
    @Override
    public void run() {
        final int fileChooserResult = fileChooser.showOpenDialog(uiBoardJFrame);

        final File choosenFile;
        switch (fileChooserResult) {
            case JFileChooser.CANCEL_OPTION:
                System.out.println("User cancelled loading of the game");
                return;
            case JFileChooser.ERROR_OPTION:
                System.out.println("Error choosing loading file");
                return;
            case JFileChooser.APPROVE_OPTION:
                choosenFile = fileChooser.getSelectedFile();
                System.out.println("Chosen file: " + (choosenFile == null ? null : choosenFile.getAbsolutePath()));
                break;

            default:
                throw new IllegalStateException("Unknown file choosing result: " + fileChooserResult);
        }


        final SavedPosition savedPosition = saveAndLoadPositionManager.getPosition(choosenFile).get();
        System.out.println(savedPosition);

        uiBoardJFrame.close();

        Runner.run(
                player.withColor(savedPosition.getColor()),
                savedPosition.getBoard()
        );
    }

}
