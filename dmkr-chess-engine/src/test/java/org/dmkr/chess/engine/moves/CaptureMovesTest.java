package org.dmkr.chess.engine.moves;

import static org.dmkr.chess.api.utils.BoardUtils.movesToString;
import static org.dmkr.chess.api.utils.MoveUtils.getCaptureMoves;
import static org.dmkr.chess.api.utils.MoveUtils.toSet;

import java.util.Set;
import java.util.stream.Stream;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Assert;
import org.junit.Test;

public class CaptureMovesTest {

	private void testAllCaptureMoves(BoardEngine board, String ... allowedMoves) {
		testAllCaptureMoves(board, Stream.of(allowedMoves).map(Move::moveOf).toArray(Move[]::new));
	}
	
	private void testAllCaptureMoves(BoardEngine board, Move ... allExpectedCaptureMoves) {
		final Set<Move> allCaptureMoves = toSet(getCaptureMoves(board), board.isInverted());
		
		Assert.assertEquals("Number of captured moves", allExpectedCaptureMoves.length, allCaptureMoves.size());
		for (Move move : allExpectedCaptureMoves)
			Assert.assertTrue(
					"Move: " + move + "\n\n" + board + movesToString(board),
					allCaptureMoves.contains(move));
	}
	
	@Test
	public void testInitialPosition() {
		testAllCaptureMoves(BoardFactory.newInitialPositionBoard(), new String[0]);
	}
	
	@Test
	public void testSimplePosition() {
		testAllCaptureMoves(
				BoardFactory.of(
				"- - - k - - - -", 
				"p - - - - p - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -",
				"Q - - n - - - -", 		
				"- - - K - - - -")
				.canOponentCastleLeft()
				.canOponentCastleRght()
				.build(),
				"A2-A7",
				"A2-F7",
				"A2-D2",
				"D1-D2");
	}
	
	@Test
	public void testKingIsUnderAtack() {
		testAllCaptureMoves(
				BoardFactory.of(
				"- - - k - - - -", 
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - - - - - - -", 		
				"- - n - - - - -", 		
				"- - - - - - N -",
				"- - - p p - - -", 		
				"- - - K - - - -")
				.canOponentCastleLeft()
				.canOponentCastleRght()
				.build(),
				"D1-E2",
				"G3-E2");
	}
}
