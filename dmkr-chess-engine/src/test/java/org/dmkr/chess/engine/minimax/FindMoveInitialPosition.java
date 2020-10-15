package org.dmkr.chess.engine.minimax;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.board.BoardFactory;
import org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProviders;
import org.junit.Test;

import static org.dmkr.chess.engine.function.Functions.*;
import static org.dmkr.chess.engine.minimax.MiniMax.*;
import static org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategyImpl.*;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProviders.*;

public class FindMoveInitialPosition extends FindMoveAbstractTest<BoardEngine> {

	@Override
	protected AsyncEngine<BoardEngine> getEngine() {
		final EvaluationFunctionAware<BoardEngine> evaluationFunctionAware = EvaluationFunctionAware.of(getDefaultEvaluationFunction());
		return minimax()
				.treeStrategyCreator(() ->
						treeBuildingStrategy()
								.onLevel1(allMoves())
								.onLevel2(allMoves())
				)
				.evaluationFunctionAware(evaluationFunctionAware)
				.build();
	} 
	
	@Test
	public void test1() {
		findMove(BoardFactory.newInitialPositionBoard(), ANY);
	}
}
