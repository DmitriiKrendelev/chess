package org.dmkr.chess.engine.function.impl;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.function.EvaluationFunction;
import org.dmkr.chess.engine.function.common.EvaluationFunctionPownStructureAbstract;

import static org.dmkr.chess.api.model.Constants.SIZE;
import static org.dmkr.chess.api.model.Constants.VALUE_POWN;
import static org.dmkr.chess.api.utils.BoardUtils.getX;

public class EvaluationFunctionPownsStructure extends EvaluationFunctionPownStructureAbstract<BoardEngine> {
	
	public static final EvaluationFunction<BoardEngine> INSTANCE = new EvaluationFunctionPownsStructure();

	@Override
	public int calculateOneSidedValue(BoardEngine board) {
		int result = 0;

		final byte[] pownOnFiles = new byte[SIZE];
		for (int i = SIZE; i < SIZE * (SIZE - 1); i ++) {
			final int x = getX(i);
			switch (board.at(i)) {
				case VALUE_POWN :
					pownOnFiles[x] += 1;
					// pown chains
					if (i >= SIZE * 2) {
						// left-down
						if (x != 0 && board.at(i - SIZE - 1) == VALUE_POWN) {
							result += POWNS_CHAIN_VALUE;
						}
						// rght-down
						if (x != 7 && board.at(i - SIZE + 1) == VALUE_POWN) {
							result += POWNS_CHAIN_VALUE;
						}
					}
					continue;
					
				default :
					continue;
			}
		}
		
		result += isolatedAndDoubledPowns(pownOnFiles);
		
		return result;
	}
}
