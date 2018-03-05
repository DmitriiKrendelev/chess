package org.dmkr.chess.engine.api;

import java.util.Optional;
import java.util.SortedSet;

import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.engine.minimax.BestLine;

public interface ProgressProvider {

	int getCurrentProgressPercents();
	
	int getCurrentTimeInProgress();

	long getCurrentCount();
	
	SortedSet<BestLine> getCurrentEvaluation();
	
	Move getCurrentMove();
	
	long getSpeed();
	
	boolean isInProgress();

	long getEvaluationFinishedTime();

	double getParallelLevel();

	default BestLine getBestLine() {
		return Optional.ofNullable(getCurrentEvaluation())
			.map(lines -> lines.isEmpty() ? null : lines.first())
			.orElse(null);
	}
	
	default Move getBestMove() {
		return Optional.ofNullable(getBestLine())
			.map(BestLine::getMoves)
			.map(moves -> moves.isEmpty() ? null : moves.get(0))
			.orElse(null);
	}
	
	default double getCurrentProgressPerUnit() {
		return (double) getCurrentProgressPercents() / 100;
	}
	
}
