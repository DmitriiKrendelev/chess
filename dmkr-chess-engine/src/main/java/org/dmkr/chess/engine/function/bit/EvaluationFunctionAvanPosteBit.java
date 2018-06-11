package org.dmkr.chess.engine.function.bit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.engine.function.EvaluationFunction;
import org.dmkr.chess.engine.function.common.EvaluationFunctionAvanPosteAbstract;

import static java.lang.Long.numberOfTrailingZeros;
import static org.dmkr.chess.api.model.Constants.*;
import static org.dmkr.chess.api.utils.BitBoardMasks.*;
import static org.dmkr.chess.api.utils.BitBoardMasks.BOARD_FIELDS_INVERTED;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EvaluationFunctionAvanPosteBit extends EvaluationFunctionAvanPosteAbstract<BitBoard> {
    public static final EvaluationFunction<BitBoard> INSTANCE = new EvaluationFunctionAvanPosteBit();

    @Override
    public int calculateOneSidedValue(BitBoard board) {
        final long pawns = board.pieces(VALUE_PAWN);
        final long pawnAtacksLeft = (pawns & NOT_A) << (SIZE + 1);
        final long pawnAtacksRght = (pawns & NOT_H) << (SIZE - 1);

        long avanPostesCandidates =
                (board.pieces(VALUE_KNIGHT) | board.pieces(VALUE_BISHOP)) & (pawnAtacksLeft | pawnAtacksRght);

        if (avanPostesCandidates == 0) {
            return 0;
        }

        final long oponentPawns = board.oponentPieces(VALUE_PAWN);
        int result = 0;
        while (avanPostesCandidates != 0L) {
            final int avanPosteBitIndex = numberOfTrailingZeros(avanPostesCandidates);
            final int avanPosteIndex = BOARD_INDEX_TO_LONG_INDEX[avanPosteBitIndex];
            if ((AVANT_POSTE_OPONENT_PAWN_ATACKS[avanPosteIndex] & oponentPawns) == 0) {
                result += AVAN_POST_VALUE;
            }

            avanPostesCandidates &= BOARD_FIELDS_INVERTED[avanPosteIndex];
        }

        return result;
    }
}
