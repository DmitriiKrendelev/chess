package org.dmkr.chess.engine.function.common;

import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.function.EvaluationFunction;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EvaluationFunctionDeepPenalty<T extends BoardEngine> implements EvaluationFunction<T> {
	private final IntSupplier deepProvider;
	private final BooleanSupplier isOponentMove;
	
	@Override
	public int value(T board) {
		final int deep = deepProvider.getAsInt();
		return isOponentMove.getAsBoolean() ? deep : -deep;
	}

	@Override
	public String toString() {
		return null;
	}
	
}
