package org.dmkr.chess.engine.function;

import static org.dmkr.chess.api.model.Constants.VALUE_BISHOP;
import static org.dmkr.chess.api.model.Constants.VALUE_KING;
import static org.dmkr.chess.api.model.Constants.VALUE_KNIGHT;
import static org.dmkr.chess.api.model.Constants.VALUE_POWN;
import static org.dmkr.chess.api.model.Constants.VALUE_QUEEN;
import static org.dmkr.chess.api.model.Constants.VALUE_ROOK;

import org.dmkr.chess.api.utils.BoardUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ItemPositionValuesProvider {
	private static final int[] POWN_POSITION_VALUES = BoardUtils.toBoardArray(
				" 0,  0,  0,  0,  0,  0,  0,  0", 
				"50, 50, 50, 50, 50, 50, 50, 50", 
				"10, 10, 20, 30, 30, 20, 10, 10", 
				" 5,  5, 10, 25, 25, 10,  5,  5", 
				" 0,  0,  0, 25, 25,  0,  0,  0",
				" 5, -5,  5,  0,  0,  5, -5,  5",
				" 5, 10, 10,-20,-20, 10, 10,  5", 
				" 0,  0,  0,  0,  0,  0,  0,  0");
	
	private static final int[] KNIGHT_POSITION_VALUES = BoardUtils.toBoardArray(
				"-40,-30,-20,-20,-20,-20,-30,-40",
				"-30,-10,  0,  0,  0,  0,-10,-30",
				"-20,  0, 10, 15, 15, 10,  0,-20",
				"-20,  5, 15, 20, 20, 15,  5,-20",
				"-20,  0, 15, 20, 20, 15,  0,-20",
				"-20,  5, 10, 15, 15, 10,  5,-20",
				"-30,-10,  0,  5,  5,  0,-10,-30",
				"-40,-30,-20,-20,-20,-20,-30,-40");
		
	private static final int[] BISHOP_POSITION_VALUES = BoardUtils.toBoardArray(
				"-20,-10,-10,-10,-10,-10,-10,-20",
				"-10,  0,  0,  0,  0,  0,  0,-10",
				"-10,  0,  5, 10, 10,  5,  0,-10",
				"-10,  5,  5, 10, 10,  5,  5,-10",
				"-10,  0, 10, 10, 10, 10,  0,-10",
				"-10, 10, 10, 10, 10, 10, 10,-10",
				"-10,  5,  0,  0,  0,  0,  5,-10",
				"-20,-10,-10,-10,-10,-10,-10,-20");
	
	private static final int[] ROOK_POSITION_VALUES = BoardUtils.toBoardArray(
				" 0,  0,  0,  0,  0,  0,  0,  0",
				" 5, 10, 10, 10, 10, 10, 10,  5",
				"-5,  0,  0,  0,  0,  0,  0, -5",
				"-5,  0,  0,  0,  0,  0,  0, -5",
				"-5,  0,  0,  0,  0,  0,  0, -5",
				"-5,  0,  0,  0,  0,  0,  0, -5",
				"-5,  0,  0,  0,  0,  0,  0, -5",
				" 0,  0,  0,  5,  5,  0,  0,  0");
	
	private static final int[] QUEEN_POSITION_VALUES = BoardUtils.toBoardArray(
				"-20,-10,-10, -5, -5,-10,-10,-20",
				"-10,  0,  0,  0,  0,  0,  0,-10",
				"-10,  0,  5,  5,  5,  5,  0,-10",
				" -5,  0,  5,  5,  5,  5,  0, -5",
				"  0,  0,  5,  5,  5,  5,  0, -5",
				"-10,  5,  5,  5,  5,  5,  0,-10",
				"-10,  0,  5,  0,  0,  0,  0,-10",
				"-20,-10,-10, -5, -5,-10,-10,-20");
	
	private static final int[] KING_POSITION_VALUES = BoardUtils.toBoardArray(
				"-30,-40,-40,-50,-50,-40,-40,-30",
				"-30,-40,-40,-50,-50,-40,-40,-30",
				"-30,-40,-40,-50,-50,-40,-40,-30",
				"-30,-40,-40,-50,-50,-40,-40,-30",
				"-20,-30,-30,-40,-40,-30,-30,-20",
				"-10,-20,-20,-20,-20,-20,-20,-10",
				" 20, 20,  0,  0,  0,  0, 20, 20",
				" 20, 30, 10,  0,  0, 10, 30, 20");
	
	private static final int[][] POSITION_VALUES;
		
	static {
		POSITION_VALUES = new int[7][];
		POSITION_VALUES[VALUE_POWN] = POWN_POSITION_VALUES;
		POSITION_VALUES[VALUE_KNIGHT] = KNIGHT_POSITION_VALUES;
		POSITION_VALUES[VALUE_BISHOP] = BISHOP_POSITION_VALUES;
		POSITION_VALUES[VALUE_ROOK] = ROOK_POSITION_VALUES;
		POSITION_VALUES[VALUE_QUEEN] = QUEEN_POSITION_VALUES;
		POSITION_VALUES[VALUE_KING] = KING_POSITION_VALUES;
	}
	
	public static int[] positionValues(int itemType) {
		return POSITION_VALUES[itemType];
	}

}
