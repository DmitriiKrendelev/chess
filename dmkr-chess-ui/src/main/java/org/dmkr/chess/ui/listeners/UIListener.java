package org.dmkr.chess.ui.listeners;

import javax.swing.*;
import java.util.EventListener;

public interface UIListener  extends EventListener {

    default String getDisplayedName() {
        return null;
    }

    default KeyStroke getRunKeys() {
        return null;
    }

    default void run() {
        throw new UnsupportedOperationException();
    }
}
