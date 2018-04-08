package org.dmkr.chess.api.utils;

import lombok.experimental.UtilityClass;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Long.bitCount;
import static org.dmkr.chess.api.model.Constants.SIZE;
import static org.dmkr.chess.api.utils.BitBoardMasks.BOARD_FIELDS_INVERTED;
import static org.dmkr.chess.api.utils.BitBoardMasks.BOARD_INDEX_TO_LONG_INDEX;
import static com.google.common.primitives.Longs.toByteArray;
import static org.dmkr.chess.common.primitives.Bytes.intStream;
import static org.apache.commons.lang3.StringUtils.leftPad;
import static java.lang.Long.numberOfTrailingZeros;
import static java.util.stream.Collectors.joining;

import java.util.function.IntConsumer;
import java.util.function.IntUnaryOperator;

import com.google.common.primitives.Longs;

@UtilityClass
public class BitBoardUtils {

	public static byte fromBinaryString(String bitRow) {
		int bitIndex = 0;
		byte result = 0;
		for (int i = bitRow.length(); i > 0; i --) {
			switch (bitRow.charAt(i - 1)) {
				case '0':
					bitIndex ++;
					break;
				case '1' : 	
					result |= (1 << bitIndex); 
					bitIndex ++;
					break;
				case ' ' :
					break;
				default :
					throw new IllegalArgumentException(bitRow);
			}
		}
		return result;
	}
	
	public static long fromBinaryStrings(String ... bitRows) {
		checkArgument(bitRows.length == SIZE);
		
		final byte[] bytes = new byte[SIZE];
		for (int i = 0; i < SIZE; i ++) {
			bytes[i] = fromBinaryString(bitRows[i]);
		}
		return Longs.fromByteArray(bytes);
	}

	public static String toBinaryString(long board) {
		return intStream(toByteArray(board))
				.mapToObj(b -> b & 0xFF)
				.map(Integer::toBinaryString)
				.map(s -> leftPad(s, SIZE, '0').replace("", " ").trim())
				.collect(joining("\n"));
	}

	
	public static void doWithUpBits(long fields, IntConsumer doWithBit) {
		while (fields != 0L) {
			final int lastBit = numberOfTrailingZeros(fields);
			final int boardIndex = BOARD_INDEX_TO_LONG_INDEX[lastBit];
			doWithBit.accept(boardIndex);
			fields &= BOARD_FIELDS_INVERTED[boardIndex];
		}
	}
	
	public static int doWithUpBits(long fields, IntUnaryOperator doWithBit) {
		int result = 0;
		while (fields != 0L) {
			final int lastBit = numberOfTrailingZeros(fields);
			final int boardIndex = BOARD_INDEX_TO_LONG_INDEX[lastBit];
			result += doWithBit.applyAsInt(boardIndex);
			fields &= BOARD_FIELDS_INVERTED[boardIndex];
		}
		return result;
	}

	public static int bitCountOfZeroble(long value) {
		return value == 0L? 0 : bitCount(value);
	}
}
