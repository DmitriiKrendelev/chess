package org.dmkr.chess.engine.moves;

import static org.dmkr.chess.api.model.Move.moveOf;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Assert;
import org.junit.Test;

public class CastelingFlagsTest {

	private void check(BoardEngine board, 
			boolean canCastleLeft, 
			boolean canCastleRght,
			boolean canOponentCastleLeft,
			boolean canOponentCastleRght) {
		
		Assert.assertEquals(canCastleLeft, board.canCastleLeft());
		Assert.assertEquals(canCastleRght, board.canCastleRght());
		Assert.assertEquals(canOponentCastleLeft, board.canOponentCastleLeft());
		Assert.assertEquals(canOponentCastleRght, board.canOponentCastleRght());
	}
	
	private void check(BoardEngine board, 
			Move move,
			boolean canCastleLeft, 
			boolean canCastleRght,
			boolean canOponentCastleLeft,
			boolean canOponentCastleRght) {
		
		boolean canCastleLeftBefore = board.canCastleLeft(); 
		boolean canCastleRghtBefore = board.canCastleRght();
		boolean canOponentCastleLeftBefore = board.canOponentCastleLeft();
		boolean canOponentCastleRghtBefore = board.canOponentCastleRght();
		Assert.assertTrue("Move: " + move + "\nMoves: " + board.getAllowedMoves(), board.getAllowedMoves().contains(move));
		
		final BoardEngine clone = board.clone();
		board.applyMove(move);
		Assert.assertEquals("Move: " + move, canCastleLeft, board.canCastleLeft());
		Assert.assertEquals("Move: " + move, canCastleRght, board.canCastleRght());
		Assert.assertEquals("Move: " + move, canOponentCastleLeft, board.canOponentCastleLeft());
		Assert.assertEquals("Move: " + move, canOponentCastleRght, board.canOponentCastleRght());
		
		board.rollback();
		Assert.assertEquals("Move: " + move, canCastleLeftBefore, board.canCastleLeft());
		Assert.assertEquals("Move: " + move, canCastleRghtBefore, board.canCastleRght());
		Assert.assertEquals("Move: " + move, canOponentCastleLeftBefore, board.canOponentCastleLeft());
		Assert.assertEquals("Move: " + move, canOponentCastleRghtBefore, board.canOponentCastleRght());
	
		Assert.assertEquals(clone, board);
	}
	
	@Test
	public void test1() {
		final BoardEngine board = BoardFactory.of(
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
				.build();
		check(board, true, true, true, true);
		check(board, moveOf("E1-C1"), true, true, false, false);
		check(board, moveOf("E1-G1"), true, true, false, false);
		check(board, moveOf("E1-E2"), true, true, false, false);
		check(board, moveOf("A1-D1"), true, true, true, false);
		check(board, moveOf("A1-A8"), true, true, true, false);
		check(board, moveOf("H1-H8"), true, true, false, true);
	}

	@Test
	public void test2() {
		final BoardEngine board = BoardFactory.of(
				"r - - - k - - r", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"- - - - - - - -", 		
				"- - - - K - - R")
				.canCastleRght()
				.canOponentCastleLeft()
				.canOponentCastleRght()
				.build();
		check(board, false, true, true, true);
		check(board, moveOf("E1-G1"), true, true, false, false);
		check(board, moveOf("E1-E2"), true, true, false, false);
		check(board, moveOf("H1-H8"), true, true, false, false);
	}
}
