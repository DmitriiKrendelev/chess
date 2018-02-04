package org.dmkr.chess.engine.moves;

import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Test;

public class RollbackMoveTest extends RollbackMoveAbstractTest {

	@Test
	public void test1() {
		testApplyRollbackAllMoves(
				BoardFactory.of(
				"- - - - - - k r", 
				"- - - - - - - p", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - P", 		
				"- - - - - - K R")
				.build());
	}
	
	@Test
	public void test2() {
		testApplyRollbackAllMoves(
				BoardFactory.of(
				"- - - - - k - r", 
				"- - - - - - - p", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - P", 		
				"- - - - - K - R")
				.build());
	}
	
	@Test
	public void test3() {
		testApplyRollbackAllMoves(
				BoardFactory.of(
				"- - - - k - - r", 
				"- - - - - - - p", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - P", 		
				"- - - - K - - R")
				.build());
	}
	
	@Test
	public void test4() {
		testApplyRollbackAllMoves(
				BoardFactory.of(
				"- - - k - - - r", 
				"- - - - - - - p", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - P", 		
				"- - - K - - - R")
				.build());
	}
	
	@Test
	public void test5() {
		testApplyRollbackAllMoves(
				BoardFactory.of(
				"r - b q k b n r", 
				"p p p p p p p p", 		
				"- - n - - - - -", 		
				"- - - - - - - -", 		
				"- - B - P - - -", 		
				"- - - - - - - N",
				"P P P P - P P P", 		
				"R N B Q K - - R")
				.canCastleLeft()
				.canCastleRght()
				.canOponentCastleLeft()
				.canOponentCastleRght()
				.build()
				.invert());
	}
}
