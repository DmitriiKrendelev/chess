package org.dmkr.chess.ui.listeners;

import javax.swing.*;

public interface UIListener {

    String getDisplayedName();

    KeyStroke getRunKeys();

    void run();
}
