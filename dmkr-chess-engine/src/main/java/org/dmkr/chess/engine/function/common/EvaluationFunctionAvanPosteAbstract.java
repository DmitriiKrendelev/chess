package org.dmkr.chess.engine.function.common;

import org.dmkr.chess.api.BoardEngine;

public abstract class EvaluationFunctionAvanPosteAbstract<T extends BoardEngine> extends EvaluationFunctionBasedBoardInversion<T> {
    public static final int AVAN_POST_VALUE = 50;

    @Override
    public String toString() {
        return "Avan Postes";
    }
}
