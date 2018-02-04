package org.dmkr.chess.engine.minimax;

import static org.dmkr.chess.api.model.Move.moveOf;
import static org.dmkr.chess.engine.function.Functions.getDefaultEvaluationFunction;
import static org.dmkr.chess.engine.minimax.MiniMax.minimax;
import static org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategyImpl.treeBuildingStrategyWithParams;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Test;

public class FindMoveCheckmateOneMoveTest extends FindMoveAbstractTest<BoardEngine> {
	
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
	
	@Override
	protected void checkMove(BoardEngine board, Move move, AsyncEngine<BoardEngine> engine) {
		testCheckmateMove(board, move, engine);
	}
	
	@Override
	protected void checkBestLine(BoardEngine board, BestLine bestLine, AsyncEngine<BoardEngine> engine) {
		testCheckmateBestLine(board, bestLine, engine);
	}
	
	@Test
	public void test1() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"n - N - - - - -", 		
				"- p - - - - - -",
				"p - - - - - - r", 		
				"k - - - K - - R")
				.canCastleRght()
				.build();
		
		findMove(board, moveOf("E1-G1"));
	}
	
	@Test
	public void test2() {
		final BoardEngine board = BoardFactory.of(
				"Q - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - p -", 		
				"- - p - - - - p", 		
				"- - P - - - k P", 		
				"- - - - - - P -",
				"- - - - - K - -", 		
				"q - - - - - - -")
				.build();
		
		findMove(board, moveOf("A8-C8"));
	}
	
	@Test
	public void test3() {
		final BoardEngine board = BoardFactory.of(
				"r - b q - - n r", 
				"p p - n p - b p", 		
				"- - - p - k p -", 		
				"- - p - - - N -", 		
				"- - - P P - - -", 		
				"- - - - - - - -",
				"P P P - - P P P", 		
				"R N B Q - R K -")
				.build();
		
		findMove(board, moveOf("D1-F3"));
	}
	
	@Test
	public void test4() {
		final BoardEngine board = BoardFactory.of(
				"- - - Q - - - -", 
				"- B - - R - R -", 		
				"- - - - - k - -", 	
				"- - - - - - - -", 	
				"- - - - - - - -", 	
				"- - - - - - - -",
				"- - - - - - - -",  		
				"K - - - - - - -")
				.build();
		
		findMove(board, moveOf("D8-F8"));
	}
	
	@Test
	public void test5() {
		final BoardEngine board = BoardFactory.of(
				"- - - R - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - k", 	
				"- - - - - - - -", 	
				"- - - - - - - -", 	
				"- - - - - - - -",
				"- - - - - - - -",  		
				"- - - - - K R -")
				.build();
		
		findMove(board, moveOf("D8-H8"));
	}
}
