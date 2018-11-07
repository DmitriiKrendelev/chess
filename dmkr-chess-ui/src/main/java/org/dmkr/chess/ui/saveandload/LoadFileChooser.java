package org.dmkr.chess.ui.saveandload;

import com.google.inject.Inject;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;

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

        registerDeleteAction();
    }

    private void registerDeleteAction() {
        final AbstractAction deleteFileAction = new AbstractAction() {
            @SneakyThrows
            public void actionPerformed(ActionEvent actionEvent) {
                final File selectedFile = getSelectedFile();
                if (selectedFile != null) {
                    int selectedAnswer = JOptionPane.showConfirmDialog(null, "Are you sure want to permanently delete this file?", "Confirm", JOptionPane.YES_NO_OPTION);

                    if (selectedAnswer == JOptionPane.YES_OPTION) {
                        Files.delete(selectedFile.toPath());
                        rescanCurrentDirectory();
                    }
                }
            }
        };

        getActionMap().put("deleteAction", deleteFileAction);
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DELETE"), "deleteAction");
    }
}
