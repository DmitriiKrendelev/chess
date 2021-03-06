package org.dmkr.chess.ui.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.swing.*;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractPressAndTypedListener extends KeyAdapter implements UIListener {
    protected static final int CTRL = KeyEvent.VK_CONTROL;

	private final int pressed, typed;
	@Getter
	private final String displayedName;
	@Getter
	private final KeyStroke runKeys;

	private boolean isPressed;


	@SneakyThrows
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == pressed) {
			if (pressed == typed) {
				run();
				return;
			}

			isPressed = true;
		} else if (e.getKeyCode() == typed && isPressed) {
			run();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == pressed) {
			isPressed = true;
		}
	}
	
	public abstract void run();

}
