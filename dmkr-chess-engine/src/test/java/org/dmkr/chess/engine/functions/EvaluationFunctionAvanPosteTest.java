package org.dmkr.chess.engine.functions;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.board.bit.BitBoardBuilder;
import org.dmkr.chess.engine.board.impl.BoardBuilder;
import org.dmkr.chess.engine.function.bit.EvaluationFunctionAvanPosteBit;
import org.dmkr.chess.engine.function.impl.EvaluationFunctionAvanPoste;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EvaluationFunctionAvanPosteTest {

    @Test
    public void testAvanPoste() {
        final BoardEngine board = BoardBuilder.of(
                "- - - - k - - -",
                "- - - - - - - -",
                "- - - - - - - p",
                "- p N p - p - -",
                "- P - - - - - -",
                "- - - - p - - -",
                "- - - - - - - -",
                "- - - - K - - -")
                .build();

        final int value = EvaluationFunctionAvanPoste.INSTANCE.value(board);
        assertEquals(EvaluationFunctionAvanPoste.AVAN_POST_VALUE, value);
    }

    @Test
    public void testAvanPosteBit() {
        final BitBoard board = BitBoardBuilder.of(
                "- - - - k - - -",
                "- - - - - - - -",
                "- - - - - - - p",
                "- p N p - p - -",
                "- P - - - - - -",
                "- - - - p - - -",
                "- - - - - - - -",
                "- - - - K - - -")
                .build();

        final int value = EvaluationFunctionAvanPosteBit.INSTANCE.value(board);
        assertEquals(EvaluationFunctionAvanPosteBit.AVAN_POST_VALUE, value);
    }
}
