package org.dmkr.chess.engine.board;
import static com.google.common.collect.Sets.newHashSet;
import static org.dmkr.chess.api.utils.BitBoardUtils.doWithUpBits;
import static org.dmkr.chess.api.utils.BitBoardUtils.fromBinaryStrings;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class BitBoardUtilsTest {

	@Test
	public void testDoWithUpBitsTest() {
		final long test = fromBinaryStrings(
				"0 0 0 0 0 0 1 1",
				"0 0 0 0 0 0 0 0",
				"0 0 0 0 0 0 0 0",
				"0 0 0 0 0 0 0 0",
				"0 0 0 1 0 0 0 0",
				"0 0 0 0 0 0 0 0",
				"0 0 1 0 0 0 0 0",
				"0 1 0 0 0 0 1 0"
				);
		
		final Set<Integer> ints = newHashSet();
		doWithUpBits(test, ints::add);
		
		Assert.assertEquals(newHashSet(1, 6, 10, 27, 62, 63), ints);
	}
	
	
}
