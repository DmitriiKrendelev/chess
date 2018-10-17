package org.dmkr.chess.engine.api;

import java.util.Optional;
import java.util.SortedSet;

import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.engine.minimax.BestLine;

import static org.dmkr.chess.common.collections.CollectionUtils.isEmpty;

public interface ProgressProvider {
	int REPEAT_MOVES_TREASHOLD = 50;

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
		final SortedSet<BestLine> evaluation = getCurrentEvaluation();
		if (isEmpty(evaluation)) {
			return null;
		}

		final BestLine first = evaluation.first();
		if (!first.isCached() || evaluation.size() == 1) {
			return first;
		}

		for (BestLine bestLine: evaluation) {
			if (!bestLine.isCached() && bestLine.getLineValue() > REPEAT_MOVES_TREASHOLD) {
                System.out.println("Use not first line with value: " + bestLine.getLineValue() + ". Don't repeat moves.");
                return bestLine;
			}
		}

        System.out.println("Use cached line with value: " + first.getLineValue() + ". Repeat moves.");
		return first;
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
