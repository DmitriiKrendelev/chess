package org.dmkr.chess.api.utils;

import static java.util.stream.IntStream.range;
import static org.dmkr.chess.api.model.Constants.SIZE;
import static org.dmkr.chess.api.utils.BoardUtils.getX;
import static org.dmkr.chess.api.utils.BoardUtils.getY;
import static org.dmkr.chess.api.utils.BoardUtils.index;
import static org.dmkr.chess.api.utils.ItemGoesFunctionsBit.ItemGoesFunctionBit.GO_DOWN;
import static org.dmkr.chess.api.utils.ItemGoesFunctionsBit.ItemGoesFunctionBit.GO_DOWN_LEFT;
import static org.dmkr.chess.api.utils.ItemGoesFunctionsBit.ItemGoesFunctionBit.GO_DOWN_RIGHT;
import static org.dmkr.chess.api.utils.ItemGoesFunctionsBit.ItemGoesFunctionBit.GO_LEFT;
import static org.dmkr.chess.api.utils.ItemGoesFunctionsBit.ItemGoesFunctionBit.GO_RIGHT;
import static org.dmkr.chess.api.utils.ItemGoesFunctionsBit.ItemGoesFunctionBit.GO_UP;
import static org.dmkr.chess.api.utils.ItemGoesFunctionsBit.ItemGoesFunctionBit.GO_UP_LEFT;
import static org.dmkr.chess.api.utils.ItemGoesFunctionsBit.ItemGoesFunctionBit.GO_UP_RIGHT;

import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;
import java.util.stream.IntStream;

import org.dmkr.chess.api.utils.ItemGoesFunctionsBit.ItemGoesFunctionBit;

public class BitBoardMasks {
	public static final long LINE_1 = 255L;
	public static final long LINE_2 = LINE_1 << SIZE;
	public static final long LINE_3 = LINE_2 << SIZE;
	public static final long LINE_4 = LINE_3 << SIZE;
	public static final long LINE_5 = LINE_4 << SIZE;
	public static final long LINE_6 = LINE_5 << SIZE;
	public static final long LINE_7 = LINE_6 << SIZE;
	public static final long LINE_8 = LINE_7 << SIZE;
	
	public static final long FILE_A = upBits(7, 15, 23, 31, 39, 47, 55, 63);
	public static final long FILE_B = FILE_A >>> 1;
	public static final long FILE_C = FILE_A >>> 2;
	public static final long FILE_D = FILE_A >>> 3;
	public static final long FILE_E = FILE_A >>> 4;
	public static final long FILE_F = FILE_A >>> 5;
	public static final long FILE_G = FILE_A >>> 6;
	public static final long FILE_H = FILE_A >>> 7;
	
	public static final long NOT_A = ~FILE_A;
	public static final long NOT_H = ~FILE_H;
	public static final long NOT_1 = ~LINE_1;
	public static final long NOT_8 = ~LINE_8;
	
	public static final long NOT_AB = ~upBits(6, 7, 14, 15, 22, 23, 30, 31, 38, 39, 46, 47, 54, 55, 62, 63);
	public static final long NOT_GH = ~upBits(0, 1, 8, 9, 16, 17, 24, 25, 32, 33, 40, 41, 48, 49, 56, 57);
	public static final long NOT_12 = ~LINE_1 & ~LINE_2;
	public static final long NOT_78 = ~LINE_7 & ~LINE_8;
	
	public static final long EMPTY_FOR_CASTELING_LEFT_SHORT = upBits(6, 5);
	public static final long EMPTY_FOR_CASTELING_LEFT_LONG = upBits(6, 5, 4);
	public static final long EMPTY_FOR_CASTELING_RGHT_SHORT = upBits(1, 2);
	public static final long EMPTY_FOR_CASTELING_RGHT_LONG = upBits(1, 2, 3);
	
	public static final int[] BOARD_INDEX_TO_LONG_INDEX = range(0, SIZE * SIZE).map(i -> index(SIZE - 1 - getX(i), getY(i))).toArray();
	public static final long[] FILES = {FILE_A, FILE_B, FILE_C, FILE_D, FILE_E, FILE_F, FILE_G, FILE_H};
	
	
	public static final long[] POWN_CAN_GO_TWO_STEPS = IntStream.range(0, SIZE).mapToLong(i -> 1L << (3 * SIZE - 1 - i) | 1L << (4 * SIZE - 1 - i)).toArray();
	public static final long[] BOARD_FIELDS = IntStream.of(BOARD_INDEX_TO_LONG_INDEX).mapToLong(i -> (1L << i)).toArray();
	public static final long[] BOARD_FIELDS_INVERTED = IntStream.of(BOARD_INDEX_TO_LONG_INDEX).mapToLong(i -> ~(1L << i)).toArray();
	public static final long[] KING_ATACKS = boardLongFieldsStream()
			.mapToLong(i -> 
				((1L << (i + SIZE + 1)) & NOT_H & NOT_1) |
				((1L << (i + SIZE)) & NOT_1) |
				((1L << (i + SIZE - 1)) & NOT_A & NOT_1) |
				((1L << (i + 1)) & NOT_H) |
				((1L << (i - 1)) & NOT_A) |
				((1L << (i - SIZE + 1)) & NOT_H & NOT_8) |
				((1L << (i - SIZE)) & NOT_8) |
				((1L << (i - SIZE - 1)) & NOT_A & NOT_8))
			.toArray();   
	
	public static final long[] POWN_ATACKS = boardLongFieldsStream()
			.mapToLong(i -> 
				((1L << (i + SIZE + 1)) & NOT_H) |
				((1L << (i + SIZE - 1)) & NOT_A))
			.toArray();   
	
	public static final long[] KNIGHT_ATACKS = boardLongFieldsStream()
			.mapToLong(i -> 
				((1L << (i + SIZE + SIZE + 1)) & NOT_12 & NOT_H) |
				((1L << (i + SIZE + SIZE - 1)) & NOT_12 & NOT_A) |
				((1L << (i - SIZE - SIZE + 1)) & NOT_78 & NOT_H) |
				((1L << (i - SIZE - SIZE - 1)) & NOT_78 & NOT_A) |
				((1L << (i + SIZE + 1 + 1)) & NOT_1 & NOT_GH) |
				((1L << (i + SIZE - 1 - 1)) & NOT_1 & NOT_AB) |
				((1L << (i - SIZE - 1 - 1)) & NOT_8 & NOT_AB) |
				((1L << (i - SIZE + 1 + 1)) & NOT_8 & NOT_GH))
			.toArray();   
	
	public static final long[] BISHOP_ATACKS = boardLongFieldsStream()
			.mapToLong(i -> 
				toFieldsLong(i, GO_UP_LEFT) |
				toFieldsLong(i, GO_UP_RIGHT) |
				toFieldsLong(i, GO_DOWN_LEFT) |
				toFieldsLong(i, GO_DOWN_RIGHT)
			)
			.toArray();   
	
	public static final long[] ROOK_ATACKS = boardLongFieldsStream()
			.mapToLong(i -> 
				toFieldsLong(i, GO_UP) |
				toFieldsLong(i, GO_RIGHT) |
				toFieldsLong(i, GO_LEFT) |
				toFieldsLong(i, GO_DOWN)
			)
			.toArray(); 
	
	
	public static long toFieldsLong(int index, ItemGoesFunctionBit itemGoesFunction) {
		long field = 1L << index;
		long result = 0;
		final LongUnaryOperator goFunction = itemGoesFunction.goFunction();
		final LongPredicate stopPredicate = itemGoesFunction.stopPredicate();
		
		while (true) {
			field = goFunction.applyAsLong(field);
			if (stopPredicate.test(field)) {
				break;
			}
			result |= field;
		}
		
		return result;
	}
	
	public static long upBits(int ... bits) {
		long result = 0L;
		for (int bit : bits) {
			result |= 1L << bit;
		}
		return result;
	}
	
	public static IntStream boardLongFieldsStream() {
		return range(0, SIZE * SIZE).map(i -> BOARD_INDEX_TO_LONG_INDEX[i]);
	}
}
