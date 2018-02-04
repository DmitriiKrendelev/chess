package org.dmkr.chess.ui.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractPressAndTypedListener extends KeyAdapter {
	private final int pressed;
	private final int typed;
	private boolean isPressed;
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == pressed) {
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
