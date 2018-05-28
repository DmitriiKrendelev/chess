package org.dmkr.chess.common.primitives;

import org.dmkr.chess.common.primitives.IntArrayBuilder;
import org.junit.Assert;
import org.junit.Test;

public class IntArrayBuilderTest {

	@Test
	public void testArrayBuilder() {
		final IntArrayBuilder builder = new IntArrayBuilder();
	
		final int[] array1 = builder.add(1).add(2).add(3).build();
		final int[] expected1 = {1, 2, 3};
		Assert.assertArrayEquals(expected1, array1);

		final int[] array2 = builder.add(4).add(5).add(6).add(7).build();
		final int[] expected2 = {4, 5, 6, 7};
		Assert.assertArrayEquals(expected2, array2);
		
		final int[] array3 = builder.build();
		final int[] expected3 = {};
		Assert.assertArrayEquals(expected3, array3);
	}

	@Test
	public void testSubstituteLowest() {
		final IntArrayBuilder builder = new IntArrayBuilder();

		builder.add(1).add(2).add(3).add(4).add(5);

		builder.substituteLowest(i -> i, 0);
		builder.substituteLowest(i -> i, 6);
		builder.substituteLowest(i -> i, -1);
		builder.substituteLowest(i -> i, 9);

		final int[] array = builder.build();
		Assert.assertArrayEquals(new int[]{6, 9, 3, 4, 5}, array);
	}
}
