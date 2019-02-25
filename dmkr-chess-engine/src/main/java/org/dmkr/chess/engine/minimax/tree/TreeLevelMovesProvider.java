package org.dmkr.chess.engine.minimax.tree;

import static org.dmkr.chess.engine.board.impl.MovesSelectorImpl.ALLOW_CHECK_MOVES_SELECTOR;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.utils.MoveUtils.CaptureMovesFilter;
import org.dmkr.chess.common.primitives.IntsValuesCollector;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.function.EvaluationFunction;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;

public interface TreeLevelMovesProvider {

	int[] getMoves(BoardEngine board);
	
	default boolean onCaptureMoveOnly() {
		return false;
	}
	
	static TreeLevelMovesProvider allMoves() {
		return new AllMovesProvider();
	}
	
	static TreeLevelMovesProvider bestNMoves(int numberOfBestMoves, EvaluationFunctionAware<? extends BoardEngine> evaluationFunctionAware) {
		return new BestEvaluationProvider(new IntsValuesCollector(numberOfBestMoves), evaluationFunctionAware);
	}

	static TreeLevelMovesProvider capturedMoves() {
		return new CaptureMovesProvider();
	}

	static TreeLevelMovesProvider capturedMoves(int maxNotCaptureMovesLimit, int maxCaptureMovesLimit) {
		return new CaptureMovesProvider(maxNotCaptureMovesLimit, maxCaptureMovesLimit);
	}
	
	class AllMovesProvider implements TreeLevelMovesProvider {
		@Override
		public int[] getMoves(BoardEngine board) {
			return board.allowedMoves();
		}
	}
	
	@RequiredArgsConstructor
	class BestEvaluationProvider implements TreeLevelMovesProvider {
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
	
	class CaptureMovesProvider implements TreeLevelMovesProvider {
		private static final int MAX_NOT_CAPTURE_MOVES_LIMIT = 2;
		private static final int MAX_CAPTURE_MOVES_LIMIT = 4;

		private final Function<int[], int[]> capturedMovesFilter;

		private CaptureMovesProvider() {
			this(MAX_NOT_CAPTURE_MOVES_LIMIT, MAX_CAPTURE_MOVES_LIMIT);
		}

		private CaptureMovesProvider(int maxNotCaptureMovesLimit, int maxCaptureMovesLimit) {
			capturedMovesFilter = new CaptureMovesFilter(maxNotCaptureMovesLimit, maxCaptureMovesLimit);
		}


		@Override
		public int[] getMoves(BoardEngine board) {
			return board.allowedMoves(capturedMovesFilter);
		}
		
		@Override
		public boolean onCaptureMoveOnly() {
			return true;
		}
	}
	
	
}
