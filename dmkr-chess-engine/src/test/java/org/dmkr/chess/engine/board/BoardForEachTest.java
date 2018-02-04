package org.dmkr.chess.engine.board;

import java.util.Map;
import java.util.function.Consumer;

import org.dmkr.chess.api.model.Color;
import org.dmkr.chess.api.model.ColoredItem;
import org.dmkr.chess.api.model.Field;
import org.dmkr.chess.api.model.Item;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import static org.dmkr.chess.api.model.Field.*;
import static org.dmkr.chess.api.model.Color.*;
import static org.dmkr.chess.api.model.Item.*;
import static org.junit.Assert.assertEquals;

public class BoardForEachTest {
	@Test
	public void testNotInverted() {
		final Map<Field, Consumer<ColoredItem>> testers = ImmutableMap.<Field, Consumer<ColoredItem>>of(
					A1, tester(A1, WHITE, ROOK),
					A8, tester(A8, BLACK, ROOK),
					H1, tester(H1, WHITE, ROOK),
					H8, tester(H8, BLACK, ROOK),
					D4, tester(D4, null, null)
				);
		
		BoardFactory.newInitialPositionBoard().forEach(
				(field, coloredItem) -> testers.containsKey(field),
				(field, coloredItem) -> testers.get(field).accept(coloredItem)
			);
	}
	
	@Test
	public void testInverted() {
		final Map<Field, Consumer<ColoredItem>> testers = ImmutableMap.<Field, Consumer<ColoredItem>>of(
					A1, tester(A1, WHITE, ROOK),
					A8, tester(A8, BLACK, ROOK),
					H1, tester(H1, WHITE, ROOK),
					H8, tester(H8, BLACK, ROOK),
					D4, tester(D4, null, null)
				);
		
		BoardFactory.newInitialPositionBoard().invert().forEach(
				(field, coloredItem) -> testers.containsKey(field),
				(field, coloredItem) -> testers.get(field).accept(coloredItem)
			);
	}
	
	private Consumer<ColoredItem> tester(Field field, Color expectedColor, Item expectedItem) {
		return coloredItem -> {
			assertEquals(field.name(), expectedColor, coloredItem.color());
			assertEquals(field.name(), expectedItem, coloredItem.item());
		};
	}
	
}
