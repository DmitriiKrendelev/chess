package org.dmkr.chess.engine.function.impl;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.function.EvaluationFunction;
import org.dmkr.chess.engine.function.common.EvaluationFunctionPawnStructureAbstract;

import static org.dmkr.chess.api.model.Constants.SIZE;
import static org.dmkr.chess.api.model.Constants.VALUE_PAWN;
import static org.dmkr.chess.api.utils.BoardUtils.getX;

public class EvaluationFunctionPawnsStructure extends EvaluationFunctionPawnStructureAbstract<BoardEngine> {
	
	public static final EvaluationFunction<BoardEngine> INSTANCE = new EvaluationFunctionPawnsStructure();

	@Override
	public int calculateOneSidedValue(BoardEngine board) {
		int result = 0;

		final byte[] pawnOnFiles = new byte[SIZE];
		for (int i = SIZE; i < SIZE * (SIZE - 1); i ++) {
			final int x = getX(i);
			switch (board.at(i)) {
				case VALUE_PAWN :
					pawnOnFiles[x] += 1;
					// pawn chains
					if (i >= SIZE * 2) {
						// left-down
						if (x != 0 && board.at(i - SIZE - 1) == VALUE_PAWN) {
							result += PAWNS_CHAIN_VALUE;
						}
						// rght-down
						if (x != 7 && board.at(i - SIZE + 1) == VALUE_PAWN) {
							result += PAWNS_CHAIN_VALUE;
						}
					}
					continue;
					
				default :
					continue;
			}
		}
		
		result += isolatedAndDoubledPawns(pawnOnFiles);
		
		return result;
	}
}
