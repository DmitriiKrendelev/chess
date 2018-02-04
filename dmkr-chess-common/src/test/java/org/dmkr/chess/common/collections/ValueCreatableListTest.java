package org.dmkr.chess.common.collections;

import java.util.List;

import static org.junit.Assert.*;

import org.dmkr.chess.common.collections.ValueAutoCreateList;
import org.junit.Test;

public class ValueCreatableListTest {
	
	@Test
	public void test() {
		final int index = 10;
		final int value = -1;
		final List<Integer> list = new ValueAutoCreateList<>(() -> value);
		
		final int result = list.get(index);
		
		assertEquals("size", index, list.size() - 1);
		assertEquals("value", value, result);
	}
}
