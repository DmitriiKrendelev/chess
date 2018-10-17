package org.dmkr.chess.ui.saveandload;

import com.google.common.collect.ImmutableMap;
import lombok.*;
import org.dmkr.chess.ui.Player;
import org.dmkr.chess.ui.helpers.UIBoardImagesHelper;

import javax.inject.Inject;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static java.lang.System.getProperty;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.dmkr.chess.api.model.Constants.ENGINE_NAME_AND_VERSION;
import static org.dmkr.chess.common.io.FileUtils.getOrCreateDir;

@RequiredArgsConstructor(onConstructor = @_(@Inject), access = AccessLevel.PRIVATE)
public class
SaveAndLoadPositionManager {
    private static final Map<File, Optional<SavedPosition>> gamesCache = new ConcurrentHashMap<>();

    static final String SAVES_DIR = "saves";
    static final String SAVES_FILE_EXT = "dmkr";
    static final String SAVE_FILE_NAME_TEMPLATE = "%PLAYER%_vs_%OPONENT%_%DATETIME%";

    private final UIBoardImagesHelper imagesHelper;

    @Getter(AccessLevel.PRIVATE)
    private final Player player;
    private final AtomicReference<File> playerSaveFolder = new AtomicReference<>();


    private final Map<String, Supplier<String>> SAVE_FILE_MACROS_REPLACE = ImmutableMap.of(
            "PLAYER", () -> getPlayer().getName(),
            "OPONENT", () -> ENGINE_NAME_AND_VERSION,
            "DATETIME", () -> LocalDateTime.now().format(ofPattern("uuuuMMMdd_HHmmss", Locale.ENGLISH))
    );

    public File createSaveFile() {
        return new File(getOrCreateSavesDir().getAbsolutePath() + "/" + getSaveFileName());
    }

    private String getSaveFileName() {
        String saveFileName = SAVE_FILE_NAME_TEMPLATE;
        for (Map.Entry<String, Supplier<String>> macroReplacer : SAVE_FILE_MACROS_REPLACE.entrySet()) {
            saveFileName = saveFileName.replace("%" + macroReplacer.getKey() + "%", macroReplacer.getValue().get());
        }
        final String resultSaveFileName = saveFileName
                .replaceAll("\\s+", "")
                .replace(".", "")
                .concat(".dmkr");

        System.out.println("Create file: " + resultSaveFileName);
        return resultSaveFileName;
    }

    public Optional<SavedPosition> getPosition(File file) {
        return gamesCache.computeIfAbsent(file, this::loadPosition);
    }

    @SneakyThrows
    private Optional<SavedPosition> loadPosition(File file) {
        if (file == null || !file.getName().endsWith("." + SAVES_FILE_EXT)) {
            return Optional.empty();
        }

        try (
                final InputStream is = new FileInputStream(file);
                final ObjectInputStream ois = new ObjectInputStream(is);
        ) {
            final Optional<SavedPosition> savedPosition = Optional.of((SavedPosition) ois.readObject());
            System.out.println("Load position from: " + file.getName() + "\n" + savedPosition.get());
            return savedPosition;
        }
    }

    public File getOrCreateSavesDir() {
        return playerSaveFolder.updateAndGet(
                file -> file != null ? file : getOrCreateDir(
                        getProperty("user.dir"),
                        SAVES_DIR,
                        player.getName().replaceAll("\\s+", "")));
    }
}
