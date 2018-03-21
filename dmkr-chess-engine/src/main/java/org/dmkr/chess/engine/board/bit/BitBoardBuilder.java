package org.dmkr.chess.engine.board.bit;

import static org.dmkr.chess.api.utils.BitBoardMasks.BOARD_FIELDS;
import static org.dmkr.chess.engine.board.bit.BitBoardImpl.ITEMS_LENGTH;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.engine.board.AbstractBoardBuilder;

import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(staticName = "board")
public class BitBoardBuilder extends AbstractBoardBuilder {

	public static BitBoardBuilder of(@NonNull String ... yLines) {
		return of(BitBoardBuilder::new, yLines);
	}
	
	public static BitBoard newInitialPositionBoard() {
		return (BitBoard) newInitialPositionBoard(BitBoardBuilder::new);
	}

	@Override
	public BitBoard build() {
		final long[] items = new long[ITEMS_LENGTH];
		final long[] oponentItems = new long[ITEMS_LENGTH];

		for (int i = 0; i < board.length; i ++) {
			final byte item = board[i];
			
			if (item > 0) {
				items[item - 1] |= BOARD_FIELDS[i];
			} else if (item < 0) {
				oponentItems[-item - 1] |= BOARD_FIELDS[i];
			}
		}
		
		return new BitBoardImpl(items, oponentItems, canCastleLeft, canCastleRght, canOponentCastleLeft, canOponentCastleRght, inverted);
	}
	
}
