package org.dmkr.chess.engine.functions;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.board.impl.BoardBuilder;
import org.dmkr.chess.engine.function.common.EvaluationFunctionBasedBoardInversion;
import org.junit.Test;

import static org.dmkr.chess.engine.function.impl.EvaluationFunctionMoves.*;
import static org.junit.Assert.assertEquals;

public class EvaluationFunctionMovesTest {

    private static final EvaluationFunctionBasedBoardInversion<BoardEngine> FUNCTION = INSTANCE_NOT_CHECK_KING_UNDER_ATACKS;

    @Test
    public void testOneBishop() {
        final BoardEngine board = BoardBuilder.of(
                "- - - - k - - -",
                "- - - - - - - -",
                "- - - - - - - -",
                "- - - - - - - -",
                "- - - - - - - -",
                "- - - - - - - -",
                "- - - - - - - -",
                "K - - - B - - -")
                .build();

        final int value = FUNCTION.calculateOneSidedValue(board);
        final int moves = 7;
        assertEquals(LIGHT_PIECE_MOVE_VALUE * moves, value);
    }

    @Test
    public void testOneBishopAndOponentPawns() {
        final BoardEngine board = BoardBuilder.of(
                "- - - - - - - k",
                "- - - - - - - -",
                "- p - - - - - -",
                "b - - - - - - -",
                "- - - p - - - p",
                "- - - - p - - -",
                "- - - - - - - -",
                "- - - - B - - K")
                .build();

        final int value = FUNCTION.calculateOneSidedValue(board);
        final int allowedMoves = 2;
        assertEquals(LIGHT_PIECE_MOVE_VALUE * allowedMoves, value);
    }
}
