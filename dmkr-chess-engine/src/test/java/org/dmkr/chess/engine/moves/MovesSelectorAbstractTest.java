package org.dmkr.chess.engine.moves;

import static org.dmkr.chess.engine.board.impl.MovesSelectorImpl.movesSelector;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.stream.Stream;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.MovesSelector;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.api.utils.MoveUtils;

public class MovesSelectorAbstractTest {

	protected void testAllMoves(BoardEngine board, boolean checkKingUnderAtack, byte[] items, String ... allExpectedMoves) {
		testAllMoves(board, checkKingUnderAtack, items, Stream.of(allExpectedMoves).map(Move::moveOf).toArray(Move[]::new));
	}
	
	protected void testAllMoves(BoardEngine board, boolean checkKingUnderAtack, byte[] items, Move ... allExpectedMoves) {
		final MovesSelector movesSelector = movesSelector()
				.checkKingUnderAtack(false)
				.itemsToSelect(items)
				.build();
		
		
		final int[] moves = board.calculateAllowedMoves(movesSelector);
		final Set<Move> resultMoves = MoveUtils.toSet(moves, board.isInverted());
		
		assertEquals("Number of allowed moves", moves.length, allExpectedMoves.length);
		resultMoves.forEach(move -> assertTrue("Move: " + move, resultMoves.contains(move)));
	}
}
