package org.dmkr.chess.engine.minimax;

import static org.dmkr.chess.engine.function.Functions.getDefaultEvaluationFunction;
import static org.dmkr.chess.engine.minimax.MiniMax.minimax;
import static org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategyImpl.treeBuildingStrategyWithParams;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Test;

public class MinimaxLinesCutOffTest {

	private AsyncEngine<BoardEngine> getEngine(boolean enableLinesCutOff) {
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
				.enableLinesCutOff(enableLinesCutOff)
				.isAsynchronous(true)
				.build();
	} 

	private void test(BoardEngine board) {
		System.out.println(board);
		
		final AsyncEngine<BoardEngine> engineEnableLinesCutOff = getEngine(true);
	    final AsyncEngine<BoardEngine> engineDisableLinesCutOff = getEngine(false);
		
		engineEnableLinesCutOff.run(board);
		engineDisableLinesCutOff.run(board);
		
		engineEnableLinesCutOff.join();
		engineDisableLinesCutOff.join();
		
		final BestLine enableLinesCutOffBestLine = engineEnableLinesCutOff.getBestLine();
		final BestLine disableLinesCutOffBestLine = engineDisableLinesCutOff.getBestLine();
		
		assertEquals(disableLinesCutOffBestLine, enableLinesCutOffBestLine);
		final long cutOffCount = engineEnableLinesCutOff.getCurrentCount();
		final long noCutOffCount = engineDisableLinesCutOff.getCurrentCount();
		
		System.out.println("Enable Lines Cut-Off:\n" + cutOffCount);
		System.out.println("Disable Lines Cut-Off:\n" + noCutOffCount);
		assertTrue("NoCutOffCount = " + noCutOffCount + " CutOffCount = " + cutOffCount, noCutOffCount > cutOffCount);
	}
	
	@Test
	public void test1() {
		final BoardEngine board = BoardFactory.of(
				"r - b - k - - r",  
				"- p p - - p p p",  
				"- p - - - - - -",  
				"- - - n P - - -",  
				"- - - N - P - -",  
				"- - - - - - - -",  
				"P - - N - - P P",  
				"R - - - K - - R")
				.canOponentCastleLeft()
				.canOponentCastleRght()
				.canCastleLeft()
				.canCastleRght()
				.build()
				.invert();
		
		test(board);
	}
	
	
	
	
}
