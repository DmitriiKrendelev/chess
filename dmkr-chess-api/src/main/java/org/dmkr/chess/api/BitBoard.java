package org.dmkr.chess.api;

public interface BitBoard extends BoardEngine {

	long items(byte itemType);

	long oponentItems(byte itemType);
}
