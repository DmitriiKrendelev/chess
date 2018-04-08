package org.dmkr.chess.api.model;

import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "index")
@ToString(of = {"color", "piece"})
public class ColoredPiece {
	@Getter
	private final Color color;
	@Getter
	private final Piece piece;
	private final int index;
	
	private static final int PIECES_LENGTH = Piece.values().length;
	private static final ColoredPiece[] CACHE = new ColoredPiece[PIECES_LENGTH * 2];
	private static final ColoredPiece NULL_COLORED_PIECE = new ColoredPiece(null, null, -1);
	
	private static int cacheIndex(Color color, Piece piece) {
		return color.ordinal() * PIECES_LENGTH + piece.ordinal();
	}
	
	static {
		for (Color color : Color.values()) {
			for (Piece piece : Piece.values()) {
				final int index = cacheIndex(color, piece);
				CACHE[index] = new ColoredPiece(color, piece, index);
			}
		}
	}
	
	public static ColoredPiece of(Color color, Piece piece) {
		return color == null || piece == null ? NULL_COLORED_PIECE : CACHE[cacheIndex(color, piece)];
	}

	public static Stream<ColoredPiece> stream() {
		return Stream.of(CACHE);
	}
	
	public boolean isNull() {
		return this == NULL_COLORED_PIECE;
	}
}
