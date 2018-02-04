package org.dmkr.chess.engine;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Test;

public class KingIsUnderAtackWhiteTest {
	
	@Test
	public void testKing1() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"K - - - - - - -",
				"- - - - - - - -", 		
				"k - - - - - - -")
				.build();
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testKing2() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- K - - - - - -",
				"- - - k - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testKing3() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - K",
				"- - - k - - - -", 		
				"- - - - - - - -")
				.build();
		
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testKing4() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - K - - - - -", 		
				"- - - - k - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testKing5() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - K", 
				"- - - - - - - -", 		
				"- - - - - - - k", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testKing6() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - K", 
				"- - - - - - - k", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testKing7() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - k", 		
				"- - - - - - K -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testKing8() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - K k - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testKing9() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - K - - - -", 		
				"- - - k - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testKing10() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - K - - - -", 		
				"- - - k - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testPown1() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - p - - - -", 		
				"- - - K - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testPown2() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - K - - - -", 		
				"- - - p - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testPown3() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - K p - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		
		assertFalse("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testPown4() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - K - - - -", 		
				"- - - - p - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertFalse("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testPown5() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - K - - - -", 		
				"- - p - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertFalse("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testPown6() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - p - - -", 		
				"- - - K - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertTrue("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testPown7() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - p - - - - -", 		
				"- - - K - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertTrue("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testPown8() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- p - - - - - -", 		
				"K - - - - - - -")
				.build();
		
		assertTrue("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testPown9() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- p - - - - - -", 		
				"- - K - - - - -")
				.build();
		
		assertTrue("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testPown10() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - p -", 		
				"- - - - - - - K")
				.build();
		
		assertTrue("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testKnight1() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"n - - - - - - -",
				"K - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertFalse("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testKnight2() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"K n - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertFalse("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testKnight3() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"K - - - - - - -", 		
				"- n - - - - - -")
				.build();
		
		
		assertFalse("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testKnight4() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - n - - - -", 		
				"- - - - - - - -", 		
				"- - - K - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertFalse("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testKnight5() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - n - - - -", 		
				"- - n n n - - -", 		
				"- n n K n n - -", 		
				"- - n n n - - -",
				"- - - n - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertFalse("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testKnight6() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - K - - - -", 		
				"- n - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertTrue("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testKnight7() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- n - - - - - -", 		
				"- - - K - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertTrue("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testKnight8() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - n - - -", 		
				"- - - - - - - -", 		
				"- - - K - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertTrue("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testKnight9() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - K - - - -", 		
				"- - - - - n - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertTrue("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testKnight10() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - n - - -", 		
				"- - P P P - - -", 		
				"- - P K P - - -", 		
				"- - P P P - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertTrue("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testBishop1() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - b", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - n - - -", 		
				"- - - K - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertFalse("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testBishop2() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k b b -", 
				"- - - - - - - b", 		
				"- - - - - - - b", 		
				"- - - - - - - -", 		
				"- - - K - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertFalse("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testBishop3() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - b", 
				"b - - - - - - -", 		
				"- - - - - - - -", 		
				"- - P - P - - -", 		
				"- - - K - - - -", 		
				"- - P - P - - -",
				"- - - - - - - -", 		
				"b - - - - - b -")
				.build();
		
		
		assertFalse("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testBishop4() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - P b P - - -", 		
				"- - b K b - - -", 		
				"- - P b P - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertFalse("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testBishop5() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - B B B - - -", 		
				"- - B K B - - -", 		
				"- - B B B - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertFalse("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testBishop6() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - b", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - K - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testBishop7() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - K - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - b -")
				.build();
		
		assertTrue("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testBishop8() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - b - - - - -", 		
				"- - - K - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		
		assertTrue("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testBishop9() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - K - - - -", 		
				"- - - - - - - -",
				"- b - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertTrue("Error:\n" + board,board.isKingUnderAtack());
	}
	
	@Test
	public void testBishop10() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - b", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - P P - - - -", 		
				"- - P K P - - -", 		
				"- - P P P - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build();
		
		assertTrue("Error:\n" + board,board.isKingUnderAtack());
	}
}

