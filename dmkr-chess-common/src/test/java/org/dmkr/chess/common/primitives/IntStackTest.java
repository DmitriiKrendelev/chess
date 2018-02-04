package org.dmkr.chess.common.primitives;

import org.dmkr.chess.common.primitives.IntStack;
import org.junit.Assert;
import org.junit.Test;

public class IntStackTest {
	@Test
	public void test() {
		final IntStack stack = new IntStack();
	
		Assert.assertTrue(stack.empty());
		Assert.assertTrue(stack.push(1) == 1);
		Assert.assertTrue(stack.peek() == 1);
		Assert.assertFalse(stack.empty());	
		
		Assert.assertTrue(stack.push(2) == 2);
		Assert.assertTrue(stack.peek() == 2);
		Assert.assertFalse(stack.empty());
		
		final int[] array = new int[5];
		stack.copy(array);
		Assert.assertArrayEquals(new int[]{1, 2, 0, 0 ,0}, array);
		
		Assert.assertTrue(stack.pop() == 2);
		Assert.assertTrue(stack.peek() == 1);
		Assert.assertFalse(stack.empty());
		
		Assert.assertTrue(stack.pop() == 1);
		Assert.assertTrue(stack.empty());
	}
}
