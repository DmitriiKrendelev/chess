package org.dmkr.chess.common.primitives;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.function.IntUnaryOperator;

public class IntArrayBuilder {
	private static final int[] EMPTY = {};
	private static final int DEFAULT_SIZE = 1 << 10; 
	
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
}
