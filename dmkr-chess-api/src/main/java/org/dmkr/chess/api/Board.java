package org.dmkr.chess.api;

import org.dmkr.chess.api.model.Move;

public interface Board extends BoardImmutable {

	void applyMove(Move move);
	
	void rollback();
}
