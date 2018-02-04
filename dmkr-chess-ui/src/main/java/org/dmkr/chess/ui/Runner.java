package org.dmkr.chess.ui;

import java.awt.EventQueue;

import org.dmkr.chess.ui.guice.UIHelpersModule;
import org.dmkr.chess.ui.guice.UIListenersModule;
import org.dmkr.chess.ui.guice.UIModule;

import com.google.inject.Guice;

public class Runner {
	
	public static void main(String args[]) {
		EventQueue.invokeLater(() -> {
            try {
                Guice.createInjector(
                        new UIModule(),
                        new UIHelpersModule(),
                        new UIListenersModule()
                ).getInstance(UIBoard.class).run();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
	
}
