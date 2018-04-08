package org.dmkr.chess.engine.minimax;

import static org.dmkr.chess.api.model.Field.C1;
import static org.dmkr.chess.api.model.Field.C2;
import static org.dmkr.chess.api.model.Field.D3;
import static org.dmkr.chess.api.model.Move.moveOf;
import static org.dmkr.chess.engine.board.BoardFactory.getBoardType;
import static org.dmkr.chess.engine.function.Functions.PIECE_VALUES;
import static org.dmkr.chess.engine.minimax.MiniMax.minimax;
import static org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategyImpl.treeBuildingStrategyWithParams;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Test;

public class FindMoveStalemateTest extends FindMoveAbstractTest<BoardEngine> {
	
	@Override
	protected AsyncEngine<BoardEngine> getEngine() {
		final EvaluationFunctionAware<BoardEngine> evaluationFunctionAware = EvaluationFunctionAware.of(PIECE_VALUES.getFunction(getBoardType()));
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
				.build();
	} 

	@Test
	public void test1() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - p - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - K - - - -",
				"p - - - - - - -", 		
				"k - - - - - - -")
				.build();
		
		findMove(board, 
				moveOf(D3, C2), ANY,
				moveOf(C2, C1), ANY,
				moveOf(C1, C2), ANY);
	}
}
