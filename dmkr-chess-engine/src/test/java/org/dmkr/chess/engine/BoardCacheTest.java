package org.dmkr.chess.engine;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dmkr.chess.api.Board;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Field;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.dmkr.chess.common.cache.CachableCreator.*;

public class BoardCacheTest {
	
	private Board board() {
		final Board board = BoardFactory.of(
				"r n b q k b n r", 
				"p p p p p p p p", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"P P P P P P P P", 		
				"R N B Q K B N R")
				.canCastleLeft()
				.canCastleRght()
				.canOponentCastleLeft()
				.canOponentCastleRght()
				.build();
		
		return createCachableProxy(board);
	}
	
	@Test
	public void testCache() {
		final Board board = board();

		final Board boardClone = ((BoardEngine) board).clone();
		assertFalse(isCachableProxy(boardClone));
		
		testCacheMoves(board);
		
		assertEquals(board, boardClone);
		assertFalse(board == boardClone);
		
	}
	
	private void testCacheMoves(Board board) {
		assertTrue(isCachableProxy(board));
		
		final Set<Move> moves0 = board.getAllowedMoves();
		final Map<Field, Set<Field>> movesFields0 = board.getAllowedMovesFields();
		final List<Move> movesHistory0 = board.getMovesHistory();
		
		assertNotNull(moves0);
		assertNotNull(movesFields0);
		assertNotNull(movesHistory0);
		
		for (int i = 0; i < 10; i ++) {
			assertTrue(moves0 == board.getAllowedMoves());
			assertTrue(movesFields0 == board.getAllowedMovesFields());
			assertTrue(movesHistory0 == board.getMovesHistory());
		}
		
		board.applyMove(Move.moveOf("E2-E4"));
		board.rollback();
		
		for (int i = 0; i < 10; i ++) {
			assertFalse(moves0 == board.getAllowedMoves());
			assertFalse(movesFields0 == board.getAllowedMovesFields());
			assertFalse(movesHistory0 == board.getAllowedMovesFields());
			assertEquals(moves0, board.getAllowedMoves());
			assertEquals(movesFields0, board.getAllowedMovesFields());
			assertEquals(movesHistory0, board.getMovesHistory());
		}
	}
	
	
	
}
