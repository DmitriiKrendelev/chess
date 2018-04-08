package org.dmkr.chess.api.model;

import static org.dmkr.chess.api.model.Constants.*;

import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Piece {

	KING(VALUE_KING, 'K'),
	QUEEN(VALUE_QUEEN, 'Q'),
	ROOK(VALUE_ROOK, 'R'),
	BISHOP(VALUE_BISHOP, 'B'),
	KNIGHT(VALUE_KNIGHT, 'N'),
	PAWN(VALUE_PAWN, 'P');

	public static final char NO_PIECE = '-';

	@Getter
	private final byte value;
	@Getter
	private final char shortName;
	
	public static Piece withShortName(char shortName) {
		return Stream.of(values())
			.filter(piece -> piece.shortName == shortName || piece.shortName == Character.toUpperCase(shortName))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("shortName = " + shortName));
	}
	
	public static Piece withValue(byte value) {
		return value == VALUE_EMPTY ? null : Stream.of(values())
			.filter(piece -> piece.value == value || piece.value == -value)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("value = " + value));
	}
	
	public static char shortName(byte value) {
		if (value == VALUE_EMPTY)
			return NO_PIECE;
		
		for (Piece piece : values())
			if (piece.value == value)
				return piece.shortName;
			else if (piece.value == -value)
				return Character.toLowerCase(piece.shortName);
		
		throw new IllegalArgumentException("value = " + value);
	}
	
}
