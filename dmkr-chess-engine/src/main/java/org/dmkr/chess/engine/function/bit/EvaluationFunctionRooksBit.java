package org.dmkr.chess.engine.function.bit;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.engine.function.EvaluationFunction;
import org.dmkr.chess.engine.function.common.EvaluationFunctionRooksAbstract;

import static java.lang.Long.*;
import static org.dmkr.chess.api.model.Constants.SIZE;
import static org.dmkr.chess.api.model.Constants.VALUE_PAWN;
import static org.dmkr.chess.api.utils.BitBoardMasks.*;
import static org.dmkr.chess.api.model.Constants.VALUE_ROOK;
import static org.dmkr.chess.api.utils.BitBoardUtils.bitCountOfZeroble;

public class EvaluationFunctionRooksBit extends EvaluationFunctionRooksAbstract<BitBoard> {
    public static final EvaluationFunction<BitBoard> INSTANCE = new EvaluationFunctionRooksBit();

    private static final long ROOKS_BASE_FIELDS = LINE_1 | LINE_2;

    @Override
    public int calculateOneSidedValue(BitBoard board) {
        final long rooks = board.pieces(VALUE_ROOK);
        if ((rooks & ROOKS_BASE_FIELDS) == 0L) {
            return 0;
        }

        int result = 0;

        final long piecePositionsOponent = board.piecePositionsOponent() & NOT_1;
        for (long file : FILES) {
            final long rooksOnFile = rooks & file;

            if (rooksOnFile == 0) {
                continue;
            }
            if ((board.pieces(VALUE_PAWN) & file) != 0) {
                continue;
            }
            if ((rooksOnFile & ROOKS_BASE_FIELDS) == 0L) {
                continue;
            }

            final boolean battery = bitCountOfZeroble(rooksOnFile) > 1;
            final boolean blocked;
            final long piecePositionsOponentOnFile = piecePositionsOponent & file;
            if (piecePositionsOponentOnFile == 0) {
                blocked = false;
            } else {
                final int lowestOponentPieceOnFile = BOARD_INDEX_TO_LONG_INDEX[numberOfTrailingZeros(piecePositionsOponentOnFile)];
                blocked = (PAWN_ATACKS[lowestOponentPieceOnFile] & board.oponentPieces(VALUE_PAWN)) != 0;
            }

            if (blocked) {
                result += battery ? BATTERY_ROOKS_BLOCKED_VALUE : OPEN_FILE_BLOCKED_VALUE;
            } else {
                result += battery ? BATTERY_ROOKS_VALUE : OPEN_FILE_VALUE;
            }
        }

        return result;
    }
}
