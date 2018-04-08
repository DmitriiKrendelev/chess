package org.dmkr.chess.engine;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Test;

public class KingIsUnderAtackBlackTest {
	
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
				.build().invert();
		
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
				.build().invert();
		
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
				.build().invert();
		
		
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
				.build().invert();
		
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
				.build().invert();
		
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
				.build().invert();
		
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
				.build().invert();
		
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
				.build().invert();
		
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
				.build().invert();
		
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
				.build().invert();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testPawn1() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - k - - - -", 		
				"- - - P - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - K - - -")
				.build().invert();
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testPawn2() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - P - - - -", 		
				"- - - k - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - K - - - -")
				.build().invert();
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testPawn3() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - k P - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - K - - -")
				.build().invert();
		
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testPawn4() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - P - - -", 		
				"- - - k - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - K - - -")
				.build().invert();
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testPawn5() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - P - - - - -", 		
				"- - - k - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - K - - -")
				.build().invert();
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testPawn6() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - k - - - -", 		
				"- - - - P - - -",
				"- - - - - - - -", 		
				"- - - - K - - -")
				.build().invert();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testPawn7() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - P - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - K - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build().invert();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testPawn8() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - P - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"K - - - - - - -")
				.build().invert();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testPawn9() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - k", 
				"- - - - - - P -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - K - - - - -")
				.build().invert();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testPawn10() {
		final BoardEngine board = BoardFactory.of(
				"- - - - n k - -", 
				"- - - - P p - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - K")
				.build().invert();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testKnight1() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - N - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"K - - - - - - -", 		
				"- - - - - - - -")
				.build().invert();
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testKnight2() {
		final BoardEngine board = BoardFactory.of(
				"- - - N k - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"K - - - - - - -", 		
				"- - - - - - - -")
				.build().invert();
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testKnight3() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - N - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"K - - - - - - -", 		
				"- - - - - - - -")
				.build().invert();
		
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testKnight4() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - n - - -", 		
				"- - - - - - - -", 		
				"- - - K - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build().invert();
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testKnight5() {
		final BoardEngine board = BoardFactory.of(
				"- - - - K - - -", 
				"- - - - - - - -", 		
				"- - - N - - - -", 		
				"- - N N N - - -", 		
				"- N N k N N - -", 		
				"- - N N N - - -",
				"- - - N - - - -", 		
				"- - - - - - - -")
				.build().invert();
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testKnight6() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - N - - - -", 		
				"- - - - - - - -", 		
				"- - - K - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build().invert();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testKnight7() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - - -", 		
				"- - - - - N - -", 		
				"- - - - - - - -", 		
				"- - - K - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build().invert();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testKnight8() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - - - - - N -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - K - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build().invert();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testKnight9() {
		final BoardEngine board = BoardFactory.of(
				"- - - - k - - -", 
				"- - N - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - K - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build().invert();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testKnight10() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - p p p - - -", 		
				"- - p k p - - -", 		
				"- - p p p - - -",
				"- - - - N - - -", 		
				"- - - - K - - -")
				.build().invert();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testBishop1() {
		final BoardEngine board = BoardFactory.of(
				"- - - - K - - B", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - N - - -", 		
				"- - - k - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build().invert();
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testBishop2() {
		final BoardEngine board = BoardFactory.of(
				"- - - - K B B -", 
				"- - - - - - - B", 		
				"- - - - - - - B", 		
				"- - - - - - - -", 		
				"- - - k - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build().invert();
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testBishop3() {
		final BoardEngine board = BoardFactory.of(
				"- - - - K - - B", 
				"B - - - - - - -", 		
				"- - - - - - - -", 		
				"- - p - p - - -", 		
				"- - - k - - - -", 		
				"- - p - p - - -",
				"- - - - - - - -", 		
				"B - - - - - B -")
				.build().invert();
		
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testBishop4() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - p B p - - -", 		
				"- - B k B - - -", 		
				"- - p B p - - -",
				"- - - - - - - -", 		
				"K - - - - - - -")
				.build().invert();
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testBishop5() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - b b b - - -", 		
				"- - b k b - - -", 		
				"- - b b b - - -",
				"- - - - - - - -", 		
				"- - - - K - - -")
				.build().invert();
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testBishop6() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - B", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - k - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - K - - - -")
				.build().invert();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testBishop7() {
		final BoardEngine board = BoardFactory.of(
				"- - - - K - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - k - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - B -")
				.build().invert();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testBishop8() {
		final BoardEngine board = BoardFactory.of(
				"- - - - K - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - B - - - - -", 		
				"- - - k - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build().invert();
		
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testBishop9() {
		final BoardEngine board = BoardFactory.of(
				"- - - - K - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - k - - - -", 		
				"- - - - - - - -",
				"- B - - - - - -", 		
				"- - - - - - - -")
				.build().invert();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testBishop10() {
		final BoardEngine board = BoardFactory.of(
				"- - - - K - - B", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - p p - - - -", 		
				"- - p k p - - -", 		
				"- - p p p - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build().invert();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testRook1() {
		final BoardEngine board = BoardFactory.of(
				"- - - - K - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - k P - - R", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build().invert();
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testRook2() {
		final BoardEngine board = BoardFactory.of(
				"- - R - R - - K", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"R - - - - - - R", 		
				"- - - k - - - -", 		
				"R - - - - - - R",
				"- - - - - - - -", 		
				"- - R - R - - -")
				.build().invert();
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testRook3() {
		final BoardEngine board = BoardFactory.of(
				"- - - R - - - K", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - p - - - -", 		
				"R - p k p - - R", 		
				"- - - p - - - -",
				"- - - - - - - -", 		
				"- - - R - - - -")
				.build().invert();
		
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testRook4() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - R p R - - -", 		
				"- - p k p - - -", 		
				"- - R p R - - -",
				"- - - - - - - -", 		
				"K - - - - - - -")
				.build().invert();
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testRook5() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - r b r - - -", 		
				"- - r k r - - -", 		
				"- - r r r - - -",
				"- - - - - - - -", 		
				"- - - - - K - -")
				.build().invert();
		
		assertFalse("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testRook6() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - k - - - R", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build().invert();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testRook7() {
		final BoardEngine board = BoardFactory.of(
				"- - - - K - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - k - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - R - - - -")
				.build().invert();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testRook8() {
		final BoardEngine board = BoardFactory.of(
				"- - - R K - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - k - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build().invert();
		
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testRook9() {
		final BoardEngine board = BoardFactory.of(
				"- - - - K - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"R - - k - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - -")
				.build().invert();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	@Test
	public void testRook10() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"n - N - - - - -", 		
				"- p - - - - - -",
				"p - - - - - - -", 		
				"k - - - - R K -")
				.build().invert();
		
		assertTrue("Error:\n" + board, board.isKingUnderAtack());
	}
	
	
}

