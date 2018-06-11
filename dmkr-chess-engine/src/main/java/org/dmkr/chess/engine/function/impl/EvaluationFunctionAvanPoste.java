package org.dmkr.chess.engine.function.impl;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.utils.BoardUtils;
import org.dmkr.chess.engine.function.EvaluationFunction;
import org.dmkr.chess.engine.function.bit.EvaluationFunctionAvanPosteBit;
import org.dmkr.chess.engine.function.common.EvaluationFunctionAvanPosteAbstract;

import static org.dmkr.chess.api.model.Constants.*;

public class EvaluationFunctionAvanPoste extends EvaluationFunctionAvanPosteAbstract<BoardEngine> {
    public static final EvaluationFunction<BoardEngine> INSTANCE = new EvaluationFunctionAvanPoste();

    @Override
    public int calculateOneSidedValue(BoardEngine board) {
        int result = 0;
        nextBoardIndex: for (int i = 0; i < SIZE * SIZE; i ++) {
            final byte item = board.at(i);
            if (item != VALUE_KNIGHT && item != VALUE_BISHOP) {
                continue;
            }

            if (isAvanPoste(i, board)) {
                result += AVAN_POST_VALUE;
            }
        }
        return result;
    }

    private boolean isAvanPoste(int i, BoardEngine board) {
        if (!BoardUtils.isFieldUnderPawnDefence(i, board)) {
            return false;
        }

        // if can be attacked by the left pawn
        if ((i & 7) != 0) {
            for (int j = i + SIZE - 1; j < SIZE * SIZE; j += SIZE) {
                if (board.at(j) == -VALUE_PAWN) {
                    return false;
                }
            }
        }

        // if can be attacked by the rght pawn
        if ((i & 7) != 7) {
            for (int j = i + SIZE + 1; j < SIZE * SIZE; j += SIZE) {
                if (board.at(j) == -VALUE_PAWN) {
                    return false;
                }
            }
        }

        return true;
    }
}
