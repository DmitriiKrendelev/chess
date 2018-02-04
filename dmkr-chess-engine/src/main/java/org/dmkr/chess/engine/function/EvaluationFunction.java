package org.dmkr.chess.engine.function;

import org.dmkr.chess.api.BoardEngine;

@FunctionalInterface
public interface EvaluationFunction<T extends BoardEngine> {

	int value(T board);

	default EvaluationFunction<T> and(EvaluationFunction<T> other) {
		return board -> this.value(board) + other.value(board);
	}
	
}
