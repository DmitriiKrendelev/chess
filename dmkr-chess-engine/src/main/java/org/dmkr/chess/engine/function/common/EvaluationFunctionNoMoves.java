package org.dmkr.chess.engine.function.common;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.function.EvaluationFunction;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EvaluationFunctionNoMoves<T extends BoardEngine> implements EvaluationFunction<T> {
	private static final EvaluationFunction<? extends BoardEngine> INSTANCE = new EvaluationFunctionNoMoves<>();
	
	private static final int CHECKMATE_VALUE = -1000000;
	private static final int STALEMATE_VALUE = 0;
	
	@SuppressWarnings("unchecked")
	public static <T extends BoardEngine> EvaluationFunction<T> noMovesEvaluationFunction() {
		return (EvaluationFunction<T>) INSTANCE;
	}
	
	@Override
	public int value(T board) {
		return board.isKingUnderAtack() ? CHECKMATE_VALUE : STALEMATE_VALUE;
	}
	
}
