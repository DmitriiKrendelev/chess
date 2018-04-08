package org.dmkr.chess.engine.function.bit;

import static org.dmkr.chess.api.model.Constants.NUMBER_OF_PIECES;
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
			final long pieces = board.pieces(pieceType);
			result += doWithUpBits(pieces, field -> positionValues(pieceType)[field]);
			
			final long oponentPieces = board.oponentPieces(pieceType);
			result -= doWithUpBits(oponentPieces, field -> positionValues(pieceType)[invertIndex(field)]);
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		return "Position";
	}
}
