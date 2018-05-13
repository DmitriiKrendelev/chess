package org.dmkr.chess.engine.function.bit;

import static org.dmkr.chess.api.model.Constants.VALUE_QUEEN;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.api.utils.BitBoardUtils;
import org.dmkr.chess.engine.function.EvaluationFunction;

import lombok.RequiredArgsConstructor;
import org.dmkr.chess.engine.function.common.EvaluationFunctionMovesAbstract;

@RequiredArgsConstructor
public class EvaluationFunctionQueenInTheCenterBit extends EvaluationFunctionMovesAbstract<BitBoard> {
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
	public int calculateOneSidedValue(BitBoard board) {
		return calculateQueenInTheCenterTooEarlyPenalty(board);
	}

	public static int calculateQueenInTheCenterTooEarlyPenalty(BitBoard board) {
		final int penalty = NUMBER_MOVES_TO_STAY_QUEEN - board.moveNumber();

		if (penalty < 0) {
			return 0;
		}

		final long queenField = board.pieces(VALUE_QUEEN);
		return isIndexInTheCenter(queenField) ? - (penalty * PENALTY_VALUE) : 0;
	}

	private static boolean isIndexInTheCenter(long queenField) {
		return (queenField & CENTER) != 0;
	}
	
	public String toString() {
		return "Queen in The center";
	}
}
