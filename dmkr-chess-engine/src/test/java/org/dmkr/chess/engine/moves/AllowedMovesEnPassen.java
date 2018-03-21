package org.dmkr.chess.engine.moves;

import static org.dmkr.chess.api.model.Move.moveOf;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.utils.BoardUtils;
import org.dmkr.chess.engine.board.BoardFactory;
import org.dmkr.chess.engine.board.impl.BoardBuilder;
import org.junit.Test;

public class AllowedMovesEnPassen extends AllowedMovesAbstractTest {

	@Test
	public void test1() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - k", 
				"- - p - - - - -", 		
				"- - - - - - - -", 		
				"- P - P - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - K")
				.build().invert();
				
		board.applyMove(moveOf("C7-C5"));
		
		testAllowedMove(
				board,
				"D5-C6",
				"B5-C6");
	}
	
	@Test
	public void test2() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - k", 
				"- p - - - - - -", 		
				"- - - - - - - -", 		
				"P - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - K")
				.build().invert();
				
		board.applyMove(moveOf("B7-B5"));
		
		testAllowedMove(
				board,
				"A5-B6");
	}
	
	@Test
	public void test3() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - k", 
				"p - - - - - - -", 		
				"- - - - - - - -", 		
				"- P - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - K")
				.build().invert();
				
		board.applyMove(moveOf("A7-A5"));
		
		testAllowedMove(
				board,
				"B5-A6");
	}
	
	@Test
	public void test4() {
		final BoardEngine board = BoardFactory.of(
				"- - - - - - - k", 
				"- - - - - - p -", 		
				"- - - - - - - -", 		
				"- - - - - - - P", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - - - - K")
				.build().invert();
				
		board.applyMove(moveOf("G7-G5"));
		
		testAllowedMove(
				board,
				"H5-G6");
	}
	
	@Test
	public void test5() {
		final BoardEngine board = BoardFactory.newInitialPositionBoard();
		
		BoardUtils.applayMoves(board, 
				"E2-E4",
				"A7-A5",
				"H2-H4");
				
		testDisallowedMove(board, 
				"A5-H3", 
				"A5-H4");
	}
}
