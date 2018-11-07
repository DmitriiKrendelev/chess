package org.dmkr.chess.ui;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.dmkr.chess.ui.guice.UIListenersModule;
import org.dmkr.chess.ui.listeners.UIListener;

import javax.swing.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class UIBoardJMenuBar extends JMenuBar {

    @Inject
    private UIBoardJMenuBar(Injector injector) {
        final JMenu file = new JMenu("Game Menu");
        add(file);

        UIListenersModule.UI_LISTENERS.stream()
            .filter(UIListener.class::isAssignableFrom)
            .map(injector::getInstance)
            .map(UIListener.class::cast)
            .filter(listener -> isNotBlank(listener.getDisplayedName()))
            .map(UIBoardJMenuBar::createJMenuItem)
            .forEach(file::add);
    }

    private static JMenuItem createJMenuItem(UIListener listener) {
        final JMenuItem menuItem = new JMenuItem(listener.getDisplayedName());

        menuItem.setAccelerator(listener.getRunKeys());
        menuItem.addActionListener(e -> listener.run());

        return menuItem;
    }
}
