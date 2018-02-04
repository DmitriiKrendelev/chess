package org.dmkr.chess.common.primitives;

import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Bytes {
	public static final int BYTE_1 = (1 << 8) - 1;
	public static final int BYTE_2 = (1 << 16) - BYTE_1 - 1;
	public static final int BYTE_3 = (1 << 24) - BYTE_2 - BYTE_1 - 1;
	public static final int BYTE_4 = - BYTE_3 - BYTE_2 - BYTE_1 - 1;	
	
	public static int intByte1(int intValue) {
		return BYTE_1 & intValue;
	}
	
	public static int intByte2(int intValue) {
		return BYTE_2 & intValue;
	}
	
	public static int intByte3(int intValue) {
		return BYTE_3 & intValue;
	}
	
	public static int intByte4(int intValue) {
		return BYTE_4 & intValue;
	}
	
	public static byte byte1(int intValue) {
		return (byte) intByte1(intValue);
	}
	
	public static byte byte2(int intValue) {
		return (byte) (intByte2(intValue) >>> 8);
	}
	
	public static byte byte3(int intValue) {
		return (byte) (intByte3(intValue) >>> 16);
	}
	
	public static byte byte4(int intValue) {
		return (byte) (intByte4(intValue) >>> 24);
	}
	
	public static int toInt(int byte1) {
		return byte1;
	}
	
	public static byte abs(byte value) {
		return value >= 0 ? value : ((byte) -value);
	}
	
	public static int toInt(int byte1, int byte2) {
		return byte1 ^ (byte2 << 8);
	}
	
	public static int toInt(int byte1, int byte2, int byte3) {
		return byte1 ^ (byte2 << 8) ^ (byte3 << 16);
	}
	
	public static int toInt(int byte1, int byte2, int byte3, int byte4) {
		return byte1 ^ (byte2 << 8) ^ (byte3 << 16) ^ (byte4 << 24);
	}
	
	public static int setEmptyByte1(int i, byte byte1) {
		return i ^ byte1;
	}
	
	public static int setEmptyByte2(int i, byte byte2) {
		return i ^ (byte2 << 8);
	}
	
	public static int setEmptyByte3(int i, byte byte3) {
		return i ^ (byte3 << 16);
	}
	
	public static int setEmptyByte4(int i, byte byte4) {
		return i ^ (byte4 << 24);
	}
	
	public static void reverse(byte[] array) {
		int i = 0;
		int j = array.length - 1;
		byte tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}
	
	public static void apply(byte[] array, IntUnaryOperator function) {
		for (int i = 0; i < array.length; i ++)
			array[i] = (byte) function.applyAsInt(array[i]);
	}
	
	public static IntStream intStream(byte[] bytes) {
		return IntStream.range(0, bytes.length).map(i -> bytes[i]);
	}
}
