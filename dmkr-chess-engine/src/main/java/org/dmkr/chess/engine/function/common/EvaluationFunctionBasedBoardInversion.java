package org.dmkr.chess.engine.function.common;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.function.EvaluationFunction;
import org.dmkr.chess.engine.function.EvaluationFunctionUtils;

import java.util.*;

public abstract class EvaluationFunctionBasedBoardInversion<T extends BoardEngine> implements EvaluationFunction<T> {

    @Override
    public int value(T board) {
        int result = 0;

        result += calculateOneSidedValue(board);
        board.invert();
        result -= calculateOneSidedValue(board);
        board.invert();

        return result;
    }

    public abstract int calculateOneSidedValue(T board);
}

