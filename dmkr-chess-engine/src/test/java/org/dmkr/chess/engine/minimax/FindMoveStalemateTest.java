package org.dmkr.chess.engine.minimax;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Test;

import static org.dmkr.chess.api.model.Field.*;
import static org.dmkr.chess.api.model.Move.*;
import static org.dmkr.chess.engine.board.BoardFactory.*;
import static org.dmkr.chess.engine.function.Functions.*;
import static org.dmkr.chess.engine.minimax.MiniMax.*;
import static org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategyImpl.*;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProviders.*;

public class FindMoveStalemateTest extends FindMoveAbstractTest<BoardEngine> {
	
	@Override
	protected AsyncEngine<BoardEngine> getEngine() {
		final EvaluationFunctionAware<BoardEngine> evaluationFunctionAware = EvaluationFunctionAware.of(PIECE_VALUES.getFunction(getBoardType()));
		return minimax()
				.treeStrategyCreator(() ->
						treeBuildingStrategy()
								.onLevel1(allMoves())
								.onLevel2(allMoves())
								.onLevel3(allMoves())
								.onLevel4(allMoves())
				)
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
