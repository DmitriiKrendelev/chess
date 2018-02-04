package org.dmkr.chess.api.model;

import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "index")
@ToString(of = {"color", "item"})
public class ColoredItem {
	@Getter
	private final Color color;
	@Getter
	private final Item item;
	private final int index;
	
	private static final int ITEMS_LENGTH = Item.values().length;
	private static final ColoredItem[] CACHE = new ColoredItem[ITEMS_LENGTH * 2];
	private static final ColoredItem NULL_COLORED_ITEM = new ColoredItem(null, null, -1);
	
	private static int cacheIndex(Color color, Item item) {
		return color.ordinal() * ITEMS_LENGTH + item.ordinal();
	}
	
	static {
		for (Color color : Color.values()) {
			for (Item item : Item.values()) {
				final int index = cacheIndex(color, item);
				CACHE[index] = new ColoredItem(color, item, index);
			}
		}
	}
	
	public static ColoredItem of(Color color, Item item) {
		return color == null || item == null ? NULL_COLORED_ITEM : CACHE[cacheIndex(color, item)];
	}

	public static Stream<ColoredItem> stream() {
		return Stream.of(CACHE);
	}
	
	public boolean isNull() {
		return this == NULL_COLORED_ITEM;
	}
}
