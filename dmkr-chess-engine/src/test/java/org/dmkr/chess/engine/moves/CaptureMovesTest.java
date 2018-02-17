package org.dmkr.chess.engine.moves;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.api.utils.MoveUtils.CaptureMovesFilter;
import org.dmkr.chess.engine.board.BoardFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Stream;

import static org.dmkr.chess.api.utils.BoardUtils.movesToString;
import static org.dmkr.chess.api.utils.MoveUtils.toSet;

public class CaptureMovesTest {

	private void testAllCaptureMoves(BoardEngine board, String ... allowedMoves) {
		testAllCaptureMoves(board, Stream.of(allowedMoves).map(Move::moveOf).toArray(Move[]::new));
	}
	
	private void testAllCaptureMoves(BoardEngine board, Move ... allExpectedCaptureMoves) {
		final CaptureMovesFilter allCaptureMovesSelector = new CaptureMovesFilter(0, 100);

		final Set<Move> allCaptureMoves = toSet(board.allowedMoves(allCaptureMovesSelector), board.isInverted());
		
		Assert.assertEquals("Number of captured moves", allExpectedCaptureMoves.length, allCaptureMoves.size());
		for (Move move : allExpectedCaptureMoves) {
			Assert.assertTrue(
					"Move: " + move + "\n\n" + board + movesToString(board),
					allCaptureMoves.contains(move));
		}
	}

	private void testCaptureMovesFilter(
			BoardEngine board,
			int maxNumberOfNotCaptured,
			int maxNumberOfCaptured,
			Set<Move> expectedNotContain,
			Set<Move> expectedContain) {

		final CaptureMovesFilter allCaptureMovesSelector = new CaptureMovesFilter(maxNumberOfNotCaptured, maxNumberOfCaptured);

		System.out.println(board.getAllowedMoves());
		final Set<Move> foundMoves = toSet(board.allowedMoves(allCaptureMovesSelector), board.isInverted());

		for (Move move : expectedContain) {
			Assert.assertTrue(
					board + "\nMove: " + move + "\n\n" + foundMoves,
					foundMoves.contains(move));
		}

		for (Move move : expectedNotContain) {
			Assert.assertTrue(
					board + "\nMove: " + move + "\n\n" + foundMoves,
					!foundMoves.contains(move));
		}

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

	@Test
	public void testCaptureMovesFilter() {
		testCaptureMovesFilter(
				BoardFactory.of(
						"- - - k - - - -",
						"- - - - - - - -",
						"- - p - p - - -",
						"- n - - - b - -",
						"- - - N - - - -",
						"- r - - - r - -",
						"P - - - P - - -",
						"- - - K - - - -")
						.build(),
				0,
				6,
				toSet("D4-C6", "D4-E6"),
				toSet("D4-B5", "D4-B3", "D4-F5", "D4-F3", "A2-B3", "E2-F3")
				);
	}
}
