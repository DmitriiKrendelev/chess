package org.dmkr.chess.engine.api;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.minimax.BestLine;

import java.util.Optional;

public interface AsyncEngine<T extends BoardEngine> extends ProgressProvider, EvaluationFunctionAware<T> {

	void run(T board);
	
	void interrupt();
	
	void join();

	@Override
	default BestLine getBestLine() {
		while (isInProgress()) {
			join();
		}
		return ProgressProvider.super.getBestLine();
	}
}
