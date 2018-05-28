package org.dmkr.chess.engine;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.utils.BoardUtils;
import org.dmkr.chess.engine.board.bit.BitBoardBuilder;
import org.dmkr.chess.engine.board.impl.BoardBuilder;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.lang3.ArrayUtils.*;
import static org.dmkr.chess.api.model.Constants.*;
import static org.dmkr.chess.api.utils.BoardUtils.isFieldUnderPawnAtack;

public class BoardUtilsTest {
	
	@Test
	public void toBoardArrayTest() {
		final String[] boardArray = {
				"57, 58, 59, 60, 61, 62, 63, -64",	
				"49, 50, 51, 52, 53, 54, 55, -56",	
				"41, 42, 43, 44, 45, 46, 47, -48",
				"33, 34, 35, 36, 37, 38, 39, -40",
				"25, 26, 27, 28, 29, 30, 31, -32",
				"17, 18, 19, 20, 21, 22, 23, -24",
				" 9, 10, 11, 12, 13, 14, 15, -16",
				" 1,  2,  3,  4,  5,  6,  7,  -8",
			};
		
		final int[] expectedResult = {
				1,   2,  3,  4,  5,  6,  7,  -8,
			 	9,  10, 11, 12, 13, 14, 15, -16,
				17, 18, 19, 20, 21, 22, 23, -24,
				25, 26, 27, 28, 29, 30, 31, -32,
				33, 34, 35, 36, 37, 38, 39, -40,
				41, 42, 43, 44, 45, 46, 47, -48,
				49, 50, 51, 52, 53, 54, 55, -56,	
				57, 58, 59, 60, 61, 62, 63, -64
			};

		Assert.assertArrayEquals(expectedResult, BoardUtils.toBoardArray(boardArray));
	}

	@Test
	public void testIsFieldUnderPawnAtack() {
		final BoardEngine board = BoardBuilder.of(
				"- - - - - - - -",
				"p - - p - - - p",
				"- - - - - - - -",
				"- - - - - - - -",
				"p - - p - - - p",
				"- - - - - - - -",
				"p - - p - - - p",
				"- - - - - - - -")
				.build();

		final int[] allFieldsUnderPawnAtacks = {1, 2, 4, 6, 17, 18, 20, 22, 41, 42, 44, 46};

		for (int i = 0; i <  SIZE * SIZE; i ++) {
			Assert.assertEquals(contains(allFieldsUnderPawnAtacks, i), isFieldUnderPawnAtack(i, board));
		}

	}

	@Test
	public void testIsFieldUnderPawnAtackBit() {
		final BitBoard board = BitBoardBuilder.of(
				"- - - - - - - -",
				"p - - p - - - p",
				"- - - - - - - -",
				"- - - - - - - -",
				"p - - p - - - p",
				"- - - - - - - -",
				"p - - p - - - p",
				"- - - - - - - -")
				.build();

		final int[] allFieldsUnderPawnAtacks = {1, 2, 4, 6, 17, 18, 20, 22, 41, 42, 44, 46};

		for (int i = 0; i <  SIZE * SIZE; i ++) {
			Assert.assertEquals("Fail on index = " + i,
					contains(allFieldsUnderPawnAtacks, i),
					isFieldUnderPawnAtack(i, board));
		}

	}
}
