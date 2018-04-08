package org.dmkr.chess.engine.function.common;

import static org.dmkr.chess.api.model.Constants.SIZE;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.function.EvaluationFunction;

public abstract class EvaluationFunctionPawnStructureAbstract<T extends BoardEngine> extends EvaluationFunctionBasedBoardInversion<T> {
	protected static final int PAWNS_CHAIN_VALUE = 4;
	protected static final int ISOLATED_PAWN_VALUE_SINGLE = -20;
	protected static final int ISOLATED_PAWN_VALUE_DOUBLE = -40;
	protected static final int ISOLATED_PAWN_VALUE_MULTIPLE = -60;
	protected static final int PAWN_VALUE_SINGLE = 0;
	protected static final int PAWN_VALUE_DOUBLE = -10;
	protected static final int PAWN_VALUE_MULTIPLE = -30;
	
	
	protected int isolatedAndDoubledPawns(byte[] numPawnsOnFiles) {
		int result = 0;
		
		for (int i = 0; i < SIZE; i ++) {
			if (numPawnsOnFiles[i] == 0) {
				continue;
			}
			
			final boolean leftSupport = i != 0 && numPawnsOnFiles[i - 1] != 0;
			final boolean rghtSupport = i != 7 && numPawnsOnFiles[i + 1] != 0;
			
			final boolean isolated = !leftSupport && !rghtSupport;
			
			if (isolated) {
				switch (numPawnsOnFiles[i]) {
					case 1:
						result += ISOLATED_PAWN_VALUE_SINGLE;
						break;
					case 2:
						result += ISOLATED_PAWN_VALUE_DOUBLE;
						break;	
					default:
						result += ISOLATED_PAWN_VALUE_MULTIPLE;
				}
			} else {
				switch (numPawnsOnFiles[i]) {
					case 1:
						result += PAWN_VALUE_SINGLE;
						break;
					case 2:
						result += PAWN_VALUE_DOUBLE;
						break;	
					default:
						result += PAWN_VALUE_MULTIPLE;
				}
			}
		}
		
		return result;
	}
	
	
	@Override
	public String toString() {
		return "Pawns Structure";
	} 
}
