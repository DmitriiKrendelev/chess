package org.dmkr.chess.engine.minimax.tree;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.api.utils.MoveUtils;
import org.dmkr.chess.engine.board.BoardFactory;
import org.dmkr.chess.engine.function.EvaluationFunction;
import org.junit.Test;

import java.util.Set;

import static org.dmkr.chess.engine.function.Functions.*;
import static org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategyImpl.*;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProviders.*;

public class TreeBuildingStrategyCutoffTest {
	
	@Test
	public void testBestMovesInitialPosition() {
		final EvaluationFunction<? extends BoardEngine> evaluationFunction = getDefaultEvaluationFunction();

		final TreeBuildingStrategy treeStrategy =
				treeBuildingStrategy()
						.onLevel1(nBestMoves(6))
						.onLevel2(nBestMoves(6))
						.evaluationFunction(evaluationFunction)
						.build();
		
		final BoardEngine board = BoardFactory.newInitialPositionBoard();
		final Set<Move> moves = MoveUtils.toSet(treeStrategy.getSubtreeMoves(board, () -> 0), board.isInverted());
		System.out.println(moves);
	}
	
	@Test
	public void testBestMovesInitialPositionOponent() {
		final EvaluationFunction<BoardEngine> evaluationFunction = getDefaultEvaluationFunction();

		final TreeBuildingStrategy treeStrategy =
				treeBuildingStrategy()
						.onLevel1(nBestMoves(6))
						.onLevel2(nBestMoves(6))
						.evaluationFunction(evaluationFunction)
						.build();



		final BoardEngine board = BoardFactory.newInitialPositionBoard();
		board.applyMove(Move.moveOf("E2-E4"));
		
		final Set<Move> moves = MoveUtils.toSet(treeStrategy.getSubtreeMoves(board, () -> 0), board.isInverted());
		System.out.println(moves);
	}
	
}
