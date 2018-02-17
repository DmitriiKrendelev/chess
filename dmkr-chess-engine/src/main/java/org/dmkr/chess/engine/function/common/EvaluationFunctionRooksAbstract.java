package org.dmkr.chess.engine.function.common;

import org.dmkr.chess.api.BoardEngine;

public abstract class EvaluationFunctionRooksAbstract<T extends BoardEngine> extends EvaluationFunctionBasedBoardInversion<T> {
    public static final int LINKED_ROOKS_VALUE = 15;
    public static final int BATTERY_ROOKS_VALUE = 40;
    public static final int OPEN_FILE_VALUE = 20;

    public static final int BATTERY_ROOKS_BLOCKED_VALUE = 20;
    public static final int OPEN_FILE_BLOCKED_VALUE = 10;

    @Override
    public String toString() {
        return "Rooks";
    }
}
