package org.dmkr.chess.common.collections;

import java.util.function.Function;
import java.util.stream.Collector;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import lombok.experimental.UtilityClass;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Collectors.toList;

@UtilityClass
public class StreamUtils {

	public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(
            Function<? super T, ? extends K> keyMapper,
            Function<? super T, ? extends V> valueMapper) {
		
	    return collectingAndThen(toMap(keyMapper, valueMapper), ImmutableMap::copyOf);
	}
	
	public static <V> Collector<V, ?, ImmutableSet<V>> toImmutableSet() {

		return collectingAndThen(toSet(), ImmutableSet::copyOf);
	}
	
	public static <V> Collector<V, ?, ImmutableList<V>> toImmutableList() {
		return collectingAndThen(toList(), ImmutableList::copyOf);
	}
}
