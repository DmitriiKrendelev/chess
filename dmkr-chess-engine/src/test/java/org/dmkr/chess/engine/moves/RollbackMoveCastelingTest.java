package org.dmkr.chess.engine.moves;

import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Test;

public class RollbackMoveCastelingTest extends RollbackMoveAbstractTest {
	
	@Test
	public void test1() {
		testApplyRollbackAllMoves(
				BoardFactory.of(
				"r - - - k - - r", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"R - - - K - - R")
				.canCastleLeft()
				.canCastleRght()
				.canOponentCastleLeft()
				.canOponentCastleRght()
				.build());
	}

	@Test
	public void test2() {
		testApplyRollbackAllMoves(
				BoardFactory.of(
				"r - - - k - - r", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"R - - - K - - R")
				.canCastleLeft()
				.canCastleRght()
				.canOponentCastleLeft()
				.canOponentCastleRght()
				.build()
				.invert());
	}

}
