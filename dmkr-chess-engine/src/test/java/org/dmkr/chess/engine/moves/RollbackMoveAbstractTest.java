package org.dmkr.chess.engine.moves;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Move;

import static org.dmkr.chess.api.utils.BoardUtils.movesToString;
import static org.junit.Assert.*;

public abstract class RollbackMoveAbstractTest {

	protected void testApplyRollbackAllMoves(BoardEngine board) {
		final BoardEngine copy = board.clone();
		final int[] allowedMoves = copy.allowedMoves();
		
		for (int move : allowedMoves) {
			final String message = "Move " + Move.moveOf(move, board.isInverted()) + "\n\n";
			board.applyMove(move);
			assertEquals(message, 1, board.getMovesHistory().size());
			assertTrue(message, !board.isKingUnderAtack());
			assertNotEquals(message, copy, board);
			board.rollbackMove();
			assertEquals(0, board.getMovesHistory().size());
			assertEquals(message, copy, board);
		}
		
		board.invert();
		final BoardEngine invertedCopy = board.clone();
		final int[] invertedAllowedMoves = invertedCopy.allowedMoves();
		final String allMovesString = movesToString(board);
		
		for (int move : invertedAllowedMoves) {
			final String message = "Move " + Move.moveOf(move, board.isInverted()) + "\n" + allMovesString + "\n";
			board.applyMove(move);
			assertEquals(1, board.getMovesHistory().size());
			assertTrue(message, !board.isKingUnderAtack());
			assertNotEquals(message, invertedCopy, board);
			board.rollbackMove();
			assertEquals(0, board.getMovesHistory().size());
			assertEquals(message, invertedCopy, board);
		}
	}
}
