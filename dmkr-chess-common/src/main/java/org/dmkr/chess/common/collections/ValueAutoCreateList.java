package org.dmkr.chess.common.collections;

import java.util.ArrayList;
import java.util.function.Supplier;

import lombok.RequiredArgsConstructor;

@SuppressWarnings("serial")
@RequiredArgsConstructor
public class ValueAutoCreateList<T> extends ArrayList<T> {
	private final Supplier<T> valueCreator;

	@Override
	public T get(int index) {
		while (index >= size()) {
			add(valueCreator.get());
		}

		return super.get(index);
	}

}
