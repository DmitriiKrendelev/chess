package org.dmkr.chess.api;

public interface BitBoard extends BoardEngine {

	long pieces(byte pieceType);

	long oponentPieces(byte pieceType);

	long piecePositions();

	long piecePositionsOponent();

	long emptyPositions();
}
