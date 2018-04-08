package org.dmkr.chess.engine.function.impl;

import static org.dmkr.chess.api.model.Constants.SIZE;
import static org.dmkr.chess.api.model.Constants.VALUE_EMPTY;
import static org.dmkr.chess.engine.function.PieceValuesProvider.valueOf;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.function.EvaluationFunction;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EvaluationFunctionPiecesValues implements EvaluationFunction<BoardEngine> {
	
	public static final EvaluationFunction<BoardEngine> INSTANCE = new EvaluationFunctionPiecesValues();
	
	@Override
	public int value(BoardEngine board) {
	    int result = 0;
		
		for (int i = 0; i < SIZE * SIZE; i ++) {
			final byte piece = board.at(i);
			
			if (piece != VALUE_EMPTY)
				result += valueOf(piece);
		}
		
		return result;
	}

	@Override
	public String toString() {
		return "Pieces";
	}
}
