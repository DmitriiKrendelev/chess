package org.dmkr.chess.engine.function;

import com.google.common.collect.ImmutableMap;
import org.dmkr.chess.api.BoardEngine;

import java.util.HashMap;
import java.util.Map;

@FunctionalInterface
public interface EvaluationFunction<T extends BoardEngine> {

	int value(T board);

	default EvaluationFunction<T> and(EvaluationFunction<T> other) {
		return new EvaluationFunction<T>() {
			@Override
			public int value(T board) {
				return EvaluationFunction.this.value(board) + other.value(board);
			}

			@Override
			public Map<String, Integer> getEvaluationDetails(T board) {
				final Map<String, Integer> result = new HashMap<>();
				result.putAll(EvaluationFunction.this.getEvaluationDetails(board));
				result.putAll(other.getEvaluationDetails(board));
				return result;
			}
		};
	}

	default Map<String, Integer> getEvaluationDetails(T board) {
		return ImmutableMap.of(toString(), value(board));
	}
}
