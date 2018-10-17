package org.dmkr.chess.ui.saveandload;

import com.google.inject.Inject;

import javax.swing.*;

public class LoadFileChooser extends JFileChooser {

    @Inject
    private LoadFileChooser(
            LoadFileFilter fileFilter,
            SaveAndLoadPositionManager saveAndLoadManager,
            LoadPositionPreviewPanel loadPositionPreviewPanel) {

        setApproveButtonText("Load Game");
        setApproveButtonToolTipText("Load game from the choosen file");
        setFileFilter(fileFilter);
        setAcceptAllFileFilterUsed(false);
        addChoosableFileFilter(fileFilter);
        setCurrentDirectory(saveAndLoadManager.getOrCreateSavesDir());
        setAccessory(loadPositionPreviewPanel);
        addPropertyChangeListener(loadPositionPreviewPanel);
    }
}
