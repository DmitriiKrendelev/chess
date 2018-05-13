package org.dmkr.chess.engine.function.bit;

import static java.lang.Long.numberOfTrailingZeros;
import static org.dmkr.chess.api.model.Constants.NUMBER_OF_PIECES;
import static org.dmkr.chess.api.utils.BitBoardMasks.BOARD_FIELDS_INVERTED;
import static org.dmkr.chess.api.utils.BitBoardMasks.BOARD_INDEX_TO_LONG_INDEX;
import static org.dmkr.chess.api.utils.BitBoardUtils.doWithUpBits;
import static org.dmkr.chess.api.utils.BoardUtils.invertIndex;
import static org.dmkr.chess.engine.function.PiecePositionValuesProvider.positionValues;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.engine.function.EvaluationFunction;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EvaluationFunctionPiecesPositionsBit implements EvaluationFunction<BitBoard> {
	
	public static final EvaluationFunction<BitBoard> INSTANCE = new EvaluationFunctionPiecesPositionsBit();
	
	@Override
	public int value(BitBoard board) {
		int result = 0;
		
		for (int i = 0; i < NUMBER_OF_PIECES; i ++) {
			final byte pieceType = (byte) (i + 1);

			long pieces = board.pieces(pieceType);
			while (pieces != 0L) {
				final int piecesLastBit = numberOfTrailingZeros(pieces);
				final int piecesBoardIndex = BOARD_INDEX_TO_LONG_INDEX[piecesLastBit];
				result += positionValues(pieceType)[piecesBoardIndex];
				pieces &= BOARD_FIELDS_INVERTED[piecesBoardIndex];
			}

			long oponentPieces = board.oponentPieces(pieceType);
			while (oponentPieces != 0L) {
				final int oponentPiecesLastBit = numberOfTrailingZeros(oponentPieces);
				final int oponentPiecesBoardIndex = BOARD_INDEX_TO_LONG_INDEX[oponentPiecesLastBit];
				result -= positionValues(pieceType)[invertIndex(oponentPiecesBoardIndex)];
				oponentPieces &= BOARD_FIELDS_INVERTED[oponentPiecesBoardIndex];
			}
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		return "Position";
	}
}
