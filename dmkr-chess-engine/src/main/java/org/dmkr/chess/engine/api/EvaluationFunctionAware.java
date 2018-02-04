package org.dmkr.chess.engine.api;

import static org.dmkr.chess.engine.function.common.EvaluationFunctionNoMoves.noMovesEvaluationFunction;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.function.EvaluationFunction;

public interface EvaluationFunctionAware<T extends BoardEngine> {

	EvaluationFunction<T> getEvaluationFunction();
	
	EvaluationFunction<T> getNoMovesFunction();
	
	static <T extends BoardEngine> EvaluationFunctionAware<T> of(EvaluationFunction<T> evaluationFunction) {
		return new EvaluationFunctionAware<T>() {
			@Override
			public EvaluationFunction<T> getEvaluationFunction() {
				return evaluationFunction;
			}

			@Override
			public EvaluationFunction<T> getNoMovesFunction() {
				return noMovesEvaluationFunction();
			}
		};
	}
	
	default EvaluationFunctionAware<T> and(EvaluationFunction<T> function) {
		final EvaluationFunction<T> newEvaluationFunction = getEvaluationFunction().and(function);
		final EvaluationFunction<T> newNoMovesFunction = getNoMovesFunction().and(function);
		
		return new EvaluationFunctionAware<T>() {
			@Override
			public EvaluationFunction<T> getEvaluationFunction() {
				return newEvaluationFunction;
			}

			@Override
			public EvaluationFunction<T> getNoMovesFunction() {
				return newNoMovesFunction;
			}
		};
	}
}
