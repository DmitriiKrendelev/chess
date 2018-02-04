package org.dmkr.chess.engine.function.impl;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.function.EvaluationFunction;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import static org.dmkr.chess.api.model.Constants.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EvaluationFunctionQueenInTheCenter implements EvaluationFunction<BoardEngine> {
	private static final int NUMBER_MOVES_TO_STAY_QUEEN = 10;
	private static final int PENALTY_VALUE = 10;
	
	public static final EvaluationFunction<BoardEngine> INSTANCE = new EvaluationFunctionQueenInTheCenter();
	
	@Override
	public int value(BoardEngine board) {
		final int penalty = NUMBER_MOVES_TO_STAY_QUEEN - board.moveNumber();
		
		if (penalty < 0) {
			return 0;
		}
		
		int queenIndex = -1;
		int oponentQueenIndex = -1;
		for (int i = 0; i < SIZE * SIZE; i ++) {
			final byte item = board.at(i);
			if (item == VALUE_QUEEN) { 
				queenIndex = i;
			}
			if (item == -VALUE_QUEEN) {
				oponentQueenIndex = i;
			}
		}

		return ((isIndexInTheCenter(queenIndex) ? -1 : 0) + (isIndexInTheCenter(oponentQueenIndex) ? 1 : 0)) * penalty * PENALTY_VALUE; 
	}

	private static boolean isIndexInTheCenter(int index) {
		return index >= SIZE * 2 && index < SIZE * (SIZE - 2);
	}
	
	public String toString() {
		return null;
	}
	
}
