package org.dmkr.chess.ui;

import javax.swing.*;

import com.google.inject.Injector;
import org.dmkr.chess.common.lang.SwitchByType;
import org.dmkr.chess.ui.config.UIBoardConfig;
import org.dmkr.chess.ui.guice.UIListenersModule;

import com.google.inject.Inject;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EventListener;

@SuppressWarnings("serial")
public class UIBoard extends JFrame implements AutoCloseable {
	@Inject private UIBoardConfig config;
	@Inject private UIBoardJComponent jComponent;
	@Inject private UIBoardJMenuBar menuBar;
	@Inject private Injector injector;

	public void run() {
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setTitle(config.getTitle());
		setSize(config.getSize());
		setBackground(config.getBackgroundColor());
		
		setContentPane(jComponent);

		UIListenersModule.UI_LISTENERS.forEach(
			uiListenerClass -> {
				final EventListener listener = injector.getInstance(uiListenerClass);

				SwitchByType.switchByType(listener)
						.ifInstanceThenDo(MouseMotionListener.class, this::addMouseMotionListener)
						.ifInstanceThenDo(MouseListener.class, this::addMouseListener)
						.ifInstanceThenDo(KeyListener.class, this::addKeyListener)
						.elseFail();
			}
		);

		setJMenuBar(menuBar);

        jComponent.run();
	}

	@Override
	public void close() throws Exception {
        System.out.println("Close: " + getClass().getSimpleName());
		jComponent.close();
		setVisible(false);
		dispose();
	}
}
