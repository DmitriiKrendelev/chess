package org.dmkr.chess.ui.listeners;

import javax.swing.*;
import java.util.EventListener;

import static org.apache.commons.lang3.StringUtils.*;

public interface UIListener  extends EventListener {

    default String getDisplayedName() {
        return null;
    }

    default boolean isJMenuListener() {
        return isNotBlank(getDisplayedName());
    }

    default KeyStroke getRunKeys() {
        return null;
    }

    default void run() {
        throw new UnsupportedOperationException();
    }
}
