package org.dmkr.chess.engine.minimax;

import static org.dmkr.chess.api.model.Field.D4;
import static org.dmkr.chess.api.model.Field.F4;
import static org.dmkr.chess.api.model.Field.G1;
import static org.dmkr.chess.api.model.Field.G3;
import static org.dmkr.chess.api.model.Field.G5;
import static org.dmkr.chess.api.model.Field.G7;
import static org.dmkr.chess.api.model.Field.H2;
import static org.dmkr.chess.api.model.Field.H4;
import static org.dmkr.chess.api.model.Field.H6;
import static org.dmkr.chess.api.model.Field.H8;
import static org.dmkr.chess.api.model.Move.moveOf;
import static org.dmkr.chess.engine.minimax.MiniMax.minimax;
import static org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategyImpl.*;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProviders.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Test;

import static org.dmkr.chess.engine.function.Functions.getDefaultEvaluationFunction;

public class FindMoveCheckmateThreeMovesTest extends FindMoveAbstractTest<BoardEngine> {

	@Override
	protected AsyncEngine<BoardEngine> getEngine() {
		final EvaluationFunctionAware<BoardEngine> evaluationFunctionAware = EvaluationFunctionAware.of(getDefaultEvaluationFunction());
		return minimax()
				.treeStrategyCreator(() ->
						treeBuildingStrategy()
								.onLevel1(allMoves())
								.onLevel2(allMoves())
								.onLevel3(allMoves())

				)
				.evaluationFunctionAware(evaluationFunctionAware)
				.build();
	} 
	
	@Override
	protected void checkMove(BoardEngine board, Move move, AsyncEngine<BoardEngine> engine) {
		assertTrue(engine.getBestLine().getLineValue() > CHACKMATE_BARIER_VALUE);
		board.applyMove(move);
		assertFalse(board.isCheckmate());

		engine.run(board);
		assertTrue(-engine.getBestLine().getLineValue() > CHACKMATE_BARIER_VALUE);
		board.applyMove(engine.getBestMove());
		
		engine.run(board);
		assertTrue(engine.getBestLine().getLineValue() > CHACKMATE_BARIER_VALUE);
		board.applyMove(engine.getBestMove());
		
		engine.run(board);
		assertTrue(-engine.getBestLine().getLineValue() > CHACKMATE_BARIER_VALUE);
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
	public void test1() throws Exception {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - K - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - R -", 		
				"- - - - - - - R", 		
				"- - k - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		findMove(board, 
				moveOf(G5, G3), ANY,
				moveOf(H4, H2), ANY,
				moveOf(G3, G1));
	}
	
	@Test
	public void test2() throws Exception {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - K - - - - -", 		
				"- - - - - - r -", 		
				"- - - - - - - r", 		
				"- - k - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build()
				.invert();
		
		findMove(board, 
				moveOf(H4, H6), ANY,
				moveOf(G5, G7), ANY,
				moveOf(H6, H8));
	}
	
	
	@Test
	public void test3() throws Exception {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- p - - - - - -", 		
				"- p - - - - - -", 		
				"k p - - - - - -", 		
				"- p - Q - - - -", 		
				"b R - P - - - -",
				"p - - - - - K -", 		
				"B - - - - - - -")
				.build();
		
		findMove(board, 
				moveOf(D4, F4), ANY);
	}
	
}
