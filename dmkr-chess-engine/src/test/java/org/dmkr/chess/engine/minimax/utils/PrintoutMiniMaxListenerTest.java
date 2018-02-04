package org.dmkr.chess.engine.minimax.utils;

import static org.dmkr.chess.engine.function.Functions.getDefaultEvaluationFunction;
import static org.dmkr.chess.engine.minimax.MiniMax.minimax;
import static org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategyImpl.treeBuildingStrategyWithParams;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.board.BoardFactory;
import org.dmkr.chess.engine.minimax.BestLine;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class PrintoutMiniMaxListenerTest {

	@Test
	public void test() {
		final BoardEngine board = BoardFactory.of(
				"r - b q k b - r", 
				"p p p p - p p p",  
				"- - n - - n - -",  
				"- - - P p - - -",  
				"- - - - P - - -",  
				"- - - - - P - -",  
				"P P P - - - P P",  
				"R N B Q K B N R") 
				.build()
				.invert();
		
		
		final AsyncEngine<BoardEngine> engine = getEngine();
		engine.run(board);
		engine.join();
		
		final BestLine bestLine = engine.getBestLine();

		System.out.println("Best Line: " + bestLine);
	}
	
	private AsyncEngine<BoardEngine> getEngine() {
		final EvaluationFunctionAware<BoardEngine> evaluationFunctionAware = EvaluationFunctionAware.of(getDefaultEvaluationFunction());
		
		return minimax()
				.treeStrategyCreator(() -> 
					treeBuildingStrategyWithParams()
						.fullScanLevel(4)
						.cutOffLevel(0)
						.cutOffNumberOfMoves(0)
						.captureMovesLevel(0)
						.evaluationFunctionAware(evaluationFunctionAware)
						.build())
				.evaluationFunctionAware(evaluationFunctionAware)
				.isAsynchronous(true)
				.miniMaxListener(new PrintoutMiniMaxListener("C6-D4", "C2-C3", "D4-E6"))
				.parallelLevel(2)
				.build();
	}
}
