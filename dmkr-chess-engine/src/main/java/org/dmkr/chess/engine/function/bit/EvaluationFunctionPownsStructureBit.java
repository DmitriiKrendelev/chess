package org.dmkr.chess.engine.function.bit;

import static org.dmkr.chess.api.model.Constants.VALUE_POWN;
import static org.dmkr.chess.api.utils.BitBoardMasks.NOT_A;
import static org.dmkr.chess.api.utils.BitBoardMasks.NOT_H;
import static org.dmkr.chess.api.model.Constants.SIZE;
import static java.lang.Long.bitCount;
import static org.dmkr.chess.api.utils.BitBoardMasks.FILES;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.engine.function.EvaluationFunction;
import org.dmkr.chess.engine.function.common.EvaluationFunctionPownStructureAbstract;

public class EvaluationFunctionPownsStructureBit extends EvaluationFunctionPownStructureAbstract<BitBoard> {
	
	public static final EvaluationFunction<BitBoard> INSTANCE = new EvaluationFunctionPownsStructureBit();
	
	
	@Override
	public int value(BitBoard board) {
		int result = 0;
		final long powns = board.items(VALUE_POWN);
		final long pownsOponent = board.oponentItems(VALUE_POWN);

		// pown chains
		final long pownChainsLeft = powns & ((powns & NOT_A) >>> (SIZE - 1));
		final long pownChainsRght = powns & ((powns & NOT_H) >>> (SIZE + 1));
		final long pownChainsLeftOponent = pownsOponent & ((pownsOponent & NOT_A) << (SIZE + 1));
		final long pownChainsRghtOponent = pownsOponent & ((pownsOponent & NOT_H) << (SIZE - 1));
		result += bitCount(pownChainsLeft) * POWNS_CHAIN_VALUE;
		result += bitCount(pownChainsRght) * POWNS_CHAIN_VALUE;
		result -= bitCount(pownChainsLeftOponent) * POWNS_CHAIN_VALUE;
		result -= bitCount(pownChainsRghtOponent) * POWNS_CHAIN_VALUE;
		
		final byte[] numPownsOnFiles = new byte[SIZE];
		final byte[] numPownsOnFilesOponent = new byte[SIZE];
		
		for (int i = 0; i < SIZE; i ++) {
			numPownsOnFiles[i] = (byte) bitCount(powns & FILES[i]);
			numPownsOnFilesOponent[i] = (byte) bitCount(pownsOponent & FILES[i]);
		}
		
		result += isolatedAndDoubledPowns(numPownsOnFiles, numPownsOnFilesOponent);
		result -= isolatedAndDoubledPowns(numPownsOnFilesOponent, numPownsOnFiles);
		
		return result;
	}
}
