package org.dmkr.chess.engine.board;

import org.dmkr.chess.api.BoardEngine;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class BoardInvertTest {

	private void test(BoardEngine board) {
		final BoardEngine clone = board.clone();
		
		final int[] allowedMovesClone = board.allowedMoves();
		final int[] allowedMoves = board.allowedMoves();
		assertArrayEquals(allowedMoves, allowedMovesClone);
		
		board.invert();
		final int[] invertedAllowedMoves = board.allowedMoves();
		board.invert();
		
		final int[] doubleInvertedAllowedMoves = board.allowedMoves();
		assertArrayEquals(allowedMoves, doubleInvertedAllowedMoves);
		
		assertEquals(clone, board);
		
		board.allowedMoves();
		clone.invert();
		
		final int[] invertedAllowedMovesClone = clone.allowedMoves();
		assertArrayEquals(invertedAllowedMoves, invertedAllowedMovesClone);
		
		clone.allowedMoves();
		board.invert();
	
		assertEquals(clone, board);
	}
	
	@Test
	public void test1() {
		final BoardEngine board = BoardFactory.of(
				"r - b - k - - r",  
				"- p p - - p p p",  
				"- p - - - - - -",  
				"- - - n P - - -",  
				"- - - N - P - -",  
				"- - - - - - - -",  
				"P - - N - - P P",  
				"R - - - K - - R")
				.canOponentCastleLeft()
				.canOponentCastleRght()
				.canCastleLeft()
				.canCastleRght()
				.build();
		
		test(board);
	}
}
