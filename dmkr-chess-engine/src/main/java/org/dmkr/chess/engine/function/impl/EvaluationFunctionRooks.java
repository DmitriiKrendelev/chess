package org.dmkr.chess.engine.function.impl;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.function.EvaluationFunction;
import org.dmkr.chess.engine.function.common.EvaluationFunctionRooksAbstract;

import static org.dmkr.chess.api.model.Constants.*;

public class EvaluationFunctionRooks extends EvaluationFunctionRooksAbstract<BoardEngine> {
    public static final EvaluationFunction<BoardEngine> INSTANCE = new EvaluationFunctionRooks();

    @Override
    public int calculateOneSidedValue(BoardEngine board) {
        int result = 0;
        for (int i = 0; i < SIZE; i ++) {
            if (board.at(i) == VALUE_ROOK) {
                result += valueOfRookOnFile(i, board);
            } else if (board.at(i + SIZE) == VALUE_ROOK) {
                result += valueOfRookOnFile(i + SIZE, board);
            }
        }

        return result;
    }

    private int valueOfRookOnFile(int rookIndex, BoardEngine board) {
        boolean battery = false;
        boolean blocked = false;
        boolean blockerFound = false;
        for (int i = rookIndex + SIZE; i < SIZE * SIZE; i += SIZE) {
            final byte item = board.at(i);

            if (item == VALUE_EMPTY) {
                continue;
            } else if (item == VALUE_POWN) {
                return 0;
            } else if (item == VALUE_ROOK) {
                battery = true;
            } else if (item > 0) {
                continue;
            } else if (item < 0) {
                if (!blockerFound) {
                    blocked = i < SIZE * (SIZE - 1) && (((i & 7) != 0 && board.at(i + SIZE - 1) == -VALUE_POWN) || ((i & 7) != 7 && board.at(i + SIZE + 1) == -VALUE_POWN));
                    blockerFound = true;
                }
            }
        }

        if (blocked) {
            return battery ? BATTERY_ROOKS_BLOCKED_VALUE : OPEN_FILE_BLOCKED_VALUE;
        } else {
            return battery ? BATTERY_ROOKS_VALUE : OPEN_FILE_VALUE;
        }
    }
}
