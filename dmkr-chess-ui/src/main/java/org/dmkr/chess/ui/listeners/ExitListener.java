package org.dmkr.chess.ui.listeners;

import com.google.inject.Inject;


import static java.awt.event.KeyEvent.VK_ESCAPE;
import static javax.swing.KeyStroke.getKeyStroke;

public class ExitListener extends AbstractPressAndTypedListener {

    @Inject
    private ExitListener() {
        super(VK_ESCAPE, VK_ESCAPE, "Exit", getKeyStroke(VK_ESCAPE, 0));
    }

    @Override
    public void run() {
        System.exit(0);
    }
}
