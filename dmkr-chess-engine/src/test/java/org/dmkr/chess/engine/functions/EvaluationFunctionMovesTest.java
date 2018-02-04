package org.dmkr.chess.engine.functions;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.board.BoardFactory;
import org.dmkr.chess.engine.function.common.EvaluationFunctionMoves;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EvaluationFunctionMovesTest {

	@Test
	public void test1() {
		final BoardEngine board = BoardFactory.of(
				"r n b - k b n r",
				"p p p p - p p p", 
				"- - - - p - - -",
				"- - - - - - q -", 
				"- - B - P - - -", 
				"- - - - - - - -",
				"P P P P - P P P",
				"R N B Q K - N R")
				.canOponentCastleLeft()
				.canOponentCastleRght()
				.canCastleLeft()
				.canCastleRght()
				.build();

		final int value = EvaluationFunctionMoves.valueOfAtacks(board.allowedMoves(), board);
		
		board.invert();
		final int valueOponent = EvaluationFunctionMoves.valueOfAtacks(board.allowedMoves(), board);
		board.invert();
		
		assertEquals(value, valueOponent);
	}
	
}
