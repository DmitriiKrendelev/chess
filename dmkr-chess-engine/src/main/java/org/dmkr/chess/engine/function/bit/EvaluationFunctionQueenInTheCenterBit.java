package org.dmkr.chess.engine.function.bit;

import static org.dmkr.chess.api.model.Constants.VALUE_QUEEN;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.api.utils.BitBoardUtils;
import org.dmkr.chess.engine.function.EvaluationFunction;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EvaluationFunctionQueenInTheCenterBit implements EvaluationFunction<BitBoard> {
	private static final int NUMBER_MOVES_TO_STAY_QUEEN = 10;
	private static final int PENALTY_VALUE = 10;
	
	private static final long CENTER = BitBoardUtils.fromBinaryStrings(
			"0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0",
			"1 1 1 1 1 1 1 1",
			"1 1 1 1 1 1 1 1",
			"1 1 1 1 1 1 1 1",
			"1 1 1 1 1 1 1 1",
			"0 0 0 0 0 0 0 0",
			"0 0 0 0 0 0 0 0");
	
	public static final EvaluationFunction<BitBoard> INSTANCE = new EvaluationFunctionQueenInTheCenterBit();
	
	@Override
	public int value(BitBoard board) {
		final int penalty = NUMBER_MOVES_TO_STAY_QUEEN - board.moveNumber();
		
		if (penalty < 0) {
			return 0;
		}
		
		final long queenField = board.pieces(VALUE_QUEEN);
		final long oponentQueenField = board.oponentPieces(VALUE_QUEEN);

		return ((isIndexInTheCenter(queenField) ? -1 : 0) + (isIndexInTheCenter(oponentQueenField) ? 1 : 0)) * penalty * PENALTY_VALUE; 
	}

	private static boolean isIndexInTheCenter(long queenField) {
		return (queenField & CENTER) != 0;
	}
	
	public String toString() {
		return "Queen in The center";
	}
}
