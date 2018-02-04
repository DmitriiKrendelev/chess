package org.dmkr.chess.engine.moves;

import static org.dmkr.chess.api.utils.BoardUtils.movesToString;

import java.util.Set;
import java.util.stream.Stream;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Move;
import org.junit.Assert;

public abstract class AllowedMovesAbstractTest {

	protected void testAllAlowedMoves(BoardEngine board, String ... allowedMoves) {
		testAllAlowedMoves(board, Stream.of(allowedMoves).map(Move::moveOf).toArray(Move[]::new));
	}
	
	protected void testAllAlowedMoves(BoardEngine board, Move ... allExpectedAllowedMoves) {
		final BoardEngine clone = board.clone();
		final Set<Move> allAllowedMoves = board.getAllowedMoves();
		Assert.assertEquals(clone, board);
		
		Assert.assertEquals("Number of allowed moves", allExpectedAllowedMoves.length, allAllowedMoves.size());
		for (Move move : allExpectedAllowedMoves)
			Assert.assertTrue(
					"Move: " + move + "\n\n" + board + movesToString(board),
					allAllowedMoves.contains(move));
	}
	
	protected void testAllowedMove(BoardEngine board, String ... allowedMoves) {
		testAllowedMove(board, Stream.of(allowedMoves).map(Move::moveOf).toArray(Move[]::new));
	}
	
	protected void testAllowedMove(BoardEngine board, Move ... allowedMoves) {
		final Set<Move> allAllowedMoves = board.getAllowedMoves();
		
		for (Move move : allowedMoves)
			Assert.assertTrue(
					"\nMove: " + move + "\n" + board + "\nMoves: " + movesToString(board),
					allAllowedMoves.contains(move));
	}
	
	protected void testDisallowedMove(BoardEngine board, String ... disallowedMoves) {
		testDisallowedMove(board, Stream.of(disallowedMoves).map(Move::moveOf).toArray(Move[]::new));
	}
	
	protected void testDisallowedMove(BoardEngine board, Move ... disallowedMoves) {
		final Set<Move> allAllowedMoves = board.getAllowedMoves();
		
		for (Move move : disallowedMoves)
			Assert.assertTrue(
					"Move: " + move + "\n\n" + board + movesToString(board),
					!allAllowedMoves.contains(move));
	}
}
