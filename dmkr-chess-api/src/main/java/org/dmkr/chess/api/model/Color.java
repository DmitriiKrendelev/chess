package org.dmkr.chess.api.model;

import static org.dmkr.chess.api.model.Constants.*;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Color {
	WHITE(VALUE_WHITE),
	BLACK(VALUE_BLACK);

	@Getter
	private final byte value;
	
	public byte piece(Piece piece) {
		return (byte) (value * piece.value());
	}
	
	public static Color ofPiece(char c) {
		return Character.isUpperCase(c) ? WHITE : BLACK;
	}
	
	public static Color ofPiece(byte piece, boolean isInverted) {
		if (piece == VALUE_EMPTY) {
			return null;
		} 
		
		return (isInverted ? -piece : piece) > 0 ? WHITE : BLACK;
	}
}
