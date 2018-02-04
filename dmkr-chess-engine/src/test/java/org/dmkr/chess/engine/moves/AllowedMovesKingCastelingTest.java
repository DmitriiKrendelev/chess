package org.dmkr.chess.engine.moves;

import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Test;

public class AllowedMovesKingCastelingTest extends AllowedMovesAbstractTest {
	
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
				"- - - - - - - -", 		
				"R - - - K - - R")
				.canCastleLeft()
				.canCastleRght()
				.build(),
				"E1-C1", 
				"E1-G1");
	}
	
	@Test
	public void test2() {
		testDisallowedMove(
				BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"R - - - K - - R")
				.build(),
				"E1-C1",
				"E1-G1");
	}
	
	@Test
	public void test3() {
		testDisallowedMove(
				BoardFactory.of(
				"- - r - - r - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"R - - - K - - R")
				.canCastleLeft()
				.canCastleRght()
				.build(),
				"E1-C1", 
				"E1-G1");
	}
	
	@Test
	public void test4() {
		testDisallowedMove(
				BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"b - - - - - - -", 		
				"- - - - b - - -",
				"- - - - - - - -", 		
				"R - - - K - - R")
				.canCastleLeft()
				.canCastleRght()
				.build(),
				"E1-C1",
				"E1-G1");
	}
	
	@Test
	public void test5() {
		testAllowedMove(
				BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - b - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"R - - - K - - R")
				.canCastleLeft()
				.canCastleRght()
				.build(),
				"E1-C1",
				"E1-G1");
	}
	
	@Test
	public void test6() {
		testDisallowedMove(
				BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - n - - -",
				"- - - - - - - -", 		
				"R - - - K - - R")
				.canCastleLeft()
				.canCastleRght()
				.build(),
				"E1-C1", 
				"E1-G1");
	}
	
	@Test
	public void test7() {
		testDisallowedMove(
				BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - n - - -",
				"- - - - - - - -", 		
				"R - - - K - - R")
				.canCastleLeft()
				.canCastleRght()
				.build(),
				"E1-C1",
				"E1-G1");
	}
	
	@Test
	public void test8() {
		testAllowedMove(
				BoardFactory.of(
				"- r - - - r - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - p - -",
				"- P - - - - - -", 		
				"R - - - K - - R")
				.canCastleLeft()
				.canCastleRght()
				.build(),
				"E1-C1",
				"E1-G1");
	}
	
	@Test
	public void test9() {
		testDisallowedMove(
				BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"R n - - K - N R")
				.canCastleLeft()
				.canCastleRght()
				.build(),
				"E1-C1", 
				"E1-G1");
	}

	@Test
	public void test10() {
		testDisallowedMove(
				BoardFactory.of(
				"- - - - q - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"R - - - K - - R")
				.canCastleLeft()
				.canCastleRght()
				.build(),
				"E1-C1", 
				"E1-G1");
	}
	
	@Test
	public void test11() {
		testAllowedMove(
				BoardFactory.of(
				"r - - - k - - r", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.canOponentCastleLeft()
				.canOponentCastleRght()
				.build().invert(),
				"E8-G8",
				"E8-C8");
	}
	
	@Test
	public void test12() {
		testDisallowedMove(
				BoardFactory.of(
				"r - - - k - - r", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build().invert(),
				"E8-G8", 
				"E8-C8");
	}
	
	@Test
	public void test13() {
		testDisallowedMove(
				BoardFactory.of(
				"r - - - k - - r", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - R - - R - -")
				.canOponentCastleLeft()
				.canOponentCastleRght()
				.build().invert(),
				"E8-G8",
				"E8-C8");
	}
	
	@Test
	public void test14() {
		testDisallowedMove(
				BoardFactory.of(
				"r - - - k - - r", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - B", 		
				"- - - - - - - -",
				"B - - - - - - -", 		
				"- - - - - - - -")
				.canOponentCastleLeft()
				.canOponentCastleRght()
				.build().invert(),
				"E8-G8",
				"E8-C8");
	}
	
	@Test
	public void test15() {
		testAllowedMove(
				BoardFactory.of(
				"r - - - k - - r", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - B - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.canOponentCastleLeft()
				.canOponentCastleRght()
				.build().invert(),
				"E8-G8", 
				"E8-C8");
	}
	
	@Test
	public void test16() {
		testDisallowedMove(
				BoardFactory.of(
				"r - - - k - - r", 
				"- - - - - - - -", 		
				"- - - - N - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.canOponentCastleLeft()
				.canOponentCastleRght()
				.build().invert(),
				"E8-G8",
				"E8-C8");
	}
	
	@Test
	public void test17() {
		testDisallowedMove(
				BoardFactory.of(
				"r - - - k - - r", 
				"- - - - N - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.canOponentCastleLeft()
				.canOponentCastleRght()
				.build().invert(),
				"E8-G8",
				"E8-C8");
	}
	
	@Test
	public void test18() {
		testAllowedMove(
				BoardFactory.of(
				"r - - - k - - r", 
				"- - p - - - - -", 		
				"- - - - - - P -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - R - - - R -")
				.canOponentCastleLeft()
				.canOponentCastleRght()
				.build().invert(),
				"E8-G8",
				"E8-C8");
	}
	
	@Test
	public void test19() {
		testDisallowedMove(
				BoardFactory.of(
				"r n - - k - N r", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.canOponentCastleLeft()
				.canOponentCastleRght()
				.build().invert(),
				"E8-G8",
				"E8-C8");
	}

	@Test
	public void test20() {
		testDisallowedMove(
				BoardFactory.of(
				"r - - - k - - r", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - Q - - -")
				.canOponentCastleLeft()
				.canOponentCastleRght()
				.build().invert(),
				"E8-G8", 
				"E8-C8");
	}
}
