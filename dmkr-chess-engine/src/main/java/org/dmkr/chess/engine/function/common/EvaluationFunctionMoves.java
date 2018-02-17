package org.dmkr.chess.engine.function.common;


import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.MovesSelector;
import org.dmkr.chess.engine.function.EvaluationFunction;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.dmkr.chess.api.model.Constants.*;
import static org.dmkr.chess.common.primitives.Bytes.byte1;
import static org.dmkr.chess.common.primitives.Bytes.byte3;
import static org.dmkr.chess.engine.function.ItemValuesProvider.valueOf;

import static org.dmkr.chess.engine.board.impl.MovesSelectorImpl.movesSelector;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EvaluationFunctionMoves extends EvaluationFunctionBasedBoardInversion<BoardEngine> {
	public static final EvaluationFunction<? extends BoardEngine> INSTANCE = new EvaluationFunctionMoves();

	private static final int TREASHOLD = 100;
	private static final byte[] STRONG_ITEMS = {
			VALUE_KNIGHT,
			VALUE_BISHOP,
			VALUE_ROOK,
			VALUE_QUEEN
		};

	public static final MovesSelector MOVES_SELECTOR = movesSelector()
			.checkKingUnderAtack(false)
			.itemsToSelect(STRONG_ITEMS)
			.build();

    @Override
	public int calculateOneSidedValue(BoardEngine board) {
    	final int[] moves = board.calculateAllowedMoves(MOVES_SELECTOR);
		return valueOfAtacks(moves, board) + valueOfNumberPotentialMoves(moves, board);
    }

	public static int valueOfAtacks(int moves[], BoardEngine board) {
		int checks = 0;
		int valuableAtack = 0;

		for (int move : moves) {
			final byte captured = byte3(move);

			if (captured == VALUE_EMPTY) {
				continue;
			} else if (captured == VALUE_KING) {
				checks ++;
			} else {
				final byte item = board.at(byte1(move));
				final int itemValue = valueOf(item);
				final int capturedValue = valueOf(captured);

				if (capturedValue - itemValue > TREASHOLD) {
					valuableAtack ++;
				}
			}
		}

		final int totalAtacks = checks + valuableAtack;
		if (totalAtacks == 0) {
			return 0;
		} else if (totalAtacks == 1) {
			return 20;
		} else {
			return 200;
		}
	}
	
	private static int valueOfNumberPotentialMoves(int[] moves, BoardEngine board) {
		int result = 0;
		for (int move : moves) {
			final byte item = board.at(byte1(move));
			result += item == VALUE_QUEEN ? 2 : 4;
		}
		return result;
	}
	
	@Override
	public String toString() {
		return "Moves";
	}

}
