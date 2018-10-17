package org.dmkr.chess.api;

import org.dmkr.chess.api.model.Move;

import java.io.Externalizable;

public interface Board extends BoardImmutable, Externalizable {

	void applyMove(Move move);
	
	void rollback();
}
