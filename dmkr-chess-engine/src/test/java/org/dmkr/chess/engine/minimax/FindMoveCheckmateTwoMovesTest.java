package org.dmkr.chess.engine.minimax;

import static org.dmkr.chess.api.model.Field.A6;
import static org.dmkr.chess.api.model.Field.F4;
import static org.dmkr.chess.api.model.Field.F7;
import static org.dmkr.chess.api.model.Field.G2;
import static org.dmkr.chess.api.model.Field.G4;
import static org.dmkr.chess.api.model.Field.G6;
import static org.dmkr.chess.api.model.Field.G7;
import static org.dmkr.chess.api.model.Field.H1;
import static org.dmkr.chess.api.model.Field.H3;
import static org.dmkr.chess.api.model.Field.H4;
import static org.dmkr.chess.api.model.Field.H6;
import static org.dmkr.chess.api.model.Field.H7;
import static org.dmkr.chess.api.model.Move.moveOf;
import static org.dmkr.chess.engine.function.Functions.getDefaultEvaluationFunction;
import static org.dmkr.chess.engine.minimax.MiniMax.minimax;
import static org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategyImpl.treeBuildingStrategy;
import static org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategyImpl.treeBuildingStrategyWithParams;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProvider.allMovesProvider;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProvider.bestNMovesProvider;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProvider.capturedMovesProvider;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Test;

public class FindMoveCheckmateTwoMovesTest extends FindMoveAbstractTest<BoardEngine> {
	
	@Override
	protected AsyncEngine<BoardEngine> getEngine() {
		final EvaluationFunctionAware<BoardEngine> evaluationFunctionAware = EvaluationFunctionAware.of(getDefaultEvaluationFunction());
		return minimax()
				.treeStrategyCreator(() ->
						treeBuildingStrategy()
								.onFirstLevel(allMovesProvider())
								.onSecondLevel(allMovesProvider())
								.build())
				.evaluationFunctionAware(evaluationFunctionAware)
				.isAsynchronous(false)
				.build();
	}
	
	@Override
	protected void checkMove(BoardEngine board, Move move, AsyncEngine<BoardEngine> engine) {
		assertTrue(engine.getBestLine().getLineValue() > CHACKMATE_BARIER_VALUE);
		board.applyMove(move);
		assertFalse(board.isCheckmate());

		engine.run(board);
		final int bestLineValue = -engine.getBestLine().getLineValue();
		assertTrue(board.toString() + "\n" + engine.toString() + "\n BestLineValue = " + bestLineValue,
				bestLineValue > CHACKMATE_BARIER_VALUE);
		board.applyMove(engine.getBestMove());
		
		engine.run(board);
		assertTrue(engine.getBestLine().getLineValue() > CHACKMATE_BARIER_VALUE);
		testCheckmateMove(board, engine.getBestMove(), engine);
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
				"- - - - - - R K", 		
				"- - - - - - - R",
				"- - - k - - - -", 		
				"- - - - - - - -")
				.build();
		
		findMove(board, 
				moveOf(G4, G2), ANY,
				moveOf(H3, H1));
	}
	
	@Test
	public void test2() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - Q", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - p - - - - -",
				"- - p N - N - -", 		
				"- - k - - K - -")
				.build();
		
		findMove(board, 
				moveOf(H6, A6), ANY);
	}
	
	@Test
	public void test3() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - K p", 		
				"- - - - - - - R", 		
				"- p - - - - k -", 		
				"- Q - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		findMove(board, 
				moveOf(G7, F7), ANY);
	}
	
	@Test
	public void test4() {
		final BoardEngine board = BoardFactory.of(
				"- - - - r - - -", 
				"- B Q - p R - -", 		
				"- - - r - - - -", 		
				"- p - P k p - -", 		
				"- - - - - - - B", 		
				"- - - - - P P -",
				"- N N - - - - K", 		
				"- - n n - - - -")
				.build();
		
		findMove(board, 
				moveOf(F7, H7), ANY);
	}
	
	@Test
	public void test5() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"B n N - n - - -", 		
				"B - - - P - - -", 		
				"- - r - - - R K", 		
				"r - - k - - - N", 		
				"- - - - p P - -",
				"P b P - - - - -", 		
				"- - - - Q - - -")
				.build();
		
		findMove(board, 
				moveOf(H4, G6), ANY);
	}
	
	@Test
	public void test6() {
		final BoardEngine board = BoardFactory.of(
				"- - R - - - - -", 
				"- - - - b p p k", 		
				"- p - p - - - -", 		
				"- - - - - R - P", 		
				"- - - - P Q - -", 		
				"- - - - - P - -",
				"r - - - - q - P", 		
				"- - - - - - - K")
				.build();
		
		findMove(board, 
				moveOf(F4, H6), ANY);
	}
}
