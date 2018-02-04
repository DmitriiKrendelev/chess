package org.dmkr.chess.common.collections;

import lombok.Getter;

public class ArrayStack<T> {
	private final Object[] elements;
	@Getter
	private int size;
	
	public ArrayStack(int capacity) {
		this.elements = new Object[capacity];
	}
	
	public T push(T element) {
		elements[size ++] = element;
		return element;
	}
	
	@SuppressWarnings("unchecked")
	public T pop() {
		return (T) elements[-- size];
	}
	
	@SuppressWarnings("unchecked")
	public T peek() {
		return (T) elements[size - 1];
	}
	
	public boolean startsWith(T[] array) {
		if (size < array.length) {
			return false;
		}
		
		for (int i = 0; i < array.length; i ++) {
			if (!elements[i].equals(array[i])) {
				return false;
			}
		}
		
		return true;
	}
	
}
