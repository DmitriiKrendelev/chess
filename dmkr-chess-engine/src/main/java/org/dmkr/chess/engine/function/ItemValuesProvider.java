package org.dmkr.chess.engine.function;

import static org.dmkr.chess.api.model.Constants.*;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ItemValuesProvider {

	private static final int[] ITEM_VALUES;
	
	static {
		ITEM_VALUES = new int[7];
		ITEM_VALUES[VALUE_POWN] = 100;
		ITEM_VALUES[VALUE_KNIGHT] = 320;
		ITEM_VALUES[VALUE_BISHOP] = 330;
		ITEM_VALUES[VALUE_ROOK] = 500;
		ITEM_VALUES[VALUE_QUEEN] = 900;
		ITEM_VALUES[VALUE_KING] = 0;
	}
	
	public static int valueOf(byte item) { 
		final boolean positive = item > 0;
		final int result = ITEM_VALUES[positive ? item : -item];
		return positive ? result : -result;
	}
}
