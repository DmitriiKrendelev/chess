package org.dmkr.chess.ui.saveandload;

import com.google.inject.Inject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.swing.filechooser.FileFilter;
import java.io.File;

import static org.dmkr.chess.ui.saveandload.SaveAndLoadPositionManager.SAVES_FILE_EXT;



@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @_(@Inject))
public class LoadFileFilter extends FileFilter {
    private static final String FILTER_DESCRIPTION = "*.dmkr";
    private final SaveAndLoadPositionManager saveAndLoadPositionManager;

    @Override
    public boolean accept(File f) {
        if (f == null || f.isDirectory()) {
            return false;
        }

        if (!saveAndLoadPositionManager.getOrCreateSavesDir().equals(f.getParentFile())) {
            return false;
        }

        final String fileName = f.getName();
        final int i = fileName.lastIndexOf('.');
        if (i > 0 && i < fileName.length() - 1) {
            final String ext = fileName.substring(i + 1).toLowerCase();
            return SAVES_FILE_EXT.equals(ext);
        } else {
            return false;
        }
    }

    @Override
    public String getDescription() {
        return FILTER_DESCRIPTION;
    }
}
