package org.dmkr.chess.engine.board;

import java.util.Map;
import java.util.function.Consumer;

import org.dmkr.chess.api.model.Color;
import org.dmkr.chess.api.model.ColoredPiece;
import org.dmkr.chess.api.model.Field;
import org.dmkr.chess.api.model.Piece;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import static org.dmkr.chess.api.model.Field.*;
import static org.dmkr.chess.api.model.Color.*;
import static org.dmkr.chess.api.model.Piece.*;
import static org.junit.Assert.assertEquals;

public class BoardForEachTest {
	@Test
	public void testNotInverted() {
		final Map<Field, Consumer<ColoredPiece>> testers = ImmutableMap.<Field, Consumer<ColoredPiece>>of(
					A1, tester(A1, WHITE, ROOK),
					A8, tester(A8, BLACK, ROOK),
					H1, tester(H1, WHITE, ROOK),
					H8, tester(H8, BLACK, ROOK),
					D4, tester(D4, null, null)
				);
		
		BoardFactory.newInitialPositionBoard().forEach(
				(field, coloredPiece) -> testers.containsKey(field),
				(field, coloredPiece) -> testers.get(field).accept(coloredPiece)
			);
	}
	
	@Test
	public void testInverted() {
		final Map<Field, Consumer<ColoredPiece>> testers = ImmutableMap.<Field, Consumer<ColoredPiece>>of(
					A1, tester(A1, WHITE, ROOK),
					A8, tester(A8, BLACK, ROOK),
					H1, tester(H1, WHITE, ROOK),
					H8, tester(H8, BLACK, ROOK),
					D4, tester(D4, null, null)
				);
		
		BoardFactory.newInitialPositionBoard().invert().forEach(
				(field, coloredPiece) -> testers.containsKey(field),
				(field, coloredPiece) -> testers.get(field).accept(coloredPiece)
			);
	}
	
	private Consumer<ColoredPiece> tester(Field field, Color expectedColor, Piece expectedPiece) {
		return coloredPiece -> {
			assertEquals(field.name(), expectedColor, coloredPiece.color());
			assertEquals(field.name(), expectedPiece, coloredPiece.piece());
		};
	}
	
}
