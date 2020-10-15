package org.dmkr.chess.engine.minimax;

import static org.dmkr.chess.engine.board.BoardFactory.getBoardType;
import static org.dmkr.chess.engine.function.Functions.PIECE_VALUES;
import static org.dmkr.chess.engine.minimax.MiniMax.minimax;
import static org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategyImpl.*;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProviders.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Test;

public class InterruptionTest {
	private static final int NUMBER_OF_RETRYS = 5;
	
	@Test
	public void test1() throws Exception {
		final BoardEngine board = BoardFactory.newInitialPositionBoard();
		final EvaluationFunctionAware<BoardEngine> evaluationFunctionAware = EvaluationFunctionAware.of(PIECE_VALUES.getFunction(getBoardType()));
		
		final MiniMax<BoardEngine> minimax = minimax()
				.treeStrategyCreator(() ->
						treeBuildingStrategy()
								.onLevel1(allMoves())
								.onLevel2(allMoves())
								.onLevel3(allMoves())
								.onLevel4(allMoves())
				)
				.evaluationFunctionAware(evaluationFunctionAware)
				.isAsynchronous(true)
				.build();

		for (int i = 0; i < NUMBER_OF_RETRYS; i ++) {
			minimax.run(board);
			Thread.sleep(100);
			assertTrue(minimax.isInProgress());
			minimax.interrupt();
			Thread.sleep(100);
			assertFalse(minimax.isInProgress());
			minimax.join();
		}
	}
}
