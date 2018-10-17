package org.dmkr.chess.ui.listeners;

import org.dmkr.chess.api.Board;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.AsyncEngine;

import com.google.inject.Inject;

public class MovesRollbackListener extends AbstractPressAndTypedListener {
	private static final int Z = 90;
	
	private final AsyncEngine<?extends BoardEngine> engine;
	private final Board board;
	
	@Inject
	private MovesRollbackListener(BoardEngine board, AsyncEngine<BoardEngine> engine) {
		super(CTRL, Z);
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
