package org.dmkr.chess.common.io;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.io.File;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

@UtilityClass
public class FileUtils {

    public static File getOrCreateDir(@NonNull String ... path) {
        checkArgument(path.length != 0, "Empty path");

        final StringBuilder currendPath = new StringBuilder();
        File fileDir = null;
        for (String dir : path) {
            currendPath.append(dir).append('/');
            fileDir = new File(currendPath.toString());
            if (!fileDir.exists()) {
                fileDir.mkdir();
            }
            checkState(fileDir.isDirectory() && fileDir.exists(), "Invalid %s dir %s", dir, fileDir.getAbsolutePath());
        }

        return fileDir;
    }

}
