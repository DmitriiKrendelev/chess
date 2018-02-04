package org.dmkr.chess.engine.moves;

import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Test;

public class AllowedMovesKingTest extends AllowedMovesAbstractTest {
	
	@Test
	public void test1() {
		testAllAlowedMoves(
				BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - b - - - - -", 		
				"- - b - - - - -", 		
				"- - - K - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build(),
				"D3-C4",
				"D3-C3",
				"D3-C2",
				"D3-D2", 
				"D3-E4");
	}
}
