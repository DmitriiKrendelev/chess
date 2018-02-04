package org.dmkr.chess.engine.function.bit;

import static org.dmkr.chess.api.model.Constants.NUMBER_OF_ITEMS;
import static org.dmkr.chess.engine.function.ItemValuesProvider.valueOf;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.engine.function.EvaluationFunction;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EvaluationFunctionItemsValuesBit implements EvaluationFunction<BitBoard> {

	public static final EvaluationFunction<BitBoard> INSTANCE = new EvaluationFunctionItemsValuesBit();
	
	@Override
	public int value(BitBoard board) {
		int result = 0;
		
		for (int i = 0; i < NUMBER_OF_ITEMS; i ++) {
			final byte itemType = (byte) (i + 1);
			final long items = board.items(itemType);
			result += Long.bitCount(items) * valueOf(itemType);
			
			final long oponentItems = board.oponentItems(itemType);
			result -= Long.bitCount(oponentItems) * valueOf(itemType);
		}
		
		return result;
	}

	@Override
	public String toString() {
		return "Items";
	}
}
