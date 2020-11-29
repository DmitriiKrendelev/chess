package org.dmkr.chess.ui.listeners.impl;

import com.google.inject.name.Named;
import org.dmkr.chess.api.Board;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.AsyncEngine;

import com.google.inject.Inject;
import org.dmkr.chess.ui.listeners.AbstractPressAndTypedListener;

import javax.swing.*;

import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.*;
import static javax.swing.KeyStroke.getKeyStroke;

public class MovesRollbackListener extends AbstractPressAndTypedListener {

	private final AsyncEngine<?extends BoardEngine> engine;
	private final Board board;
	
	@Inject
	private MovesRollbackListener(BoardEngine board, @Named("engine1") AsyncEngine<BoardEngine> engine) {
		super(CTRL, VK_Z, "Undo", getKeyStroke("ctrl Z"));
		this.board = board;
		this.engine = engine;
	}

	@Override
	public void run() {
		if (engine.isInProgress()) {
			engine.interrupt();
			engine.join();
		} else {
			board.rollback();
		}
		
		board.rollback();
	}

}
