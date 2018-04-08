package org.dmkr.chess.engine.function;

import static org.dmkr.chess.api.model.Constants.*;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PieceValuesProvider {

	private static final int[] PIECE_VALUES;
	
	static {
		PIECE_VALUES = new int[7];
		PIECE_VALUES[VALUE_PAWN] = 100;
		PIECE_VALUES[VALUE_KNIGHT] = 320;
		PIECE_VALUES[VALUE_BISHOP] = 330;
		PIECE_VALUES[VALUE_ROOK] = 500;
		PIECE_VALUES[VALUE_QUEEN] = 900;
		PIECE_VALUES[VALUE_KING] = 0;
	}
	
	public static int valueOf(byte piece) {
		final boolean positive = piece > 0;
		final int result = PIECE_VALUES[positive ? piece : -piece];
		return positive ? result : -result;
	}
}
