package org.dmkr.chess.engine.minimax;

import static org.dmkr.chess.engine.function.Functions.getDefaultEvaluationFunction;
import static org.dmkr.chess.engine.minimax.MiniMax.minimax;
import static org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategyImpl.treeBuildingStrategyWithParams;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Test;

public class FindMoveInitialPosition extends FindMoveAbstractTest<BoardEngine> {

	@Override
	protected AsyncEngine<BoardEngine> getEngine() {
		final EvaluationFunctionAware<BoardEngine> evaluationFunctionAware = EvaluationFunctionAware.of(getDefaultEvaluationFunction());
		return minimax()
				.treeStrategyCreator(() -> 
					treeBuildingStrategyWithParams()
						.fullScanLevel(2)
						.cutOffLevel(0)
						.cutOffNumberOfMoves(0)
						.captureMovesLevel(0)
						.evaluationFunctionAware(evaluationFunctionAware)
						.build())
				.evaluationFunctionAware(evaluationFunctionAware)
				.build();
	} 
	
	@Test
	public void test1() {
		findMove(BoardFactory.newInitialPositionBoard(), ANY);
	}
}
