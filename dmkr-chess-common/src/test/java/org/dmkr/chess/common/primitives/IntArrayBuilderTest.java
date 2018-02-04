package org.dmkr.chess.common.primitives;

import org.dmkr.chess.common.primitives.IntArrayBuilder;
import org.junit.Assert;
import org.junit.Test;

public class IntArrayBuilderTest {

	@Test
	public void test() {
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
	
}
