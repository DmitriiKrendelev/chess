package org.dmkr.chess.common.primitives;

import static com.google.common.base.Preconditions.checkArgument;

public class IntStack {
	private static final int DEFAULT_SIZE = 1 << 10; 
	private final int[] array;
	private int size;
	
	public IntStack() {
		this(DEFAULT_SIZE);
	}
	
	public IntStack(int capacity) {
		this.array = new int[capacity];
	}
	
	public int push(int i) {
		array[size ++] = i;
		return i;
	}
	
	public int pop() {
		return array[-- size];
	}
	
	public int peek() {
		return array[size - 1];
	}
	
	public int[] array() {
		final int[] result = new int[size];
		System.arraycopy(array, 0, result, 0, result.length);
		return result;
	}
	
	public void copy(int[] array) {
		System.arraycopy(this.array, 0, array, 0, Math.min(array.length, size));
	}
	
	public int size() {
		return size;
	}
	
	public boolean empty() {
		return size == 0;
	}
	
	public void reset(IntStack other) {
		checkArgument(other.array.length == array.length);
		System.arraycopy(other.array, 0, array, 0, array.length);
		size = other.size;
	}
	
	public void clear() {
		size = 0;
	}
}
