package org.dmkr.chess.engine.function.bit;

import static org.dmkr.chess.api.model.Constants.VALUE_PAWN;
import static org.dmkr.chess.api.utils.BitBoardMasks.NOT_A;
import static org.dmkr.chess.api.utils.BitBoardMasks.NOT_H;
import static org.dmkr.chess.api.model.Constants.SIZE;
import static org.dmkr.chess.api.utils.BitBoardMasks.FILES;
import static org.dmkr.chess.api.utils.BitBoardUtils.*;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.engine.function.EvaluationFunction;
import org.dmkr.chess.engine.function.common.EvaluationFunctionPawnStructureAbstract;

public class EvaluationFunctionPawnsStructureBit extends EvaluationFunctionPawnStructureAbstract<BitBoard> {
	
	public static final EvaluationFunction<BitBoard> INSTANCE = new EvaluationFunctionPawnsStructureBit();

	@Override
	public int calculateOneSidedValue(BitBoard board) {
		int result = 0;
		final long pawns = board.pieces(VALUE_PAWN);

		final long pawnChainsLeft = pawns & ((pawns & NOT_A) >>> (SIZE - 1));
		final long pawnChainsRght = pawns & ((pawns & NOT_H) >>> (SIZE + 1));

		result += bitCountOfZeroble(pawnChainsLeft) * PAWNS_CHAIN_VALUE;
		result += bitCountOfZeroble(pawnChainsRght) * PAWNS_CHAIN_VALUE;

		final byte[] numPawnsOnFiles = new byte[SIZE];

		for (int i = 0; i < SIZE; i ++) {
			numPawnsOnFiles[i] = (byte) bitCountOfZeroble(pawns & FILES[i]);
		}

		result += isolatedAndDoubledPawns(numPawnsOnFiles);

		return result;
	}
}
