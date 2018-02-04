package org.dmkr.chess.engine.moves;

import static org.dmkr.chess.api.model.Constants.VALUE_BISHOP;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Test;

public class MovesSelectorTest extends MovesSelectorAbstractTest {

	@Test
	public void test1() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - k", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - P - - - - -",
				"- - - - - - - -", 		
				"B - - - - - - K")
				.build();
				
		
		testAllMoves(
				board,
				false,
				new byte[] {VALUE_BISHOP},
				"A1-B2"
				);
	}
	
}
