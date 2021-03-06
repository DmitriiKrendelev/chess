package org.dmkr.chess.engine.function.common;


import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.MovesSelector;
import org.dmkr.chess.api.utils.BitBoardUtils;


import static org.dmkr.chess.api.model.Constants.*;

import static org.dmkr.chess.engine.board.impl.MovesSelectorImpl.movesSelector;

public abstract class EvaluationFunctionMovesAbstract<T extends BoardEngine> extends EvaluationFunctionBasedBoardInversion<T> {
	public static final int LIGHT_PIECE_MOVE_VALUE = 4;
	public static final int ROOK_MOVE_VALUE = 3;
	public static final int QUEEN_MOVE_VALUE = 2;

	public static final int TREASHOLD = 100;
	public static final int ATACK_VALUE = 20;
	public static final int DOUBLE_ATACK_VALUE = 200;
	public static final int MULTIPLE_ATACK_VALUE = 300;

	public static final int ATACK_NEAR_KING_EMPTY_FIELD_LIGHT = 10;
	public static final int ATACK_NEAR_KING_EMPTY_FIELD_HEAVY = 20;

	private static final byte[] STRONG_PIECES = {
			VALUE_PAWN,
			VALUE_KNIGHT,
			VALUE_BISHOP,
			VALUE_ROOK,
			VALUE_QUEEN
		};

	public static final MovesSelector MOVES_SELECTOR = movesSelector()
			.checkKingUnderAtack(false)
			.skipEnPassenMoves(true)
			.piecesToSelect(STRONG_PIECES)
			.build();


	protected int valueOfNumberAtacks(long valuableAtackedFields) {
		final int numberOfAtacks = BitBoardUtils.bitCountOfZeroble(valuableAtackedFields);
		if (numberOfAtacks == 0) {
			return 0;
		} else if (numberOfAtacks == 1) {
			return ATACK_VALUE;
		} else if (numberOfAtacks == 2) {
			return DOUBLE_ATACK_VALUE;
		} else {
			return MULTIPLE_ATACK_VALUE;
		}
	}

	@Override
	public String toString() {
		return "Moves";
	}

}
