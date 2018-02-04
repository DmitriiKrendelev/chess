package org.dmkr.chess.engine.board;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Field;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.dmkr.chess.api.model.Field.*;
import static org.dmkr.chess.engine.board.BoardFactory.newInitialPositionBoard;

public class BoardAllowedMovesFields {

	@Test
	public void testNotInverted() {
		final Map<Field, Set<Field>> allowedMoves = newInitialPositionBoard().getAllowedMovesFields();
		assertEquals(10, allowedMoves.size());
		
		final Set<Field> movesB1 = allowedMoves.get(B1);
		assertEquals(2, movesB1.size());
		assertTrue(movesB1.containsAll(Arrays.asList(A3, C3)));
		
		final Set<Field> movesG1 = allowedMoves.get(G1);
		assertEquals(2, movesG1.size());
		assertTrue(movesG1.containsAll(Arrays.asList(H3, F3)));
	
		final Set<Field> movesE2 = allowedMoves.get(E2);
		assertEquals(2, movesE2.size());
		assertTrue(movesE2.containsAll(Arrays.asList(E3, E4)));
	}
	
	@Test
	public void testInverted() {
		final BoardEngine invertedInitialBoard = newInitialPositionBoard().invert();
		
		final Map<Field, Set<Field>> allowedMoves = invertedInitialBoard.getAllowedMovesFields();
		assertEquals(10, allowedMoves.size());
		
		final Set<Field> movesB8 = allowedMoves.get(B8);
		assertEquals(2, movesB8.size());
		assertTrue(movesB8.containsAll(Arrays.asList(A6, C6)));
		
		final Set<Field> movesG8 = allowedMoves.get(G8);
		assertEquals(2, movesG8.size());
		assertTrue(movesG8.containsAll(Arrays.asList(H6, F6)));
	
		final Set<Field> movesE7 = allowedMoves.get(E7);
		assertEquals(2, movesE7.size());
		assertTrue(movesE7.containsAll(Arrays.asList(E6, E5)));
	}
}
