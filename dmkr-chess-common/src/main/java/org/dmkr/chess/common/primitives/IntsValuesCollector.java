package org.dmkr.chess.common.primitives;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Arrays;
import java.util.function.IntPredicate;

public class IntsValuesCollector {
	private static  final long LOW_32_BITS = -1L >>> 32;
	
	public static interface IntAndValueConsumer {
		public void consume(int i, int value);
	}
	
	private final int capacity;
	private final long[] intAndValueArray;

	private IntPredicate intPredicate = i -> true;
	private int size = 0;
	
	public IntsValuesCollector(int capacity) {
		checkArgument(capacity > -1);

		this.capacity = capacity;
		this.intAndValueArray = new long[capacity];
	}
	
	public void setIntPredicate(IntPredicate intPredicate) {
		this.intPredicate = intPredicate;
	}
	
	public void add(int i, int value) {
		if (size < capacity) {
			if (!intPredicate.test(i)) {
				return;
			}
			
			intAndValueArray[size ++] = toIntAndValue(i, value);
			if (size == capacity) {
				Arrays.sort(intAndValueArray);
			}
		} else {
			final long intAndValue = toIntAndValue(i, value);
			if (intAndValueArray[0] >= intAndValue || !intPredicate.test(i) ) {
				return;
			}
			
			final int firstIndexOfHigherValue = firstIndexOfHigherValue(intAndValue);
			System.arraycopy(intAndValueArray, 1, intAndValueArray, 0, firstIndexOfHigherValue - 1);
			intAndValueArray[firstIndexOfHigherValue - 1] = intAndValue;
		}
	}
	
	private static long toIntAndValue(int i, long value) {
		return ((i & LOW_32_BITS) | (value << 32));
	}
	
	public int valueAt(int index) {
		return (int) (intAndValueArray[index] & LOW_32_BITS);
	}
	
	public int weightAt(int index) {
		return (int) (intAndValueArray[index] >> 32);
	}
	
	private int firstIndexOfHigherValue(long value) {
		int topIndex = capacity - 1;
		int lowIndex = 0;
		
		while (true) {
			int index = (topIndex + lowIndex) / 2;
			
			if (intAndValueArray[index] > value) {
				topIndex = index;
			} else {
				lowIndex = index;
			}

			if (topIndex - lowIndex < 4) {
				for (int i = lowIndex; i <= topIndex; i ++ ) {
					if (intAndValueArray[i] > value) {
						return i;
					}
				}
				
				return topIndex + 1;
			}
		}
	}
	
	public int[] intsArray() {
		final int[] arr = new int[size];
		for (int i = 0; i < size; i ++) {
			arr[i] = valueAt(i);
		}
		return arr;
	}
	
	public int[] intsArrayDescending() {
		final int[] arr = new int[size];
		for (int i = 0; i < size; i ++) {
			arr[i] = valueAt(size - 1 - i);
		}
		return arr;
	}
	
	public void forEach(IntAndValueConsumer consumer) {
		for (int i = 0; i < size; i ++) {
			consumer.consume(valueAt(i), weightAt(i));
		}
	}
	
	public void reset() {
		this.size = 0;
	}
	
	public int size() {
		return size;
	}
}
