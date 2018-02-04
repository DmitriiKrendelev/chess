package org.dmkr.chess.api.model;

import static org.dmkr.chess.api.model.Constants.*;

import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Item {

	KING(VALUE_KING, 'K'),
	QUEEN(VALUE_QUEEN, 'Q'),
	ROOK(VALUE_ROOK, 'R'),
	BISHOP(VALUE_BISHOP, 'B'),
	KNIGHT(VALUE_KNIGHT, 'N'),
	PAWN(VALUE_POWN, 'P');

	public static final char NO_ITEM = '-';

	@Getter
	private final byte value;
	@Getter
	private final char shortName;
	
	public static Item withShortName(char shortName) {
		return Stream.of(values())
			.filter(item -> item.shortName == shortName || item.shortName == Character.toUpperCase(shortName))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("shortName = " + shortName));
	}
	
	public static Item withValue(byte value) {
		return value == VALUE_EMPTY ? null : Stream.of(values())
			.filter(item -> item.value == value || item.value == -value)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("value = " + value));
	}
	
	public static char shortName(byte value) {
		if (value == VALUE_EMPTY)
			return NO_ITEM;
		
		for (Item item : values())
			if (item.value == value)
				return item.shortName;
			else if (item.value == -value)
				return Character.toLowerCase(item.shortName);
		
		throw new IllegalArgumentException("value = " + value);
	}
	
}
