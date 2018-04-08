package org.dmkr.chess.engine.board.impl;

import static com.google.common.base.MoreObjects.firstNonNull;
import static org.dmkr.chess.api.model.Constants.NUMBER_OF_PIECES;
import static org.dmkr.chess.api.model.Constants.VALUE_BISHOP;
import static org.dmkr.chess.api.model.Constants.VALUE_KING;
import static org.dmkr.chess.api.model.Constants.VALUE_KNIGHT;
import static org.dmkr.chess.api.model.Constants.VALUE_PAWN;
import static org.dmkr.chess.api.model.Constants.VALUE_QUEEN;
import static org.dmkr.chess.api.model.Constants.VALUE_ROOK;

import org.dmkr.chess.api.MovesSelector;

import lombok.Builder;
import lombok.Getter;

public class MovesSelectorImpl implements MovesSelector {
	@Getter
	private final boolean checkKingUnderAtack;
	private final boolean[] piecesToSelect;
    @Getter
	private final boolean skipEnPassenMoves;

	private final static byte[] PIECES = {
			VALUE_PAWN,
			VALUE_KNIGHT,
			VALUE_BISHOP,
			VALUE_ROOK,
			VALUE_QUEEN,
			VALUE_KING
	};
	
	public static final MovesSelector DEFAULT = movesSelector()
                    .checkKingUnderAtack(true)
                    .skipEnPassenMoves(false)
                    .piecesToSelect(PIECES)
                    .build();

	public static final MovesSelector ALLOW_CHECK_MOVES_SELECTOR = movesSelector()
                    .checkKingUnderAtack(false)
                    .skipEnPassenMoves(false)
                    .piecesToSelect(PIECES)
                    .build();

	public static MovesSelectorImpl.MovesSelectorImplBuilder movesSelector() {
		return new MovesSelectorImpl.MovesSelectorImplBuilder();
	}

	@Builder(builderMethodName = "movesSelector")
	private MovesSelectorImpl(boolean checkKingUnderAtack, boolean skipEnPassenMoves, byte ... piecesToSelect) {
		this.checkKingUnderAtack = checkKingUnderAtack;
		this.skipEnPassenMoves = skipEnPassenMoves;

		this.piecesToSelect = new boolean[NUMBER_OF_PIECES + 1];

		for (byte piece : firstNonNull(piecesToSelect, PIECES)) {
			this.piecesToSelect[piece] = true;
		}
	}

	@Override
	public boolean selectMoves(byte piece) {
		return piecesToSelect[piece];
	}

}
