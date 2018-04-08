package org.dmkr.chess.engine.board.bit;

import static org.dmkr.chess.api.utils.BitBoardMasks.BOARD_FIELDS;
import static org.dmkr.chess.engine.board.bit.BitBoardImpl.PIECES_LENGTH;

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
		final long[] pieces = new long[PIECES_LENGTH];
		final long[] oponentPieces = new long[PIECES_LENGTH];

		for (int i = 0; i < board.length; i ++) {
			final byte piece = board[i];
			
			if (piece > 0) {
				pieces[piece - 1] |= BOARD_FIELDS[i];
			} else if (piece < 0) {
				oponentPieces[-piece - 1] |= BOARD_FIELDS[i];
			}
		}
		
		return new BitBoardImpl(pieces, oponentPieces, canCastleLeft, canCastleRght, canOponentCastleLeft, canOponentCastleRght, inverted);
	}
	
}
