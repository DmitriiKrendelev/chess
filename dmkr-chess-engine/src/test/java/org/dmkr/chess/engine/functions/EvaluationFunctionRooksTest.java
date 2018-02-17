package org.dmkr.chess.engine.functions;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.engine.board.bit.BitBoardBuilder;
import org.dmkr.chess.engine.function.EvaluationFunction;
import org.dmkr.chess.engine.function.bit.EvaluationFunctionRooksBit;
import org.dmkr.chess.engine.function.common.EvaluationFunctionRooksAbstract;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EvaluationFunctionRooksTest {
    private final EvaluationFunction<BitBoard> func = EvaluationFunctionRooksBit.INSTANCE;

    @Test
    public void testRookOnOpenFileA() {
        final BitBoard board = BitBoardBuilder.of(
                "- - - - k - - -",
                "- - - - - - - -",
                "- - - - - - - -",
                "- - - - - - - -",
                "- - - - - - - -",
                "- - - - - - - -",
                "- - - - - - - -",
                "R - - - K - - -")
                .build();

        final int value = func.value(board);
        assertEquals(EvaluationFunctionRooksAbstract.OPEN_FILE_VALUE, value);
    }

    @Test
    public void testRookOnOpenFileABlocked() {
        final BitBoard board = BitBoardBuilder.of(
                "- - - - k - - -",
                "- p - - - - - -",
                "n - - - - - - -",
                "- - - - - - - -",
                "- - - - - - - -",
                "- - - - - - - -",
                "- - - - - - - -",
                "R - - - K - - -")
                .build();

        final int value = func.value(board);
        assertEquals(EvaluationFunctionRooksAbstract.OPEN_FILE_BLOCKED_VALUE, value);
    }

    @Test
    public void testRookOnOpenFileBlocked() {
        final BitBoard board = BitBoardBuilder.of(
                "- - - - - - r -",
                "- - p - - - p -",
                "r p - k - - - -",
                "p - - N - - - p",
                "P P - - - p - -",
                "- - - - - - n -",
                "Q - - K - P R -",
                "- - - - - - n -")
                .build();

        final int value = func.value(board);
        assertEquals(EvaluationFunctionRooksAbstract.OPEN_FILE_BLOCKED_VALUE, value);
    }

}
