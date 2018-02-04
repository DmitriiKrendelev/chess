package org.dmkr.chess.engine;

import static org.junit.Assert.assertTrue;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Test;

public class CheckmateTest {

	
	@Test
	public void test1() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"n - N - - - - -", 		
				"- p - - - - - -",
				"p - - - - - - -", 		
				"k - - - - R K -")
				.canCastleRght()
				.build().invert();
		
		assertTrue(board.isCheckmate());
		
	}
}
