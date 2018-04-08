package org.dmkr.chess.engine.function.bit;

import static org.dmkr.chess.api.model.Constants.NUMBER_OF_PIECES;
import static org.dmkr.chess.api.utils.BitBoardUtils.bitCountOfZeroble;
import static org.dmkr.chess.engine.function.PieceValuesProvider.valueOf;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.api.utils.BitBoardUtils;
import org.dmkr.chess.engine.function.EvaluationFunction;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EvaluationFunctionPiecesValuesBit implements EvaluationFunction<BitBoard> {

	public static final EvaluationFunction<BitBoard> INSTANCE = new EvaluationFunctionPiecesValuesBit();
	
	@Override
	public int value(BitBoard board) {
		int result = 0;
		
		for (int i = 0; i < NUMBER_OF_PIECES; i ++) {
			final byte pieceType = (byte) (i + 1);
			final long pieces = board.pieces(pieceType);
			result += bitCountOfZeroble(pieces) * valueOf(pieceType);
			
			final long oponentPieces = board.oponentPieces(pieceType);
			result -= bitCountOfZeroble(oponentPieces) * valueOf(pieceType);
		}
		
		return result;
	}

	@Override
	public String toString() {
		return "Pieces";
	}
}
