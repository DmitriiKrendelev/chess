package org.dmkr.chess.engine.minimax.tree;

import static org.dmkr.chess.api.utils.MoveUtils.IS_CAPTURE_MOVE;
import static org.dmkr.chess.engine.board.impl.MovesSelectorImpl.ALLOW_CHECK_MOVES_SELECTOR;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.utils.MoveUtils.LimitedMovesSelector;
import org.dmkr.chess.common.primitives.IntsValuesCollector;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.function.EvaluationFunction;

import lombok.RequiredArgsConstructor;

public interface TreeLevelMovesProvider {

	int[] getMoves(BoardEngine board);
	
	default boolean onCaptureMoveOnly() {
		return false;
	}
	
	public static TreeLevelMovesProvider allMovesProvider() {
		return new AllMovesProvider();
	}
	
	public static TreeLevelMovesProvider bestNMovesProvider(int numberOfBestMoves, EvaluationFunctionAware<? extends BoardEngine> evaluationFunctionAware) {
		return new BestEvaluationProvider(new IntsValuesCollector(numberOfBestMoves), evaluationFunctionAware);
	}
	
	public static TreeLevelMovesProvider capturedMovesProvider() {
		return new CaptureMovesProvider();
	}
	
	static class AllMovesProvider implements TreeLevelMovesProvider {
		@Override
		public int[] getMoves(BoardEngine board) {
			return board.allowedMoves();
		}
	}
	
	@RequiredArgsConstructor
	static class BestEvaluationProvider implements TreeLevelMovesProvider {
		private final IntsValuesCollector bestMovesCollector;
		private final EvaluationFunctionAware<? extends BoardEngine> evaluationFunctionAware;
		
		@Override
		public int[] getMoves(BoardEngine board) {
			bestMovesCollector.setIntPredicate(move -> !board.isKingUnderAtackAfterMove(move));
			
			final EvaluationFunction<? extends BoardEngine> evaluationFunction = evaluationFunctionAware.getEvaluationFunction();
			for (int move : board.calculateAllowedMoves(ALLOW_CHECK_MOVES_SELECTOR)) {
				collectMoveWithEvaluation(move, board, evaluationFunction, bestMovesCollector);
			}			
			
			final int[] result = bestMovesCollector.intsArrayDescending();
			bestMovesCollector.reset();
			
			return result;
		}
		
		private void collectMoveWithEvaluation(
				int move, 
				BoardEngine board, 
				EvaluationFunction<? extends BoardEngine> evaluationFunction,
				IntsValuesCollector bestMovesCollector) {
			
			board.applyMove(move);
			@SuppressWarnings({ "rawtypes", "unchecked" })
			final int moveValue = ((EvaluationFunction) evaluationFunction).value(board);
			
			board.rollbackMove();
			bestMovesCollector.add(move, moveValue);
		}
	}
	
	static class CaptureMovesProvider implements TreeLevelMovesProvider {
		private static final int MAX_NOT_CAPTURE_MOVES_LIMIT = 2;
		private final LimitedMovesSelector onCapturedMoveSelector = new LimitedMovesSelector(IS_CAPTURE_MOVE.negate(), MAX_NOT_CAPTURE_MOVES_LIMIT);

		@Override
		public int[] getMoves(BoardEngine board) {
			final int[] selectedMoves = board.allowedMoves(onCapturedMoveSelector);
			onCapturedMoveSelector.reset();
			return selectedMoves;
		}
		
		@Override
		public boolean onCaptureMoveOnly() {
			return true;
		}
	}
	
	
}
