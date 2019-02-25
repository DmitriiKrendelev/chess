package org.dmkr.chess.common.primitives;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import org.dmkr.chess.common.primitives.IntsValuesCollector.IntAndValueConsumer;
import org.junit.Test;

import com.google.common.base.Preconditions;


public class IntsValuesCollectorTest {
	
	private static class IntAndValueConsumerImpl implements IntAndValueConsumer {
		private final Map<Integer, Integer> intAndValuesMap = new LinkedHashMap<>();

		@Override
		public void consume(int i, int value) {
			intAndValuesMap.put(i, value);
		}
		
		public void cleanup() {
			intAndValuesMap.clear();
		}
	}
	
	private Map<Integer, Integer> map(int[] keys, int[] values) {
		Preconditions.checkState(keys.length == values.length);
		final Map<Integer, Integer> result = new LinkedHashMap<>();

		for (int i = 0; i < keys.length; i ++) {
			result.put(keys[i], values[i]);
		}
		
		return result;
	}
	
	@Test
	public void testAddLowestAndHighest() {
		for (int capacity = 1; capacity < 20; capacity ++) {
			final IntsValuesCollector collector = new IntsValuesCollector(capacity);
			
			// add below capacity
			for (int i = 0; i < capacity; i ++) {
				collector.add(i, i);
				assertEquals(i + 1, collector.size());
			}
			
			for (int i = 0; i < collector.size(); i ++) {
				assertEquals(i, collector.valueAt(i));
				assertEquals(i, collector.weightAt(i));
			}
			
			// add low values to skip
			collector.add(-1, -1);
			collector.add(0, 0);
			for (int i = 0; i < collector.size(); i ++) {
				assertEquals(i, collector.valueAt(i));
				assertEquals(i, collector.weightAt(i));
			}
			
			collector.add(1, 1);
			assertEquals(1, collector.valueAt(0));
			for (int i = 1; i < collector.size(); i ++) {
				assertEquals(i, collector.valueAt(i));
				assertEquals(i, collector.weightAt(i));
			}
			
			// add to the top
			collector.add(capacity, capacity);
			for (int i = 0; i < collector.size(); i ++) {
				assertEquals(i + 1, collector.valueAt(i));
				assertEquals(i + 1, collector.weightAt(i));
			}
			
		}
	}
	
	@Test
	public void testAddInTheMiddle() {
		final int capacity = 10;
		final IntAndValueConsumerImpl intAndValueConsumer = new IntAndValueConsumerImpl();
		
		
		final IntsValuesCollector collector = new IntsValuesCollector(capacity);
		for (int i = 0; i < capacity; i ++) {
			collector.add(i, i);
		}

		collector.forEach(intAndValueConsumer);
		assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, collector.intsArray());
		assertEquals(map(
				new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
				new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}), 
				intAndValueConsumer.intAndValuesMap);
		intAndValueConsumer.cleanup();
		
		collector.add(6, 6);
		collector.forEach(intAndValueConsumer);
		assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 6, 7, 8, 9}, collector.intsArray());
		assertEquals(map(
				new int[]{1, 2, 3, 4, 5, 6, 6, 7, 8, 9},
				new int[]{1, 2, 3, 4, 5, 6, 6, 7, 8, 9}), 
				intAndValueConsumer.intAndValuesMap);
		intAndValueConsumer.cleanup();
		
		collector.add(5, 5);
		collector.forEach(intAndValueConsumer);
		assertArrayEquals(new int[]{2, 3, 4, 5, 5, 6, 6, 7, 8, 9}, collector.intsArray());
		assertEquals(map(
				new int[]{2, 3, 4, 5, 5, 6, 6, 7, 8, 9},
				new int[]{2, 3, 4, 5, 5, 6, 6, 7, 8, 9}), 
				intAndValueConsumer.intAndValuesMap);
		intAndValueConsumer.cleanup();
		
		collector.add(11, 11);
		collector.forEach(intAndValueConsumer);
		assertArrayEquals(new int[]{3, 4, 5, 5, 6, 6, 7, 8, 9, 11}, collector.intsArray());
		assertEquals(map(
				new int[]{3, 4, 5, 5, 6, 6, 7, 8, 9, 11},
				new int[]{3, 4, 5, 5, 6, 6, 7, 8, 9, 11}), 
				intAndValueConsumer.intAndValuesMap);
		intAndValueConsumer.cleanup();
		
		collector.add(10, 10);
		collector.forEach(intAndValueConsumer);
		assertArrayEquals(new int[]{4, 5, 5, 6, 6, 7, 8, 9, 10, 11}, collector.intsArray());
		assertEquals(map(
				new int[]{4, 5, 5, 6, 6, 7, 8, 9, 10, 11},
				new int[]{4, 5, 5, 6, 6, 7, 8, 9, 10, 11}), 
				intAndValueConsumer.intAndValuesMap);
		intAndValueConsumer.cleanup();
		
		collector.add(11, 11);
		collector.forEach(intAndValueConsumer);
		assertArrayEquals(new int[]{5, 5, 6, 6, 7, 8, 9, 10, 11, 11}, collector.intsArray());
		assertEquals(map(
				new int[]{5, 5, 6, 6, 7, 8, 9, 10, 11, 11},
				new int[]{5, 5, 6, 6, 7, 8, 9, 10, 11, 11}), 
				intAndValueConsumer.intAndValuesMap);
		intAndValueConsumer.cleanup();
		
		collector.add(1234567890, 1234567890);
		collector.forEach(intAndValueConsumer);
		assertArrayEquals(new int[]{5, 6, 6, 7, 8, 9, 10, 11, 11, 1234567890}, collector.intsArray());
		assertEquals(map(
				new int[]{5, 6, 6, 7, 8, 9, 10, 11, 11, 1234567890},
				new int[]{5, 6, 6, 7, 8, 9, 10, 11, 11, 1234567890}), 
				intAndValueConsumer.intAndValuesMap);
		intAndValueConsumer.cleanup();
	}
	
	@Test
	public void testAddInTheMiddleNegative() {
		final IntAndValueConsumerImpl intAndValueConsumer = new IntAndValueConsumerImpl();
		final int capacity = 10;
		
		final IntsValuesCollector collector = new IntsValuesCollector(capacity);
		for (int i = 0; i < capacity; i ++) {
			collector.add(-i, -i);
		}
		
		collector.forEach(intAndValueConsumer);
		assertArrayEquals(new int[]{-9, -8, -7, -6, -5, -4, -3, -2, -1, 0}, collector.intsArray());
		assertEquals(map(
				new int[]{-9, -8, -7, -6, -5, -4, -3, -2, -1, 0},
				new int[]{-9, -8, -7, -6, -5, -4, -3, -2, -1, 0}), 
				intAndValueConsumer.intAndValuesMap);
		intAndValueConsumer.cleanup();
		
		collector.add(-6, -6);
		collector.forEach(intAndValueConsumer);
		assertArrayEquals(new int[]{-8, -7, -6, -6, -5, -4, -3, -2, -1, 0}, collector.intsArray());
		assertEquals(map(
				new int[]{-8, -7, -6, -6, -5, -4, -3, -2, -1, 0},
				new int[]{-8, -7, -6, -6, -5, -4, -3, -2, -1, 0}), 
				intAndValueConsumer.intAndValuesMap);
		intAndValueConsumer.cleanup();
		
		collector.add(-5, -5);
		collector.forEach(intAndValueConsumer);
		assertArrayEquals(new int[]{-7, -6, -6, -5, -5, -4, -3, -2, -1, 0}, collector.intsArray());
		assertEquals(map(
				new int[]{-7, -6, -6, -5, -5, -4, -3, -2, -1, 0},
				new int[]{-7, -6, -6, -5, -5, -4, -3, -2, -1, 0}), 
				intAndValueConsumer.intAndValuesMap);
		intAndValueConsumer.cleanup();
		
		collector.add(0, 0);
		collector.forEach(intAndValueConsumer);
		assertArrayEquals(new int[]{-6, -6, -5, -5, -4, -3, -2, -1, 0, 0}, collector.intsArray());
		assertEquals(map(
				new int[]{-6, -6, -5, -5, -4, -3, -2, -1, 0, 0},
				new int[]{-6, -6, -5, -5, -4, -3, -2, -1, 0, 0}), 
				intAndValueConsumer.intAndValuesMap);
		intAndValueConsumer.cleanup();
		
		collector.add(-7, -7);
		collector.forEach(intAndValueConsumer);
		assertArrayEquals(new int[]{-6, -6, -5, -5, -4, -3, -2, -1, 0, 0}, collector.intsArray());
		assertEquals(map(
				new int[]{-6, -6, -5, -5, -4, -3, -2, -1, 0, 0},
				new int[]{-6, -6, -5, -5, -4, -3, -2, -1, 0, 0}), 
				intAndValueConsumer.intAndValuesMap);
		intAndValueConsumer.cleanup();
		
		collector.add(-1234567890, -1234567890);
		collector.forEach(intAndValueConsumer);
		assertArrayEquals(new int[]{-6, -6, -5, -5, -4, -3, -2, -1, 0, 0}, collector.intsArray());
		assertEquals(map(
				new int[]{-6, -6, -5, -5, -4, -3, -2, -1, 0, 0},
				new int[]{-6, -6, -5, -5, -4, -3, -2, -1, 0, 0}), 
				intAndValueConsumer.intAndValuesMap);
		intAndValueConsumer.cleanup();
		
		collector.add(MIN_VALUE, MIN_VALUE);
		collector.forEach(intAndValueConsumer);
		assertArrayEquals(new int[]{-6, -6, -5, -5, -4, -3, -2, -1, 0, 0}, collector.intsArray());
		assertEquals(map(
				new int[]{-6, -6, -5, -5, -4, -3, -2, -1, 0, 0},
				new int[]{-6, -6, -5, -5, -4, -3, -2, -1, 0, 0}), 
				intAndValueConsumer.intAndValuesMap);
		intAndValueConsumer.cleanup();
	}
	
	@Test 
	public void testAddMinAndMax() {
		final int capacity = 5;
		final IntsValuesCollector collector = new IntsValuesCollector(capacity);
		
		for (int i = 0; i < capacity; i ++) {
			collector.add(MIN_VALUE, MIN_VALUE);
		}
		assertArrayEquals(new int[]{MIN_VALUE, MIN_VALUE, MIN_VALUE, MIN_VALUE, MIN_VALUE}, collector.intsArray());
	
		for (int i = 0; i < capacity; i ++) {
			collector.add(-1234567890, -1234567890);
		}
		assertArrayEquals(new int[]{-1234567890, -1234567890, -1234567890, -1234567890, -1234567890}, collector.intsArray());
	
		for (int i = 0; i < capacity; i ++) {
			collector.add(0, 0);
		}
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, collector.intsArray());
		
		
		for (int i = 0; i < capacity; i ++) {
			collector.add(1234567890, 1234567890);
		}
		assertArrayEquals(new int[]{1234567890, 1234567890, 1234567890, 1234567890, 1234567890}, collector.intsArray());
		
		for (int i = 0; i < capacity; i ++) {
			collector.add(MAX_VALUE, MAX_VALUE);
		}
		assertArrayEquals(new int[]{MAX_VALUE, MAX_VALUE, MAX_VALUE, MAX_VALUE, MAX_VALUE}, collector.intsArray());
	}
	
	@Test 
	public void testIntsAndValuesCollector() {
		final IntsValuesCollector collector = new IntsValuesCollector(10);
		final IntAndValueConsumerImpl intAndValueConsumer = new IntAndValueConsumerImpl();
		
		collector.add(0, 0);
		collector.add(1, 2);
		collector.add(2, 321);
		collector.add(3, 1234567890);
		collector.add(4, Integer.MAX_VALUE);
		collector.add(5, -2);
		collector.add(6, -321);
		collector.add(7, -1234567890);
		collector.add(8, Integer.MIN_VALUE);
		
		collector.forEach(intAndValueConsumer);
		
		assertEquals(map(
				new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8},
				new int[]{0, 2, 321, 1234567890, Integer.MAX_VALUE, -2, -321, -1234567890, Integer.MIN_VALUE}), 
				intAndValueConsumer.intAndValuesMap);
	}
}
