package org.dmkr.chess.engine.function.bit;

import static org.dmkr.chess.api.model.Constants.NUMBER_OF_ITEMS;
import static org.dmkr.chess.api.utils.BitBoardUtils.doWithUpBits;
import static org.dmkr.chess.api.utils.BoardUtils.invertIndex;
import static org.dmkr.chess.engine.function.ItemPositionValuesProvider.positionValues;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.engine.function.EvaluationFunction;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EvaluationFunctionItemsPositionsBit implements EvaluationFunction<BitBoard> {
	
	public static final EvaluationFunction<BitBoard> INSTANCE = new EvaluationFunctionItemsPositionsBit();
	
	@Override
	public int value(BitBoard board) {
		int result = 0;
		
		for (int i = 0; i < NUMBER_OF_ITEMS; i ++) {
			final byte itemType = (byte) (i + 1);
			final long items = board.items(itemType);
			result += doWithUpBits(items, field -> positionValues(itemType)[field]);
			
			final long oponentItems = board.oponentItems(itemType);
			result -= doWithUpBits(oponentItems, field -> positionValues(itemType)[invertIndex(field)]);
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		return "Position";
	}
}
