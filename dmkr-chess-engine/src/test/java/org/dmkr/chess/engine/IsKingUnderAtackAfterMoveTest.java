package org.dmkr.chess.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.stream.IntStream;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.utils.BoardUtils;
import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Test;

public class IsKingUnderAtackAfterMoveTest {

	@Test
	public void test() {
		final BoardEngine board = BoardFactory.newInitialPositionBoard();
		
		BoardUtils.applayMoves(board, 
				"E2-E4", 
				"B8-C6");
		
		testMovesKingIsNotUnderAtack(board);
	}
	
	
	private void testMovesKingIsNotUnderAtack(BoardEngine board) {
		IntStream.of(board.allowedMoves())
			.forEach(moveValue -> {
				final BoardEngine clone = board.clone();
				
				assertFalse(board.isKingUnderAtackAfterMove(moveValue));
				assertEquals(board, clone);
			});
	}
	
}
