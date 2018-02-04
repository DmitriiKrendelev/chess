package org.dmkr.chess.common.cache;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Cache<T> implements Supplier<T> {

	private final AtomicReference<T> value = new AtomicReference<>();
	private final Supplier<T> valueCreator;

	static Cache<Object> cache(Object object, Method method, Object[] args) {
		return new Cache<Object>(() -> { 
			try {
				return method.invoke(object, args);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}
	
	@Override
	public T get() {
		return value.updateAndGet(value -> value == null ? valueCreator.get() : value);
	}
	
	public void reset() {
		value.set(null);
	}
}
