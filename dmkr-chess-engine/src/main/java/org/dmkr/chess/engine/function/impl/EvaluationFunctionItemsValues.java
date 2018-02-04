package org.dmkr.chess.engine.function.impl;

import static org.dmkr.chess.api.model.Constants.SIZE;
import static org.dmkr.chess.api.model.Constants.VALUE_EMPTY;
import static org.dmkr.chess.engine.function.ItemValuesProvider.valueOf;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.function.EvaluationFunction;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EvaluationFunctionItemsValues implements EvaluationFunction<BoardEngine> {
	
	public static final EvaluationFunction<BoardEngine> INSTANCE = new EvaluationFunctionItemsValues();
	
	@Override
	public int value(BoardEngine board) {
	    int result = 0;
		
		for (int i = 0; i < SIZE * SIZE; i ++) {
			final byte item = board.at(i);
			
			if (item != VALUE_EMPTY)
				result += valueOf(item);
		}
		
		return result;
	}

	@Override
	public String toString() {
		return "Items";
	}
}
