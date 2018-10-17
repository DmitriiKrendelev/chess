package org.dmkr.chess.ui.listeners;

import com.google.inject.Inject;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.ui.UIBoardJComponent;
import org.dmkr.chess.ui.saveandload.LoadFileChooser;
import org.dmkr.chess.ui.saveandload.SaveAndLoadPositionManager;
import org.dmkr.chess.ui.saveandload.SavedPosition;

import javax.swing.*;
import java.io.*;

public class LoadPositionListener extends AbstractPressAndTypedListener{
    private static final int L = 76;
    private final SaveAndLoadPositionManager saveAndLoadPositionManager;
    private final LoadFileChooser fileChooser;
    private final UIBoardJComponent uiBoardJComponent;

    @Inject
    private LoadPositionListener(
            SaveAndLoadPositionManager saveAndLoadPositionManager,
            LoadFileChooser fileChooser,
            UIBoardJComponent uiBoardJComponent) {
        super(CTRL, L);
        this.saveAndLoadPositionManager = saveAndLoadPositionManager;
        this.fileChooser = fileChooser;
        this.uiBoardJComponent = uiBoardJComponent;
    }

    @Override
    public void run() throws Exception {
        final int fileChooserResult = fileChooser.showOpenDialog(uiBoardJComponent);

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
    }

}
