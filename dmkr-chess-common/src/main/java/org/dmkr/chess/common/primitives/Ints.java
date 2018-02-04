package org.dmkr.chess.common.primitives;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.function.IntUnaryOperator;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Ints {
	public static int max(int i1, int i2) {
		return i1 > i2 ? i1 : i2;
	}

	public static int min(int i1, int i2) {
		return i1 < i2 ? i1 : i2;
	}

	public static int[] apply(int[] array, IntUnaryOperator function) {
		for (int i = 0; i < array.length; i ++)
			array[i] = function.applyAsInt(array[i]);
	
		return array;
	}
	
	public static int[] reset(int[] array, int[] newValues) {
		checkArgument(array.length == newValues.length);
		System.arraycopy(newValues, 0, array, 0, array.length);
		return array;
	}
}
