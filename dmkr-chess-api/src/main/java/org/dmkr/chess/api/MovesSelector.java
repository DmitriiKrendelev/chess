package org.dmkr.chess.api;

public interface MovesSelector {

	boolean checkKingUnderAtack();
	
	boolean selectMoves(byte item);

	boolean skipEnPassenMoves();
}
