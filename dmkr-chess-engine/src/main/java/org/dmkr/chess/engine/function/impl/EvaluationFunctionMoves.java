package org.dmkr.chess.engine.function.impl;

import lombok.RequiredArgsConstructor;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.function.common.EvaluationFunctionMovesAbstract;
import org.dmkr.chess.engine.function.common.EvaluationFunctionNoMoves;

import static org.dmkr.chess.api.model.Constants.*;
import static org.dmkr.chess.api.utils.BitBoardMasks.BOARD_FIELDS;
import static org.dmkr.chess.api.utils.BoardUtils.*;
import static org.dmkr.chess.common.primitives.Bytes.byte1;
import static org.dmkr.chess.common.primitives.Bytes.byte2;
import static org.dmkr.chess.common.primitives.Bytes.byte3;
import static org.dmkr.chess.engine.function.PieceValuesProvider.valueOf;

@RequiredArgsConstructor
public class EvaluationFunctionMoves<T extends BoardEngine> extends EvaluationFunctionMovesAbstract<T> {
    public static final EvaluationFunctionMoves<BoardEngine> INSTANCE = new EvaluationFunctionMoves(true);
    public static final EvaluationFunctionMoves<BoardEngine> INSTANCE_NOT_CHECK_KING_UNDER_ATACKS = new EvaluationFunctionMoves(false);

    private final boolean checkIsKingUnderAtack;

    @Override
    public int calculateOneSidedValue(BoardEngine board) {
        final int[] moves = board.calculateAllowedMoves(MOVES_SELECTOR);

        return valueOfAtacks(moves, board) +
                valueOfNumberPotentialMoves(moves, board) +
                (checkIsKingUnderAtack ? valueOfKingSafeMoves(moves, board) : 0);
    }

    private int valueOfAtacks(int moves[], BoardEngine board) {
        long atackedFields = 0;
        for (int move : moves) {
            final byte captured = byte3(move);

            if (captured == VALUE_EMPTY) {
                continue;
            } else if (captured == VALUE_KING) {
                atackedFields |= BOARD_FIELDS[byte2(move)];
            } else {
                final byte piece = board.at(byte1(move));
                final int pieceValue = valueOf(piece);
                final int capturedValue = valueOf(captured);

                if (capturedValue - pieceValue > TREASHOLD) {
                    atackedFields |= BOARD_FIELDS[byte2(move)];
                }
            }
        }

        return valueOfNumberAtacks(atackedFields);
    }

    private int valueOfNumberPotentialMoves(int[] moves, BoardEngine board) {
        int result = 0;

        final int oponentKingIndex = findOponentKing(board);
        for (int move : moves) {
            final int moveTo = byte2(move);
            final byte piece = board.at(byte1(move));

            if (board.at(moveTo) >= 0 && isNeighbor(moveTo, oponentKingIndex)) {
                switch (piece) {
                    case VALUE_KNIGHT:
                    case VALUE_BISHOP:
                        result += ATACK_NEAR_KING_EMPTY_FIELD_LIGHT;
                        break;
                    case VALUE_ROOK:
                    case VALUE_QUEEN:
                        result += ATACK_NEAR_KING_EMPTY_FIELD_HEAVY;
                        break;
                }
            }

            if (!isFieldUnderPawnAtack(moveTo, board)) {
                switch (piece) {
                    case VALUE_KNIGHT:
                    case VALUE_BISHOP:
                        result += LIGHT_PIECE_MOVE_VALUE;
                        break;
                    case VALUE_ROOK:
                        result += ROOK_MOVE_VALUE;
                        break;
                    case VALUE_QUEEN:
                        result += QUEEN_MOVE_VALUE;
                        break;
                }
            }
        }

        return result;
    }


    private int valueOfKingSafeMoves(int[] moves, BoardEngine board) {
        boolean atLeastOneKingSafeMove = false;
        for (int move : moves) {
            atLeastOneKingSafeMove = atLeastOneKingSafeMove || !board.isKingUnderAtackAfterMove(move);
        }

        return atLeastOneKingSafeMove ? 0 : EvaluationFunctionNoMoves.noMovesEvaluationFunction().value(board);
    }
}
