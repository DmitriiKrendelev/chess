package org.dmkr.chess.engine.function.bit;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.engine.function.EvaluationFunction;
import org.dmkr.chess.engine.function.common.EvaluationFunctionRooksAbstract;

import static java.lang.Long.*;
import static org.dmkr.chess.api.model.Constants.SIZE;
import static org.dmkr.chess.api.model.Constants.VALUE_POWN;
import static org.dmkr.chess.api.utils.BitBoardMasks.*;
import static org.dmkr.chess.api.model.Constants.VALUE_ROOK;

public class EvaluationFunctionRooksBit extends EvaluationFunctionRooksAbstract<BitBoard> {
    public static final EvaluationFunction<BitBoard> INSTANCE = new EvaluationFunctionRooksBit();

    private static final long ROOKS_BASE_FIELDS = LINE_1 | LINE_2;

    @Override
    public int calculateOneSidedValue(BitBoard board) {
        final long rooks = board.items(VALUE_ROOK);
        if ((rooks & ROOKS_BASE_FIELDS) == 0L) {
            return 0;
        }

        int result = 0;

        final long itemPositionsOponent = board.itemPositionsOponent() & NOT_1;
        for (long file : FILES) {
            final long rooksOnFile = rooks & file;

            if (rooksOnFile == 0) {
                continue;
            }
            if ((board.items(VALUE_POWN) & file) != 0) {
                continue;
            }
            if ((rooksOnFile & ROOKS_BASE_FIELDS) == 0L) {
                continue;
            }

            final boolean battery = bitCount(rooksOnFile) > 1;
            final boolean blocked;
            final long itemPositionsOponentOnFile = itemPositionsOponent & file;
            if (itemPositionsOponentOnFile == 0) {
                blocked = false;
            } else {
                final int lowestOponentItemOnFile = BOARD_INDEX_TO_LONG_INDEX[numberOfTrailingZeros(itemPositionsOponentOnFile)];
                blocked = (POWN_ATACKS[lowestOponentItemOnFile] & board.oponentItems(VALUE_POWN)) != 0;
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
