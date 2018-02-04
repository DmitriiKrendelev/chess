package org.dmkr.chess.engine.moves;

import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Test;

public class AllowedMovesPownTest extends AllowedMovesAbstractTest {

	@Test
	public void test1() {
		testAllowedMove(
				BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"P - - P - - - P", 		
				"- - - - K - - -")
				.build(),
				"A2-A3", 
				"A2-A4",
				"D2-D3", 
				"D2-D4", 
				"H2-H3",
				"H2-H4");
	}
	
	@Test
	public void test2() {
		testAllowedMove(
				BoardFactory.of(
				"- - - - - - - -", 
				"- p - - - - - -", 		
				"P - - n - n - -", 		
				"- - - - P - - -", 		
				"- - - - - - n -", 		
				"- - - - - - - P",
				"- - - - - - - -", 		
				"- - - - K - - -")
				.build(),
				"A6-B7", 
				"E5-D6",
				"E5-F6", 
				"H3-G4");
	}
}
