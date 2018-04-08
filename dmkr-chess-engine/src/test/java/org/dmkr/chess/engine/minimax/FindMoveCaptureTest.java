package org.dmkr.chess.engine.minimax;

import static org.dmkr.chess.api.model.Move.moveOf;
import static org.dmkr.chess.engine.function.Functions.PIECE_VALUES;
import static org.dmkr.chess.engine.minimax.MiniMax.minimax;
import static org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategyImpl.treeBuildingStrategyWithParams;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.board.BoardFactory;
import org.dmkr.chess.engine.function.EvaluationFunction;
import org.junit.Test;


public class FindMoveCaptureTest extends FindMoveAbstractTest<BoardEngine> {
	
	@Override
	protected AsyncEngine<BoardEngine> getEngine() {
		final EvaluationFunction<BoardEngine> evaluationFunction = PIECE_VALUES.getFunction(BoardFactory.getBoardType());
		final EvaluationFunctionAware<BoardEngine> evaluationFunctionAware = EvaluationFunctionAware.of(evaluationFunction);
		
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
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - p - - - - -", 		
				"- - - b - n - -", 		
				"- - - - P - - -", 		
				"- - - - - - - -",
				"k - - - - - - K", 		
				"- - - - - - - -")
				.build();
		
		findMove(board, moveOf("E4-F5"));
	}
}
