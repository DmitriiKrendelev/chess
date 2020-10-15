package org.dmkr.chess.engine.minimax.tree;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.utils.MoveUtils;
import org.dmkr.chess.common.primitives.IntsValuesCollector;
import org.dmkr.chess.engine.function.EvaluationFunction;

import java.util.function.Function;

import static org.dmkr.chess.engine.board.impl.MovesSelectorImpl.*;

public interface TreeLevelMovesProviders {
    static TreeLevelMovesProvider allMoves() {
        return BoardEngine::allowedMoves;
    }

    static TreeLevelMovesProvider nBestMoves(int numberOfBestMoves) {
        return new BestEvaluationProvider(new IntsValuesCollector(numberOfBestMoves));
    }

    static TreeLevelMovesProvider capturedMoves() {
        return new CaptureMovesProvider();
    }

    static TreeLevelMovesProvider capturedMoves(int maxNotCaptureMovesLimit, int maxCaptureMovesLimit) {
        return new CaptureMovesProvider(maxNotCaptureMovesLimit, maxCaptureMovesLimit);
    }

    @RequiredArgsConstructor
    class BestEvaluationProvider implements TreeLevelMovesProvider {
        private final IntsValuesCollector bestMovesCollector;

        @Setter(onMethod = @_(@Override))
        private EvaluationFunction<? extends BoardEngine> evaluationFunction;

        @Override
        public int[] getMoves(BoardEngine board) {
            bestMovesCollector.setIntPredicate(move -> !board.isKingUnderAtackAfterMove(move));

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

            board.previewMove(move);
            @SuppressWarnings({ "rawtypes", "unchecked" })
            final int moveValue = ((EvaluationFunction) evaluationFunction).value(board);

            board.rollbackPreviewMove(move);
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
            capturedMovesFilter = new MoveUtils.CaptureMovesFilter(maxNotCaptureMovesLimit, maxCaptureMovesLimit);
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
