package org.dmkr.chess.common.primitives;

import java.util.function.IntConsumer;
import java.util.function.IntUnaryOperator;

import static com.google.common.base.Preconditions.checkArgument;

public class IntArrayBuilder {
	private static final int[] EMPTY = {};
	private static final int DEFAULT_SIZE = 1 << 9;
	
	private final int[] array;
	private int size;
	
	public IntArrayBuilder() {
		this(DEFAULT_SIZE);
	}
	
	public IntArrayBuilder(int capacity) {
		this.array = new int[capacity];
	}
	
	public IntArrayBuilder add(int i) {
		array[size ++] = i;
		return this;
	}

	public void substituteLowest(IntUnaryOperator valuesComparator, int newValue) {
		int currentMinValue = Integer.MAX_VALUE;
		int currentMinValueIndex = -1;
		for (int i = 0; i < size; i ++) {
			final int moveValue = valuesComparator.applyAsInt(array[i]);
			if (moveValue < currentMinValue) {
				currentMinValue = moveValue;
				currentMinValueIndex = i;
			}
		}

		if (currentMinValue < valuesComparator.applyAsInt(newValue)) {
			array[currentMinValueIndex]	 = newValue;
		}
	}

	public void forEach(IntConsumer consumer) {
		for (int i = 0; i < size; i ++) {
			consumer.accept(array[i]);
		}
	}
	
	public int[] build() {
		if (size == 0)
			return EMPTY;
		
		final int[] result = new int[size];
		System.arraycopy(array, 0, result, 0, result.length);
		size = 0;
		return result;
	}
	
	public int[] build(IntUnaryOperator valueConverter) {
		final int[] result = build();
		
		for (int i = 0; i < result.length; i ++)
			result[i] = valueConverter.applyAsInt(result[i]);
		
		return result;
	}
	
	public void reset(IntArrayBuilder other) {
		checkArgument(other.array.length == array.length);
		System.arraycopy(other.array, 0, array, 0, array.length);
		size = other.size;
	}

	public int size() {
		return size;
	}

	public static int[] build(IntArrayBuilder first, IntArrayBuilder second) {
		final int[] result = new int[first.size + second.size];

		System.arraycopy(first.array, 0, result, 0, first.size);
		System.arraycopy(second.array, 0, result, first.size, second.size);

		first.size = 0;
		second.size = 0;

		return result;
	}
}
