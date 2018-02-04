package org.dmkr.chess.engine.minimax.tree;

import static org.dmkr.chess.engine.function.Functions.getDefaultEvaluationFunction;
import static org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategyImpl.treeBuildingStrategyWithParams;

import java.util.Set;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.api.utils.MoveUtils;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.board.BoardFactory;
import org.dmkr.chess.engine.function.EvaluationFunction;
import org.junit.Test;

public class TreeBuildingStrategyCutoffTest {
	
	@Test
	public void testBestMovesInitialPosition() {
		final EvaluationFunction<BoardEngine> evaluationFunction = getDefaultEvaluationFunction();
		
		final TreeBuildingStrategy treeStrategy = treeBuildingStrategyWithParams()
				.fullScanLevel(0)
				.cutOffLevel(2)
				.cutOffNumberOfMoves(6)
				.captureMovesLevel(2)
				.evaluationFunctionAware(EvaluationFunctionAware.of(evaluationFunction))
				.build();
		
		final BoardEngine board = BoardFactory.newInitialPositionBoard();
		final Set<Move> moves = MoveUtils.toSet(treeStrategy.getSubtreeMoves(board, () -> 0), board.isInverted());
		System.out.println(moves);
	}
	
	@Test
	public void testBestMovesInitialPositionOponent() {
		final EvaluationFunction<BoardEngine> evaluationFunction = getDefaultEvaluationFunction();
		
		final TreeBuildingStrategy treeStrategy = treeBuildingStrategyWithParams()
				.fullScanLevel(0)
				.cutOffLevel(2)
				.cutOffNumberOfMoves(6)
				.captureMovesLevel(2)
				.evaluationFunctionAware(EvaluationFunctionAware.of(evaluationFunction))
				.build();
		
		
		final BoardEngine board = BoardFactory.newInitialPositionBoard();
		board.applyMove(Move.moveOf("E2-E4"));
		
		final Set<Move> moves = MoveUtils.toSet(treeStrategy.getSubtreeMoves(board, () -> 0), board.isInverted());
		System.out.println(moves);
	}
	
}
