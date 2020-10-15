package org.dmkr.chess.engine.minimax.tree;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.function.EvaluationFunction;

public interface TreeLevelMovesProvider {

	int[] getMoves(BoardEngine board);
	
	default boolean onCaptureMoveOnly() {
		return false;
	}

	default void setEvaluationFunction(EvaluationFunction<? extends BoardEngine> evaluationFunction) {

	}

}
