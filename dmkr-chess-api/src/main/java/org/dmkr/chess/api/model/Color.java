package org.dmkr.chess.api.model;

import static org.dmkr.chess.api.model.Constants.*;

import org.dmkr.chess.api.model.Item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Color {
	WHITE(VALUE_WHITE),
	BLACK(VALUE_BLACK);

	@Getter
	private final byte value;
	
	public byte item(Item item) {
		return (byte) (value * item.value());
	}
	
	public static Color ofItem(char c) {
		return Character.isUpperCase(c) ? WHITE : BLACK;
	}
	
	public static Color ofItem(byte item, boolean isInverted) {
		if (item == VALUE_EMPTY) {
			return null;
		} 
		
		return (isInverted ? -item : item) > 0 ? WHITE : BLACK;
	}
}
