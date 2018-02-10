package org.dmkr.chess.engine.function.common;

import static org.dmkr.chess.api.model.Constants.SIZE;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.function.EvaluationFunction;

public abstract class EvaluationFunctionPownStructureAbstract<T extends BoardEngine> extends EvaluationFunctionBasedBoardInversion<T> {
	protected static final int POWNS_CHAIN_VALUE = 4;
	protected static final int ISOLATED_POWN_VALUE_SINGLE = -20;
	protected static final int ISOLATED_POWN_VALUE_DOUBLE = -40;
	protected static final int ISOLATED_POWN_VALUE_MULTIPLE = -60;
	protected static final int POWN_VALUE_SINGLE = 0;
	protected static final int POWN_VALUE_DOUBLE = -10;
	protected static final int POWN_VALUE_MULTIPLE = -30;	
	
	
	protected int isolatedAndDoubledPowns(byte[] numPownsOnFiles) {
		int result = 0;
		
		for (int i = 0; i < SIZE; i ++) {
			if (numPownsOnFiles[i] == 0) {
				continue;
			}
			
			final boolean leftSupport = i != 0 && numPownsOnFiles[i - 1] != 0; 
			final boolean rghtSupport = i != 7 && numPownsOnFiles[i + 1] != 0; 
			
			final boolean isolated = !leftSupport && !rghtSupport;
			
			if (isolated) {
				switch (numPownsOnFiles[i]) {
					case 1:
						result += ISOLATED_POWN_VALUE_SINGLE;
						break;
					case 2:
						result += ISOLATED_POWN_VALUE_DOUBLE;
						break;	
					default:
						result += ISOLATED_POWN_VALUE_MULTIPLE;
				}
			} else {
				switch (numPownsOnFiles[i]) {
					case 1:
						result += POWN_VALUE_SINGLE;
						break;
					case 2:
						result += POWN_VALUE_DOUBLE;
						break;	
					default:
						result += POWN_VALUE_MULTIPLE;
				}
			}
		}
		
		return result;
	}
	
	
	@Override
	public String toString() {
		return "Powns Structure";
	} 
}
