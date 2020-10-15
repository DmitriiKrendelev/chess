package org.dmkr.chess.engine.minimax;

import static org.dmkr.chess.engine.function.Functions.getDefaultEvaluationFunction;
import static org.dmkr.chess.engine.minimax.MiniMax.minimax;
import static org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategyImpl.*;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProviders.*;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Test;

public class FindMoveMiniMaxAny extends FindMoveAbstractTest<BoardEngine> {

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
				.isAsynchronous(false)
				.build();
	}
		
	@Test
	public void test1() {
		final BoardEngine board = BoardFactory.of(
				"r n b q k - - N", 
				"p p p p - - p p", 		
				"- - - b - - - -", 		
				"- - - - - - - -", 		
				"- - - - P p - -", 		
				"- - n - - - - -",
				"P P P P - - P P", 		
				"R - B Q K B - R")
				.canCastleLeft()
				.canCastleRght()
				.canOponentCastleLeft()
				.canOponentCastleRght()
				.build()
				.invert();
		
		System.out.println(board);
		
		findMove(board, ANY);
	}
}	
